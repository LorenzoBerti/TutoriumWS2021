/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public class ExchangeOption {

	private final RandomVariable asset1;
	private final RandomVariable asset2;

	public ExchangeOption(RandomVariable asset1, RandomVariable asset2) {
		super();
		this.asset1 = asset1;
		this.asset2 = asset2;
	}

	public RandomVariable getPayoff() {

		return asset1.sub(asset2).floor(0.0);
	}

}
