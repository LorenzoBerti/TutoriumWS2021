/**
 * 
 */
package com.lorenzoberti.session2;

/**
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionCheck {

	private static int numberOfPaths = 100000;
	private static int numberOfTimeSteps = 100;
	private static double timeStep = 1.0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// we create an object of type BrownianMotionInterface
		// we create an object of type BrownianMotion
		BrownianMotionInterface brownian = new BrownianMotionSimple(numberOfPaths, numberOfTimeSteps, timeStep);

		// we take the process at a given time
		double[] process = brownian.getProcessAtTimeIndex(10);

		// check the average (it should be 0)
		System.out.println("The average is: " + getAverage(process));

		// check the variance (it should be == time)
		System.out.println("The variance is: " + getVariance(process));

		// check the covariance (it should be min(s,t))
		double[] process2 = brownian.getProcessAtTimeIndex(8);
		System.out.println("The covariance is: " + getCovariance(process, process2));

		brownian.printPath(10);

	}

	private static double getAverage(double[] array) {

		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		double average = (sum / numberOfPaths);
		return average;

	}

	private static double getVariance(double[] array) {

		double sumSquared = 0;
		for (int i = 0; i < array.length; i++) {
			sumSquared += Math.pow(array[i], 2);
		}

		double variance = sumSquared / numberOfPaths - Math.pow(getAverage(array), 2);

		return variance;
	}

	private static double getCovariance(double[] array1, double[] array2) {

		double[] product = new double[array1.length];
		for (int i = 0; i < array1.length; i++) {
			product[i] = array1[i] * array2[i];
		}

		double covariance = getAverage(product) - getAverage(array1) * getAverage(array2);

		return covariance;

	}

}