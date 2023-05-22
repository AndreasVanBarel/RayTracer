package shape;

import main.ShadeRec;
import math.Ray;
import math.Vector;
import math.Point;

/**
 * Represents a three dimensional sphere.
 * 
 * @author Andreas Van Barel
 * @version 1.0
 */
public class Plane extends Shape {
	public final Point p;
	public final Vector n;

	/**
	 * Creates a new {@link Plane} with the given point p and normal n
	 */
	public Plane(Point p, Vector n) {
		this.p = p;
		this.n = n.normalize();
	}

	@Override
	public void intersect(ShadeRec rec) {
		Point e = rec.ray.origin;
		Vector d = rec.ray.direction;
		double t = n.dot(p.subtract(e))/n.dot(d);
		if(t>=0){
			rec.hit = true;
			rec.time = t;
			rec.hitpoint = rec.ray.getPoint(t);
			if (n.dot(d) > 0){ //d is negatief
				rec.normalvector = n.scale(-1);
			} else {
				rec.normalvector = n;
			}
		} else {
			rec.hit = false;
		}
	}

	public void intersectT(ShadeRec rec) {
		Ray ray = rec.ray;
		Point e = ray.origin;
		Vector d = ray.direction;
		double t = n.dot(p.subtract(e))/n.dot(d);
		if (t>=0){rec.time = t;
			return;}
		rec.time = Double.POSITIVE_INFINITY;
	}

	//@Override
	public AABB getAABB() {
		// TODO Auto-generated method stub
		return null;
	}
}