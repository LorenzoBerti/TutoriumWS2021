/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * This is an abstract class that implements the methods of the interface
 * FinancialProducts. Note the use of the abstract class: we can delegate to
 * this class the implementation of the methods of the interface
 * FinancialProduct because there is no difference in pricing, i.e. the price is
 * given by the expectation under a certain measure of the discounted payoff
 * (discounted with respect the right numéraire). The only difference between
 * the financial products is in the payoff, thus we write more specific class
 * for that. In order to write the class, in particular the constructor of the
 * class, try to think what really define an option and leave the user as free
 * as possible.
 * 
 * @author Lorenzo Berti
 *
 */
public abstract class EuropeanOption implements FinancialProduct {

	@Override
	public RandomVariable getPrice(double discountFactor, RandomVariable payoff) {

		// write your the code here

		return null;

	}

	@Override
	public RandomVariable getPrice(RandomVariable discountFactor, RandomVariable payoff) {

		// write your the code here

		return null;

	}

	@Override
	public double getPriceAsDouble(double discountFactor, RandomVariable payoff) {

		// write your the code here

		return 0;

	}

	@Override
	public double getPriceAsDouble(RandomVariable discountFactor, RandomVariable payoff) {

		// write your the code here

		return 0;

	}

}
