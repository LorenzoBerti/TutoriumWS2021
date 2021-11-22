/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * This interface represents a generic financial product
 * 
 * @author Lorenzo Berti
 *
 */
public interface FinancialProduct {

	/**
	 * This method return the price as object of type RandomVariable of the
	 * financial product.
	 * 
	 * @param discountFactor
	 * @param payoff
	 * @return
	 */
	RandomVariable getPrice(double discountFactor, RandomVariable payoff);

	/**
	 * This method return the price as object of type RandomVariable of the
	 * financial product.
	 * 
	 * @param discountFactor
	 * @param payoff
	 * @return
	 */
	RandomVariable getPrice(RandomVariable discountFactor, RandomVariable payoff);

	/**
	 * This method return the price as a double of the financial product.
	 * 
	 * @param discountFactor
	 * @param payoff
	 * @return
	 */
	double getPriceAsDouble(double discountFactor, RandomVariable payoff);

	/**
	 * This method return the price as a double of the financial product.
	 * 
	 * @param discountFactor
	 * @param payoff
	 * @return
	 */
	double getPriceAsDouble(RandomVariable discountFactor, RandomVariable payoff);

}
