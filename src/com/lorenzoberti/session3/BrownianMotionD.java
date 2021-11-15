/**
 * 
 */
package com.lorenzoberti.session3;

import net.finmath.functions.NormalDistribution;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.randomnumbers.MersenneTwister;
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
		
		// 3 for loops: one for each dimension of the 3-dim array, i.e. paths, factors
		// and times
		for (int pathIndex = 0; pathIndex < numberOfPaths; pathIndex++) {
			for (int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
				// at time 0 the brownian motion is 0
				brownianPathsArray[0][factorIndex][pathIndex] = 0;

				// random number generator
				MersenneTwister mersenne = new MersenneTwister();

				for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
					// generate the uniform random number on [0,1]
					double random = mersenne.nextDouble();
					// compute the brownian increments: normal distribution * sqrt(Δ)
					brownianIncrementsArray[timeIndex][factorIndex][pathIndex] = NormalDistribution
							.inverseCumulativeDistribution(random)
							* Math.sqrt(times.getTimeStep(timeIndex));
					// compute the brownian value
					brownianPathsArray[timeIndex
							+ 1][factorIndex][pathIndex] = brownianPathsArray[timeIndex][factorIndex][pathIndex]
									+ brownianIncrementsArray[timeIndex][factorIndex][pathIndex];
				}
			}
		}

		// Now we have to fill our array of Random Variable. 
		// First allocate the memory
		// brownianIncrements = new RandomVariable[numberOfTimeSteps][numberOfFactors];
		// brownianPaths = new RandomVariable[numberOfTimeSteps + 1][numberOfFactors];
		brownianIncrements = new RandomVariable[numberOfTimeSteps][numberOfFactors];
		brownianPaths = new RandomVariable[numberOfTimeSteps + 1][numberOfFactors];
		
		// Then fill the arrays: we use the class RandomVariableFromDoubleArray of the
		// finmath lib in order to create the random variables
		for (int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
			// first create the non stochastic random variables of vector 0 (first entries
			// of the
			// brownian vector)
			brownianPaths[0][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(0), 0);
			// then we fill the brownianIncrements and the brownianPaths array using the
			// same class
			// Note: the realizations of the random variable at time t are given by the
			// values of the
			// paths at time t
			for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
				brownianIncrements[timeIndex][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(timeIndex),
						brownianIncrementsArray[timeIndex][factorIndex]);
				// we have timeIndex+1 because the first entry is 0
				brownianPaths[timeIndex + 1][factorIndex] = new RandomVariableFromDoubleArray(
						times.getTime(timeIndex + 1), brownianPathsArray[timeIndex + 1][factorIndex]);
			}
		


	}

}
}
