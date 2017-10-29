package br.com.fat.jrsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	private static void encrypt(String msg_filename, String encrypt_filename, RSA encoder) {
		
		System.out.println("\nArquivo da mensagem original: " + msg_filename);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(msg_filename));
			String curr_line = null;
			String msg = "";
			while ((curr_line = in.readLine()) != null)
					msg += curr_line;
			in.close();
			
			System.out.println("Encriptando arquivo...");
			Long[] enc = encoder.encodeStr(msg);

			System.out.println("Salvando arquivo encriptado em: " + encrypt_filename);		
			
			FileOutputStream out = new FileOutputStream(new File(encrypt_filename));
			DataOutputStream writer = new DataOutputStream(out);
			for(Long c: enc) {
				writer.writeLong(c);
			}
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			System.err.println("\nErro: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("\nErro: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void decode(String encrypt_filename, String msgdec_filename, RSA decoder) {

		System.out.println("\nAbrindo arquivo encriptado: " + encrypt_filename);

		try {
			FileInputStream in = new FileInputStream(new File(encrypt_filename));
			DataInputStream reader = new DataInputStream(in);
			
			ArrayList<Long> clist = new ArrayList<Long>();

			while(reader.available() >= Long.BYTES)
				clist.add(reader.readLong());
			reader.close();
			Long[] enc = clist.toArray(new Long[0]);
			
			System.out.println("\nDesencriptando arquivo...");
			String msgdec = decoder.decodeStr(enc);

			System.out.println("Salvando arquivo desencriptado em: " + msgdec_filename);
			BufferedWriter out = new BufferedWriter(new FileWriter(msgdec_filename));
			out.write(msgdec);
			out.close();

		} catch (FileNotFoundException e) {
			System.err.println("\nErro: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("\nErro: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		
		String pubk_filename = "./data/keys/pubk.txt";
		String privk_filename = "./data/keys/privk.txt";

		String msg_filename = "./data/sample/message.txt";
		String encrypt_filename = "./data/sample/encrypt_msg.dat";
		String msgdec_filename = "./data/sample/decod_message.txt";
		
		RSAKey key = new RSAKey();
		System.out.println(key);
		
		RSA encoder = new RSA(key);
		
		//encoder.testAll("create view cotistas as");
		
		encrypt(msg_filename, encrypt_filename, encoder);
		decode(encrypt_filename, msgdec_filename, encoder);
		
	}
}
