
package core.network.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author gekoncze
 */
public class Socket {

    private final DatagramSocket socket;

    public Socket() throws SocketException {
        socket = new DatagramSocket();
    }
    
    public Socket(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }
    
    public Packet recieve() throws IOException {
        Packet packet = new Packet();
        socket.receive(packet.getDatagramPacket());
        return packet;
    }
    
    public void send(Packet packet) throws IOException {
        socket.send(packet.getDatagramPacket());
    }
    
    public void close(){
        socket.close();
    }
}
