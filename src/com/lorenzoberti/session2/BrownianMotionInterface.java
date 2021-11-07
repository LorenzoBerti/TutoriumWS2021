/**
 * 
 */
package com.lorenzoberti.session2;

/**
 * This interface provides the discretization of a one-dimensional Brownian
 * motion. The Brownian motion is simulated for a time discretization
 * (t_0,t_1,..,t_n), supposing t_i-t_{i-1} = Δ, i.e., constant for any i =
 * 1,..,n. Note: Here we use array of double to make life easier, but of course
 * it could be done in a more efficient way, e.g. using RandomVariable from the
 * finmath library...
 * 
 * @author Lorenzo Berti
 *
 */
public interface BrownianMotionInterface {
	
	/**
	 * It returns the one dimensional array of object of doubles representing all
	 * the paths of the Brownian motion
	 * 
	 * @return the entire path of the Brownian motion
	 */
	double[][] getPaths();

	/**
	 * This method returns an array representing the value of the Brownian motion at
	 * a specific time
	 * 
	 * @param timeIndex
	 * @return a Random Variable which represents the Brownian motion at a given
	 *         time index
	 */
	double[] getProcessAtTimeIndex(int timeIndex);

	/**
	 * This method returns the specified path of the Brownian motion
	 * 
	 * @param path
	 * @return the whole specific path of the Brownian motion
	 */
	double[] getSpecificPath(int path);

	/**
	 * This method return the value of a specific path the Brownian motion at a
	 * given time.
	 * 
	 * @param path
	 * @param timeIndex
	 * @return specific value of the Brownian motion
	 */
	double getSpecificValue(int path, int timeIndex);
	


}
