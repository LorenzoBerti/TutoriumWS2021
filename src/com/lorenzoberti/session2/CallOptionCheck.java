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
		int timeStep = 1;
		
		// Option parameter
		double strike = 100;
		double maturity = numberOfTimeSteps * timeStep;
		
		// double timeStep = maturity / numberOfTimeSteps;

		double evaluationTime = 0;

		// Create an array storing all the values of the Brownian motion that you need
		// in order to
		// simulate the process...


		// ... and fill it
		// write the right method

		// Create an array storing all the value of S(T)...
		double[] finalValue = new double[numberOfSimulation];
		// ...and fill it

		BrownianMotionInterface brownian = new BrownianMotionSimple(numberOfSimulation, numberOfTimeSteps, timeStep);
		double[] lastBrownianValue = brownian.getProcessAtTimeIndex((int) maturity);
		
		// now we create an array storing all the value of S(T)
		for(int i = 0; i < numberOfSimulation; i++) {

		}
		
		// Create an array storing all the payoff values...
		double[] payoff;

		// ... and fill it
		for (int i = 0; i < numberOfSimulation; i++) {

		}
		
		// Now get the price by using the Monte Carlo method
		double price = 0;
		
		// implement the Monte Carlo method here

		// discounting...
		price = price * Math.exp(-riskFreeRate * maturity);

		// ... to evaluation time
		price = price * Math.exp(riskFreeRate * evaluationTime);

		System.out.println("The price is: " + price);
		
		// analytic price

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFreeRate, sigma, maturity,
				strike);

		System.out.println("The analytic price is: " + analyticPrice);

		// Let's price the call by using the finmath library

		double initialTime = 0;
		int seed = 3013;

		// create the model
		ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFreeRate, sigma);

		// discretization of the interval
		TimeDiscretization timeDiscretization = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps,
				timeStep);

		// create the Brownian motion
		BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(timeDiscretization, 1,
				numberOfSimulation, seed);

		// generate the process
		MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);

		MonteCarloAssetModel blackScholesMonteCarloModel = new MonteCarloAssetModel(process);

		// construct an object of type EuropeanOption
		EuropeanOption option = new EuropeanOption(maturity, strike);

		// get the value
		double value = option.getValue(blackScholesMonteCarloModel);

		System.out.println("The option value with the finmath lib is: " + value);

	}

}