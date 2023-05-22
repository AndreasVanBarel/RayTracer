package shape;

import main.ShadeRec;
import math.Point;
import math.Ray;
import math.Utility;
import math.Vector;

public class AABox extends Shape{
	public final Point pmin;
	public final Point pmax;

	/**
	 * Creates a new {@link AABox} with the given points a,b and c.
	 */
	public AABox(Point p1, Point p2) {
		this.pmin = new Point(Utility.min(p1.x, p2.x), Utility.min(p1.y, p2.y), Utility.min(p1.z, p2.z));
		this.pmax = new Point(Utility.max(p1.x, p2.x), Utility.max(p1.y, p2.y), Utility.max(p1.z, p2.z));
	}
	
	@Override
	public void intersect(ShadeRec rec) {
		Ray ray = rec.ray;
		Point e = ray.origin;
		Vector d = ray.direction;
		// x direction
		double t = Double.POSITIVE_INFINITY;
		Vector n = null;
		double temp = (pmin.x - e.x)/d.x;
		Point p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.y >= pmin.y)&&(p.y <= pmax.y)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
			n = new Vector(-1,0,0);
		}
		temp = (pmax.x - e.x)/d.x;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.y >= pmin.y)&&(p.y <= pmax.y)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
			n = new Vector(1,0,0);
		}
		// y direction
		temp = (pmin.y - e.y)/d.y;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
			n = new Vector(0,-1,0);
		}
		temp = (pmax.y - e.y)/d.y;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
			n = new Vector(0,1,0);
		}
		// z direction
		temp = (pmin.z - e.z)/d.z;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.y >= pmin.y)&&(p.y <= pmax.y)){
			t = temp;
			n = new Vector(0,0,-1);
		}
		temp = (pmax.z - e.z)/d.z;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.y >= pmin.y)&&(p.y <= pmax.y)){
			t = temp;
			n = new Vector(0,0,1);
		}
		if (t<Double.POSITIVE_INFINITY){
			rec.time = t;
			rec.hit = true;
			rec.hitpoint = ray.getPoint(t);
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
		// x direction
		double t = Double.POSITIVE_INFINITY;
		double temp = (pmin.x - e.x)/d.x;
		Point p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.y >= pmin.y)&&(p.y <= pmax.y)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
		}
		temp = (pmax.x - e.x)/d.x;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.y >= pmin.y)&&(p.y <= pmax.y)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
		}
		// y direction
		temp = (pmin.y - e.y)/d.y;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
		}
		temp = (pmax.y - e.y)/d.y;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.z >= pmin.z)&&(p.z <= pmax.z)){
			t = temp;
		}
		// z direction
		temp = (pmin.z - e.z)/d.z;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.y >= pmin.y)&&(p.y <= pmax.y)){
			t = temp;
		}
		temp = (pmax.z - e.z)/d.z;
		p = ray.getPoint(temp);
		if((temp>0)&&(temp<t)&&(p.x >= pmin.x)&&(p.x <= pmax.x)&&(p.y >= pmin.y)&&(p.y <= pmax.y)){
			t = temp;
		}
		rec.time = t;
	}
}
