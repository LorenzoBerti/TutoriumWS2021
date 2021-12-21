/**
 * 
 */
package com.lorenzoberti.session7;

import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.stochastic.RandomVariable;

/**
 * This class represent a Caplet.
 * 
 * @author Lorenzo Berti
 *
 */
public class Caplet implements FinancialProductInterface {

	private final double strike;
	private final double notional;
	private final double paymentDate;
	private final double fixingDate;
	private final double timeLength;

	public Caplet(double strike, double notional, double paymentDate, double fixingDate) {
		super();
		this.strike = strike;
		this.notional = notional;
		this.paymentDate = paymentDate;
		this.fixingDate = fixingDate;
		this.timeLength = paymentDate - fixingDate;
	}

	@Override
	public RandomVariable getPrice(ProcessSimulation process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(fixingDate).sub(strike).floor(0.0);
		RandomVariable price = payoff.mult(notional).mult(discountFactor);

		return price.average();
	}

	@Override
	public double getPriceAsDouble(ProcessSimulation process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(fixingDate).sub(strike).floor(0.0);
		RandomVariable price = payoff.mult(notional * timeLength).mult(discountFactor);

		return price.getAverage();
	}



}