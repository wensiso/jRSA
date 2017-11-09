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
import org.apache.commons.cli.ParseException;

import br.com.fat.jrsa.RSAKey;
import br.com.fat.jrsa.RSA;

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
public class Main {

	static String pubk_filename = "./data/keys/pubk.txt";
	static String privk_filename = "./data/keys/privk.txt";
			
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		
		String prog = "jRSA - Uma ferramenta educacional para ensino do algoritmo RSA\n";
		String usage = "Modo de uso:\n"
				+ "\tjava jRSA -e <open_file> -o <encrypted_file> [-k] [-v]\n"
				+ "\tjava jRSA -d <encrypted_file> -o <open_file> [-k] [-v]";
		
		System.out.println(prog);
		
		String msg_filename = "./data/sample/message.txt";
		String encrypt_filename = "./data/sample/encrypt_msg.dat";
		String out_filename = "./data/sample/decod_message.txt";
		
		boolean encrypt = false;
		boolean decrypt = false;
		
		CommandLineParser parser = new DefaultParser();	
		Options options = new Options();
		
		Option enc = Option.builder("e")
			    .longOpt( "encrypt" )
			    .desc( "arquivo para encriptar"  )
			    .hasArg()
			    .argName( "msg_filename" )
			    .build();
		
		Option desenc = Option.builder("d")
			    .longOpt( "desencrypt" )
			    .desc( "arquivo para desencriptar"  )
			    .hasArg()
			    .argName( "encrypt_filename" )
			    .build();
		
		Option output = Option.builder("o")
			    .longOpt( "output" )
			    .desc( "arquivo de saída"  )
			    .hasArg()
			    .argName( "dest_filename" )
			    .build();
		
		options.addOption(enc);
		options.addOption(desenc);
		options.addOption(output);
		
		options.addOption("k", false, "gera um novo par de chaves");
		options.addOption("v", false, "imprime arquivo aberto");
		
		RSAKey key = new RSAKey();
		
		try {
			Scanner in = new Scanner(System.in);
			String useropt = "n";
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.hasOption("e")){
				msg_filename = cmd.getOptionValue("e");
				System.out.println("Arquivo para encriptar: " + msg_filename);
				encrypt = true;
			} else if(cmd.hasOption("d")) {
				encrypt_filename = cmd.getOptionValue("d");
				System.out.println("Arquivo para encriptar: " + encrypt_filename);
				decrypt = true;
			} else {
				throw new ParseException("Arquivo de origem não informado");
			}
			
			if(cmd.hasOption("o")) {
				out_filename = cmd.getOptionValue("o");
				System.out.println("Arquivo de destino: " + out_filename + "\n");
			} else {
				throw new ParseException("Arquivo de destino não informado");
			}
			
			if(cmd.hasOption("k")) {
				if(decrypt)
					throw new ParseException("Não gerar nova chave para desencriptar...");
			    System.out.println("Gerando as chaves");
			    key.autoBuildKeys();
			    key.savePublicKey(pubk_filename);
				key.savePrivateKey(privk_filename);
				System.out.println(key);
				
				System.out.println("Reutilizar a chave recem-gerada? (S/N) ");
				useropt = in.nextLine();
			} 
			if(useropt.startsWith("n") || useropt.startsWith("N")) {
				if (encrypt) {
					// ask for keys
					System.out.println("Informe a chave pública (n, e): ");
					Long n = in.nextLong();
					Long e = in.nextLong();
					key.setPubKey(n, e);
					System.out.println("Sua chave pública: (" + n + ", " + e + ")\n");
				} else if (decrypt) {
					System.out.println("Informe a chave privada (n, d): ");
					Long n = in.nextLong();
					Long d = in.nextLong();
					key.setPrivKey(n, d);
					System.out.println("Sua chave privada: (" + n + ", " + d + ")\n");
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
			
			System.out.println("\nTempo decorrido: " + (end - begin) + " ms");
			
		} catch (ParseException e) {
			System.out.println(e.getMessage());
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
