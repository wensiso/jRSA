package br.edu.jrsa.examples.chat;

import java.io.*;
import java.net.*;

class ChatSender {
	
	private String chat_addr;
	private int chat_port;
	private Socket clientSocket;
		
    public ChatSender(String chat_addr, int chat_port) {
		super();
		this.chat_addr = chat_addr;
		this.chat_port = chat_port;
	}

	public void start() {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		try {
			clientSocket = new Socket(this.chat_addr, this.chat_port);
			if (clientSocket.isConnected())
				System.out.println("Conected to " + this.chat_addr + ":" + this.chat_port);

			DataOutputStream outToChat = new DataOutputStream(clientSocket.getOutputStream());
			
			this.sendFirstMessage(outToChat);
			
			String msg = "";
			while (!msg.equalsIgnoreCase(EncryptedChat.SAIR) || !clientSocket.isClosed()) {
				System.out.print("> ");
				msg = inFromUser.readLine();
				outToChat.writeBytes("[" + EncryptedChat.myself.getId() + "]: " + msg + '\n');
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendFirstMessage(DataOutputStream outToChat) throws IOException {
		String firstMessage = EncryptedChat.myself.getId() + ":" + EncryptedChat.myself.getPort();
		outToChat.writeBytes(firstMessage + "\n");
	}

	public void sendReject() {
		try {
			clientSocket = new Socket(this.chat_addr, this.chat_port);
			DataOutputStream outToChat = new DataOutputStream(clientSocket.getOutputStream());
			outToChat.writeBytes("[" + EncryptedChat.myself.getId() + "]: " + EncryptedChat.SAIR + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
