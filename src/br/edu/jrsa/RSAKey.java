package br.edu.jrsa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RSAKey {
	
	public static Long MIN = 64L;
	public static Long MAX = 512L;
	
//	public static Long MIN = 3L;
//	public static Long MAX = 6L;
//	
	private Long p, q, n, z, e, d;
	private Long[] pubkey, privkey;
	
	public RSAKey(Long p, Long q) {
		this.buildKeys(p, q);
	}

	public RSAKey(int p, int q) {
		this((long) p, (long) q);
	}
	
	public RSAKey() {
	}
	
	
	/**
	 * Generate the public and private keys, given p and q
	 * @param p
	 * @param q
	 * @return 
	 */
	private void buildKeys(Long p, Long q) {
		if(p > MIN && Numbers.isPrime(p))
			this.p = p;
		else
			this.p = Numbers.getRandomPrime(MIN, MAX);
		
		if(q > MIN && Numbers.isPrime(q))
			this.q = q;
		else
			this.q = Numbers.getRandomPrime(MIN, MAX);
		
		this.n = this.p * this.q;
		this.z = (this.p - 1) * (this.q - 1);
		
		this.e = Numbers.getRandomPrime((long)this.n / 2, n-1);
		this.d = this.pick_d(false);
		
		this.pubkey = new Long[2];
		this.pubkey[0] = n;
		this.pubkey[1] = e;
		
		this.privkey = new Long[2];
		this.privkey[0] = n;
		this.privkey[1] = d;
	}
	
	/**
	 * Build public and private keys automatically
	 */
	public void autoBuildKeys() {
		this.buildKeys(MIN, MIN);
	}
	
	/**
	 * TODO Verify MDC between e and z
	 * @param n
	 * @param e
	 * @return
	 */
	public boolean setPubKey(Long n, Long e) {
		System.out.println(e <= n);
		if ((e <= n) /* && TODO Testar se é primo entre si */) {
			this.n = n;
			this.e = e;
			this.pubkey = new Long[2];
			this.pubkey[0] = n;
			this.pubkey[1] = e;
			return true;
		}
		return false;
	}
	
	/**
	 * TODO Verify d
	 * @param n
	 * @param d
	 * @return
	 */
	public boolean setPrivKey(Long n, Long d) {
		this.n = n;
		this.d = d;
		this.privkey = new Long[2];
		this.privkey[0] = this.n;
		this.privkey[1] = this.d;
		return true;
	}

	
	/**
	 * Pick d value
	 * @param verbose
	 * @return
	 */
	private Long pick_d(boolean verbose) {		
		Long error = -1L;
		for(Long d=2L; d <= this.z; ++d) {
			if((this.e * d) % this.z == 1) {
				if (verbose) {
					System.out.println("d found: " + d);
					System.out.println("e * d: " + this.z * d);
					System.out.println("(e * d) % z: " + (this.e * d) % this.z);
				}
				return d;
			}
		}
		System.out.println("d not found!");
		return error;
	}

	@Override
	public String toString() {
		return "RSAKey [p=" + p + ", q=" + q + ", n=" + n + ", z=" + z + ", e=" + e + ", d=" + d + "]";
	}
	
	/**
	 * 
	 * @return
	 */
	public Long[] getPublic() {
		return pubkey;
	}
	
	public Long[] getPrivate() {
		return privkey;
	}
	
	/**
	 * Save the chosen key in file 
	 * @param key
	 * @param path
	 */
	private void saveFileKey(Long[] key, String path) {
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter writer = new BufferedWriter(fw);		
			String str = "(" + key[0].toString() + ", " + key[1].toString() + ")";
			writer.write(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("\nErro: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void savePublicKey(String path) {
		this.saveFileKey(pubkey, path);
	}
	
	public void savePrivateKey(String path){
		this.saveFileKey(privkey, path);
	}
	
}
