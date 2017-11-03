package br.com.fat.jrsa;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.cli.*;


public class Main {

	static String pubk_filename = "./data/keys/pubk.txt";
	static String privk_filename = "./data/keys/privk.txt";

	static String msg_filename = "./data/sample/message.txt";
	static String encrypt_filename = "./data/sample/encrypt_msg.dat";
	static String msgdec_filename = "./data/sample/decod_message.txt";
			
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		
		String prog = "jRSA - A RSA education tool writen in Java\n";
		String usage = "\tjava jRSA -e <open_file> -o <encrypted_file> [-k] [-v]\n"
				+ "\tjava jRSA -d <encrypted_file> -o <open_file> [-k] [-v]";
		
		System.out.println(prog);
		
		CommandLineParser parser = new DefaultParser();
		
		Options options = new Options();
		options.addOption("k", false, "gera um novo par de chaves");
		
		
		RSAKey key = new RSAKey();
		
		try {
			Scanner in = new Scanner(System.in);
			String useropt = "n";
			CommandLine cmd = parser.parse(options, args);
			if(cmd.hasOption("k")) {
			    System.out.println("Gerando as chaves");
			    key.autoBuildKeys();
			    key.savePublicKey(pubk_filename);
				key.savePrivateKey(privk_filename);
				System.out.println(key);
				
				System.out.println("Utilizar a chave recem-gerada? (S/N) ");
				useropt = in.nextLine();
			} 
			if(useropt.startsWith("n") || useropt.startsWith("N")) {
				//ask for keys
				System.out.println("Informe a chave pública (n, e): ");
				Long n = in.nextLong();
				Long e = in.nextLong();
				System.out.println("Sua chave pública: (" + n + ", " + e + ")\n");
				System.out.println("Informe a chave privada (d): ");
				Long d = in.nextLong();
				System.out.println("Sua chave privada: (" + n + ", " + d + ")\n");
				
				key.setPubKey(n, e);
				key.setPrivKey(n, d);
			}
			
			in.close();
			
			RSA encoder = new RSA(key);
			
			//encoder.testAll("create view cotistas as\n"
			//		+ "select * from alunos where cota=\"True\"");
			
			long begin = System.currentTimeMillis();
			encrypt(msg_filename, encrypt_filename, encoder);
			decode(encrypt_filename, msgdec_filename, encoder);
			long end =  System.currentTimeMillis();
			
			System.out.println("\nTempo decorrido: " + (end - begin) + " ms");
			
		} catch (ParseException e) {
			System.out.println(usage + "\n");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Encrypts an text file
	 * 
	 * @param msg_filename
	 * @param encrypt_filename
	 * @param encoder
	 */
	private static void encrypt(String msg_filename, String encrypt_filename, RSA encoder) {
		
		System.out.println("\nAbrindo arquivo da mensagem original: " + msg_filename);
		
		try {
			
			String msg = new String(Files.readAllBytes(Paths.get(msg_filename)));
						
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
			
			System.out.println("Desencriptando arquivo...");
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
	
}
