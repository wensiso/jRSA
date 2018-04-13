package br.edu.jrsa.examples.chat;

import br.edu.jrsa.RSA;
import br.edu.jrsa.RSAKey;

public class ChatUser {
	
	private String id;
	private String username;
	private String host;
	private String addr;
	private int port;
	
	private RSAKey key;
	private RSA encoder;
		
	public ChatUser(String announce) {
		this.id = ChatUser.readId(announce);
		this.username = id.split("@")[0];
		this.host = id.split("@")[1];
		this.addr = ChatUser.readAddr(announce);
		this.port = ChatUser.readPort(announce);
		this.key = ChatUser.readPubKey(announce);
		this.encoder = new RSA(this.key);	
	}

	public ChatUser(String id, String addr, int port) {
		this.id = id;
		this.username = id.split("@")[0];
		this.host = id.split("@")[1];
		this.addr = addr;
		this.port = port;
		this.createKeys();
	}
	
	public ChatUser(String user, String host, String addr, int port, RSAKey pubkey) {
		this.id = user + "@" + host;
		this.username = user;
		this.host = host;
		this.addr = addr;
		this.port = port;
		this.key = pubkey;
		this.encoder = new RSA(this.key);
	}
	
	private void createKeys() {
		this.key = new RSAKey();
		this.key.autoBuildKeys();
		this.encoder = new RSA(this.key);
	}
	
	public Long[] getPublicKey() {
		return key.getPublic();
	}
	
	public String getStrPublicKey() {
		Long[] pubk = this.getPublicKey();
        return pubk[0].toString() + "," + pubk[1].toString();
	}

	public Long[] getPrivateKey() {
		return key.getPrivate();
	}
	
	public RSA getEncoder() {
		return encoder;
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
	
	private static RSAKey readPubKey(String announce) {
		for(String line : announce.split("\r\n")) {
			if(line.startsWith("pubk")) {
				String k = line.substring(5).trim();
				Long n = Long.parseLong(k.split(",")[0]);
				Long e = Long.parseLong(k.split(",")[1]);
				RSAKey pk = new RSAKey();
				pk.setPubKey(n, e);
				return pk;
			}
		}
		return null;
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
