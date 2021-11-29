/**
 * 
 */
package com.lorenzoberti.session5;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * @author Lorenzo Berti
 *
 */
public class CallTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 1000000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.1;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		BrownianMotionMultiD brownian = new BrownianMotionD(times, 1, numberOfPaths);

		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		double strike = 100.0;
		double maturity = finalTime;

		ProcessSimulation process = new EulerSchemeBlackScholes(brownian, initialValue, riskFree, sigma);

		ProcessSimulation processAnalytic = new BlackScholesAnalyticProcess(brownian, initialValue, riskFree, sigma);

		RandomVariable lastValue = process.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueAnalytic = processAnalytic.getProcessAtGivenTime(finalTime);

		DoubleUnaryOperator payoff = x -> {
			return Math.max(x - strike, 0.0);
		};

		double price1 = lastValue.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);
		double price2 = lastValueAnalytic.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);
		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);

		System.out.println("Price with simulated process...: " + price1);
		System.out.println("Price with analytic process....: " + price2);
		System.out.println("Analytic price.................: " + analyticPrice);

	}


}

