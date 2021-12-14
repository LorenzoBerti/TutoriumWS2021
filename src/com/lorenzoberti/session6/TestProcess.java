/**
 * 
 */
package com.lorenzoberti.session6;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;
import com.lorenzoberti.session5.BlackScholesAnalyticProcess;
import com.lorenzoberti.session5.EulerSchemeBlackScholes;
import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.stochastic.RandomVariable;
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

		int numberOfPaths = 100000;
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
		ProcessSimulation processAnalytic = new BlackScholesAnalyticProcess(brownian, initialValue, mu, sigma);
		ProcessSimulation process = new EulerSchemeBlackScholes(brownian, initialValue, mu, sigma);
		AbstractEulerScheme processNew = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, mu, sigma);

		// Take the average and the variance and print the values
		RandomVariable lastValueAnalytic = processAnalytic.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueOld = process.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueNew = processNew.getProcessAtGivenTime(finalTime);

		double averageAnalytic = lastValueAnalytic.getAverage();
		double averageOld = lastValueOld.getAverage();
		double averageNew = lastValueNew.getAverage();

		System.out.println("The analytic average is: " + averageAnalytic);
		System.out.println("The old average is     : " + averageOld);
		System.out.println("The new average is     : " + averageNew);

		System.out.println();

		double varianceAnalytic = lastValueAnalytic.getVariance();
		double varianceOld = lastValueOld.getVariance();
		double varianceNew = lastValueNew.getVariance();

		System.out.println("The analytic variance is: " + varianceAnalytic);
		System.out.println("The old variance is     : " + varianceOld);
		System.out.println("The new variance is     : " + varianceNew);


	}

}