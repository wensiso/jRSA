package br.edu.jrsa.examples.chat;

public class ChatUser {
	
	private String id;
	private String username;
	private String host;
	private String addr;
	private int port;
	
	public ChatUser(String announce) {
		this(readId(announce), readAddr(announce), readPort(announce));
	}
	
	public ChatUser(String id, String addr, int port) {
		this.id = id;
		this.username = id.split("@")[0];
		this.host = id.split("@")[1];
		this.addr = addr;
		this.port = port;
	}
	
	public ChatUser(String user, String host, String addr, int port) {
		this.id = user + "@" + host;
		this.username = user;
		this.host = host;
		this.addr = addr;
		this.port = port;
	}
	
	private static String readId(String announce) {
		for(String line : announce.split("\r\n")) {
			if(line.startsWith("id"))
				return line.substring(4).trim();
		}
		return "";
	}
	
	private static String readAddr(String announce) {
		for(String line : announce.split("\r\n")) {
			if(line.startsWith("addr"))
				return line.substring(6).trim();
		}
		return "";
	}
	
	private static int readPort(String announce) {
		for(String line : announce.split("\r\n")) {
			if(line.startsWith("port"))
				return Integer.parseInt(line.substring(6).trim());
		}
		return 0;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getHost() {
		return host;
	}

	public String getAddr() {
		return addr;
	}

	public int getPort() {
		return port;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		// Class name is Employ & have lastname
		ChatUser user = (ChatUser) obj;
		return this.hashCode() == user.hashCode();
	}
	
	@Override
    public int hashCode() {
        int result = 17;
        result += this.id.hashCode();
        result += this.port;
        return result;
    }

}
