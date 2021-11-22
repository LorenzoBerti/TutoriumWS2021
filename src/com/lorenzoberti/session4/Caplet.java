/**
 * 
 */
package com.lorenzoberti.session4;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public class Caplet extends EuropeanOption {

	private final RandomVariable liborRate;
	private final double strike;
	private final double notional;
	private final double timeLength;

	public Caplet(double maturity, double initialTime, RandomVariable liborRate, double strike, double notional) {
		this.liborRate = liborRate;
		this.strike = strike;
		this.notional = notional;
		this.timeLength = maturity - initialTime;
	}

	public Caplet(double timeLength, RandomVariable liborRate, double strike, double notional) {
		this.liborRate = liborRate;
		this.strike = strike;
		this.notional = notional;
		this.timeLength = timeLength;
	}

	public RandomVariable getPayoff() {

		return liborRate.sub(strike).floor(0.0).mult(timeLength * notional);
	}

}
