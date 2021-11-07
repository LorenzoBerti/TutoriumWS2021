/**
 * 
 */
package com.lorenzoberti.session2;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.montecarlo.model.ProcessModel;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.montecarlo.process.MonteCarloProcess;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * @author Lorenzo Berti
 *
 */
public class CallOptionCheck {

	/**
	 * @param args
	 * @throws CalculationException
	 */
	public static void main(String[] args) throws CalculationException {
		
		// model parameter of S
		double initialValue = 100.0;
		double riskFreeRate = 0.05;
		double sigma = 0.2;
		
		// Monte Carlo simulation parameter
		int numberOfSimulation = 100000;
		int numberOfTimeSteps = 100;
		double timeStep = 1.0;
		
		// Option parameter
		double strike = 100;
		int maturity = 10;
		
		double evaluationTime = 0;

		double[] finalValue = new double[numberOfSimulation];
		BrownianMotionInterface brownian = new BrownianMotionSimple(numberOfSimulation, numberOfTimeSteps, timeStep);
		double[] lastBrownianValue = brownian.getProcessAtTimeIndex(maturity);
		
		// now we create an array storing all the value of S(T)
		for(int i = 0; i < numberOfSimulation; i++) {
			finalValue[i] = initialValue
					* Math.exp((riskFreeRate - 0.5 * sigma * sigma) * maturity + sigma * lastBrownianValue[i]);
		}
		
		// get the array containing all the payoff
		double[] payoff = new double[numberOfSimulation];
		for (int i = 0; i < numberOfSimulation; i++) {
			payoff[i] = Math.max(finalValue[i] - strike, 0);
		}
		
		// now get the price
		double sum = 0;
		for (int i = 0; i < numberOfSimulation; i++) {
			sum += payoff[i];
		}
		double price = sum / numberOfSimulation;
		
		// discounting...
		price = price * Math.exp(-riskFreeRate * maturity);

		// ... to evaluation time
		price = price * Math.exp(riskFreeRate * evaluationTime);

		System.out.println("The price is: " + price);
		
		// analytic price

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFreeRate, sigma, maturity,
				strike);

		System.out.println("The analytic price is: " + analyticPrice);

		// With the finmath library

		double initialTime = 0;
		int seed = 3013;

		ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFreeRate, sigma);

		TimeDiscretization timeDiscretization = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps,
				timeStep);

		BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(timeDiscretization, 1,
				numberOfSimulation, seed);

		MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);

		MonteCarloAssetModel blackScholesMonteCarloModel = new MonteCarloAssetModel(process);

		EuropeanOption option = new EuropeanOption(maturity, strike);

		double value = option.getValue(blackScholesMonteCarloModel);

		System.out.println("The option value with the finmath lib is: " + value);

	}

}
