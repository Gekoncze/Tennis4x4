
package core.network.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author gekoncze
 */
public class PacketWriter {
    private final Packet packet;
    private int position = 0;

    public PacketWriter(Packet packet) {
        this.packet = packet;
    }
    
    public void writeByte(byte value){
        if(position >= Packet.SIZE)
            throw new ArrayIndexOutOfBoundsException("Exceeding packet size. (w)");
        
        packet.getData()[position] = value;
        position++;
    }

    public void writeBytes(byte[] value){
        if((position + value.length) >= Packet.SIZE)
            throw new ArrayIndexOutOfBoundsException("Exceeding packet size. (w)");
        
        System.arraycopy(value, 0, packet.getData(), position, value.length);
        position += value.length;
    }
    
    public void writeBoolean(boolean value){
        writeByte(booleanToByte(value));
    }
    
    public void writeInt(int value){
        writeBytes(intToBytes(value));
    }
    
    private byte[] intToBytes(int value){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }
    
    private byte booleanToByte(boolean value){
        return value == true ? (byte)1 : (byte)0;
    }
}
