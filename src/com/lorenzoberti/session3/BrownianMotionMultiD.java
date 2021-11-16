package com.lorenzoberti.session3;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This interface describes the methods implemented by a (possibly)
 * multi-dimension Brownian motion, that is, a vector (B^1,...B^d) of
 * indipendent Brownian motions B^ì i = 1,...,n. Here, each the Brownian motion
 * is simulated for a time discretization (the time discretization is the same
 * for all Brownian motions) and representend by an object of type
 * RandomVariable. Have a look at net.finmath.stochastic.RandomVariable and the
 * class that implement it (in particular RandomVariableFromDoubleArray). Here,
 * the timeStep is NOT supposed to be constant. However, the Brownian motion can
 * be generated in the same way, i.e. B(t_j) = B(t_{j-1}) + ΔB(t_j) but be
 * careful with the distribution of the Brownian increments...
 * 
 * @author Lorenzo Berti
 *
 */
public interface BrownianMotionMultiD {

	/**
	 * This method returns the time discretization
	 * 
	 * @return the time discretization used to discretize the process
	 */
	TimeDiscretization getTimeDiscretization();

	/**
	 * This method gets and returns all the paths of the Brownian vector
	 * (B^1,...,B^d). Note: dimensions are the number of times and the number of
	 * factor, i.e. the Random Variable "dimension" is given by the
	 * numberOfSimulation.
	 * 
	 * @return the whole path of the Brownian vector
	 */
	RandomVariable[][] getAllPaths();

	/**
	 * This method returns a Random Variable representing the specific Brownian
	 * motion at the given time
	 * 
	 * @param indexFactor
	 * @param time
	 * @return the specific Brownian motion at the given time
	 */
	RandomVariable getBrownianMotionAtSpecificTime(int indexFactor, double time);

	/**
	 * This method returns a Random Variable representing the specific Brownian
	 * motion at the given time index
	 * 
	 * @param indexFactor
	 * @param timeIndex
	 * @return he specific Brownian motion at the given time index
	 */
	RandomVariable getBrownianMotionAtSpecificTimeIndex(int indexFactor, int timeIndex);

	/**
	 * This methods returns an array of Random Variables representing the whole
	 * paths of the specific Brownian motion
	 * 
	 * @param indexFactor
	 * @return the whole paths of the given Brownian motion
	 */
	RandomVariable[] getPathsForFactor(int indexFactor);

	/**
	 * This methods returns an array representing the specific path of the given
	 * Brownian motion
	 * 
	 * @param indexFactor
	 * @param pathIndex
	 * @return an array storing the given path of the given factor
	 */
	double[] getSpecificPathForFactor(int indexFactor, int pathIndex);

	/**
	 * This method returns the two-dimensional array of Random Variables
	 * representing the Brownian increments
	 * 
	 * @return the two dimensional array of Random Variables representing the
	 *         Brownian increments
	 */
	RandomVariable[][] getBrownianIncrements();

	/**
	 * This method returns the Random Variable representing the the Brownian
	 * increment for a given factor at the given timeIndex
	 * 
	 * @param timeIndex
	 * @param indexFactor
	 * @return
	 */
	RandomVariable getBrownianIncrement(int timeIndex, int indexFactor);

}
