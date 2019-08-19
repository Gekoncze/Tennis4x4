
package core.network.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;

/**
 *
 * @author gekoncze
 */
public class PacketReader {
    private final Packet packet;
    private int position = 0;

    public PacketReader(Packet packet) {
        this.packet = packet;
    }
    
    public byte readByte(){
        if(position >= Packet.SIZE)
            throw new ArrayIndexOutOfBoundsException("Exceeding packet size. (r)");
        
        byte rval = packet.getData()[position];
        position++;
        return rval;
    }
    
    public byte[] readBytes(int n){
        if(n <= 0) throw new InvalidParameterException("Method readBytes expects n > 0, " + n + " provided.");
        if((position + n) >= Packet.SIZE)
            throw new ArrayIndexOutOfBoundsException("Exceeding packet size. (w)");
        
        byte[] rval = new byte[n];
        System.arraycopy(packet.getData(), position, rval, 0, n);
        
        position += n;
        
        return rval;
    }
    
    public boolean readBoolean(){
        return byteToBoolean(readByte());
    }
    
    public int readInt(){
        return bytesToInt(readBytes(4));
    }
    
    private int bytesToInt(byte[] value){
        ByteBuffer bb = ByteBuffer.wrap(value);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }
    
    private boolean byteToBoolean(byte value){
        return value == 1;
    }
}
