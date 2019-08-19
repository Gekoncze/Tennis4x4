
package core.network.udpext;

import core.network.udp.Packet;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author gekoncze
 */
public class PacketCollector implements ISocketListener {
    private final Queue<Packet> packetQueue = new LinkedList<>();
    private final int limit;

    public PacketCollector(int limit) {
        if(limit <= 0) throw new InvalidParameterException("Packet limit must be > 0.");
        this.limit = limit;
    }
    
    public synchronized boolean isEmpty(){
        return packetQueue.isEmpty();
    }
    
    public synchronized Packet remove(){
        return packetQueue.remove();
    }
    
    public synchronized void clear(){
        packetQueue.clear();
    }

    @Override
    public synchronized void packetRecieved(Packet packet) {
        if(packetQueue.size() >= limit) return;
        packetQueue.add(packet);
    }
}
