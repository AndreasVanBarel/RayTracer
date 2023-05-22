package shape;

import main.ShadeRec;
import math.Ray;
import math.Utility;
import math.Vector;
import math.Point;

/**
 * Represents a three dimensional sphere.
 * 
 * @author Andreas Van Barel
 * @version 1.0
 */

// DIENT geoptimaliseerd te worden voor al dan niet Phong interpolatie.
public class Triangle extends Shape {
	public final Point a;
	public final Point b;
	public final Point c;
	
	public final Vector normala;
	public final Vector normalb;
	public final Vector normalc;

	/**
	 * Creates a new {@link Triangle} with the given points a,b and c, and given normals in those points.
	 */
	public Triangle(Point a, Point b, Point c, Vector normala, Vector normalb, Vector normalc) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.normala = normala.normalize();
		this.normalb = normalb.normalize();
		this.normalc = normalc.normalize();
	}
	
	/**
	 * Creates a new {@link Triangle} with the same normal in every point.
	 */
	public Triangle(Point a, Point b, Point c, Vector normal) {
		this(a,b,c,normal,normal,normal);
	}
	
	/**
	 * Creates a new {@link Triangle} with logical normal vector.
	 */
	public Triangle(Point a, Point b, Point c) {
		this(a,b,c,b.subtract(a).cross(c.subtract(a)));
	}
	
	@Override
	public void intersect(ShadeRec rec) {
		Ray ray = rec.ray;
		Point o = ray.origin;
		Vector d = ray.direction;
		Vector v1 = a.subtract(b);
		Vector v2 = a.subtract(c);
		Vector vb = a.subtract(o);
		
		double m = v2.y*d.z - v2.z*d.y;
		double n = vb.y*d.z - vb.z*d.y;
		double p = v2.y*vb.z - vb.y*v2.z;
		double q = d.y*v1.z - v1.y*d.z;
		double r = v1.y*vb.z - vb.y*v1.z;
		double s = v1.y*v2.z - v2.y*v1.z;
		
		double inv_denom = 1.0/(v1.x*m + v2.x*q + d.x*s);
		
		double e1 = vb.x*m - v2.x*n - d.x*p;
		double beta = e1*inv_denom;
		
		if ((beta < 0) || (beta > 1)){
			rec.hit = false;
			return;
		}
		
		double e2 = v1.x*n + vb.x*q + d.x*r;
		double gamma = e2*inv_denom;
		
		if ((gamma < 0) || (beta + gamma > 1)){
			rec.hit = false;
			return;
		}
		
		double e3 = v1.x*p-v2.x*r+vb.x*s;
		double t = e3*inv_denom;
		
		if (t<0){
			rec.hit = false;
			return;
		}

		rec.time = t;
		rec.hit = true;
		rec.hitpoint = ray.getPoint(t);
		Vector normal = normala.scale(1-beta-gamma).add(normalb.scale(beta)).add(normalc.scale(gamma)); //niet opnieuw genormaliseerd
		normal = normala; //DEZE REGEL MOET WEG, DEBUG ONLY.
		if (normal.dot(ray.direction) > 0){ //ray.direction is negatief
			rec.normalvector = normal.scale(-1);
		} else {
			rec.normalvector = normal;
		}
	}

	@Override
	public void intersectT(ShadeRec rec) {
		Ray ray = rec.ray;
		Point o = ray.origin;
		Vector d = ray.direction;
		Vector v1 = a.subtract(b);
		Vector v2 = a.subtract(c);
		Vector vb = a.subtract(o);
		
		double m = v2.y*d.z - v2.z*d.y;
		double n = vb.y*d.z - vb.z*d.y;
		double p = v2.y*vb.z - vb.y*v2.z;
		double q = d.y*v1.z - v1.y*d.z;
		double r = v1.y*vb.z - vb.y*v1.z;
		double s = v1.y*v2.z - v2.y*v1.z;
		
		double inv_denom = 1.0/(v1.x*m + v2.x*q + d.x*s);
		
		double e1 = vb.x*m - v2.x*n - d.x*p;
		double beta = e1*inv_denom;
		
		if ((beta < 0) || (beta > 1)){rec.time = Double.POSITIVE_INFINITY;
			return; }
		
		double e2 = v1.x*n + vb.x*q + d.x*r;
		double gamma = e2*inv_denom;
		
		if ((gamma < 0) || (beta + gamma > 1)){rec.time = Double.POSITIVE_INFINITY;
			return ;}
		
		double e3 = v1.x*p-v2.x*r+vb.x*s;
		double t = e3*inv_denom;
		
		if (t<0){rec.time = Double.POSITIVE_INFINITY;
			return;}

		rec.time = t;
	}

	//@Override
	public AABB getAABB() {
		// TODO Auto-generated method stub
		return null;
	}
}