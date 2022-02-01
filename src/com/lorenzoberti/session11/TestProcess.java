package com.lorenzoberti.session11;

import java.text.DecimalFormat;
import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class tests the implementation of the classes BlackScholesEulerShceme
 * and LogEulerScheme.
 * 
 * @author Lorenzo Berti
 *
 */
public class TestProcess {

	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000; // 1*10^5
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		int numberOfExperiments = 3;

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		// BrownianMotionMultiD brownian = new BrownianMotionD(times, 1, numberOfPaths);

		double initialValue = 100.0;
		double mu = 0.05;
		double sigma = 0.2;

		DoubleUnaryOperator analyticProcess = x -> {
			double drift = (mu - sigma * sigma * 0.5) * finalTime;
			double diffusion = sigma * x;

			return initialValue * Math.exp(drift + diffusion);

		};

//		AbstractEulerScheme euler = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, mu, sigma);
//		AbstractEulerScheme logEuler = new LogEulerScheme(numberOfPaths, initialValue, times, mu, sigma);
//
//		RandomVariable lastValueAnalytic = brownian.getBrownianMotionAtSpecificTime(0, finalTime)
//				.apply(analyticProcess);
//		double averageAnalytic = lastValueAnalytic.getAverage();
//		double varianceAnalytic = lastValueAnalytic.getVariance();

//		System.out.println("Analytic average......: " + FORMATTERPOSITIVE.format(averageAnalytic));
//		System.out.println("Analytic variance.....: " + FORMATTERPOSITIVE.format(varianceAnalytic));

//		System.out.println();
//
//		System.out.println("\t\t\t Value \t\t Error \n");

		for (int i = 0; i < numberOfExperiments; i++) {

			BrownianMotionMultiD brownian = new BrownianMotionD(times, 1, numberOfPaths);

			AbstractEulerScheme euler = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, mu, sigma);
			AbstractEulerScheme logEuler = new LogEulerScheme(numberOfPaths, initialValue, times, mu, sigma);

			RandomVariable lastValueAnalytic = brownian.getBrownianMotionAtSpecificTime(0, finalTime)
					.apply(analyticProcess);
			double averageAnalytic = lastValueAnalytic.getAverage();
			double varianceAnalytic = lastValueAnalytic.getVariance();

			System.out.println("Analytic average......: " + FORMATTERPOSITIVE.format(averageAnalytic));
			System.out.println("Analytic variance.....: " + FORMATTERPOSITIVE.format(varianceAnalytic));

			System.out.println();

			System.out.println("\t\t\t Value \t\t Error \n");

			RandomVariable lastValueEuler = euler.getProcessAtGivenTime(finalTime);
			RandomVariable lastValueLogEuler = logEuler.getProcessAtGivenTime(finalTime);

			double averageEuler = lastValueEuler.getAverage();
			double averageLogEuler = lastValueLogEuler.getAverage();

			double varianceEuler = lastValueEuler.getVariance();
			double varianceLogEuler = lastValueLogEuler.getVariance();

			double averageEulerError = Math.abs(averageAnalytic - averageEuler) / averageAnalytic;
			double averageLogEulerError = Math.abs(averageAnalytic - averageLogEuler) / averageAnalytic;

			double varianceEulerError = Math.abs(varianceAnalytic - varianceEuler) / varianceAnalytic;
			double varianceLogEulerError = Math.abs(varianceAnalytic - varianceLogEuler) / varianceAnalytic;

			System.out.println("Euler average.........: " + FORMATTERPOSITIVE.format(averageEuler) + "\t Δm = "
					+ FORMATTERPERCENTAGE.format(averageEulerError));
			System.out.println("Log Euler average.....: " + FORMATTERPOSITIVE.format(averageLogEuler) + "\t Δm = "
					+ FORMATTERPERCENTAGE.format(averageLogEulerError));

			System.out.println();

			System.out.println("Euler variance........: " + FORMATTERPOSITIVE.format(varianceEuler) + "\t ΔV = "
					+ FORMATTERPERCENTAGE.format(varianceEulerError));
			System.out.println("Log Euler variance....: " + FORMATTERPOSITIVE.format(varianceLogEuler) + "\t ΔV = "
					+ FORMATTERPERCENTAGE.format(varianceLogEulerError));

			System.out.println();

		}

	}

}