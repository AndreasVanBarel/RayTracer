package shape;

import main.ShadeRec;
import math.Point;
import math.Ray;
import math.Vector;

public class Cylinder extends Shape {
	
	public double radius;
	public double height;
	
	public Cylinder(double radius, double height){
		this.radius = radius;
		this.height = height;
	}
	
	@Override
	public void intersect(ShadeRec rec) {
		rec.hit = false;
		Ray ray = rec.ray;
		Point e = ray.origin;
		Vector d = ray.direction;
		
		double h = height;

		double a = d.x*d.x + d.z*d.z ;
		double b = 2*(e.x*d.x + e.z*d.z);
		double c = e.x*e.x + e.z*e.z- radius*radius;
		double D = Math.sqrt(b*b - 4*a*c);
		if (D<0){// Cylinder is not hit
			return;
		}

		double sqrtD = Math.sqrt(b*b - 4*a*c);
		double t2 = (-b + sqrtD)/(2*a);
		if (t2 < 0) {//Cylinder completely behind ray	
			return; 	
		}
		double t1 = (-b - sqrtD)/(2*a);

		double z1 = e.y + t1*d.y;
		double z2 = e.y + t2*d.y;
		
		if((0<=z1) && (z1<=h) && (t1>=0)){
			//intersection found (t1,z1)
			rec.time = t1;
			rec.hit = true;
			rec.hitpoint = ray.getPoint(t1);
			rec.normalvector = new Vector(rec.hitpoint.x, 0, rec.hitpoint.z).normalize();
		} else {
			if((z1>h) && (z2<=h) && (e.y > h)){
				//intersection with top cap
				double t = (h - e.y)/d.y;
				rec.time = t;
				rec.hit = true;
				rec.hitpoint = ray.getPoint(t);
				rec.normalvector = new Vector(0,1,0);
			} else if((z1<0) && (z2>=0) && (e.y < 0)) {
				// intersection with bottom cap
				double t = -e.y/d.y; //necessarily automatically positive
				rec.time = t;
				rec.hit = true;
				rec.hitpoint = ray.getPoint(t);
				rec.normalvector = new Vector(0,-1,0);
			} // else no intersection (both z1 and z2 same side of Cylinder)
		}
	}

	@Override
	public void intersectT(ShadeRec rec) {
		Ray ray = rec.ray;
		Point e = ray.origin;
		Vector d = ray.direction;
		
		double h = height;

		double a = d.x*d.x + d.z*d.z ;
		double b = 2*(e.x*d.x + e.z*d.z);
		double c = e.x*e.x + e.z*e.z- radius*radius;
		double D = Math.sqrt(b*b - 4*a*c);
		if (D<0){rec.time = Double.POSITIVE_INFINITY;
			return;} // Cylinder is not hit
		double sqrtD = Math.sqrt(b*b - 4*a*c);
		double t2 = (-b + sqrtD)/(2*a);
		if (t2 < 0){rec.time = Double.POSITIVE_INFINITY;
			return;} //Cylinder completely behind ray		
		double t1 = (-b - sqrtD)/(2*a);

		double z1 = e.y + t1*d.y;
		double z2 = e.y + t2*d.y;
		
		if((0<=z1) && (z1<=h) && (t1>=0)){
			//intersection found (t1,z1)
			rec.time = t1;
		} else {
			if((z1>h) && (z2<=h) && (e.y > h)){
				//intersection with top cap
				double t = (h - e.y)/d.y;
				rec.time = t;
			} else if((z1<0) && (z2>=0) && (e.y < 0)) {
				// intersection with bottom cap
				double t = -e.y/d.y; //necessarily automatically positive
				rec.time = t;
			} else {// else no intersection (both z1 and z2 same side of Cylinder)
				rec.time = Double.POSITIVE_INFINITY;
			}
		}
	}

}
