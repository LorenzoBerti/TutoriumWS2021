/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public abstract class EuropeanOption implements FinancialProduct {

	@Override
	public RandomVariable getPrice(double discountFactor, RandomVariable payoff) {

		return payoff.average().mult(discountFactor);

	}

	@Override
	public RandomVariable getPrice(RandomVariable discountFactor, RandomVariable payoff) {

		return payoff.mult(discountFactor).average();

	}

	@Override
	public double getPriceAsDouble(double discountFactor, RandomVariable payoff) {

		return payoff.getAverage() * discountFactor;

	}

	@Override
	public double getPriceAsDouble(RandomVariable discountFactor, RandomVariable payoff) {

		return payoff.mult(discountFactor).getAverage();

	}

}
