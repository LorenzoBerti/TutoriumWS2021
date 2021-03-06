/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public class ExchangeOption extends EuropeanOption {

	private final RandomVariable asset1;
	private final RandomVariable asset2;

	public ExchangeOption(RandomVariable asset1, RandomVariable asset2) {
		super();
		this.asset1 = asset1;
		this.asset2 = asset2;
	}

	/**
	 * This methods return an object of type RandomVariable representing the payoff
	 * of an exchange option
	 * 
	 * @return exchange option payoff
	 */
	public RandomVariable getPayoff() {

		return asset1.sub(asset2).floor(0.0);
	}

}
