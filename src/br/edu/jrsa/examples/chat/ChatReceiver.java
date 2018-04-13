package br.edu.jrsa.examples.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Observable;

import br.edu.jrsa.RSAKey;

class ChatReceiver extends Observable implements Runnable {

	private int chat_port;
    private ServerSocket welcomeSocket;
    private boolean isrunning;
    
	public ChatReceiver(int chat_port) {
		super();
		this.chat_port = chat_port;
	}
	
	public String getHostAddress() {
		return welcomeSocket.getInetAddress().getHostAddress();
	}
	
	public boolean isRunning() {
		return this.isrunning;
	}

	private void startReceiver() throws Exception {
        welcomeSocket = new ServerSocket(this.chat_port);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept(); 
            processConnection(connectionSocket);
            connectionSocket.close();
        }
    }
	
	private void processConnection(Socket connectionSocket) throws Exception {
		
		BufferedReader inFromChat = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
		String first_msg = inFromChat.readLine();
		if(!this.parseFirstMessage(first_msg)) {
			return;
		}
		
		String msg = "";
		String text = "";
		while (true) {
			msg = inFromChat.readLine();
			text = msg.split(":")[1].trim();
			
			if(text.startsWith("enc")) {
				//TODO Desencriptar
			}
			
			if(text.equalsIgnoreCase(EncryptedChat.SAIR)) {
				this.setChanged();
				this.notifyObservers(text);
				break;
			}
			System.out.println(msg);
			System.out.print(">");
		}
	}

	private boolean parseFirstMessage(String first_msg) {
		try {
			String username = first_msg.split("@")[0];
			String fullhost = first_msg.split("@")[1].split(" ")[0];
			String addr = fullhost.split(":")[0];
			int port = Integer.parseInt(fullhost.split(":")[1]);
	
			String skey = first_msg.split(" ")[1];
			Long n = Long.parseLong(skey.split(",")[0]);
			Long e = Long.parseLong(skey.split(",")[1]);
			RSAKey pubkey = new RSAKey();
			pubkey.setPubKey(n, e);
			
			ChatUser user = new ChatUser(username, addr, addr, port, pubkey);
			this.setChanged();
			this.notifyObservers(user);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		try {
			this.isrunning = true;
			startReceiver();
		} catch (Exception e) {
			this.isrunning = false;
			e.printStackTrace();
		}	
	}

	
}
