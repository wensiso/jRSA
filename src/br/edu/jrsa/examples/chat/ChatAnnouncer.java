package br.edu.jrsa.examples.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;

public class ChatAnnouncer extends Observable implements Runnable {
	
	private boolean canrun;
	private DatagramSocket announceSocket;

    private void sendMessages() throws Exception {
        // Get the address that we are going to connect to.
        InetAddress addr = InetAddress.getByName(EncryptedChat.SERVICE_ADDR);        
        announceSocket = new DatagramSocket();
        
        String msg = "Encrypted Chat 1.0\r\n"
        		+ "id: " + EncryptedChat.myself.getId() + "\r\n"
        		+ "host: " + EncryptedChat.myself.getAddr() + "\r\n"
        		+ "port: " + EncryptedChat.myself.getPort() + "\r\n\r\n";
       
        DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, EncryptedChat.SERVICE_PORT);
        
        while(this.canrun) {
        	announceSocket.send(msgPacket);
        	try {
        		Thread.sleep(100);
			} catch (InterruptedException e) {
				this.canrun = false;
			}
        }
    }

	@Override
	public void run() {
		try {
			this.canrun = true;
			this.sendMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
