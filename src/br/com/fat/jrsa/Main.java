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
	
	/**
	 * Encrypts an text file
	 * 
	 * @param msg_filename
	 * @param encrypt_filename
	 * @param encoder
	 */
	private static void encrypt(String msg_filename, String encrypt_filename, RSA encoder) {
		
		System.out.println("\nArquivo da mensagem original: " + msg_filename);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(msg_filename));
			int cur_char;
			String msg = "";
			while ((cur_char = in.read()) != -1) 
				msg += (char) cur_char;
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
	
	/**
	 * Decode a encrypted file to the original open txt file
	 * 
	 * @param encrypt_filename
	 * @param msgdec_filename
	 * @param decoder
	 */
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
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		
		String pubk_filename = "./data/keys/pubk.txt";
		String privk_filename = "./data/keys/privk.txt";

		String msg_filename = "./data/sample/message.txt";
		String encrypt_filename = "./data/sample/encrypt_msg.dat";
		String msgdec_filename = "./data/sample/decod_message.txt";
		
		RSAKey key = new RSAKey();
		System.out.println(key);
		
		RSA encoder = new RSA(key);
		
		//encoder.testAll("create view cotistas as\n"
		//		+ "select * from alunos where cota=\"True\"");
		
		long begin = System.currentTimeMillis();
		
		encrypt(msg_filename, encrypt_filename, encoder);
		decode(encrypt_filename, msgdec_filename, encoder);
		
		long end =  System.currentTimeMillis();
		long tempoDecorrido = end - begin;
		System.out.println("Tempo decorrido: " + tempoDecorrido + " ms");
		
	}
}
