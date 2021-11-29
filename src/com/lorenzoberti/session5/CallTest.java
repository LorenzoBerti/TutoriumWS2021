/**
 * 
 */
package com.lorenzoberti.session5;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to price a call option with the objects of type
 * ProcessSimulation that you have created. Change the parameters (in particular
 * numberOfPaths, timeStep and maturity of the option) and try to explain the
 * behaviour of what you see.
 * 
 * @author Lorenzo Berti
 *
 */
public class CallTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 1000000;

		// time parameters
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.1;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		// model parameters
		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		// option parameters
		double strike = 100.0;
		double maturity = finalTime;

		// Constructor of your processes

		// Take the payoff of the option

		// Take the price

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);

		System.out.println("Price with Euler scheme...: ");
		System.out.println("Price with analytic process....: ");
		System.out.println("Analytic price.................: " + analyticPrice);

	}


}

