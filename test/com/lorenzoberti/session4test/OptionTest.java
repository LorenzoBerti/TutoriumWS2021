package com.lorenzoberti.session4test;

import java.util.function.DoubleUnaryOperator;

import org.junit.jupiter.api.Test;

import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this test class to check if your price are "quite good". Feel free to
 * change the parameters as you prefer.
 * 
 * @author Lorenzo Berti
 *
 */

class OptionTest {

	double tolerance = 0.05;

	int numberOfFactors = 2;
	int numberOfPaths = 100000;

	// Time parameter
	double initialTime = 0.0;
	double finalTime = 10.0;
	double deltaT = 1.0;
	int numberOfTimeSteps = (int) (finalTime / deltaT);

	double maturity = finalTime;

	double timeEval = 0.0;

	// Model parameter
	double firstAssetInitial = 100.0;
	double secondAssetInitial = 100.0;

	double firstAssetVolDouble = 0.3;
	double secondAssetVolDouble = 0.5;

	double riskFree = 0.05;

	// The discount factor in the classic Black-Scholes model
	double discountFactor = Math.exp((timeEval - maturity) * riskFree);

	TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

	// Constructor of our Brownian motion
	BrownianMotionMultiD brownian;

	@Test
	void exchangeTest() {

		// Use the method getAssetAtSpecificTime to find the two assets at the maturity

		// Write here the constructor of your class for the Exchange option

		// get the payoff of the Exchange option

		// get the price of the Exchange option


		double covariance = brownian.getBrownianMotionAtSpecificTime(0, maturity)
				.covariance(brownian.getBrownianMotionAtSpecificTime(1, maturity)).getAverage();

		double vol = firstAssetVolDouble * firstAssetVolDouble
				- 2 * covariance * firstAssetVolDouble * secondAssetVolDouble
				+ secondAssetVolDouble * secondAssetVolDouble;

		vol = Math.sqrt(vol);

		double analyticPriceExchange = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, 0, vol, maturity,
				secondAssetInitial);

		// Check if your price is near to the analytic one

		System.out.println("Exchange test: ");

		System.out.println("The Monte Carlo price is: ");

		System.out.println("The analytic price is: " + analyticPriceExchange);

		System.out.println();

	}

	@Test
	void callTest() {

		double strike = firstAssetInitial;

		// Use the method getAssetAtSpecificTime to find the asset at the maturity

		// Write here the constructor of your class for the Call

		// get the payoff of the Call

		// get the price of the Call

		double analyticPriceCall = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, riskFree,
				firstAssetVolDouble, maturity, strike);

		// Check if your price is near to the analytic one

		System.out.println("Call test: ");

		System.out.println("The Monte Carlo price is: ");

		System.out.println("The analytic price is: " + analyticPriceCall);

		System.out.println();
	}

	@Test
	void capletTest() {

		// paymentDate: T2,
		// fixingDate: T1
		double paymentDate = maturity;
		double fixingDate = maturity - deltaT;

		double initialLiborRate = 0.05;
		double liborVol = 0.3;

		double discountFactorAtMaturity = 0.90;

		// Use the method getAssetAtSpecificTime to find the liborRate at the fixing
		// date

		double strikeCaplet = 0.05;
		double notional = 1000.0;

		double timeLength = paymentDate - fixingDate;

		// Write here the constructor of your class for the Caplet

		// get the payoff of the Caplet

		// get the price of the Caplet

		double analyticPriceCaplet = notional * discountFactorAtMaturity * timeLength
				* AnalyticFormulas.blackScholesOptionValue(initialLiborRate, 0, liborVol, timeLength, strikeCaplet);

		// Check if your price is near to the analytic one

		System.out.println("Caplet test: ");

		System.out.println("The Monte Carlo price is: ");

		System.out.println("The analytic price is: " + analyticPriceCaplet);

		System.out.println();

	}

	/**
	 * This private method returns the stochastic process as object of type
	 * RandomVariable at the given time under the Black-Scholes model, i.e. we
	 * suppose that the assets follow a geometric brownian motion.
	 * 
	 * @param the          (multi-dimension) brownian motion
	 * @param the          indexFactor of the brownian motion
	 * @param riskFree
	 * @param sigma
	 * @param initialValue
	 * @param time
	 * @return the stochastic process at the given time
	 */
	private static RandomVariable getAssetAtSpecificTime(BrownianMotionMultiD brownian, int indexFactor,
			double riskFree, double sigma, double initialValue, double time) {

		DoubleUnaryOperator geometricBrownian = b -> {
			return initialValue * Math.exp((riskFree - sigma * sigma * 0.5) * time + sigma * b);
		};

		RandomVariable asset = brownian.getBrownianMotionAtSpecificTime(indexFactor, time).apply(geometricBrownian);

		return asset;

	}

}
