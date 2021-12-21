/**
 * 
 */
package com.lorenzoberti.session7;

import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.stochastic.RandomVariable;

/**
 * This class represents an European Call Option.
 * 
 * @author Lorenzo Berti
 *
 */
public class CallOption implements FinancialProductInterface {

	private final double strike;
	private final double maturity;

	public CallOption(double strike, double maturity) {
		super();
		this.strike = strike;
		this.maturity = maturity;
	}

	@Override
	public RandomVariable getPrice(ProcessSimulation process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(maturity).sub(strike).floor(0.0);

		return payoff.mult(discountFactor).average();
	}

	@Override
	public double getPriceAsDouble(ProcessSimulation process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(maturity).sub(strike).floor(0.0);

		return payoff.mult(discountFactor).getAverage();
	}



}