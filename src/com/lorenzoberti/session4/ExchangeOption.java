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

	private final RandomVariable firstAsset;
	private final RandomVariable secondAsset;

	public ExchangeOption(RandomVariable firstAsset, RandomVariable secondAsset) {
		this.firstAsset = firstAsset;
		this.secondAsset = secondAsset;
	}

	public RandomVariable getPayoff() {

		return firstAsset.sub(secondAsset);
	}

}
