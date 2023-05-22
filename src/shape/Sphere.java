package shape;

import main.ShadeRec;
import math.Ray;
import math.Transformation;
import math.*;

/**
 * Represents a three dimensional sphere.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Sphere extends Shape {
	public Transformation transformation;
	public final double radius;

	/**
	 * Creates a new {@link Sphere} with the given radius and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Sphere}.
	 * @param radius
	 *            the radius of this {@link Sphere}..
	 * @throws NullPointerException
	 *             when the transformation is null.
	 * @throws IllegalArgumentException
	 *             when the radius is smaller than zero.
	 */
	public Sphere(Transformation transformation, double radius) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		if (radius < 0)
			throw new IllegalArgumentException(
					"the given radius cannot be smaller than zero!");
		this.transformation = transformation;
		this.radius = radius;
	}
	
	@Override
	public void intersect(ShadeRec rec){
		Ray ray = rec.ray;
		Ray transformed = transformation.transformInverse(ray);

		Vector o = transformed.origin.toVector3D();

		double a = transformed.direction.dot(transformed.direction);
		double b = 2.0 * (transformed.direction.dot(o));
		double c = o.dot(o) - radius * radius;

		double d = b * b - 4.0 * a * c;

		if (d < 0) {
			rec.hit = false;
		}
		double dr = Math.sqrt(d);
		double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

		double t0 = q / a;
		double t1 = c / q;

		if(t0 < 0 && t1 < 0){
			rec.hit = false;
		} else {
			rec.hit = true;			
			double t = Utility.min(t0,t1);
			if(t0<0) t = t1;
			if(t1<0) t = t0;
			rec.time = t;
			rec.hitpoint = ray.getPoint(t);
			Point localhitpoint = transformed.getPoint(t);
			Vector localnormalvector = localhitpoint.toVector3D();
			Vector normalvector = transformation.getInverseTransformationMatrix().transpose().transform(localnormalvector);
			rec.normalvector = normalvector.normalize();
		}
	}
	
	/**
	 * Deze functie geeft terug de t-waarde horende bij de intersectie met dit object. 
	 * t kan niet negatief zijn. Als er geen intersectie is, geeft deze functie +Inf terug.
	 */
	public void intersectT(ShadeRec rec){
		Ray transformed = transformation.transformInverse(rec.ray);

		Vector o = transformed.origin.toVector3D();

		double a = transformed.direction.dot(transformed.direction);
		double b = 2.0 * (transformed.direction.dot(o));
		double c = o.dot(o) - radius * radius;

		double d = b * b - 4.0 * a * c;

		if (d < 0){rec.time = Double.POSITIVE_INFINITY;
			return;}
		double dr = Math.sqrt(d);
		double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

		double t0 = q / a;
		double t1 = c / q;

		if (t0<0 && t1<0) {
			rec.time = Double.POSITIVE_INFINITY;
		} else {
			rec.time = Utility.min(t0,t1);
			if(t0<0) rec.time = t1;
			if(t1<0) rec.time = t0;
		}
	}
	

	//@Override
	public AABB getAABB() {
		// TODO Auto-generated method stub
		return null;
	}

}
