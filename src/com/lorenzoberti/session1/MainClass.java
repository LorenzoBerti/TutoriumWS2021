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
	 * Something
	 * 
	 * @param string
	 */
	public void PrintWord(String string) {

		System.out.println(string);

	}

	/**
	 * something else
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


