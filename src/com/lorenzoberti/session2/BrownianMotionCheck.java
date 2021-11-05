/**
 * 
 */
package com.lorenzoberti.session2;

/**
 * This class checks if the implementation of your Brownian motion is good, i.e.
 * if the properties of the Brownian motion are satisfied.
 * 
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionCheck {

	private static int numberOfPaths = 100000;
	private static int numberOfTimeSteps = 100;
	private static double timeStep = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// we create an object of type BrownianMotionInterface
		BrownianMotionInterface brownian = new BrownianMotionSimple(numberOfPaths, numberOfTimeSteps, timeStep);

		// we take the process at a given time (just implement the right method)
		// double[] process1 = ?;

		// check the average (it should be 0)
		// System.out.println("The average is: " + getAverage(process1));

		// check the variance (it should be == time)
		// System.out.println("The variance is: " + getVariance(process1));

		// check the covariance between the two selected process (it should be min(s,t))
		// double[] process2 = ?; (just implement the right method)
		// System.out.println("The covariance is: " + getCovariance(process, process2));


	}

	/**
	 * This method computes the sample average of a given array
	 * 
	 * @param array
	 * @return the average
	 */
	private static double getAverage(double[] array) {

		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		double average = (sum / numberOfPaths);
		return average;

	}

	/**
	 * This method computes the sample variance of a given array
	 * 
	 * @param array
	 * @return the variance
	 */
	private static double getVariance(double[] array) {

		double sumSquared = 0;
		for (int i = 0; i < array.length; i++) {
			sumSquared += Math.pow(array[i], 2);
		}

		double variance = sumSquared / numberOfPaths - Math.pow(getAverage(array), 2);

		return variance;
	}

	/**
	 * This method computes the sample covariance between two array
	 * 
	 * @param array1
	 * @param array2
	 * @return the covariance
	 */
	private static double getCovariance(double[] array1, double[] array2) {

		double[] product = new double[array1.length];
		for (int i = 0; i < array1.length; i++) {
			product[i] = array1[i] * array2[i];
		}

		double covariance = getAverage(product) - getAverage(array1) * getAverage(array2);

		return covariance;

	}

}
