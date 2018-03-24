package br.edu.jrsa.examples.chat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;

public class EncryptedChat {

	final static String SERVICE_ADDR = "224.0.0.3";
    final static int SERVICE_PORT = 3737;
    
    final static String SAIR = "sair";
    
    private ChatDiscoverer discover;
    private ChatAnnouncer announcer;
    
    private Thread discoverThread;
    private Thread annoucerThread;
    
    private String username;
    private String hostname;
	private Thread clientThread;
	private Thread serverThread;
	private TCPServer server;
	private TCPClient client;
    
    private String configHostname() {
    	String hostname = "";
    	DatagramSocket socket;
		try {
			socket = new DatagramSocket();
			hostname = socket.getLocalAddress().getHostName();
			socket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return hostname;
    }

	private void startChat () throws Exception {
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Defina seu username: ");
		this.username = inFromUser.readLine();
		this.hostname = this.configHostname();
		
		String id = username + "@" + hostname;
		
		discover = new ChatDiscoverer(id);
		discoverThread = new Thread(discover);
		
		announcer = new ChatAnnouncer(id, "192.168.0.1", 12345);
		annoucerThread = new Thread(announcer);
		
		annoucerThread.start();
		discoverThread.start();
		
//		server = new TCPServer(12345);
//		serverThread = new Thread(server);
		
//		client = new TCPClient("localhost", 12345);
//		clientThread = new Thread(client);
	
//		serverThread.start();
//		clientThread.start();
    }
	
	/**
	 * Iniciar aplicação de chat
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EncryptedChat chat = new EncryptedChat();
		
		try {
			chat.startChat();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
