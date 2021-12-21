/**
 * 
 */
package com.lorenzoberti.session7;

import java.text.DecimalFormat;

import com.lorenzoberti.session3.BrownianMotionMultiD;
import com.lorenzoberti.session5.BlackScholesAnalyticProcess;
import com.lorenzoberti.session5.ProcessSimulation;
import com.lorenzoberti.session6.AbstractEulerScheme;
import com.lorenzoberti.session6.BachelierEulerScheme;
import com.lorenzoberti.session6.BlackScholesEulerScheme;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to test your implementation of the interface
 * FinancialProductInterface.
 * 
 * @author Lorenzo Berti
 *
 */
public class OptionTest {

	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000;

		// Time parameters
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		// Model parameters
		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		// Option parameter
		double strike = 100.0;
		double maturity = finalTime;

		double forward = initialValue * Math.exp(riskFree * maturity);
		double payoffUnit = Math.exp(-riskFree * maturity);

		RandomVariable discountFactor = new RandomVariableFromDoubleArray(Math.exp(-riskFree * maturity));

		// Call option test

		FinancialProductInterface call = new CallOption(strike, maturity);

		// Process constructor: BlackScholesAnalyticProcess, BlackScholesEulerScheme and
		// BachelierEulerScheme
		ProcessSimulation processAnalyticBS = new BlackScholesAnalyticProcess(initialValue, riskFree, sigma, times,
				numberOfPaths);
		AbstractEulerScheme processEulerBS = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, riskFree,
				sigma);
		AbstractEulerScheme processEulerBachelier = new BachelierEulerScheme(numberOfPaths, initialValue, times,
				riskFree, sigma);

		// Get the price of the call option for each simulation process

		double callBS = call.getPriceAsDouble(processAnalyticBS, discountFactor);
		double callBSEuler = call.getPriceAsDouble(processEulerBS, discountFactor);
		double callBachelier = call.getPriceAsDouble(processEulerBachelier, discountFactor);

		// Analytic prices
		double analyticPriceBS = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);
		double analyticPriceBachelier = AnalyticFormulas.bachelierOptionValue(forward, sigma, maturity, strike,
				payoffUnit);

		// Print price and error (relative error)
		System.out.println("Call Test");
		System.out.println("                               Price         Error");
		System.out.println();
		System.out.println("Analytic price BS           : " + FORMATTERPOSITIVE.format(analyticPriceBS) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - analyticPriceBS) / analyticPriceBS));
		System.out.println("Euler BS process            : " + FORMATTERPOSITIVE.format(callBSEuler) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - callBSEuler) / analyticPriceBS));
		System.out.println("Analytic BS process         : " + FORMATTERPOSITIVE.format(callBS) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - callBS) / analyticPriceBS));
		// System.out.println();
		System.out.println("Analytic price Bachelier    : " + FORMATTERPOSITIVE.format(analyticPriceBachelier)
				+ "         " + FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - analyticPriceBachelier) / analyticPriceBachelier));
		System.out.println("Simulated Bachelier process : " + FORMATTERPOSITIVE.format(callBachelier) + "         "
				+ FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - callBachelier) / analyticPriceBachelier));

		// Exchange option test (As first asset you can use the previous one)

		// Model parameters
		double initialValue2 = 100.0;
		double sigma2 = 0.2;

		// Process constructor: BlackScholesAnalyticProcess and BlackScholesEulerScheme
		ProcessSimulation processAnalyticBS2 = new BlackScholesAnalyticProcess(initialValue2, riskFree, sigma2, times,
				numberOfPaths);
		AbstractEulerScheme processEulerBS2 = new BlackScholesEulerScheme(numberOfPaths, initialValue2, times, riskFree,
				sigma2);

		// Recall: we need the covariance between the two asset!

		BrownianMotionMultiD brownian1 = processAnalyticBS.getStochasticDriver();
		BrownianMotionMultiD brownian2 = processAnalyticBS2.getStochasticDriver();

		RandomVariable brownian1AtMaturity = brownian1.getBrownianMotionAtSpecificTime(0, maturity);
		RandomVariable brownian2AtMaturity = brownian2.getBrownianMotionAtSpecificTime(0, maturity);

		double covariance = brownian1AtMaturity.covariance(brownian2AtMaturity).getAverage();

		double vol = sigma * sigma - 2 * covariance * sigma * sigma2 + sigma2 * sigma2;

		vol = Math.sqrt(vol);

		// Constructor of the exchange option

		FinancialProductInterface exchangeBS = new ExchangeOption(maturity, processAnalyticBS);
		FinancialProductInterface exchangeEuler = new ExchangeOption(maturity, processEulerBS);

		// Take the prices

		double exchangeBSAnalytic = exchangeBS.getPriceAsDouble(processAnalyticBS2, discountFactor);
		double exchangeBSEuler = exchangeEuler.getPriceAsDouble(processEulerBS2, discountFactor);

		// Analytic price
		double exchangeAnalytic = AnalyticFormulas.blackScholesOptionValue(initialValue, 0, vol, maturity,
				initialValue2);

		// Print
		System.out.println("Exchange Test");
		System.out.println("Covariance: " + covariance);
		System.out.println("                               Price         Error");
		System.out.println();
		System.out.println("Analytic price              : " + FORMATTERPOSITIVE.format(exchangeAnalytic) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(exchangeAnalytic - exchangeAnalytic) / exchangeAnalytic));
		System.out.println("Euler BS process            : " + FORMATTERPOSITIVE.format(exchangeBSEuler) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(exchangeAnalytic - exchangeBSEuler) / exchangeAnalytic));
		System.out.println("Analytic BS process         : " + FORMATTERPOSITIVE.format(exchangeBSAnalytic) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(exchangeAnalytic - exchangeBSAnalytic) / exchangeBSAnalytic));

	}

}