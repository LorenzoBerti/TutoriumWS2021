/**
 * 
 */
package com.lorenzoberti.session2;

import java.util.function.DoubleUnaryOperator;

import net.finmath.functions.NormalDistribution;
import net.finmath.plots.Plot2D;
import net.finmath.randomnumbers.MersenneTwister;

/**
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionSimple implements BrownianMotionInterface {

	private int numberOfPaths;
	private int numberOfTimeSteps;
	private double timeStep;
	private double wholePaths[][];

	public BrownianMotionSimple(int numberOfPaths, int numberOfTimeSteps, double timeStep) {
		super();
		this.numberOfPaths = numberOfPaths;
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.timeStep = timeStep;
	}

	@Override
	public double[][] getPaths() {
		// lazy inizialization: we inizialize it only one time
		// but this is would not be a problem because everything is random
		if (wholePaths == null) {
			generate();
		}
		return wholePaths;
	}

	@Override
	public double[] getProcessAtTimeIndex(int timeIndex) {
		if (wholePaths == null) {
			generate();
		}
		double[] processAtGivenTime = new double[numberOfPaths];

		for (int i = 0; i < numberOfPaths; i++) {
			processAtGivenTime[i] = wholePaths[i][timeIndex];
		}

		return processAtGivenTime;
	}

	@Override
	public double[] getSpecificPath(int path) {
		if (wholePaths == null) {
			generate();
		}
		double[] specificPath = new double[numberOfTimeSteps + 1];
		
		for (int i = 0; i < numberOfTimeSteps + 1; i++) {
			specificPath[i] = wholePaths[path][i];
		}
			
		return wholePaths[path];
	}

	@Override
	public double getSpecificValue(int path, int timeIndex) {
		if (wholePaths == null) {
			generate();
		}
		return wholePaths[path][timeIndex];
	}

	// Private! The user does not need this method: it is inside the class
	private void generate() {

		wholePaths = new double[numberOfPaths][numberOfTimeSteps + 1];

		// we inizialize the entire first column at 0 (the Brownian motion starts in 0)
		for (int i = 0; i < numberOfPaths; i++) {
			wholePaths[i][0] = 0;
		}
		MersenneTwister mersenne = new MersenneTwister(3083);
		// now we generate the Brownian motion path by path
		for (int i = 0; i < numberOfPaths; i++) {
			for (int j = 1; j < numberOfTimeSteps + 1; j++) {
				// we have to generate the value of the Normal distribution
				double random = mersenne.nextDouble();
				// double random = Math.random();
				double normal = NormalDistribution.inverseCumulativeDistribution(random);
				wholePaths[i][j] = wholePaths[i][j - 1] + Math.sqrt(timeStep) * normal;

			}

		}

	}

	@Override
	public void printPath(int path) {

		DoubleUnaryOperator trajectory = t -> {
			return (getSpecificValue(path, (int) t));
		};

		Plot2D plot = new Plot2D(0, numberOfTimeSteps, numberOfTimeSteps + 1, trajectory);
		plot.setTitle("Brownian motion path");
		plot.setXAxisLabel("Time");
		plot.setYAxisLabel("Brownian motion");
		plot.show();

	}

}