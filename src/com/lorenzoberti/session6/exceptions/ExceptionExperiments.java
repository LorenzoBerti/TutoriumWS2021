/**
 * 
 */
package com.lorenzoberti.session6.exceptions;

/**
 * @author Lorenzo Berti
 *
 */
public class ExceptionExperiments {

	private static int numberOfDrawings = 10000;
	// private static Integer numberOfDrawings;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		int number = numberOfDrawings;

		for (int i = 0; i < 5; i++) {

			number = number / 10;
			double value = piMonteCarlo(number);
			System.out.println("Approximation of pi: " + value);
			// piMonteCarlo(number);

		}






	}


	private static double piMonteCarlo(int numberOfDrawings) throws Exception {

//		if (numberOfDrawings == 0) {
//			throw new Exception("Error: the number of drawings cannot be 0");
//		}

		int numberOfPointsInside = 0;

		for (int i = 0; i < numberOfDrawings; i++) {
			double x = Math.random();
			double y = Math.random();
			if (x * x + y * y <= 1) {
				numberOfPointsInside += 1;
			}
		}

		double result = 0.0;

		try {
			double multiplier = Division.division(numberOfPointsInside, numberOfDrawings);
			result = 4.0 * multiplier;
			// System.out.println(result);
		} catch (DivideByZeroException e) {
			e.printExceptionMessage();
		}

		return result;
//		if (Double.isNaN(result)) {
//			throw new Exception("Error: the result is NaN. Something went wrong.");
//		}



	}

}
