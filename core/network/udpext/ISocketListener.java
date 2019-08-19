
package core.network.udpext;

import core.network.udp.Packet;

/**
 *
 * @author gekoncze
 */
public interface ISocketListener {
    public void packetRecieved(Packet packet);
}
