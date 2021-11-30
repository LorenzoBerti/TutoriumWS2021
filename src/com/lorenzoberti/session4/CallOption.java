/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public class CallOption extends EuropeanOption {

	private final double strike;

	public CallOption(double strike) {
		this.strike = strike;
	}

	/**
	 * This methods return an object of type RandomVariable representing the payoff
	 * of a call option
	 * 
	 * @param asset
	 * @return call option payoff
	 */
	public RandomVariable getPayoff(RandomVariable asset) {

		return asset.sub(strike).floor(0.0);
	}

}
