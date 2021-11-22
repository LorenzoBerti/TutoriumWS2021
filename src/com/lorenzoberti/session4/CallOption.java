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



		private final RandomVariable asset;
		private final double strike;

		public CallOption(RandomVariable asset, double strike) {
			this.asset = asset;
			this.strike = strike;
		}

		public RandomVariable getPayoff() {

			return asset.sub(strike).floor(0.0);
		}
	}


