package com.lorenzoberti.session11;

import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class implements the methods getDrift and getDiffusion for the
 * Black-Scholes model.
 * 
 * @author Lorenzo Berti
 *
 */
public class BlackScholesEulerScheme extends AbstractEulerScheme {

	double mu;
	double sigma;

	public BlackScholesEulerScheme(int numberOfSimulations, double initialValue, TimeDiscretization times, double mu,
			double sigma) {
		super(numberOfSimulations, initialValue, times);
		this.mu = mu;
		this.sigma = sigma;


	}

	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		TimeDiscretization times = getTimeDiscretization();
		double timeStep = times.getTimeStep(timeIndex);

		return lastRealization.mult(mu).mult(timeStep);
	}

	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		BrownianMotionMultiD brownian = getStochasticDriver();
		RandomVariable brownianIncrement = brownian.getBrownianIncrement(timeIndex, 0);
		return lastRealization.mult(sigma).mult(brownianIncrement);
	}

}