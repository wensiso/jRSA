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
	
	
	public void testAll (String test_str) {
		Long[] enc = this.encodeStr(test_str);
		
		System.out.println("\nString original: \n"+ test_str);
		char[] tmp = test_str.toCharArray();
		for(int i=0; i<tmp.length; ++i)
			System.out.print((long) tmp[i] + ",");
		
		System.out.print("\n\nString encriptada: \n");
		for(int i=0; i<enc.length; ++i)
			System.out.print(enc[i] + ",");
		
		String decod_teste = this.decodeStr(enc);
		System.out.println("\n\nString decodificada: \n" + decod_teste);
		char[] tmp2 = test_str.toCharArray();
		for(int i=0; i<tmp2.length; ++i)
			System.out.print((long) tmp2[i] + ",");
		System.out.println("--------------------------------\n");
	}
 
}
