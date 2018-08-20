package br.edu.jrsa;

import java.util.concurrent.ThreadLocalRandom;

public class Numbers {
	
	/**
	 * Verify if a number is a prime number
	 * 
	 * @param n
	 * @return True, if n is a prime number, or False otherwise
	 */
	public static boolean isPrime(Long n) {
		
		if (n == 2)
			return true;
		
		if ((n <= 1) || (n%2)==0)
			return false;
		
		int div = 3;
		while(div <= Math.sqrt(n)) {
			if (n % div == 0)
				return false;
			div += 2;
		}
		return true;
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
