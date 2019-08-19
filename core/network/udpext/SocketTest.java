
package core.network.udpext;

import core.network.MessageFactory;
import core.network.udp.Packet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author gekoncze
 */
public class SocketTest {
    public static void main(String[] args) throws SocketException, IOException {
        InetAddress address = InetAddress.getByName("localhost");
        int port = 56789;
        
        SocketExt server = new SocketExt(new ISocketListener() {
            @Override
            public void packetRecieved(Packet packet) {
                System.out.println("Server recieved:\n" + packet.dataToReadableString());
            }
        }, port);
        
        SocketExt client = new SocketExt(new ISocketListener() {
            @Override
            public void packetRecieved(Packet packet) {
                System.out.println("Client recieved:\n" + packet.dataToReadableString());
            }
        });
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String line;
        while(!(line = br.readLine()).equals("Stop it!")){
            if(line.length() <= 0) continue;
            Packet packet = new Packet(address, port);
            MessageFactory.createRegistrationMessage(packet, (byte) line.charAt(0));
            client.send(packet);
        }
        
        client.close();
        server.close();
    }
}
