
package core.network.udp;

/**
 *
 * @author gekoncze
 */
public class TestPacket {
    public static void main(String[] args) {
        testPacketIO();
    }
    
    public static void testPacketIO(){
        Packet packet = new Packet();
        PacketWriter pw = new PacketWriter(packet);
        pw.writeByte((byte)10);
        pw.writeByte((byte)12);
        pw.writeByte((byte)-3);
        pw.writeInt(96555);
        pw.writeBoolean(false);
        System.out.println(packet.dataToReadableString());
        
        PacketReader pr = new PacketReader(packet);
        System.out.println("1st byte: " + pr.readByte());
        System.out.println("2nd byte: " + pr.readByte());
        System.out.println("3rd byte: " + pr.readByte());
        System.out.println("int: " + pr.readInt());
        System.out.println("boolean: " + pr.readBoolean());
    }
}
