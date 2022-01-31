package com.lorenzoberti.session11;

import java.text.DecimalFormat;

import com.lorenzoberti.session7.CallOption;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class tests the implementation of the classes BlackScholesEulerShceme
 * and LogEulerScheme for the pricing of a call option.
 * 
 * @author Lorenzo Berti
 *
 */
public class CallTest {

	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000; // 1*10^5
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.1;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		int numberOfExperiments = 10;

		double sumStandardEulerPrice = 0.0;
		double sumTransformEulerPrice = 0.0;

		double sumStandardEulerError = 0.0;
		double sumTransformEulerError = 0.0;

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		double strike = 100.0;
		double maturity = finalTime;

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);

		// Let's ceate the object CallOption
		final CallOption call = new CallOption(maturity, strike);

		// We need the discount factor as RandomVariable
		RandomVariable discountFactor = new RandomVariableFromDoubleArray(Math.exp(-riskFree * maturity));

		System.out.println("Number of experiments: " + numberOfExperiments + "\n");

		// System.out.println("\t\t\t\t Price \t Error \n");

		for (int i = 0; i < numberOfExperiments; i++) {

			AbstractEulerScheme standardEulerBS = new BlackScholesEulerScheme(numberOfPaths, initialValue, times,
					riskFree, sigma);
			AbstractEulerScheme transformEulerBS = new LogEulerScheme(numberOfPaths, initialValue, times, riskFree,
					sigma);

			double priceStandardEuler = call.getPriceAsDouble(standardEulerBS, discountFactor);
			double priceTransformEuler = call.getPriceAsDouble(transformEulerBS, discountFactor);

			sumStandardEulerError += Math.abs(analyticPrice - priceStandardEuler) / analyticPrice;
			sumTransformEulerError += Math.abs(analyticPrice - priceTransformEuler) / analyticPrice;

			sumStandardEulerPrice += priceStandardEuler;
			sumTransformEulerPrice += priceTransformEuler;

//		System.out.println();
//		System.out.println("Analytic price BS............: " + FORMATTERPOSITIVE.format(analyticPrice) + "\t"
//				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPrice - analyticPrice) / analyticPrice));
//		System.out.println("Standard Euler Process.......: " + FORMATTERPOSITIVE.format(priceStandardEuler) + "\t"
//				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPrice - priceStandardEuler) / analyticPrice));
//		System.out
//				.println("Log Euler Process............: " + FORMATTERPOSITIVE.format(priceTransformEuler) + "\t"
//				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPrice - priceTransformEuler) / analyticPrice));
//
//		System.out.println();

		}

		double averageStandardEulerError = sumStandardEulerError / numberOfExperiments;
		double averageTransformEulerError = sumTransformEulerError / numberOfExperiments;

		double averageStandardEulerPrice = sumStandardEulerPrice / numberOfExperiments;
		double averageTransformEulerPrice = sumTransformEulerPrice / numberOfExperiments;

		System.out.println("Analytic price BS.....................: " + FORMATTERPOSITIVE.format(analyticPrice));

		System.out.println(
				"Average price standard Euler scheme...: " + FORMATTERPOSITIVE.format(averageStandardEulerPrice));
		System.out.println(
				"Average price log Euler scheme........: " + FORMATTERPOSITIVE.format(averageTransformEulerPrice));
		System.out.println(
				"Average error standard Euler scheme...: " + FORMATTERPERCENTAGE.format(averageStandardEulerError));
		System.out.println(
				"Average error log Euler scheme........: " + FORMATTERPERCENTAGE.format(averageTransformEulerError));

	}

}
