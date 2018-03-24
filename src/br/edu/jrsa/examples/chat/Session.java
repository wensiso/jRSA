package br.edu.jrsa.examples.chat;
import java.io.*;
import java.net.*;

public class Session implements Runnable {

	private Socket connectionSocket;
	
    public Session(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void processRequest() throws Exception {
		
		String clientSentence;
	    String capitalizedSentence;
	    
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		clientSentence = inFromClient.readLine();

		capitalizedSentence = clientSentence.toUpperCase() + '\n';
		outToClient.writeBytes(capitalizedSentence);

	}
	
	@Override
	public void run() {
		try {
			this.processRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
