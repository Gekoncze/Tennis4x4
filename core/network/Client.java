
package core.network;

import core.Game;
import core.GameException;
import core.GameListener;
import core.PlayerAction;
import core.network.udp.Packet;
import core.PlayerActions;
import core.Tennis4x4;
import core.collections.MyLinkedList;
import core.network.udpext.PacketCollector;
import core.network.udpext.SocketExt;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author gekoncze
 */
public class Client {
    
    public static final int MESSAGE_LIMIT = 100;
    public static final int STEP_WAIT_LIMIT = 100;
    public static final long STEP_RESEND_DELAY = 400;
    
    private final MyLinkedList<PlayerActions> actionList = new MyLinkedList<>();
    private final PacketCollector messages = new PacketCollector(MESSAGE_LIMIT);
    private final SocketExt socket;
    private final InetAddress serverAddress;
    private final int serverPort;
    private GameListener gameListener, inputListener, outputListener;
    private boolean started = false;
    private long startupTime = 0;
    private long lastResendTime = 0;
    private int stepId = 0;
    private Game game;

    public Client(InetAddress serverAddress, int serverPort) throws SocketException {
        if(serverPort < 49152 || serverPort > 65535)
            throw new SocketException("Port out of range: " + serverPort);
        
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        socket = new SocketExt(messages);
    }

    public Game getGame() {
        return game;
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public int getStepId(){
        return stepId;
    }
    
    public void close() {
        if(isClosed()) return;
        quitGame();
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
    
    public void setGameListener(GameListener listener) {
        gameListener = listener;
    }
    
    public void setInputListener(GameListener listener) {
        inputListener = listener;
    }
    
    public void setOutputListener(GameListener listener){
        outputListener = listener;
    }
    
    public void sendPlayerRegistration(int player) throws IOException, GameException {
        checkClosed();
        Tennis4x4.checkPlayerBounds(player);
        sendRegistration((byte)player);
    }
    
    public void sendPlayerActionMoveLeft(int player) throws IOException, GameException {
        checkClosed();
        Tennis4x4.checkPlayerBounds(player);
        sendActionMessage((byte) player, PlayerAction.MOVE_LEFT);
    }
    
    public void sendPlayerActionMoveRight(int player) throws IOException, GameException {
        checkClosed();
        Tennis4x4.checkPlayerBounds(player);
        sendActionMessage((byte) player, PlayerAction.MOVE_RIGHT);
    }
    
    public void sendPlayerActionWave(int player) throws IOException, GameException {
        checkClosed();
        Tennis4x4.checkPlayerBounds(player);
        sendActionMessage((byte) player, PlayerAction.WAVE);
    }
    
    public void update() throws IOException, GameException {
        checkClosed();
        resolveMessages();
        if(started){
            while(Tennis4x4.isTimeForStep(startupTime, stepId)){
                if(!step(actionList.get(stepId - (stepId % Tennis4x4.STEP_INPUT_SKIP_COUNT)))) break;
            }
        }
    }
    
    private boolean step(PlayerActions actions) throws IOException, GameException {
        if(actions != null){
            PlayerActions a = actionList.get(stepId - (stepId % Tennis4x4.STEP_INPUT_SKIP_COUNT));
            game.step(copyForSkip(a, stepId));
            stepId++;
            return true;
        } else {
            if(Tennis4x4.getCurrentTime() > lastResendTime + STEP_RESEND_DELAY &&
                    Tennis4x4.getExpectedStep(startupTime) > stepId + STEP_WAIT_LIMIT){
                sendStepResendMessage(stepId);
                lastResendTime = Tennis4x4.getCurrentTime();
            }
            return false;
        }
    }
    
    private PlayerActions copyForSkip(PlayerActions actions, int wantedStepId){
        if(actions.stepId == wantedStepId){
            return actions;
        } else {
            return actions.copyNoWave(wantedStepId);
        }
    }
    
    private void startNewGame(){
        if(started) return;
        
        started = true;
        startupTime = Tennis4x4.getCurrentTime();
        actionList.clear();
        messages.clear();
        stepId = 0;
        game = new Game();
    }
    
    private void quitGame() {
        actionList.clear();
        messages.clear();
        started = false;
    }
    
    private void checkClosed() throws IOException {
        if(isClosed()) throw new IOException("Cannot use closed client.");
    }
    
    private void resolveMessages(){ 
        while(!messages.isEmpty()){
            Packet packet = messages.remove();
            inputEvent(packet);
            byte type = MessageFactory.readMessageType(packet);
            
            switch(type){
                case MessageFactory.STEP_MESSAGE:
                    handleStepMessage(packet); break;
                case MessageFactory.REGISTRATION_ACCEPTED_MESSAGE:
                    gameEvent("Joined successfully."); break;
                case MessageFactory.KICKED_MESSAGE:
                    gameEvent("Kicked. (Player " + (MessageFactory.readKickedMessage(packet)+1) + ")"); break;
            }
        }
    }
    
    private void handleStepMessage(Packet packet){
        if(!started) startNewGame();
        
        PlayerActions actions = MessageFactory.readStepMessage(packet);
        
        while(actions.stepId > Tennis4x4.getExpectedStep(startupTime)){
            startupTime -= Tennis4x4.STEP_TIME;
        }
        
        if(actions.stepId == stepId) lastResendTime = 0;
        actionList.set(actions, actions.stepId);
        actionList.lock(actions.stepId);
    }
    
    private void sendActionMessage(byte player, byte action) throws IOException {
        checkClosed();
        Packet packet = new Packet(serverAddress, serverPort);
        MessageFactory.createPlayerActionMessage(packet, player, action);
        send(packet);
    }

    private void sendRegistration(byte player) throws IOException {
        checkClosed();
        Packet packet = new Packet(serverAddress, serverPort);
        MessageFactory.createRegistrationMessage(packet, player);
        send(packet);
    }
    
    private void sendStepResendMessage(int stepId) throws IOException {
        checkClosed();
        Packet packet = new Packet(serverAddress, serverPort);
        MessageFactory.createStepResendMessage(packet, stepId);
        send(packet);
    }
    
    private void send(Packet packet) throws IOException {
        outputEvent(packet);
        socket.send(packet);
    }
    
    private void gameEvent(String message){
        if(gameListener != null){
            gameListener.event(message);
        }
    }
    
    private void inputEvent(Packet packet){
        if(inputListener != null){
            inputListener.event(packet.dataToReadableStringFirstLineOnly());
        }
    }
    
    private void outputEvent(Packet packet){
        if(outputListener != null){
            outputListener.event(packet.dataToReadableStringFirstLineOnly());
        }
    }
}
