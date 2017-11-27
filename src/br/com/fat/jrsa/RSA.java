package br.com.fat.jrsa;

import java.math.BigInteger;

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
		BigInteger n = new BigInteger(this.pubkey[0].toString());
		BigInteger e = new BigInteger(this.pubkey[1].toString());
		
		char[] msg = str_msg.toCharArray();
		Long[] c = new Long[msg.length];
		
		for(int i=0; i<msg.length; ++i) {
			BigInteger m = new BigInteger(new Integer((int) msg[i]).toString());
			c[i] = m.modPow(e, n).longValueExact();
			//c[i] = m.pow(e).remainder(n).longValueExact();
		}
		return c;
	}
	
	/**
	 * 
	 * @param enc array of enrypted chars
	 * @return a String from decrypted chars
	 */
	public String decodeStr(Long[] enc) {
		BigInteger n = new BigInteger(this.privkey[0].toString());
		BigInteger d = new BigInteger(this.privkey[1].toString());

		char[] msg = new char[enc.length];
		for(int i=0; i<msg.length; ++i) {
			BigInteger c = new BigInteger(enc[i].toString());
			long m = c.modPow(d, n).longValueExact();
			//long m = c.pow(d).remainder(n).longValue();
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
		char[] tmp2 = decod_teste.toCharArray();
		for(int i=0; i<tmp2.length; ++i)
			System.out.print((long) tmp2[i] + ",");
		System.out.println("\n--------------------------------\n");
	}
 
}
