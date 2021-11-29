/**
 * 
 */
package com.lorenzoberti.session5;

import com.lorenzoberti.session3.BrownianMotionMultiD;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public interface ProcessSimulation {

	public double getInitialValue();

	public BrownianMotionMultiD getStochasticDriver();

	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex);

	public RandomVariable getProcessAtGivenTime(double time);

	public RandomVariable[] getAllPaths();

	public double[] getSpecificPath(int pathIndex);

	public double getSpecificValueOfSpecificPath(int pathIndex, int timeIndex);

	public void printPath(int pathIndex);

}
