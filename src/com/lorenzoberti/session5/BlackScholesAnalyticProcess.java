
package com.lorenzoberti.session5;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class implements the ProcessSimulation Interface. It represents the
 * discretization and simulation of a stochastic process under the Black Scholes
 * model, i.e. the process is lognormal distributed: d(X(t)) = mu X(t) dt +
 * sigma X(t) dB(t). Here we want to use the analytic solution of the sde, i.e.
 * X(t) = X(0)* exp((mu - sigma^2 * 0.5) t + sigma * W(t)) and generate the
 * whole process iteratively.
 */
public class BlackScholesAnalyticProcess implements ProcessSimulation {

	private BrownianMotionMultiD brownian;
	private double initialValue;
	private double drift;
	private double sigma;
	private TimeDiscretization times;
	private int numberOfPaths;
	private RandomVariable[] allPaths;

	public BlackScholesAnalyticProcess(double initialValue, double drift, double sigma, TimeDiscretization times,
			int numberOfPaths) {
		super();
		this.initialValue = initialValue;
		this.drift = drift;
		this.sigma = sigma;
		this.times = times;
		this.numberOfPaths = numberOfPaths;
		this.brownian = new BrownianMotionD(times, 1, numberOfPaths);
	}

	public BlackScholesAnalyticProcess(BrownianMotionMultiD brownian, double initialValue, double drift, double sigma) {
		super();
		this.brownian = brownian;
		this.initialValue = initialValue;
		this.drift = drift;
		this.sigma = sigma;
		this.times = brownian.getTimeDiscretization();
	}

	@Override
	public double getInitialValue() {

		return initialValue;
	}

	@Override
	public BrownianMotionMultiD getStochasticDriver() {

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
	public double[] getSpecificPath(int pathIndex) {
		if (allPaths == null) {
			generate();
		}

		double[] path = new double[times.getNumberOfTimes() + 1];

		path[0] = initialValue;

		for (int i = 0; i < times.getNumberOfTimeSteps(); i++) {

			path[i] = allPaths[i].get(pathIndex);

		}

		return path;
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

	private void generate() {

		allPaths = new RandomVariable[times.getNumberOfTimes()];

		allPaths[0] = new RandomVariableFromDoubleArray(initialValue);

		for (int i = 0; i < times.getNumberOfTimes() - 1; i++) {

			double addTerm = (drift - sigma * sigma * 0.5) * times.getTimeStep(i);
			RandomVariable diffusionTerm = brownian.getBrownianIncrement(i, 0).mult(sigma);

			allPaths[i + 1] = diffusionTerm.add(addTerm).exp().mult(allPaths[i]);
		}

		}


}
