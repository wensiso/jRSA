package br.edu.jrsa.examples.chat;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class ChatDiscoverer implements Runnable {

	private String id;
	private MulticastSocket discovererSocket;

	private HashMap<String, Boolean> chats;
	
	public ChatDiscoverer(String id) {
		this.id = id;
		chats = new HashMap<String, Boolean>();
	}

	private void receiveMessages() throws Exception {
		
        InetAddress address = InetAddress.getByName(EncryptedChat.SERVICE_ADDR);
        discovererSocket = new MulticastSocket(EncryptedChat.SERVICE_PORT);
        discovererSocket.joinGroup(address);

        while (true) {
        	
            byte[] buf = new byte[256];
            
            // Receive the information and print it.
            DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
            discovererSocket.receive(msgPacket);

            String msgreceived = new String(buf, 0, buf.length);
            String msg = msgreceived.trim();
            
            String other_id = this.readId(msg);
            
            // Se a mensagem for minha, não preciso imprimir...
            if(other_id.equals(this.id))
            	continue;
            
            System.out.println(msg);
            System.out.println();
        }
	}

	private String readId(String msg) {
		for(String line : msg.split("\r\n")) {
			if(line.startsWith("id"))
				return line.substring(4).trim();
		}
		return "";
	}

	@Override
    public void run () {
    	try {
			this.receiveMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
}
