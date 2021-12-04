/**
 * 
 */
package com.lorenzoberti.session6;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to test your process implementation.
 * 
 * @author Lorenzo Berti
 *
 */
public class TestProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 1000000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.1;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		BrownianMotionMultiD brownian = new BrownianMotionD(times, 1, numberOfPaths);

		double initialValue = 100.0;
		double mu = 0.1;
		double sigma = 0.2;

		// Write your constructors ("old" EulerSchemeBlackScholes, "new"
		// BlackScholesEulerScheme, BlackScholesAnalyticProcess)


		// Take the average and the variance and print the values


	}

}