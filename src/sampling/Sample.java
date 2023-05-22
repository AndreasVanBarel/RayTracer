package sampling;

import camera.Camera;

import java.util.Random;

import math.Vector;

/**
 * Encapsulates all the data necessary for a {@link Camera} to generate
 * {@link Ray}s.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Sample {
	/**
	 * x coordinate of the sample in image space.
	 */
	public final double x;

	/**
	 * y coordinate of the sample in image space.
	 */
	public final double y;

	/**
	 * Creates a new {@link Sample} for a {@link Camera} at the given position
	 * of the image.
	 * 
	 * @param x
	 *            x coordinate of the sample in image space.
	 * @param y
	 *            y coordinate of the sample in image space.
	 */
	public Sample(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	//basically another constructor; generates random sample inside [x-0.5, x+0.5]*[y-0.5, y+0.5], uniformly distributed
	public static Sample getRandomSample(double x, double y){
		return new Sample(x + rand.nextDouble() - 1/2, y + rand.nextDouble() - 1/2);
	}
	
	//another constructor; generates x and y such that sample is uniformly distributed on the sphere with radius r
	public static Sample getRandomCircle(double r){
		double rSample = rand.nextDouble()*r;
		double rTheta = rand.nextDouble()*2*Math.PI;
		return new Sample(Math.sqrt(rSample)*Math.cos(rTheta), Math.sqrt(rSample)*Math.sin(rTheta));
	}
	
	//another constructor; generates phi (stored in x) and theta (stored in y) 
	public static Sample getRandomHemisphere(double exp){
		Sample s = new Sample(2*Math.PI*rand.nextDouble(), Math.acos( Math.pow(1-rand.nextDouble(), 1/(exp+1)) ));
		return s;
	}
	
	private static Random rand = new Random();
}
