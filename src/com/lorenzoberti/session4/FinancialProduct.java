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

	RandomVariable getPrice(double discountFactor, RandomVariable payoff);

	RandomVariable getPrice(RandomVariable discountFactor, RandomVariable payoff);

	double getPriceAsDouble(double discountFactor, RandomVariable payoff);

	double getPriceAsDouble(RandomVariable discountFactor, RandomVariable payoff);

}
