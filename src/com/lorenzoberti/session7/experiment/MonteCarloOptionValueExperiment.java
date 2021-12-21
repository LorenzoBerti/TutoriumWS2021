/**
 * 
 */
package com.lorenzoberti.session7.experiment;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.NormalDistribution;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromRandomNumberGenerator;
import net.finmath.randomnumbers.MersenneTwister;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * @author Lorenzo Berti
 *
 */
public class MonteCarloOptionValueExperiment {

	// Model parameters
	private double initialValue = 100.0;
	private double riskFreeRate = 0.05;
	private double volatility = 0.2;

	// Option parameters
	private double strike = 100.0;
	private double maturity = 1.0;

	private int seed = 3083;
	private int numberOfSimulation = 10000000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		long timeStart, timeEnd;

		MonteCarloOptionValueExperiment experiment = new MonteCarloOptionValueExperiment();

		timeStart = System.currentTimeMillis();
		double valueAnalytic = experiment.getAnalyticValue();
		timeEnd = System.currentTimeMillis();

		System.out.println(
				"Analytic value...............: " + valueAnalytic + "\t" + (timeEnd - timeStart) / 1000.0 + "sec.");

		timeStart = System.currentTimeMillis();
		double valueForLoop = experiment.getValueUsingForLoop();
		timeEnd = System.currentTimeMillis();

		System.out.println(
				"Value with for loop..........: " + valueForLoop + "\t" + (timeEnd - timeStart) / 1000.0 + "sec.");

		timeStart = System.currentTimeMillis();
		double valueRandomVariable = experiment.getValueUsingRandomVariable();
		timeEnd = System.currentTimeMillis();

		System.out
				.println("Value with RV................: " + valueRandomVariable + "\t"
				+ (timeEnd - timeStart) / 1000.0 + "sec.");

		timeStart = System.currentTimeMillis();
		double valueStreams = experiment.getValueUsingStreams();
		timeEnd = System.currentTimeMillis();

		System.out
				.println("Value with Streams...........: " + valueStreams + "\t" + (timeEnd - timeStart) / 1000.0
						+ "sec.");

	}

	private double getValueUsingStreams() {

		DoubleUnaryOperator model = z -> initialValue * Math
				.exp((riskFreeRate - volatility * volatility * 0.5) * maturity + volatility * Math.sqrt(maturity) * z);

		DoubleUnaryOperator payoff = s -> Math.max(s - strike, 0) * Math.exp(-riskFreeRate * maturity);

		MersenneTwister mersenne = new MersenneTwister(seed);
		DoubleStream uniform = DoubleStream.generate(mersenne).limit(numberOfSimulation);
		DoubleStream normal = uniform.map(NormalDistribution::inverseCumulativeDistribution);

		DoubleStream underlying = normal.map(model);
		DoubleStream payoffStream = underlying.map(payoff);

		double value = payoffStream.parallel().sum();

		return value / numberOfSimulation;
	}

	private double getValueUsingRandomVariable() {

		MersenneTwister mersenne = new MersenneTwister(seed);

		TimeDiscretization time = new TimeDiscretizationFromArray(0.0, 1.0, maturity);

		BrownianMotion brownian = new BrownianMotionFromRandomNumberGenerator(time, 1, numberOfSimulation, mersenne);

		RandomVariable deltaW = brownian.getBrownianIncrement(0.0, 0);

		double drift = (riskFreeRate - 0.5 * volatility * volatility) * maturity;
		RandomVariable diffusion = deltaW.mult(volatility);
		RandomVariable asset = diffusion.add(drift).exp().mult(initialValue);

		RandomVariable payoff = asset.sub(strike).floor(0.0);

		return payoff.getAverage() * Math.exp(-riskFreeRate * maturity);
	}

	private double getValueUsingForLoop() {

		MersenneTwister mersenne = new MersenneTwister(seed);

		double sum = 0.0;

		for (int i = 0; i < numberOfSimulation; i++) {
			double uniform = mersenne.nextDouble();
			double normal = NormalDistribution.inverseCumulativeDistribution(uniform);
			double asset = initialValue * Math.exp((riskFreeRate - volatility * volatility * 0.5) * maturity
					+ normal * Math.sqrt(maturity) * volatility);
			double payoff = Math.max(asset - strike, 0.0);

			sum += payoff * Math.exp(-riskFreeRate * maturity);
			;
		}

		return sum / numberOfSimulation;
	}

	private double getAnalyticValue() {

		return AnalyticFormulas.blackScholesOptionValue(initialValue, riskFreeRate, volatility, maturity, strike);
	}

}
