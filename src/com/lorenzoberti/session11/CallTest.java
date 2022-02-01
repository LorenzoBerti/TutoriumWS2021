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
	static final DecimalFormat FORMATTERPERCENTAGEDATA = new DecimalFormat("0.00%");

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

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		double initialValue = 100.0;
		double riskFree = 0.05;
		double sigma = 0.2;

		double strike = 100.0;
		double maturity = finalTime;

		// Let's ceate the object CallOption
		final CallOption call = new CallOption(strike, maturity);

		// We need the discount factor as RandomVariable
		RandomVariable discountFactor = new RandomVariableFromDoubleArray(Math.exp(-riskFree * maturity));

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);

		System.out.println("Data: \n");
		System.out.println("Risk free rate..........: " + FORMATTERPERCENTAGEDATA.format(riskFree));
		System.out.println("Std. deviation..........: " + FORMATTERPERCENTAGEDATA.format(sigma));
		System.out.println("Initial value...........: " + initialValue);
		System.out.println("Option strike...........: " + strike);
		System.out.println("Option maturity.........: " + maturity);
		System.out.println("Analytic price BS.......: " + FORMATTERPOSITIVE.format(analyticPrice));

		System.out.println("---------------------------------------------------------------------------------------\n");

		// at each loop we increase the number of paths of a factor 10
		for (int j = 0; j < 1; j++) {

			double sumStandardEulerPrice = 0.0;
			double sumTransformEulerPrice = 0.0;

			double sumStandardEulerError = 0.0;
			double sumTransformEulerError = 0.0;

			System.out.println("Number of experiments...: " + numberOfExperiments);
			System.out.println("Number of paths.........: " + numberOfPaths);
			System.out.println("Number of time steps....: " + numberOfTimeSteps + "\n");

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

		}

		double averageStandardEulerError = sumStandardEulerError / numberOfExperiments;
		double averageTransformEulerError = sumTransformEulerError / numberOfExperiments;

		double averageStandardEulerPrice = sumStandardEulerPrice / numberOfExperiments;
		double averageTransformEulerPrice = sumTransformEulerPrice / numberOfExperiments;

		double standardEulerError = Math.abs(analyticPrice - averageStandardEulerPrice) / analyticPrice;
		double transformEulerError = Math.abs(analyticPrice - averageTransformEulerPrice) / analyticPrice;

		System.out.println(
				"Average price standard Euler scheme...: " + FORMATTERPOSITIVE.format(averageStandardEulerPrice)
						+ "\t error: " + FORMATTERPERCENTAGE.format(standardEulerError));
		System.out.println(
				"Average price log Euler scheme........: " + FORMATTERPOSITIVE.format(averageTransformEulerPrice)
						+ "\t error: " + FORMATTERPERCENTAGE.format(transformEulerError));
		System.out.println(
				"Average error standard Euler scheme...: " + FORMATTERPERCENTAGE.format(averageStandardEulerError));
		System.out.println(
				"Average error log Euler scheme........: " + FORMATTERPERCENTAGE.format(averageTransformEulerError));

		System.out.println("---------------------------------------------------------------------------------------\n");

		numberOfPaths *= 10;

	}

}

}