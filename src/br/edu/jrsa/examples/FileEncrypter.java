package br.edu.jrsa.examples;

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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.edu.jrsa.RSA;
import br.edu.jrsa.RSAKey;

/**
 * 
 * To encrypt:
 * java Main -e data/sample/message.txt -o data/sample/encrypt_msg.dat  -k
 * 
 * To decrypt:
 * java Main -d data/sample/encrypt_msg.dat -o data/sample/decod_message.txt
 * 
 * @author wendell
 *
 */
public class FileEncrypter {

	static String prog = "jRSA File Encrypter";
	
//	static String rel_path = "../../../../";
	static String rel_path = "";
	static String pubk_filename = rel_path + "data/keys/pubk.txt";
	static String privk_filename = rel_path + "data/keys/privk.txt";
			
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		
		String progfull = prog + " - An educational tool for teaching the RSA algorithm\n";
		
		String usage_str = "\njava FileEncrypter -e <plain_text_file> -o <encrypted_file> [-k] [-v]\n"
				+ "java FileEncrypter -d <encrypted_file> -o <plain_text_file> [-k] [-v]\n"
				+ "\n";
		
		System.out.println(progfull);
		
		String msg_filename = rel_path + "data/sample/message.txt";
		String encrypt_filename = rel_path + "data/sample/encrypt_msg.dat";
		String out_filename = rel_path + "data/sample/decod_message.txt";
		
		boolean encrypt = false;
		boolean decrypt = false;
		
		CommandLineParser parser = new DefaultParser();	
		Options options = new Options();
		
		Option enc = Option.builder("e")
			    .longOpt( "encrypt" )
			    .desc( "plain text file"  )
			    .hasArg()
			    .argName( "msg_filename" )
			    .build();
		
		Option desenc = Option.builder("d")
			    .longOpt( "decrypt" )
			    .desc( "encrypted file"  )
			    .hasArg()
			    .argName( "encrypt_filename" )
			    .build();
		
		Option output = Option.builder("o")
			    .longOpt( "output" )
			    .desc( "output filename"  )
			    .hasArg()
			    .argName( "dest_filename" )
			    .build();
		
		Option test = Option.builder("t")
			    .longOpt( "test" )
			    .desc( "test the program, without write files"  )
			    .hasArg()
			    .argName( "message" )
			    .build();
		
		options.addOption(enc);
		options.addOption(desenc);
		options.addOption(output);
		options.addOption(test);
		
		options.addOption("k", false, "creates a new RSA key pair");
		options.addOption("v", false, "show plain text file on terminal");
	
		RSAKey key = new RSAKey();
				
		try {
			Scanner in = new Scanner(System.in);
			String useropt = "n";
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.hasOption("t")) {
				String test_str = cmd.getOptionValue("t");
				System.out.println("Test message: " + test_str);
				key.autoBuildKeys();
				System.out.println(key);
				RSA tester = new RSA(key);
				tester.testAll(test_str);
				System.exit(0);
			}
			
			if(cmd.hasOption("e")){
				msg_filename = cmd.getOptionValue("e");
				System.out.println("Plain text file: " + msg_filename);
				encrypt = true;
			} else if(cmd.hasOption("d")) {
				encrypt_filename = cmd.getOptionValue("d");
				System.out.println("Encrypted file: " + encrypt_filename);
				decrypt = true;
			} else {
				in.close();
				throw new ParseException("Please inform an input file!");
			}
			
			if(cmd.hasOption("o")) {
				out_filename = cmd.getOptionValue("o");
				System.out.println("Output file: " + out_filename + "\n");
			} else {
				in.close();
				throw new ParseException("Please inform output file!");
			}
			
			if(cmd.hasOption("k")) {
				if(decrypt) {
					in.close();
					throw new ParseException("You cannot create a RSA key pair when decrypting files...");
				}
			    System.out.println("Creating RSA key pair...");
			    key.autoBuildKeys();
			    key.savePublicKey(pubk_filename);
				key.savePrivateKey(privk_filename);
				System.out.println(key);
				
				System.out.println("Reuse the new RSA key pair? (Y/N) ");
				useropt = in.nextLine();
			} 
			if(useropt.startsWith("n") || useropt.startsWith("N")) {
				if (encrypt) {
					// ask for keys
					System.out.println("Enter public key (n, e): ");
					Long n = in.nextLong();
					Long e = in.nextLong();
					boolean sucess = key.setPubKey(n, e);
					System.out.println("Your public key: (" + n + ", " + e + ") " + sucess + "\n");
				} else if (decrypt) {
					System.out.println("Entre your private key (n, d): ");
					Long n = in.nextLong();
					Long d = in.nextLong();
					boolean sucess = key.setPrivKey(n, d);
					System.out.println("Your private key: (" + n + ", " + d + ") " + sucess + "\n");
				}
			}
			
			in.close();
			
			RSA encoder = new RSA(key);
			
			long begin = System.currentTimeMillis();
			if(encrypt)
				encrypt(msg_filename, out_filename, encoder);
			else if (decrypt)
				decode(encrypt_filename, out_filename, encoder);
			long end =  System.currentTimeMillis();
			System.out.println("\nTime spent: " + (end - begin) + " ms");
			
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(usage_str, options);
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
		
		System.out.println("\nOpenning plain text file: " + msg_filename);
		
		try {
			
			String msg = new String(Files.readAllBytes(Paths.get(msg_filename)));
						
			System.out.println("Encrypting file...");
			Long[] enc = encoder.encryptStr(msg);

			System.out.println("Saving encrypted file: " + encrypt_filename);		
			FileOutputStream out = new FileOutputStream(new File(encrypt_filename));
			DataOutputStream writer = new DataOutputStream(out);
			for(Long c: enc) {
				writer.writeLong(c);
			}
			writer.flush();
			writer.close();

		} catch (IOException e) {
			System.err.println("\nError: " + e.getMessage());
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

		System.out.println("\nOpening encrypted file: " + encrypt_filename);

		try {
			FileInputStream in = new FileInputStream(new File(encrypt_filename));
			DataInputStream reader = new DataInputStream(in);
			
			ArrayList<Long> clist = new ArrayList<Long>();

			while(reader.available() >= Long.BYTES)
				clist.add(reader.readLong());
			reader.close();
			Long[] enc = clist.toArray(new Long[0]);
			
			System.out.println("Decrypting file...");
			String msgdec = decoder.decryptLong(enc);

			System.out.println("Saving decrypted file: " + msgdec_filename);
			BufferedWriter out = new BufferedWriter(new FileWriter(msgdec_filename));
			out.write(msgdec);
			out.close();

		} catch (FileNotFoundException e) {
			System.err.println("\nFile not found: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("\nIO Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
