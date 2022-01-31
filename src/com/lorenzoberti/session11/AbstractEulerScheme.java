package com.lorenzoberti.session11;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session3.BrownianMotionD;
import com.lorenzoberti.session3.BrownianMotionMultiD;
import com.lorenzoberti.session5.ProcessSimulation;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This abstract class implements the ProcessSimulation. It simulates a general
 * process dX(t) = mu(t,X(t))dt + sigma(t,X(t))dW(t) by using the EulerScheme,
 * i.e. X(t_{i+1}) =
 * m(t_i,X(t_i))*(t_{i+1}-t_i)+sigma(t_i,X(t_i))*(W(t_{i+1})-W(t_i))
 * 
 * @author Lorenzo Berti
 *
 */
public abstract class AbstractEulerScheme implements ProcessSimulation {

	private BrownianMotionMultiD brownian;
	private double initialValue;
	private TimeDiscretization times;

	private RandomVariable[] allPaths;

	// This two method must be implemented by the specific class because they
	// are specific of the dynamics of the process.
	protected abstract RandomVariable getDrift(RandomVariable lastRealization, int timeIndex);
	protected abstract RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex);

	protected AbstractEulerScheme(int numberOfSimulations, double initialValue, TimeDiscretization times) {
		this.initialValue = initialValue;
		this.times = times;
		this.brownian = new BrownianMotionD(times, 1, numberOfSimulations);
	}


	@Override
	public double getInitialValue() {

		return initialValue;
	}

	@Override
	public BrownianMotionMultiD getStochasticDriver() {

		return brownian;
	}

	public TimeDiscretization getTimeDiscretization() {

		return times;
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

	// This method generate the Euler scheme for a generical process
	private void generate() {

		RandomVariable drift;
		RandomVariable diffusion;

		allPaths = new RandomVariable[times.getNumberOfTimes()];

		allPaths[0] = new RandomVariableFromDoubleArray(initialValue);

		for (int i = 0; i < times.getNumberOfTimes() - 1; i++) {

			drift = getDrift(allPaths[i], i);
			diffusion = getDiffusion(allPaths[i], i);

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