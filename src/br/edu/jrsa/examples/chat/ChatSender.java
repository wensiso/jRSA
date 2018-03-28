package br.edu.jrsa.examples.chat;

import java.io.*;
import java.net.*;

class ChatSender {
	
	private String chat_addr;
	private int chat_port;
	
	private BufferedReader inFromUser;
	
	private Socket clientSocket;
	private DataOutputStream outToChat;
		
    public ChatSender(String chat_addr, int chat_port) {
		super();
		this.chat_addr = chat_addr;
		this.chat_port = chat_port;
	}

	public void start() {
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
		try {
			clientSocket = new Socket(this.chat_addr, this.chat_port);
			if (clientSocket.isConnected())
				System.out.println("Conected to " + this.chat_addr + ":" + this.chat_port);

			outToChat = new DataOutputStream(clientSocket.getOutputStream());
			this.sendFirstMessage();
			
			String msg = "";
			while (!msg.equalsIgnoreCase(EncryptedChat.SAIR)) {
				System.out.print("> ");
				msg = inFromUser.readLine();
				this.send(msg);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//Quando recebe o sair, dispara essa excecao, mas o prog continua de boa
			//e.printStackTrace();
		}
	}
	
	private void send(String msg) throws IOException {
		outToChat.writeBytes("[" + EncryptedChat.myself.getId() + "]: " + msg + '\n');
	}

	private void sendFirstMessage() throws IOException {
		String firstMessage = EncryptedChat.myself.getId() + ":" + EncryptedChat.myself.getPort();
		outToChat.writeBytes(firstMessage + "\n");
	}

	public void sendReject() {
		try {
			this.send(EncryptedChat.SAIR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
