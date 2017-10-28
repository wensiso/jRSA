package br.com.fat.jrsa;

import java.math.BigDecimal;

public class RSA {
	
	private Long[] pubkey;
	private Long[] privkey;
	
	public RSA(RSAKey key) {
		this.privkey = key.getPrivate();
		this.pubkey = key.getPublic();
	}
	
	/**
	 * Encrypt a string  
	 * 
	 * @param str_msg
	 * @return a array of encrypted chars
	 */
	public Long[] encodeStr(String str_msg) {
		BigDecimal n = new BigDecimal(this.pubkey[0]);
		int e = this.pubkey[1].intValue();
		
		char[] msg = str_msg.toCharArray();
		Long[] c = new Long[msg.length];
		
		for(int i=0; i<msg.length; ++i) {
			BigDecimal m = new BigDecimal((long) msg[i]);
			c[i] = m.pow(e).remainder(n).longValueExact();
		}
		return c;
	}
	
	/**
	 * 
	 * @param enc array of enrypted chars
	 * @return a String from decrypted chars
	 */
	public String decodeStr(Long[] enc) {
		BigDecimal n = new BigDecimal(this.privkey[0]);
		int d = this.privkey[1].intValue();

		char[] msg = new char[enc.length];
		for(int i=0; i<msg.length; ++i) {
			BigDecimal c = new BigDecimal(enc[i]);
			long m = c.pow(d).remainder(n).longValue();
			msg[i] = (char) m;
			
		}

		return new String(msg);
	}
 
}
