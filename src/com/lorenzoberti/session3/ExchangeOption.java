/**
 * 
 */
package com.lorenzoberti.session3;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.DoubleTernaryOperator;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * In this class we want to price an exchange option, i.e. and option whose
 * payoff is given by max(S_1(T) - S_2(T), 0), where S_1 and S_2 are two risky
 * assets. We consider a simple Black-Scholes model with two risky assets. Try
 * to use the methods of the RandomVariable interface!
 * 
 * @author Lorenzo Berti
 *
 */
public class ExchangeOption {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfFactors = 2;
		int numberOfSimulations = 100000;

		// Time parameter
		double initialTime = 0.0;
		double timeEval = 0.0;
		double finalTime = 10.0;
		double deltaT = 1.0;
		int numberOfTimeSteps = (int) (finalTime / deltaT);

		double maturity = finalTime;

		// Model parameter
		double firstAssetInitial = 100.0;
		double secondAssetInitial = 100.0;

		double firstAssetVol = 0.3;
		double secondAssetVol = 0.2;

		double riskFree = 0.05;

		RandomVariable firstAssetInitialValue = new RandomVariableFromDoubleArray(firstAssetInitial);
		RandomVariable secondAssetInitialValue = new RandomVariableFromDoubleArray(secondAssetInitial);

		RandomVariable firstAssetVolRandom = new RandomVariableFromDoubleArray(firstAssetVol);
		RandomVariable secondAssetVolRandom = new RandomVariableFromDoubleArray(secondAssetVol);

		TimeDiscretization time = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

		BrownianMotionMultiD brownian = new BrownianMotionD(time, numberOfFactors, numberOfSimulations);

		DoubleTernaryOperator geometricBrownian = (x, y, z) -> {

			return z * Math.exp((riskFree - y * y * 0.5) * maturity + x * y);

		};

		// First asset
		RandomVariable firstBrownian = brownian.getBrownianMotionAtSpecificTime(0, maturity);
		RandomVariable firstAssetAtMaturity = firstBrownian.apply(geometricBrownian, firstAssetVolRandom,
				firstAssetInitialValue);

		// Second asset
		RandomVariable secondBrownian = brownian.getBrownianMotionAtSpecificTime(1, maturity);
		RandomVariable secondAssetAtMaturity = firstBrownian.apply(geometricBrownian, secondAssetVolRandom,
				secondAssetInitialValue);

		double price = firstAssetAtMaturity.sub(secondAssetAtMaturity).floor(0.0).getAverage();

		price = price * Math.exp((timeEval - maturity) * riskFree);

		System.out.println("The Monte Carlo price is: " + price);
		
		double vol = firstAssetVol * firstAssetVol
				- 2 * firstAssetVol * secondAssetVol * firstBrownian.covariance(secondBrownian).getAverage()
				+ secondAssetVol * secondAssetVol;

		vol = Math.sqrt(vol);

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, 0, vol, maturity,
				secondAssetInitial);

		System.out.println("The analytic price is: " + analyticPrice);

	}

}
