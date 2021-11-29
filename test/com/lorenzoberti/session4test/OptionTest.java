package com.lorenzoberti.session4test;

import java.util.function.DoubleUnaryOperator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;
import com.lorenzoberti.session4.CallOption;
import com.lorenzoberti.session4.Caplet;
import com.lorenzoberti.session4.ExchangeOption;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

class OptionTest {

	double tolerance = 0.5;

	int numberOfFactors = 2;
	int numberOfPaths = 1000000;

	// Time parameter
	double initialTime = 0.0;
	double finalTime = 2.0;
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

	double discountFactor = Math.exp((timeEval - maturity) * riskFree);

	TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

	// Constructor of our Brownian motion
	BrownianMotionMultiD brownian = new BrownianMotionD(times, numberOfFactors, numberOfPaths);

	@Test
	void exchangeTest() {

		RandomVariable firstAsset = getAssetAtSpecificTime(brownian, 0, riskFree, firstAssetVolDouble,
				firstAssetInitial, maturity);

		RandomVariable secondAsset = getAssetAtSpecificTime(brownian, 1, riskFree, secondAssetVolDouble,
				secondAssetInitial, maturity);

		ExchangeOption exchangeOption = new ExchangeOption(firstAsset, secondAsset);

		RandomVariable payoffExchange = exchangeOption.getPayoff();

		double priceExchange = exchangeOption.getPriceAsDouble(discountFactor, payoffExchange);

		double covariance = brownian.getBrownianMotionAtSpecificTime(0, maturity)
				.covariance(brownian.getBrownianMotionAtSpecificTime(1, maturity)).getAverage();

		double vol = firstAssetVolDouble * firstAssetVolDouble
				- 2 * covariance * firstAssetVolDouble * secondAssetVolDouble
				+ secondAssetVolDouble * secondAssetVolDouble;

		vol = Math.sqrt(vol);

		double analyticPriceExchange = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, 0, vol, maturity,
				secondAssetInitial);

		Assert.assertEquals(priceExchange, analyticPriceExchange, tolerance);

		System.out.println("Exchange test: ");

		System.out.println("The Monte Carlo price is: " + priceExchange);

		System.out.println("The analytic price is: " + analyticPriceExchange);

		System.out.println();

	}

	@Test
	void callTest() {

		double strike = firstAssetInitial;

		RandomVariable firstAsset = getAssetAtSpecificTime(brownian, 0, riskFree, firstAssetVolDouble,
				firstAssetInitial, maturity);

		CallOption callOption = new CallOption(strike);

		RandomVariable payoffCall = callOption.getPayoff(firstAsset);

		double priceCall = callOption.getPriceAsDouble(discountFactor, payoffCall);

		double analyticPriceCall = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, riskFree,
				firstAssetVolDouble, maturity, strike);

		Assert.assertEquals(priceCall, analyticPriceCall, tolerance);

		System.out.println("Call test: ");

		System.out.println("The Monte Carlo price is: " + priceCall);

		System.out.println("The analytic price is: " + analyticPriceCall);

		System.out.println();
	}

	@Test
	void capletTest() {

		double paymentDate = 2.0;
		double fixingDate = 1.0;

		double initialLiborRate = 0.05;
		double liborVol = 0.3;

		double discountFactorAtMaturity = 0.91;

		RandomVariable liborRate = getAssetAtSpecificTime(brownian, 0, 0, liborVol, initialLiborRate, fixingDate);

		double strikeCaplet = 0.044;
		double notional = 1000.0;

		double timeLength = paymentDate - fixingDate;

		Caplet caplet = new Caplet(deltaT, strikeCaplet, notional);

		RandomVariable payoffCaplet = caplet.getPayoff(liborRate);

		double priceCaplet = caplet.getPriceAsDouble(discountFactorAtMaturity, payoffCaplet);

		double analyticPriceCaplet = notional * discountFactorAtMaturity * timeLength
				* AnalyticFormulas.blackScholesOptionValue(initialLiborRate, 0, liborVol, timeLength, strikeCaplet);

		Assert.assertEquals(priceCaplet, analyticPriceCaplet, tolerance);

		System.out.println("Caplet test: ");

		System.out.println("The Monte Carlo price is: " + priceCaplet);

		System.out.println("The analytic price is: " + analyticPriceCaplet);

		System.out.println();

	}

	private static RandomVariable getAssetAtSpecificTime(BrownianMotionMultiD brownian, int indexFactor,
			double riskFree, double sigma, double initialValue, double time) {

		DoubleUnaryOperator geometricBrownian = b -> {
			return initialValue * Math.exp((riskFree - sigma * sigma * 0.5) * time + sigma * b);
		};

		RandomVariable asset = brownian.getBrownianMotionAtSpecificTime(indexFactor, time).apply(geometricBrownian);

		return asset;

	}

}
