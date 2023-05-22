package camera;

import sampling.Sample;
import math.Point;
import math.Ray;
import math.Vector;

public class DOFCamera extends PerspectiveCamera {
	
	//distance d to view plane is always just 1
	double f; //focal distance
	double lens_radius;
	
	public DOFCamera(int xResolution, int yResolution, Point origin,
			Vector lookat, Vector up, double fov, double lens_radius) throws NullPointerException,
			IllegalArgumentException {
		super(xResolution, yResolution, origin, lookat, up, fov);
		
		f = lookat.toPoint3D().subtract(origin).length(); // assume lookat defines focal plane
		this.lens_radius = lens_radius;
	}
	
	public Ray generateRay(Sample sample) throws NullPointerException {
		double u = width * (sample.x / (double) xResolution - 0.5);
		double v = height * (sample.y / (double) yResolution - 0.5);
		Vector direction1 = basis.w.add(basis.u.scale(u).add(basis.v.scale(v))); //d = 1
		Point p = direction1.scale(f).toPoint3D(); //punt op focal plane
		Sample lensSample = Sample.getRandomCircle(lens_radius); //Get random sample on thin lens in (u,v) coords
		Point origin = basis.u.scale(lensSample.x).add(basis.v.scale(lensSample.y)).toPoint3D();
		Vector direction = p.subtract(origin);
		return new Ray(origin, direction);
	}

}
