/**
 * 
 */
package com.lorenzoberti.session7;

import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.stochastic.RandomVariable;

/**
 * This class represents an Exchange Option.
 * 
 * @author Lorenzo Berti
 *
 */
public class ExchangeOption implements FinancialProductInterface {

	private final double maturity;
	private final ProcessSimulation firstProcess;

	public ExchangeOption(double maturity, ProcessSimulation firstProcess) {
		super();
		this.maturity = maturity;
		this.firstProcess = firstProcess;
	}

	@Override
	public RandomVariable getPrice(ProcessSimulation process, RandomVariable discountFactor) {

		RandomVariable payoff = firstProcess.getProcessAtGivenTime(maturity)
				.sub(process.getProcessAtGivenTime(maturity)).floor(0.0);
		RandomVariable price = payoff.mult(discountFactor);

		return price.average();

	}

	@Override
	public double getPriceAsDouble(ProcessSimulation process, RandomVariable discountFactor) {

		RandomVariable payoff = firstProcess.getProcessAtGivenTime(maturity)
				.sub(process.getProcessAtGivenTime(maturity)).floor(0.0);
		RandomVariable price = payoff.mult(discountFactor);

		return price.getAverage();
	}




}