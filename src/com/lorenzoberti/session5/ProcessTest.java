package com.lorenzoberti.session5;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
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

		ProcessSimulation process = new EulerSchemeBlackScholes(brownian, initialValue, mu, sigma);

		ProcessSimulation processAnalytic = new BlackScholesAnalyticProcess(brownian, initialValue, mu, sigma);

		RandomVariable lastValue = process.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueAnalytic = processAnalytic.getProcessAtGivenTime(finalTime);
		RandomVariable lastValuePrivate = getAssetAtSpecificTime(brownian, 0, mu, sigma, initialValue, finalTime);

		double average = lastValue.getAverage();
		double averageAnalytic = lastValueAnalytic.getAverage();
		double averagePrivate = lastValuePrivate.getAverage();

		System.out.println("The Monte Carlo average is: " + average);
		System.out.println("The analytic average is: " + averageAnalytic);
		System.out.println("The average with the private method is: " + averagePrivate);

		double variance = lastValue.getVariance();
		double varianceAnalytic = lastValueAnalytic.getVariance();
		double variancePrivate = lastValuePrivate.getVariance();

		System.out.println("The Monte Carlo variance is: " + variance);
		System.out.println("The analytic variance is: " + varianceAnalytic);
		System.out.println("The variance with the private method is: " + variancePrivate);

		DoubleUnaryOperator simulatedProcessTrajectory = t -> {
			return process.getSpecificValueOfSpecificPath(10, (int) t);
		};

		DoubleUnaryOperator analyticProcessTrajectory = t -> {
			return processAnalytic.getSpecificValueOfSpecificPath(10, (int) t);
		};

		Plot2D plot = new Plot2D(0, times.getNumberOfTimes(), times.getNumberOfTimes() + 1, Arrays.asList(
				new Named<DoubleUnaryOperator>("Analytic", analyticProcessTrajectory),
				new Named<DoubleUnaryOperator>("Euler Scheme", simulatedProcessTrajectory))/* functions plotted */);

		plot.setYAxisNumberFormat(new DecimalFormat("0.0")).setTitle("Path of Simulated Process").setXAxisLabel("time")
				.setYAxisLabel("Process");

		plot.setIsLegendVisible(true);

		plot.show();

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
