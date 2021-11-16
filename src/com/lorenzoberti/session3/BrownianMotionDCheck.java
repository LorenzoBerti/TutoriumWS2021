/**
 * 
 */
package com.lorenzoberti.session3;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class checks if your implementation of the interface
 * BrownianMotionMultiD generate a "good" Brownian motion. Have a look to the
 * methods of the Interface RandomVariable.
 * 
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionDCheck {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfFactors = 2;
		int numberOfPaths = 100000;

		double initialTime = 0.0;
		double finalTime = 10.0;
		double deltaT = 1.0;
		int numberOfTimeSteps = (int) (finalTime / deltaT);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

		// Constructor
		BrownianMotionMultiD brownian = new BrownianMotionD(times, numberOfFactors, numberOfPaths);

		// check the average and the variance
		RandomVariable firstBrownian = brownian.getBrownianMotionAtSpecificTimeIndex(0, 10);
		System.out.println("Average: " + firstBrownian.getAverage());
		System.out.println("Variance: " + firstBrownian.getVariance());

		// check the covariance
		RandomVariable secondBrownian = brownian.getBrownianMotionAtSpecificTimeIndex(0, 8);
		System.out.println("Covariance:  " + secondBrownian.covariance(firstBrownian).getAverage());

		System.out.println();

		// check the brownian increment (they should be stationary and independent)
		System.out.println("Brownian increment test:\t");
		RandomVariable firstBrownianIncrement = brownian.getBrownianIncrement(3, 0);
		System.out.println("Average of brownian increment: " + firstBrownianIncrement.getAverage());
		System.out.println("Variance of brownian increment: " + firstBrownianIncrement.getVariance());
		RandomVariable secondBrownianIncrement = brownian.getBrownianIncrement(4, 0);
		System.out.println("Independence (covariance test): "
				+ firstBrownianIncrement.covariance(secondBrownianIncrement).getAverage());

		System.out.println();

		// check the independence (Covariance test)
		System.out.println("Independence between factors test:");
		RandomVariable secondFactor = brownian.getBrownianMotionAtSpecificTimeIndex(1, 5);
		System.out.println("Independence (covariance test): " + secondFactor.covariance(firstBrownian).getAverage());

	}

}
