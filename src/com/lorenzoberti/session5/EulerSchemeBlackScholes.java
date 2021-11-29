package com.lorenzoberti.session5;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * @author Lorenzo Berti
 *
 */
public class EulerSchemeBlackScholes implements ProcessSimulation {

	private BrownianMotionMultiD brownian;
	private double initialValue;
	private double mu;
	private double sigma;
	private TimeDiscretization times;
	private int numberOfPaths;

	private RandomVariable[] allPaths;

	public EulerSchemeBlackScholes(double initialValue, double mu, double sigma, TimeDiscretization times,
			int numberOfPaths, int seed) {
		super();
		this.initialValue = initialValue;
		this.mu = mu;
		this.sigma = sigma;
		this.times = times;
		this.numberOfPaths = numberOfPaths;
		this.brownian = new BrownianMotionD(times, 1, numberOfPaths);
	}

	public EulerSchemeBlackScholes(BrownianMotionMultiD brownian, double initialValue, double mu, double sigma) {
		super();
		this.brownian = brownian;
		this.initialValue = initialValue;
		this.mu = mu;
		this.sigma = sigma;
		this.times = brownian.getTimeDiscretization();
	}

	@Override
	public double getInitialValue() {
		if (allPaths == null) {
			generate();
		}

		return initialValue;
	}

	@Override
	public BrownianMotionMultiD getStochasticDriver() {
		if (allPaths == null) {
			generate();
		}

		return brownian;
	}

	@Override
	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex) {
		if (allPaths == null) {
			generate();
		}

		return allPaths[timeIndex];
	}

	@Override
	public RandomVariable getProcessAtGivenTime(double time) {
		if (allPaths == null) {
			generate();
		}

		return allPaths[times.getTimeIndex(time)];
	}

	@Override
	public RandomVariable[] getAllPaths() {
		if (allPaths == null) {
			generate();
		}

		return allPaths;
	}

	@Override
	public double[] getSpecificPath(int indexPath) {
		if (allPaths == null) {
			generate();
		}

		double[] path = new double[times.getNumberOfTimes() + 1];

		path[0] = initialValue;

		for (int i = 0; i < times.getNumberOfTimeSteps(); i++) {

			path[i] = allPaths[i].get(indexPath);

		}

		return path;
	}

	private void generate() {

		allPaths = new RandomVariable[times.getNumberOfTimes()];

		allPaths[0] = new RandomVariableFromDoubleArray(initialValue);

		for (int i = 0; i < times.getNumberOfTimes() - 1; i++) {

			RandomVariable drift = allPaths[i].mult(mu).mult(times.getTimeStep(i));
			RandomVariable diffusion = allPaths[i].mult(brownian.getBrownianIncrement(i, 0)).mult(sigma);

			allPaths[i + 1] = allPaths[i].add(drift).add(diffusion);

		}

	}

	@Override
	public double getSpecificValueOfSpecificPath(int pathIndex, int timeIndex) {
		if (allPaths == null) {
			generate();
		}

		return getSpecificPath(pathIndex)[timeIndex];
	}

	@Override
	public void printPath(int pathIndex) {

		DoubleUnaryOperator trajectory = t -> {
			return getSpecificValueOfSpecificPath(pathIndex, (int) t);
		};

		Plot2D plot = new Plot2D(0, times.getNumberOfTimes(), times.getNumberOfTimes(), trajectory);
		plot.setTitle("Simulated process path");
		plot.setXAxisLabel("Time");
		plot.setYAxisLabel("Process");
		plot.show();

	}

}
