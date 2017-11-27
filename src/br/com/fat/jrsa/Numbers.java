package br.com.fat.jrsa;

import java.util.concurrent.ThreadLocalRandom;

public class Numbers {
	
	/**
	 * Verify if a number is a prime number
	 * 
	 * @param n
	 * @return True, if n is a prime number, or False otherwise
	 */
	public static boolean isPrime(Long n) {
		boolean isprime;
		if((n == 2) || (n!=1 && n%2 == 1)) 
			isprime = true;
		else
			isprime = false;
		
		int div = 3;
		while(div <= Math.sqrt(n) && isprime) {
			if (n % div == 0)
				isprime = false;
			div += 2;
		}
		
		return isprime;
	}
	
	/**
	 * Returns a random prime number between min and max
	 * 
	 * @param min
	 * @param max
	 * @return A random prime number
	 */
	public static Long getRandomPrime(Long min, Long max) {
		Long n = ThreadLocalRandom.current().nextLong(min, max);
		while(!isPrime(n))
			n = ThreadLocalRandom.current().nextLong(min, max);
		return n;
	}

}
