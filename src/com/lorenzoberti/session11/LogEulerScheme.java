/**
 * 
 */
package com.lorenzoberti.session11;

import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class implements the methods getDrift and getDiffusion for the Log-Euler
 * discretization of the Black-Scholes model.
 * 
 * @author Lorenzo Berti
 *
 */
public class LogEulerScheme extends AbstractEulerScheme {

	double mu;
	double sigma;

	public LogEulerScheme(int numberOfSimulations, double initialValue, TimeDiscretization times, double mu,
			double sigma) {
		super(numberOfSimulations, initialValue, times);
		this.mu = mu;
		this.sigma = sigma;

		// Transform is the exponential function;
		this.transform = (x -> Math.exp(x));

		// inverseTransform is the exponential function;
		this.inverseTransform = (x -> Math.log(x));

	}

	public LogEulerScheme(BrownianMotionMultiD brownian, double mu, double sigma, double initialValue) {
		super(brownian, initialValue);
		this.mu = mu;
		this.sigma = sigma;

		// Transform is the exponential function;
		this.transform = (x -> Math.exp(x));

		// inverseTransform is the logarithmic function
		this.inverseTransform = (x -> Math.log(x));
	}

	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		TimeDiscretization times = getTimeDiscretization();
		double drift = (mu - sigma * sigma * 0.5) * times.getTimeStep(timeIndex);
		return new RandomVariableFromDoubleArray(times.getTime(timeIndex), drift);
	}

	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		BrownianMotionMultiD brownian = getStochasticDriver();
		RandomVariable brownianIncrement = brownian.getBrownianIncrement(timeIndex, 0);
		return brownianIncrement.mult(sigma);
	}

}
