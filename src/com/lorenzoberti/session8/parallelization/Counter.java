/**
 * 
 */
package com.lorenzoberti.session8.parallelization;

import java.util.stream.IntStream;

/**
 * @author Lorenzo Berti
 *
 */
public class Counter {

	// private static AtomicInteger parallelCount = new AtomicInteger();
	private static int parallelCount = 0;
	private static int sequentialCount = 0;

	public static void main(String[] args) {

		int n = 10000000;

		// I count in parallel:
		IntStream.range(0, n).parallel().forEach(i -> {
			parallelCount++;
			// parallelCount.getAndIncrement();
		});

		// I count sequentially:
		for (int i = 0; i < n; i++) {
			sequentialCount++;
		}

		System.out.println("parallelCount   = " + parallelCount);
		System.out.println("sequentialCount = " + sequentialCount);

	}

}