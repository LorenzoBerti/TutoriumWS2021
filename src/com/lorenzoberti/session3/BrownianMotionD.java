package com.lorenzoberti.session3;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionD implements BrownianMotionMultiD {

	private TimeDiscretization times;

	private int numberOfFactors;
	private int numberOfPaths;

	// two-dimension array storing the Brownian increments and the Brownian paths
	private RandomVariable[][] brownianIncrements;
	private RandomVariable[][] brownianPaths;

	public BrownianMotionD(TimeDiscretization times, int numberOfFactors, int numberOfPaths) {
		this.times = times;
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
	}

	@Override
	public TimeDiscretization getTimeDiscretization() {

		return times;
	}

	@Override
	public RandomVariable[][] getAllPaths() {
		if (brownianPaths == null) {
			generate();
		}
		return brownianPaths;
	}

	@Override
	public RandomVariable getBrownianMotionAtSpecificTime(int indexFactor, double time) {

		RandomVariable[][] allPaths = getAllPaths();

		return allPaths[times.getTimeIndex(time)][indexFactor];
	}

	@Override
	public RandomVariable getBrownianMotionAtSpecificTimeIndex(int indexFactor, int timeIndex) {

		RandomVariable[][] allPaths = getAllPaths();

		return allPaths[timeIndex][indexFactor];
	}

	@Override
	public RandomVariable[] getPathsForFactor(int indexFactor) {

		RandomVariable[][] allPaths = getAllPaths();
		int numberOfTimes = times.getNumberOfTimes();
		RandomVariable[] paths = new RandomVariableFromDoubleArray[numberOfTimes];

		for (int i = 0; i < numberOfTimes; i++) {
			paths[i] = allPaths[i][indexFactor];
		}
		return paths;
	}

	@Override
	public double[] getSpecificPathForFactor(int indexFactor, int pathIndex) {

		RandomVariable[] path = getPathsForFactor(indexFactor);
		int numberOfTimes = path.length;
		double[] specificPath = new double[numberOfTimes];
		for (int i = 0; i < numberOfTimes; i++) {
			specificPath[i] = path[i].get(pathIndex);
		}

		return specificPath;
	}

	@Override
	public RandomVariable[][] getBrownianIncrements() {

		if (brownianIncrements == null) {
			generate();
		}

		RandomVariable[][] brownianIncrementsClone = brownianIncrements.clone();

		return brownianIncrementsClone;
	}

	@Override
	public RandomVariable getBrownianIncrement(int timeIndex, int indexFactor) {

		RandomVariable[][] allIncrements = getBrownianIncrements();

		return allIncrements[timeIndex][indexFactor];
	}

	private void generate() {

		final int numberOfTimeSteps = times.getNumberOfTimeSteps();
		// numberOfTimes = numberOfTimeSteps + 1
		final int numberOfTimes = times.getNumberOfTimes();

		// Two 3-dimensional array storing the brownian increments and the brownian
		// paths
		// Note: now we store the increments as we want a method to get them
		final double[][][] brownianIncrementsArray = new double[numberOfTimeSteps][numberOfFactors][numberOfPaths];
		final double[][][] brownianPathsArray = new double[numberOfTimes][numberOfFactors][numberOfPaths];

		// Write your code here

		// Now we have to fill our array of RandomVariable
		brownianIncrements = new RandomVariable[numberOfTimeSteps][numberOfFactors];
		brownianPaths = new RandomVariable[numberOfTimeSteps + 1][numberOfFactors];

		// Then fill the arrays: use the class RandomVariableFromDoubleArray of the
		// finmath lib in order to create the random variables

		// Write your code here

}
}
