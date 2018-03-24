package br.edu.jrsa.examples.chat;

import java.net.*;

class TCPServer implements Runnable {

	int chat_port;
    private ServerSocket welcomeSocket;
    
	public TCPServer(int chat_port) {
		super();
		this.chat_port = chat_port;
	}

	private void startServer() throws Exception {
        
        welcomeSocket = new ServerSocket(this.chat_port);

        while (true) {

            Socket connectionSocket = welcomeSocket.accept();
            
            Thread t = new Thread(new Session(connectionSocket));
            t.start();           
        }
    }

	@Override
	public void run() {
		try {
			startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
