
package core.network;

import core.network.udp.Packet;
import core.network.udp.PacketWriter;
import core.PlayerAction;
import core.PlayerActions;
import core.network.udp.PacketReader;

/**
 *
 * @author gekoncze
 */
public class MessageFactory {
    public static final byte STEP_MESSAGE = 1;
    public static final byte STEP_RESEND_MESSAGE = 2;
    public static final byte PLAYER_ACTION_MESSAGE = 3;
    public static final byte REGISTRATION_MESSAGE = 5;
    public static final byte REGISTRATION_ACCEPTED_MESSAGE = 6;
    public static final byte KICKED_MESSAGE = 7;
    
    
    public static void createStepMessage(Packet packet, PlayerActions actions){
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte(STEP_MESSAGE);
        pw.writeInt(actions.stepId);
        for(PlayerAction player : actions.player){
            for(int i = 0; i < PlayerAction.ACTION_COUNT; i++){
                pw.writeBoolean(player.action[i]);
            }
        }
    }
    
    public static void createPlayerActionMessage(Packet packet, byte player, byte action){
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte(PLAYER_ACTION_MESSAGE);
        pw.writeByte(player);
        pw.writeByte(action);
    }
    
    public static void createRegistrationMessage(Packet packet, byte player){
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte(REGISTRATION_MESSAGE);
        pw.writeByte(player);
    }

    public static void createNewRegistrationAcceptedMessage(Packet packet){
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte(REGISTRATION_ACCEPTED_MESSAGE);
    }
    
    public static void createKickedMessage(Packet packet, byte playerId){
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte(KICKED_MESSAGE);
        pw.writeByte(playerId);
    }
    
    public static void createStepResendMessage(Packet packet, int stepId){
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte(STEP_RESEND_MESSAGE);
        pw.writeInt(stepId);
    }
    
    public static byte readMessageType(Packet packet){
        return packet.getData()[0];
    }
    
    public static PlayerActions readStepMessage(Packet packet){
        PacketReader pr = new PacketReader(packet);
        pr.readByte();
        
        int stepid  = pr.readInt();
        
        PlayerActions actions = new PlayerActions(stepid);
        for(PlayerAction player : actions.player){
            for(int i = 0; i < PlayerAction.ACTION_COUNT; i++){
                player.action[i] = pr.readBoolean();
            }
        }
        
        return actions;
    }
    
    public static byte[] readPlayerActionMessage(Packet packet){
        PacketReader pr = new PacketReader(packet);
        pr.readByte();
        byte[] rval = new byte[2];
        rval[0] = pr.readByte();
        rval[1] = pr.readByte();
        return rval;
    }
    
    public static byte readRegistrationMessage(Packet packet){
        PacketReader pr = new PacketReader(packet);
        pr.readByte();
        return pr.readByte();
    }
    
    public static byte readKickedMessage(Packet packet){
        PacketReader pr = new PacketReader(packet);
        pr.readByte();
        return pr.readByte();
    }
    
    public static int readStepResendMessage(Packet packet){
        PacketReader pr = new PacketReader(packet);
        pr.readByte();
        return pr.readInt();
    }
}
