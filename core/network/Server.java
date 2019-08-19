
package core.network;

import core.GameListener;
import core.PlayerAction;
import core.PlayerActions;
import core.Tennis4x4;
import core.collections.MyLinkedList;
import core.network.udp.Packet;
import core.network.udpext.PacketCollector;
import core.network.udpext.SocketExt;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author gekoncze
 */
public class Server {
    
    public static final int MESSAGE_LIMIT = 100;
    
    private class PlayerAddress {
        public InetAddress address;
        public int port;

        public PlayerAddress(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }
    
    private final SocketExt socket;
    private final MyLinkedList<PlayerActions> actionList = new MyLinkedList<>();
    private final PacketCollector messages = new PacketCollector(MESSAGE_LIMIT);
    private final PlayerAddress[] players = new PlayerAddress[Tennis4x4.PLAYER_COUNT];
    private int stepId = 0;
    private boolean started = false;
    private long startupTime = 0;
    private GameListener gameListener, inputListener, outputListener;

    public Server(int port) throws SocketException {
        if(port < 49152 || port > 65535)
            throw new SocketException("Port out of range: " + port);
        socket = new SocketExt(messages, port);
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

    public int getStepId() {
        return stepId;
    }
    
    public void close() {
        if(isClosed()) return;
        quitGame();
        socket.close();
    }
    
    public void startNewGame() throws IOException {
        checkClosed();
        
        stepId = 0;
        actionList.clear();
        actionList.set(new PlayerActions(0), 0);
        messages.clear();
        started = true;
        startupTime = Tennis4x4.getCurrentTime();
    }
    
    private void quitGame() {
        started = false;
        startupTime = 0;
        stepId = 0;
        actionList.clear();
        messages.clear();
    }
    
    public void kickPlayer(int playerId) throws IOException {
        checkClosed();
        
        if(playerId < 0 || playerId >= players.length) return;
        
        if(players[playerId] != null){
            // Important note: do not change order
            //     otehrwise the message won't be send
            try {
                sendPlayerKickedMessage(playerId);
            } catch (IOException ex) {}
            players[playerId] = null;
            gameEvent("Player " + (playerId + 1) + " was kicked.");
        }
    }
    
    public void update() throws IOException {
        checkClosed();
        
        resolveMessages();
        if(!started) return;
        while(Tennis4x4.isTimeForStep(startupTime, stepId)){
            step();
        }
    }
    
    private void step() {
        actionList.lock(stepId);
        try {
            if(stepId % Tennis4x4.STEP_INPUT_SKIP_COUNT == 0)
                sendStepMessage(stepId);
        } catch (IOException ex) {}
        stepId += Tennis4x4.STEP_INPUT_SKIP_COUNT;
        
        if(stepId % Tennis4x4.STEP_INPUT_SKIP_COUNT == 0)
            actionList.set(new PlayerActions(stepId), stepId);
    }
    
    private void checkClosed() throws IOException {
        if(isClosed()) throw new IOException("Cannot use closed server.");
    }
    
    private void resolveMessages(){
        while(!messages.isEmpty()){
            Packet packet = messages.remove();
            inputEvent(packet);
            byte type = MessageFactory.readMessageType(packet);
            switch(type){
                case MessageFactory.PLAYER_ACTION_MESSAGE:
                    handlePlayerActionMessage(packet); break;
                case MessageFactory.REGISTRATION_MESSAGE:
                    handleRegistrationMessage(packet); break;
                case MessageFactory.STEP_RESEND_MESSAGE:
                    handleStepResendMessage(packet); break;
            }
        }
    }
    
    private void handlePlayerActionMessage(Packet packet){
        if(!started) return;
        byte[] paction = MessageFactory.readPlayerActionMessage(packet);
        byte playerId = paction[0];
        if(!checkRegistration(packet, playerId)) return;
        byte action = paction[1];
        if(action < 0 || action >= PlayerAction.ACTION_COUNT) return;
        actionList.get(stepId).player[playerId].action[action] = true;
    }
    
    private void handleRegistrationMessage(Packet packet){
        byte playerId = MessageFactory.readRegistrationMessage(packet);
        if(playerId < 0 || playerId >= players.length) return;
        
        if(players[playerId] == null){
            register(packet, playerId);
            gameEvent("Client " + packet.getAddress() + " " + packet.getPort() + 
                    " joined as player " + playerId);
        }
        
        if(checkRegistration(packet, playerId)){
            try {
                sendRegistrationAcceptedMessage(playerId);
            } catch (IOException ex) {}
        }
    }
    
    private void handleStepResendMessage(Packet packet){
        int stepId = MessageFactory.readStepResendMessage(packet);
        try {
            sendStepMessage(stepId);
        } catch (IOException ex) {}
    }
    
    private boolean checkRegistration(Packet packet, byte playerId){
        if(playerId < 0 || playerId >= players.length) return false;
        if(players[playerId] == null) return false;
        return players[playerId].address.equals(packet.getAddress()) &&
               players[playerId].port == packet.getPort();
    }
    
    private void register(Packet packet, byte playerId){
        if(playerId < 0 || playerId >= players.length) return;
        players[playerId] = new PlayerAddress(packet.getAddress(), packet.getPort());
    }
    
    private void sendStepMessage(int stepId) throws IOException {
        for (PlayerAddress player : players) {
            if(player == null) continue;
            Packet packet = new Packet(player.address, player.port);
            PlayerActions actions = actionList.get(stepId);
            if(actions == null) return;
            MessageFactory.createStepMessage(packet, actions);
            send(packet);
        }
    }
    
    private void sendRegistrationAcceptedMessage(int playerId) throws IOException {
        if(playerId < 0 || playerId >= players.length) return;
        if(players[playerId] == null) return;
        Packet packet = new Packet(players[playerId].address, players[playerId].port);
        MessageFactory.createNewRegistrationAcceptedMessage(packet);
        send(packet);
    }
    
    private void sendPlayerKickedMessage(int playerId) throws IOException {
        if(playerId < 0 || playerId >= players.length) return;
        if(players[playerId] == null) return;
        Packet packet = new Packet(players[playerId].address, players[playerId].port);
        MessageFactory.createKickedMessage(packet, (byte) playerId);
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
    
    /*private PlayerActions copyActionsNoWave(PlayerActions actions){
        PlayerActions rval = new PlayerActions(actions.stepId);
        
        for(int i = 0; i < rval.player.length; i++){
            for(int j = 0; j < PlayerAction.ACTION_COUNT; j++){
                if(j == PlayerAction.WAVE) continue;
                rval.player[i].action[j] = actions.player[i].action[j];
            }
        }
        
        return rval;
    }*/
}
