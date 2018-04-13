package br.edu.jrsa;

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
	public Long[] encryptStr(String str_msg) {
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
	 * Encrypt a string  
	 * 
	 * @param str_msg the open text
	 * @param sep the separator of chars
	 * @return a string representing the array of encrypted chars
	 */
	public String encryptAndConvertStr(String str_msg, char sep) {
		Long[] c = this.encryptStr(str_msg);
		String cstr = "";
		for (Long n: c) {
			cstr += n.toString() + sep;
		}
		return cstr.substring(0, cstr.length()-1);
	}
	
	/**
	 * Encrypt a string  
	 * 
	 * @param str_msg the open text
	 * @return a string representing the array of encrypted chars
	 */
	public String encryptAndConvertStr(String str_msg) {
		return this.encryptAndConvertStr(str_msg, ',');
	}
	
	/**
	 * 
	 * @param enc array of encrypted chars in Long format
	 * @return a String from decrypted chars
	 */
	public String decryptLong(Long[] enc) {
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
	
	/**
	 * 
	 * @param enc array of encrypted chars in str format
	 * @param sep the separator
	 * @return a String from decrypted chars
	 */
	public String decryptStr(String enc, char sep) {
		String[] enc_arr = enc.split("" + sep);
		Long [] enc_long = new Long[enc_arr.length];
		for (int i=0; i<enc_arr.length; ++i) {
			enc_long[i] = Long.parseLong(enc_arr[i]);
		}
		return this.decryptLong(enc_long);
	}
	
	/**
	 * @param enc array of encrypted chars in str format
	 * @return a String from decrypted chars
	 */
	public String decryptStr(String enc) {
		return this.decryptStr(enc, ',');
	}
	
	public void testAll (String test_str) {
		Long[] enc = this.encryptStr(test_str);
		
		System.out.println("\nString original: \n"+ test_str);
		char[] tmp = test_str.toCharArray();
		for(int i=0; i<tmp.length; ++i)
			System.out.print((long) tmp[i] + ",");
		
		System.out.print("\n\nString encriptada: \n");
		for(int i=0; i<enc.length; ++i)
			System.out.print(enc[i] + ",");
		
		String decod_teste = this.decryptLong(enc);
		System.out.println("\n\nString decodificada: \n" + decod_teste);
		char[] tmp2 = decod_teste.toCharArray();
		for(int i=0; i<tmp2.length; ++i)
			System.out.print((long) tmp2[i] + ",");
		System.out.println("\n--------------------------------\n");
	}
 
}
