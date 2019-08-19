
package core.network.udpext;

import core.network.udp.Packet;
import core.network.udp.Socket;
import java.io.IOException;
import java.net.SocketException;

/**
 *
 * @author gekoncze
 */
public class SocketExt {
    private final Socket socket;
    private final SocketDaemon daemon;
    private boolean closed = false;

    public SocketExt(ISocketListener listener) throws SocketException {
        socket = new Socket();
        daemon = new SocketDaemon(socket, listener);
        daemon.start();
    }
    
    public SocketExt(ISocketListener listener, int port) throws SocketException {
        socket = new Socket(port);
        daemon = new SocketDaemon(socket, listener);
        daemon.start();
    }
    
    public void send(Packet packet) throws IOException {
        if(closed) throw new IOException("Socket is closed.");
        socket.send(packet);
    }
    
    public void close(){
        closed = true;
        daemon.stop();
    }

    public boolean isClosed() {
        return closed;
    }
}
