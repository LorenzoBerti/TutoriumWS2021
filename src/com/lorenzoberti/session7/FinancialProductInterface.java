/**
 * 
 */
package com.lorenzoberti.session7;

import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.stochastic.RandomVariable;

/**
 * This interface represents a generic financial product
 * 
 * @author Lorenzo Berti
 *
 */
public interface FinancialProductInterface {

	/**
	 * This method return the price as object of type RandomVariable of the
	 * financial product.
	 * 
	 * @param process
	 * @param discountFactor
	 * @return
	 */
	RandomVariable getPrice(ProcessSimulation process, RandomVariable discountFactor);

	/**
	 * This method return the price as a double of the financial product.
	 * 
	 * @param process
	 * @param discountFactor
	 * @return
	 */
	double getPriceAsDouble(ProcessSimulation process, RandomVariable discountFactor);

}