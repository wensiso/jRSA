package br.com.fat.jrsa;

public class RSAKey {
	
	public static Long MIN = 64L;
	public static Long MAX = 512L;
	
//	public static Long MIN = 3L;
//	public static Long MAX = 23L;
	
	private Long p, q, n, z, e, d;
	private Long[] pubkey, privkey;
	
	public RSAKey(Long p, Long q) {
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

	public RSAKey(int p, int q) {
		this((long) p, (long) q);
	}
	
	public RSAKey() {
		this(MIN,MIN);
	}
	
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
	
	public Long[] getPublic() {
		return pubkey;
	}
	
	public Long[] getPrivate() {
		return privkey;
	}
	
	
	

	
	

}
