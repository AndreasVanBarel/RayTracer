package shape;

import main.Logger;
import main.ShadeRec;
import math.Point;
import math.Ray;
import math.UV;
import math.Utility;
import math.Vector;

public class MeshTriangle extends Shape {

	public final Mesh mesh; 
	
	public final int a;
	public final int b;
	public final int c;
	
	public final int normala;
	public final int normalb;
	public final int normalc;
	
	public final int uva;
	public final int uvb;
	public final int uvc;

	/**
	 * Creates a new {@link Triangle} with the given points a,b and c, and given normals in those points.
	 */
	public MeshTriangle(Mesh mesh, int a, int b, int c, int normala, int normalb, int normalc, int uva, int uvb, int uvc) {
		this.mesh = mesh;
		this.a = a;
		this.b = b;
		this.c = c;
		this.normala = normala;
		this.normalb = normalb;
		this.normalc = normalc;
		this.uva = uva;
		this.uvb = uvb;
		this.uvc = uvc;
	}
	
	/**
	 * Creates a new {@link MeshTriangle} without uv data
	 */
	public MeshTriangle(Mesh mesh, int a, int b, int c, int normala, int normalb, int normalc) {
		this(mesh,a,b,c,normala,normalb,normalc,0,0,0);
	}
	
	@Override
	public void intersect(ShadeRec rec) {
		Point o = rec.ray.origin;
		Vector d = rec.ray.direction;
		Vector v1 = mesh.getVertex(a).subtract(mesh.getVertex(b));
		Vector v2 = mesh.getVertex(a).subtract(mesh.getVertex(c));
		Vector vb = mesh.getVertex(a).subtract(o);
		
		double m = v2.y*d.z - v2.z*d.y;
		double n = vb.y*d.z - vb.z*d.y;
		double p = v2.y*vb.z - vb.y*v2.z;
		double q = d.y*v1.z - v1.y*d.z;
		double r = v1.y*vb.z - vb.y*v1.z;
		double s = v1.y*v2.z - v2.y*v1.z;
		
		double inv_denom = 1.0/(v1.x*m + v2.x*q + d.x*s);
		
		double e1 = vb.x*m - v2.x*n - d.x*p;
		double beta = e1*inv_denom;
		
		rec.hit = false;
		if ((beta < 0) || (beta > 1))
			return;
		
		double e2 = v1.x*n + vb.x*q + d.x*r;
		double gamma = e2*inv_denom;
		
		if ((gamma < 0) || (gamma > 1) || (beta + gamma > 1))
			return;
		
		double e3 = v1.x*p-v2.x*r+vb.x*s;
		double t = e3*inv_denom;
		
		if (t<0)
			return;

		rec.hit = true;
		rec.time = t;
		rec.hitpoint = rec.ray.getPoint(t);
		UV uv = mesh.getUV(uva).scale(1-beta-gamma).add(mesh.getUV(uvb).scale(beta)).add(mesh.getUV(uvc).scale(gamma));
		//System.out.println("(" + uv.u +", "+uv.v+")");		
		//System.out.println("uv at uva(" + uva +") is (" + mesh.getUV(uva).u +", "+mesh.getUV(uva).v+")");
		rec.uv = uv;
		Vector normal;
		if (mesh.hasNormalMap()){
			normal = mesh.getNormalMap(uv);
		} else {
			normal = mesh.getNormal(normala).scale(1-beta-gamma).add(mesh.getNormal(normalb).scale(beta)).add(mesh.getNormal(normalc).scale(gamma)); //niet opnieuw genormaliseerd
			//if (normal.dot(rec.ray.direction) > 0){ //ray.direction is negatief
			//	normal = normal.scale(-1);
			//}
		}
		rec.normalvector = normal;
	}

	@Override
	public void intersectT(ShadeRec rec) {
		Ray ray = rec.ray;
		Point o = ray.origin;
		Vector d = ray.direction;
		Vector v1 = mesh.getVertex(a).subtract(mesh.getVertex(b));
		Vector v2 = mesh.getVertex(a).subtract(mesh.getVertex(c));
		Vector vb = mesh.getVertex(a).subtract(o);
		
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
			return;}
		
		double e2 = v1.x*n + vb.x*q + d.x*r;
		double gamma = e2*inv_denom;
		
		if ((gamma < 0) || (gamma > 1) || (beta + gamma > 1)){rec.time = Double.POSITIVE_INFINITY;
			return;}
		
		double e3 = v1.x*p-v2.x*r+vb.x*s;
		double t = e3*inv_denom;
		
		if (t<0){rec.time = Double.POSITIVE_INFINITY;
			return;}

		rec.time = t;
	}

	//@Override
	public AABB getAABB() {
		return new AABB(getMin(), getMax());
	}
	
	@Override
	public Point getMax(){
		Point a = mesh.getVertex(this.a);
		Point b = mesh.getVertex(this.b);
		Point c = mesh.getVertex(this.c);
		return a.max(b.max(c));
	}
	
	@Override
	public Point getMin(){
		Point a = mesh.getVertex(this.a);
		Point b = mesh.getVertex(this.b);
		Point c = mesh.getVertex(this.c);
		return a.min(b.min(c));
	}
}
