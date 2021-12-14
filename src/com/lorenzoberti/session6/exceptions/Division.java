/**
 * 
 */
package com.lorenzoberti.session6.exceptions;

/**
 * @author Lorenzo Berti
 *
 */
public class Division {

	public static double division(double dividend, double divisor) throws DivideByZeroException {
		if (divisor == 0) {
			throw new DivideByZeroException();
		} else
			return dividend / divisor;
	}

}
