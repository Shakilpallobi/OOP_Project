package UTIL;

import java.util.Random;

/**
 * A utility class providing methods for generating various types of random numbers.
 * It encapsulates a {@link Random} instance and offers convenience methods
 * for uniform and exponential distributions, as well as seed control for reproducibility.
 */
public class RandomUtil {
	/**
	 * The underlying {@link Random} object used for generating random numbers.
	 */
	private Random random;

	/**
	 * Constructs a new RandomUtil instance.
	 * Initializes the internal {@link Random} object with a default, time-dependent seed.
	 */
	public RandomUtil() {
		this.random = new Random();
	}

	/**
	 * Returns a pseudo-random, uniformly distributed {@code double} value
	 * between 0.0 (inclusive) and 1.0 (exclusive).
	 *
	 * @return A uniformly distributed pseudo-random {@code double} between 0.0 and 1.0.
	 */
	public double getUniform() {
		return random.nextDouble();
	}

	/**
	 * Returns a pseudo-random number drawn from an exponential distribution
	 * with a specified mean ($\text{aMean}$).
	 * The formula used is $X = -\ln(1 - U) \cdot \text{aMean}$, where $U$ is a
	 * uniformly distributed random number in the interval $[0, 1)$ and $\lambda = 1 / \text{aMean}$
	 * is the rate parameter.
	 *
	 * @param aMean The desired mean of the exponential distribution ($\mu$).
	 * @return A pseudo-random number from an exponential distribution with the given mean.
	 */
	public double getExponential(double aMean) {
		double u = getUniform();
		// Using the inverse transform sampling method for exponential distribution.
		// The formula is -ln(1-U) / lambda, where lambda = 1/aMean.
		// So, -ln(1-U) * aMean
		return -Math.log(1 - u) * aMean;
	}

	/**
	 * Sets the seed for the random number generator.
	 * Setting a specific seed allows for reproducible sequences of random numbers,
	 * which is useful for debugging and testing simulations.
	 *
	 * @param seed The seed value to be used by the {@link Random} instance.
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}
}