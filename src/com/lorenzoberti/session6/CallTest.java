/**
 * 
 */
package com.lorenzoberti.session6;

import java.text.DecimalFormat;

import net.finmath.functions.AnalyticFormulas;
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

		// Write here the constructors of the classes: BlackScholesAnalyticProcess,
		// BlackScholesEulerScheme and BachelierEulerScheme. Take the values of the call
		// option
		// for each process, respectively. Then, compare with the analytic values.

		double analyticPriceBS = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);
		double analyticPriceBachelier = AnalyticFormulas.bachelierOptionValue(forward, sigma, maturity, strike,
				payoffUnit);

	}
}

