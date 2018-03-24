package br.edu.jrsa.examples.chat;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;

class TCPClient implements Runnable {
	
	private String chat_addr;
	private int chat_port;
	
    public TCPClient(String chat_addr, int chat_port) {
		super();
		this.chat_addr = chat_addr;
		this.chat_port = chat_port;
	}

	private void startClient() throws Exception {
        
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket(this.chat_addr, this.chat_port);

        DataOutputStream outToChat = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromChat = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Thread responseThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String response;
				try {
					response = inFromChat.readLine();
					System.out.println("resp: " + response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

        String msg = "";
        while (!msg.equalsIgnoreCase(EncryptedChat.SAIR)) {
        	System.out.print("\n> ");
        	msg = inFromUser.readLine();
        	outToChat.writeBytes(msg + '\n');
        	responseThread.start();
        }
        clientSocket.close();
    }

	@Override
	public void run() {
		try {
			this.startClient();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
