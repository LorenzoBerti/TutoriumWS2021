/**
 * 
 */
package com.lorenzoberti.session8.parallelization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.NormalDistribution;
import net.finmath.randomnumbers.MersenneTwister;

/**
 * @author Lorenzo Berti
 *
 */
public class OptionValueParallelizationExperiment {

	private double initialValue = 100.0;
	private double riskFreeRate = 0.05;
	private double volatility = 0.20;

	private double optionMaturity = 1.0;
	private double optionStrike = 105;

	private long seed = 3216;
	private int numberOfSamples = 100000000; // 1*10^8

	/**
	 * @param args
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		long timeStart, timeEnd;

		int numberOfThreads = Runtime.getRuntime().availableProcessors();

		System.out.println("The number of threads is: " + numberOfThreads);

		OptionValueParallelizationExperiment experiment = new OptionValueParallelizationExperiment();

		timeStart = System.currentTimeMillis();
		double valueAnalytic = experiment.getAnalyticValue();
		timeEnd = System.currentTimeMillis();

		System.out.println("Analytic value.......................: " + valueAnalytic + " \t("
				+ (timeEnd - timeStart) / 1000.0 + " sec.)");

		experiment.getMonteCarloValueUsingExecutor();

		experiment.getMonteCarloValueUsingStreamParallel();

		experiment.getMonteCarloValueUsingStreamSeq();

		experiment.getMonteCarloValueUsingStreamParallelSync();

	}

	private double getAnalyticValue() {
		return AnalyticFormulas.blackScholesOptionValue(initialValue, riskFreeRate, volatility, optionMaturity,
				optionStrike);
	}

	private void getMonteCarloValueUsingExecutor() throws InterruptedException, ExecutionException {

		double timeStart = System.currentTimeMillis();

		int numberOfTask = 100;
		int numberOfSamplesPerTask = numberOfSamples / numberOfTask;

		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

		try {
			/*
			 * Distribute the tasks.
			 */
			List<Future<Double>> results = new ArrayList<>();
			for (int taskIndex = 0; taskIndex < numberOfTask; taskIndex++) {

				int startIndex = taskIndex * numberOfSamplesPerTask;

				Future<Double> value = executor.submit(() -> getValue(startIndex, numberOfSamplesPerTask));
				results.add(value);
			}

			/*
			 * Collect the results
			 */
			double sum = 0.0;
			for (int taskIndex = 0; taskIndex < numberOfTask; taskIndex++) {
				sum += results.get(taskIndex).get();
			}
			double value = sum / numberOfTask;

			double timeEnd = System.currentTimeMillis();

			System.out.println("Monte-Carlo (using Executor).........: " + value + " \t("
					+ (timeEnd - timeStart) / 1000.0 + " sec.)");

		} finally {
			executor.shutdown();
		}

	}

	private void getMonteCarloValueUsingStreamParallelSync() {

		MersenneTwister mersenne = new MersenneTwister(seed);

		long timeStart = System.currentTimeMillis();
		double value = IntStream.range(0, numberOfSamples).parallel().mapToDouble(i -> {
			synchronized (mersenne) {
				double uniform = mersenne.nextDouble();
				double normal = NormalDistribution.inverseCumulativeDistribution(uniform);
				double assetValue = initialValue
						* Math.exp(riskFreeRate * optionMaturity - 0.5 * volatility * volatility * optionMaturity
								+ volatility * Math.sqrt(optionMaturity) * normal);
				return Math.max(assetValue - optionStrike, 0);
			}
		}).sum() / numberOfSamples * Math.exp(-riskFreeRate * optionMaturity);
		long timeEnd = System.currentTimeMillis();

		System.out.println(
				"Monte-Carlo (using Stream synchr.)...: " + value + " \t(" + (timeEnd - timeStart) / 1000.0 + " sec.)");

	}

	private void getMonteCarloValueUsingStreamParallel() {

		MersenneTwister mersenne = new MersenneTwister(seed);

		long timeStart = System.currentTimeMillis();
		double value = IntStream.range(0, numberOfSamples).parallel().mapToDouble(i -> {
			double uniform = mersenne.nextDouble();
			double normal = NormalDistribution.inverseCumulativeDistribution(uniform);
			double assetValue = initialValue * Math.exp(riskFreeRate * optionMaturity
					- 0.5 * volatility * volatility * optionMaturity + volatility * Math.sqrt(optionMaturity) * normal);
			return Math.max(assetValue - optionStrike, 0);
		}).sum() / numberOfSamples * Math.exp(-riskFreeRate * optionMaturity);
		long timeEnd = System.currentTimeMillis();

		System.out.println(
				"Monte-Carlo (using Stream parallel)..: " + value + " \t(" + (timeEnd - timeStart) / 1000.0 + " sec.)");

	}

	private void getMonteCarloValueUsingStreamSeq() {

		MersenneTwister mersenne = new MersenneTwister(seed);

		long timeStart = System.currentTimeMillis();
		double value = IntStream.range(0, numberOfSamples).mapToDouble(i -> {
			double uniform = mersenne.nextDouble();
			double normal = NormalDistribution.inverseCumulativeDistribution(uniform);
			double assetValue = initialValue * Math.exp(riskFreeRate * optionMaturity
					- 0.5 * volatility * volatility * optionMaturity + volatility * Math.sqrt(optionMaturity) * normal);
			return Math.max(assetValue - optionStrike, 0);
		}).sum() / numberOfSamples * Math.exp(-riskFreeRate * optionMaturity);
		long timeEnd = System.currentTimeMillis();

		System.out.println(
				"Monte-Carlo (using Stream seq.)......: " + value + " \t(" + (timeEnd - timeStart) / 1000.0 + " sec.)");

	}

	private double getValue(int startIndex, int numberOfSamplePerTask) {

		MersenneTwister mersenne = new MersenneTwister(seed);

		double sum = 0.0;
		for (int i = startIndex; i < startIndex + numberOfSamplePerTask; i++) {
			double uniform = mersenne.nextDouble();
			double normal = NormalDistribution.inverseCumulativeDistribution(uniform);
			double underlying = initialValue * Math.exp(riskFreeRate * optionMaturity
					- 0.5 * volatility * volatility * optionMaturity + volatility * Math.sqrt(optionMaturity) * normal);
			double payoff = Math.max(underlying - optionStrike, 0.0);

			sum += payoff * Math.exp(-riskFreeRate * optionMaturity);
		}
		double value = sum / numberOfSamplePerTask;
		return value;

	}

}