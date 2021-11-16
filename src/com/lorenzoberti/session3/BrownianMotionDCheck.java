/**
 * 
 */
package com.lorenzoberti.session3;

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
		// Write the constructor

		// Check the average and the variance
		System.out.println("Brownian average and variance test:");

		// Write your code here

		// Check the covariance (between the same element of the brownian vector at
		// different times)
		System.out.println("Brownian covariance test:");

		// Write your code here



		// Check the brownian increment (they should be stationary and independent)
		System.out.println("Brownian increment test:");

		// Write you code here



		// check the independence (Covariance test)
		System.out.println("Independence between factors test:");

		// Write your code here


	}

}
