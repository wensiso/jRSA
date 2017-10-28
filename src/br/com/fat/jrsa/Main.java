package br.com.fat.jrsa;

public class Main {

	public static void main(String[] args) {
		
		RSAKey key = new RSAKey();
		System.out.println(key);
		
		RSA rsa = new RSA(key);
		
		String teste = "Teste de encriptação\n \tcom\r\n\tcaracteres especiais.";
		Long[] enc = rsa.encodeStr(teste);
		
		System.out.println("\nString original: \n"+ teste);
		char[] tmp = teste.toCharArray();
		for(int i=0; i<tmp.length; ++i)
			System.out.print((long) tmp[i] + ",");
		
		System.out.print("\n\nString encriptada: \n");
		for(int i=0; i<enc.length; ++i)
			System.out.print(enc[i] + ",");
		
		System.out.println();
		for(int i=0; i<enc.length; ++i)
			System.out.print((char) enc[i].longValue() + ",");
		
		String decod_teste = rsa.decodeStr(enc);
		System.out.println("\n\nString decodificada: \n" + decod_teste);
		char[] tmp2 = teste.toCharArray();
		for(int i=0; i<tmp2.length; ++i)
			System.out.print((long) tmp2[i] + ",");
		
	}
	
	

}
