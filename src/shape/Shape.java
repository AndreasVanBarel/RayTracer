package shape;

import main.ShadeRec;
import math.Point;
import math.Ray;
import math.Transformation;

/**
 * Interface which should be implemented by all {@link Shape}s.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public abstract class Shape {
	/**
	 * Nodig uit rec: ray
	 * Correct na de oproep if hit: hit, hitpoint, normalvector, ray(unchanged), uv(if applicable), time
	 * Correct na de oproep if not hit: hit, ray(unchanged).
	 */
	public abstract void intersect(ShadeRec rec);
	
	/**
	 * returns the t value if there is an intersection; Double.POSITIVE_INFINITY otherwise
	 */
	public abstract void intersectT(ShadeRec rec);
	
	/**
	 * Generates the AABB for the given shape
	 */
	//public AABB getAABB();
	
	//Methodes om een shape te transformeren.
	
	public Point getMax(){
		return new Point();
	}
	
	public Point getMin(){
		return new Point();
	}
	
	public Point getCenter(){
		return getMin().add(getMax().toVector3D()).scale(0.5);
	}
}
