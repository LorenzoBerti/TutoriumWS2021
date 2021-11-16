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
		int numberOfPaths = 100000;

		// Time parameter
		double initialTime = 0.0;
		double finalTime = 10.0;
		double deltaT = 1.0;
		int numberOfTimeSteps = (int) (finalTime / deltaT);

		double maturity = finalTime;

		// Model parameter
		double firstAssetInitial = 100.0;
		double secondAssetInitial = 100.0;

		double firstAssetVolDouble = 0.3;
		double secondAssetVolDouble = 0.5;

		double riskFree = 0.05;

		// Since we want to use objects of type RandomVariable we create four no
		// stochastic random variable using the model parameter
		// Note: a costant c can be seen as a trivial random variable X s.t. P(X = c) =
		// 1, P(X != c) = 0
		RandomVariable firstAssetInitialValue = new RandomVariableFromDoubleArray(firstAssetInitial);
		RandomVariable secondAssetInitialValue = new RandomVariableFromDoubleArray(secondAssetInitial);

		RandomVariable firstAssetVol = new RandomVariableFromDoubleArray(firstAssetVolDouble);
		RandomVariable secondAssetVol = new RandomVariableFromDoubleArray(secondAssetVolDouble);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

		// Constructor of our Brownian motion
		BrownianMotionMultiD brownian = new BrownianMotionD(times, numberOfFactors, numberOfPaths);

		// DoubleTernaryOperator (from the finmath-lib)
		DoubleTernaryOperator geometricBrownian = (x, y, z) -> {
			return z * Math.exp((riskFree - y * y * 0.5) * maturity + y * x);
		};

		// First asset at maturity
		RandomVariable firstBrownian = brownian.getBrownianMotionAtSpecificTime(0, maturity);
		RandomVariable firstAsset = firstBrownian.apply(geometricBrownian,
				firstAssetVol, firstAssetInitialValue);

		// Second asset at maturity
		RandomVariable secondBrownian = brownian.getBrownianMotionAtSpecificTime(1, maturity);
		RandomVariable secondAsset = secondBrownian.apply(geometricBrownian,
				secondAssetVol, secondAssetInitialValue);

		// Note the methods of the RandomVariable interface!
		double price = firstAsset.sub(secondAsset).floor(0.0).getAverage();

		// discounting...
		price = price * Math.exp(-riskFree * maturity);

		System.out.println("The price is: " + price);
		
		// Check: there exists an analytic formula for the exchange option that can be
		// recovered through the change of measure...
		double vol = firstAssetVolDouble * firstAssetVolDouble
				- 2 * firstBrownian.covariance(secondBrownian).getAverage() * firstAssetVolDouble * secondAssetVolDouble
				+ secondAssetVolDouble * secondAssetVolDouble;

		vol = Math.sqrt(vol);

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, 0, vol, maturity,
				secondAssetInitial);

		System.out.println("Analytic price: " + analyticPrice);

	}

}
