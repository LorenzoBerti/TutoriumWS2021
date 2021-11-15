/**
 * 
 */
package com.lorenzoberti.session2;

/**
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
	 * This method returns the an array representing the value of the Brownian
	 * motion at a specific time
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
	
	/**
	 * Print the specific path of the Brownian Motion
	 * 
	 * @param path
	 */
	void printPath(int path);


}
