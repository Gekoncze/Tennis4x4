
package core.network.udpext;

import core.network.udp.Socket;
import core.network.udp.Packet;
import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;

/**
 *
 * @author gekoncze
 */
public class SocketDaemon {
    private final Socket socket;
    private final ISocketListener listener;
    private final Thread thread;
    
    public SocketDaemon(Socket socket, ISocketListener listener) throws SocketException {
        Objects.requireNonNull(listener);
        
        this.listener = listener;
        this.socket = socket;
        
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listen();
                } catch(InterruptedException | IOException e){
                    return;
                }
            }
        });
        thread.setDaemon(true);
    }
    
    public void start(){
        thread.start();
    }
    
    public void stop(){
        thread.interrupt();
        socket.close();
    }
    
    private void listen() throws InterruptedException, IOException {
        Packet packet;
        while(true){
            packet = socket.recieve();
            listener.packetRecieved(packet);
            packet = null;
            Thread.sleep(10);
        }
    }
}
