package br.edu.jrsa.examples.chat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class ChatDiscoverer extends Thread {

	private HashMap<ChatUser, Boolean> chats;
	private MulticastSocket discovererSocket;
	
	public ChatDiscoverer() {
		chats = new HashMap<ChatUser, Boolean>();
		InetAddress address;
		try {
			address = InetAddress.getByName(EncryptedChat.SERVICE_ADDR);
			discovererSocket = new MulticastSocket(EncryptedChat.SERVICE_PORT);
			discovererSocket.joinGroup(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void search() {
		try {
			long start = System.currentTimeMillis();
			long dur = 0;
			
			while (true) { 
				dur = System.currentTimeMillis() - start;
				if(dur > 1000) //1 seg
					break;
				
				byte[] buf = new byte[256];

				DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				discovererSocket.receive(msgPacket);

				String msgreceived = new String(buf, 0, buf.length);
				String msg = msgreceived.trim();

				ChatUser user = new ChatUser(msg);
				if (!user.equals(EncryptedChat.myself) && !chats.containsKey(user)) {
					chats.put(user, false);
				}
			}
			// FIXME remover os caras que sairam.

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printChats() {
		System.out.println("----------------------");
		System.out.println("Online users: ");
		for (ChatUser user: this.chats.keySet()) {
			System.out.println("\t[" + user.getUsername() + "] " + user.getId() + ":" + user.getPort());
		}
		System.out.println("----------------------");
	}
	
	@Override
	public void run() {
		this.search();
	}
	
	public int usersFound() {
		return this.chats.size();
	}

}
