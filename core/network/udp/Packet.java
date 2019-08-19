
package core.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 *
 * @author gekoncze
 */
public final class Packet {
    public static final int SIZE = 64;
    public static final int READABLE_STRING_WIDTH = 8;
    public static final int READABLE_STRING_HEIGHT = 8;
    private final byte[] data = new byte[SIZE];
    private final DatagramPacket packet = new DatagramPacket(data, data.length);

    Packet() {
    }
    
    public Packet(InetAddress address, int port){
        packet.setAddress(address);
        packet.setPort(port);
    }

    public byte[] getData() {
        return data;
    }

    public DatagramPacket getDatagramPacket() {
        return packet;
    }
    
    public InetAddress getAddress() {
        return packet.getAddress();
    }
    
    public int getPort() {
        return packet.getPort();
    }

    public String dataToReadableString(){
        String rval = "";
        
        for(int y = 0; y < READABLE_STRING_HEIGHT; y++){
            for(int x = 0; x < READABLE_STRING_WIDTH; x++){
                int position = x + y * READABLE_STRING_WIDTH;
                rval += String.format("%-5s", data[position]);
            }
            rval += "\n";
        }
        
        return rval;
    }
    
    public String dataToReadableStringFirstLineOnly(){
        String rval = "";
        
        for(int y = 0; y < 1; y++){
            for(int x = 0; x < READABLE_STRING_WIDTH; x++){
                int position = x + y * READABLE_STRING_WIDTH;
                rval += data[position] + " ";
            }
        }
        
        return rval;
    }
}
