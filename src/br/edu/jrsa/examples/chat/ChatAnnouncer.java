package br.edu.jrsa.examples.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;

public class ChatAnnouncer extends Observable implements Runnable {
	
	private String id;
	private String chat_addr;
	private int chat_port;
	
	private DatagramSocket announceSocket;
	
	public ChatAnnouncer(String id, String chat_addr, int chat_port) {
		this.id = id;
		this.chat_addr = chat_addr;
		this.chat_port = chat_port;
	}
	
    private void sendMessages() throws Exception {
        // Get the address that we are going to connect to.
        InetAddress addr = InetAddress.getByName(EncryptedChat.SERVICE_ADDR);        
        announceSocket = new DatagramSocket();
        
        String msg = "Encrypted Chat 1.0\r\n"
        		+ "id: " + this.id + "\r\n"
        		+ "host: " + this.chat_addr + "\r\n"
        		+ "port: " + this.chat_port + "\r\n\r\n";
       
        DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, EncryptedChat.SERVICE_PORT);
        
        while(true) {
        	announceSocket.send(msgPacket);
        	Thread.sleep(1000);
        }
    }

	@Override
	public void run() {
		try {
			this.sendMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
