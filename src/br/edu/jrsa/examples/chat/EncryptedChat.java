package br.edu.jrsa.examples.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class EncryptedChat implements Observer {

	final static String SERVICE_ADDR = "224.0.0.3";
    final static int SERVICE_PORT = 3737;
    
    final static String SAIR = "sair";
    final static String CHAT = "chat";
    final static String SEARCH = "search";
	private static final String NO = "n";
	private static final String NO_2= "no";
    
    static ChatUser myself;
    
    private ChatDiscoverer discover;
    private ChatAnnouncer announcer;
    
    private Thread annoucerThread;
	private Thread receiverThread;
	private ChatReceiver receiver;
	private ChatSender sender;
	
	private BufferedReader inFromUser;
	private boolean chatting;
	
	public EncryptedChat() {
		discover = new ChatDiscoverer();
		this.chatting = false;
	}

	private void startChat () throws Exception {
		
		Random random = new Random(System.currentTimeMillis());
		int chat_port = random.nextInt(10000) + 10000; //randint from 10000 to 20000
		
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter a username: ");
		String username = inFromUser.readLine();
		
		receiver = new ChatReceiver(chat_port);
		receiver.addObserver(this);
		receiverThread = new Thread(receiver);
		receiverThread.start();

		//Wait server start...
		while(!receiver.isRunning())
			Thread.sleep(100);
			
		myself = new ChatUser(username, receiver.getHostAddress(), receiver.getHostAddress(), chat_port); 
		System.out.println("Your ID is: " + myself.getId() + ". Your port is " + chat_port);
		
		announcer = new ChatAnnouncer();
		annoucerThread = new Thread(announcer);
		annoucerThread.start();
				
		String statement = "";
		String command = "";
		do {
			if (this.chatting == false) {
				System.out.println(
						"Type 'search' to find other users. Type 'chat <<user-id>>' to chat with a user.\n Type 'out' to close a chat or this program.");
				System.out.print("> ");
			}
			
			statement = inFromUser.readLine();
			command = statement.split(" ")[0];
			
			if (this.chatting == true) {
				if (command.equalsIgnoreCase(EncryptedChat.NO) || command.equalsIgnoreCase(EncryptedChat.NO_2)) {
					System.out.println("Rejecting chat... ");
					sender.sendReject();
					this.chatting = false;
				} else {
					sender.start();
					continue;
				}
			}

			if (command.equalsIgnoreCase(EncryptedChat.SEARCH)) {
				System.out.println("Searching users...");
				if(discover.getState() == Thread.State.NEW)
					discover.start();
				Thread.sleep(1000);
				System.out.println("Users found: " +  discover.usersFound() );
				discover.printChats();
				continue;
			} else if(command.equalsIgnoreCase(EncryptedChat.CHAT)) {
				String args = statement.split(" ")[1];
				String chat_user = args.split(" ")[0].split("@")[0];
				String host = args.split(" ")[0].split("@")[1];
				String dst_addr = host.split(":")[0];
				int dst_port = Integer.parseInt(host.split(":")[1]);
				
				if(this.annoucerThread.isAlive())
					this.annoucerThread.interrupt();
				
				System.out.println("Connecting to " + chat_user + " at " + dst_addr + ":" + dst_port);
				sender = new ChatSender(dst_addr, dst_port);
				this.chatting = true;
				sender.start();
				this.chatting = false;
			} else {
				System.out.println("Command not found!");
			}
		} while (!command.equalsIgnoreCase(EncryptedChat.SAIR));
		
		System.out.println("Fim do programa...");
    }
	
	/**
	 * Ao inicar um chat, para de procurar outros chats
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(this.chatting == true)
			return;
		if (o instanceof ChatReceiver) {
			if (arg instanceof ChatUser) {
				ChatUser user = (ChatUser) arg;
				System.out.println("Chat request received from " + user.getId() + ":" + user.getPort() + ". Do you accept? (Y/N)");
				sender = new ChatSender(user.getAddr(), user.getPort());
				this.chatting = true;
				if (this.annoucerThread.isAlive())
					this.annoucerThread.interrupt();
			} else if (arg instanceof String) {
				String text = (String) arg;
				if(text.equalsIgnoreCase(EncryptedChat.SAIR)) {
					sender.sendReject();
					this.chatting = false;
					this.annoucerThread.start();
				}
					
			}
		}
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
