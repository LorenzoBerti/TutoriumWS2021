/**
 * 
 */
package com.lorenzoberti.session6;

import java.text.DecimalFormat;
import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session5.BlackScholesAnalyticProcess;
import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to implement the pricing of a call option using the different
 * class that implements the ProcessModel interface.
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

		int numberOfPaths = 100000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		double strike = 100.0;
		double maturity = finalTime;
		double forward = initialValue * Math.exp(riskFree * maturity);
		double payoffUnit = Math.exp(-riskFree * maturity);

		ProcessSimulation processAnalyticBS = new BlackScholesAnalyticProcess(initialValue, riskFree, sigma, times,
				numberOfPaths);
		AbstractEulerScheme processBS = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, riskFree,
				sigma);
		AbstractEulerScheme processBachelier = new BachelierEulerScheme(numberOfPaths, initialValue, times, riskFree,
				sigma);

		RandomVariable lastValueBS = processBS.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueBSAnalytic = processAnalyticBS.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueBachelier = processBachelier.getProcessAtGivenTime(finalTime);

		DoubleUnaryOperator payoff = x -> {
			return Math.max(x - strike, 0.0);
		};

		double priceBS = lastValueBS.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);
		double priceBSAnalytic = lastValueBSAnalytic.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);
		double priceBachelier = lastValueBachelier.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);

		double analyticPriceBS = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);
		double analyticPriceBachelier = AnalyticFormulas.bachelierOptionValue(forward, sigma, maturity, strike,
				payoffUnit);
		
		System.out.println("                               Price         Error");
		System.out.println();
		System.out.println(
				"Analytic price BS           : " + FORMATTERPOSITIVE.format(analyticPriceBS) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - analyticPriceBS) / analyticPriceBS));
		System.out
				.println("Simulated BS process        : " + FORMATTERPOSITIVE.format(priceBS) + "        "
						+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - priceBS) / analyticPriceBS));
		System.out.println(
				"Analytic BS process         : " + FORMATTERPOSITIVE.format(priceBSAnalytic) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - priceBSAnalytic) / analyticPriceBS));
		// System.out.println();
		System.out.println(
				"Analytic price Bachelier    : " + FORMATTERPOSITIVE.format(analyticPriceBachelier) + "         "
				+ FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - analyticPriceBachelier) / analyticPriceBachelier));
		System.out.println("Simulated Bachelier process : " + FORMATTERPOSITIVE.format(priceBachelier)
				+ "         "
				+ FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - priceBachelier) / analyticPriceBachelier));


	}


}

