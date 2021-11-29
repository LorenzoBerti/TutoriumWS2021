package com.lorenzoberti.session5;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to check of your simulation are "quite" good. You can use the
 * private method getAssetAtSpecific time to see if your implementation are good
 * enough. Note: the method getAssetAtSpecific generates directly and only the
 * process at the given time and thus not the paths! Your
 * BlackScholesAnalyticProcess should also generate the whole path...
 * 
 * @author Lorenzo Berti
 *
 */
public class ProcessTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		BrownianMotionMultiD brownian = new BrownianMotionD(times, 1, numberOfPaths);

		double initialValue = 100.0;
		double mu = 0.1;
		double sigma = 0.2;

		// Constructor of the EulerSchemeBlackScholes

		// Constructor of the BlackScholesAnalyticProcess

		// Take some value (for instance the last one) of your simulated processes

		RandomVariable lastValuePrivate = getAssetAtSpecificTime(brownian, 0, mu, sigma, initialValue, finalTime);

		// Take their average...

		double averagePrivate = lastValuePrivate.getAverage();

		// ...and print
		System.out.println("The Euler scheme average is: ");
		System.out.println("The analytic average is: ");
		System.out.println("The average with the private method is: " + averagePrivate);

		// Take their variance...

		double variancePrivate = lastValuePrivate.getVariance();

		// ...and print
		System.out.println("The Monte Carlo variance is: ");
		System.out.println("The analytic variance is: ");
		System.out.println("The variance with the private method is: " + variancePrivate);



	}

	// Private method that generate the value of the random variable at the given
	// time under
	// the Black Scholes model, i.e. the asset follows a geometric brownian motion
	private static RandomVariable getAssetAtSpecificTime(BrownianMotionMultiD brownian, int indexFactor,
			double riskFreeRate, double sigma, double initialValue, double time) {

		DoubleUnaryOperator geometricBrownian = b -> {
			return initialValue * Math.exp((riskFreeRate - sigma * sigma * 0.5) * time + sigma * b);
		};

		RandomVariable asset = brownian.getBrownianMotionAtSpecificTime(indexFactor, time).apply(geometricBrownian);

		return asset;

	}


}
