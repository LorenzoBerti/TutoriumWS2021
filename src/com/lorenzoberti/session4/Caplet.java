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

	private final double strike;
	private final double notional;
	private final double timeLength;

	public Caplet(double strike, double notional, double paymentDate, double fixingDate) {
		super();
		this.strike = strike;
		this.notional = notional;
		this.timeLength = paymentDate - fixingDate;
	}

	public Caplet(double timeLength, double strike, double notional) {
		this.strike = strike;
		this.notional = notional;
		this.timeLength = timeLength;
	}

	/**
	 * This methods return an object of type RandomVariable representing the payoff
	 * of a caplet
	 * 
	 * @param liborRate
	 * @return caplet payoff
	 */
	public RandomVariable getPayoff(RandomVariable liborRate) {

		return liborRate.sub(strike).floor(0.0).mult(notional * timeLength);
	}

}
