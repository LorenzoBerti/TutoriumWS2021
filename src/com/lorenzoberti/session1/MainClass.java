/**
 * 
 */
package com.lorenzoberti.session1;

import net.finmath.montecarlo.BrownianMotion;

/**
 * @author Lorenzo Berti
 *
 */
public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Hello World!");

		MainClass word = new MainClass();

		word.PrintWord("Hello World!");

		double c = word.Sum(3.0, 4.0);

		System.out.println(c);

		BrownianMotion brownian;

	}

	/**
	 * This method can be used to print a given string.
	 * 
	 * @param string
	 */
	public void PrintWord(String string) {

		System.out.println(string);

	}

	/**
	 * This method computes the sum of two double.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double Sum(double a, double b) {

		double c = a + b;

		return c;

	}
		
	}


