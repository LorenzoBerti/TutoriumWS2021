/**
 * 
 */
package com.lorenzoberti.session2;

/**
 * This class implements the BrownianMotionInterface.
 * 
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionSimple implements BrownianMotionInterface {

	private int numberOfPaths;
	private int numberOfTimeSteps;

	// the Δ of the time discretization; assume it constant.
	private double timeStep;

	// this matrix stores the paths of the Brownian motion, i.e. it should contains
	// all the values of the Brownian motion for each path (the columns could store
	// the paths and the rows the respective values, or viceversa.
	private double wholePaths[][];

	// Constructor
	public BrownianMotionSimple(int numberOfPaths, int numberOfTimeSteps, double timeStep) {
		super();
		this.numberOfPaths = numberOfPaths;
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.timeStep = timeStep;
	}

	@Override
	public double[][] getPaths() {
		// lazy inizialization: we inizialize it only one time
		// (but this would not be a problem because everything is random...)
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

		// This method should generate the whole sample path of the Brownian motion,
		// i.e. generate the values of the Brownian motion and fill them in the matrix
		// wholePaths[][].
		// Hint: use the property of the Brownian motion: W(t_j) = W(t_j-1) +
		// \sqrt(Δ) * N(0,1)
		// Hint: Have a look at the class NormalRandomVariable

			}





}
