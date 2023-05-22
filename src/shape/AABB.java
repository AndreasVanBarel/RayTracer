package shape;

import java.util.ArrayList;
import java.util.TreeMap;

import scene.Item;
import main.ShadeRec;
import math.Point;
import math.Ray;
import math.Utility;
import math.Vector;

public class AABB extends Shape{
	
	public static final boolean LOGGING = false;
	public Point pmin;
	public Point pmax;
	public ArrayList<Shape> shapes;
	private boolean containsJustTwoAABBs = false;

	/**
	 * Creates a new {@link AABB} with the given points a,b and c.
	 */
	public AABB(Point p1, Point p2) {
		this.pmin = new Point(Utility.min(p1.x, p2.x), Utility.min(p1.y, p2.y), Utility.min(p1.z, p2.z));
		this.pmax = new Point(Utility.max(p1.x, p2.x), Utility.max(p1.y, p2.y), Utility.max(p1.z, p2.z));
		shapes = new ArrayList<Shape>();
	}
	
	/**
	 * Creates an empty unspecified AABB. The AABB will be specified when the first item is added.
	 */
	public AABB(){
		double posint = Double.POSITIVE_INFINITY; 
		double negint = Double.NEGATIVE_INFINITY;
		pmax = new Point(negint, negint, negint);
		pmin = new Point(posint, posint, posint);
		shapes = new ArrayList<Shape>();
	}
	
	/**Genereert een AABB uit de gegeven Mesh. Deze AABB zal zelf de TriangleMeshes bevatten van de Mesh. Verder kan de structuur efficienter 
	 * gemaakt worden door split op te roepen op deze bounding box.
	 * @param mesh
	 */
	public AABB(Mesh mesh){
		ArrayList<MeshTriangle> triangles = mesh.getTriangles();
		double posinf = Double.POSITIVE_INFINITY;
		double neginf = Double.NEGATIVE_INFINITY;
		Point pmin = new Point(posinf, posinf, posinf);
		Point pmax = new Point(neginf, neginf, neginf);
		for (MeshTriangle mt : triangles){
			pmin = pmin.min(mt.getMin());
			pmax = pmax.max(mt.getMax());
		}
		this.pmin = pmin;
		this.pmax = pmax;
		shapes = new ArrayList<Shape>(mesh.getTriangles());
	}
	
	public void add(Shape shape){
		pmin = pmin.min(shape.getMin());
		pmax = pmax.max(shape.getMax());
		shapes.add(shape);
	}
	
	/**
	 * Causes this AABB to contain just 2 shapes: 2 new AABB. The new AABB boxes contain all the elements that were previously present in this AABB.
	 * The split is done along the longest axis of the AABB. Furthermore, each new AABB contains an equal (+-1) amount of shapes. The two AABB's on
	 * this next level in the recursion are themselves further divided into new AABB according to this same procedure.
	 * @param maxShapesInLeaf	Stopping criterion: if the amount of shapes left in an AABB is lower than or equal to this value, splitting will stop.
	 * 							In other words, the maximum amount of shapes left in a box anywhere in the hierarchy is always smaller than or equal to
	 * 							this value.
	 * @param counter			To pass in which level of the splitting recursion this function finds itself. This counter is useful for logging
	 * 							or for defining new kinds of stopping criteria.
	 */
	public void split(int maxShapesInLeaf, int counter){
		if(shapes.size() <= maxShapesInLeaf) return; //Te Weinig Shapes om te splitsen.
		//finding the longest axis
		Vector diff = pmax.subtract(pmin);
		AABB box1 = new AABB();
		AABB box2 = new AABB();
		Sortable[] sortables = new Sortable[shapes.size()];
		if((diff.x > diff.y) && (diff.x > diff.z)){
			int i = 0;
			for (Shape shape : shapes) {
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.x + tempmax.x)/2;
				sortables[i++] = new Sortable(center, shape);
			}
			
		} else if(diff.y > diff.z) {
			int i = 0;
			for (Shape shape : shapes) {
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.y + tempmax.y)/2;
				sortables[i++] = new Sortable(center, shape);
			}
			
		} else {
			int i = 0;
			for (Shape shape : shapes) {
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.z + tempmax.z)/2;
				sortables[i++] = new Sortable(center, shape);
			}
		}
		
		Sortable.sortMiddle(sortables);
		
		for (int i=0; i<shapes.size()/2; i++) {
			box1.add(sortables[i].shape);
		}
		for (int i=shapes.size()/2; i<shapes.size(); i++) {
			box2.add(sortables[i].shape);
		}	
		
		shapes = new ArrayList<Shape>();
		shapes.add(box1);
		shapes.add(box2);
		//containsJustTwoAABBs = true; // (1) see below
		log("split done. counter: " + (counter+1) + ", box1: " + box1.shapes.size() + " box2: " + box2.shapes.size());
		box1.split(maxShapesInLeaf, counter+1);
		box2.split(maxShapesInLeaf, counter+1);
	}
	
	@Override
	public void intersect(ShadeRec rec){
		/** calculate object that is intersected first */
		rec.counter += 1;
		if(!intersectedBy(rec.ray)){
			rec.hit = false;
			return;
		}
		
		if(containsJustTwoAABBs){ //Hier nog bugs in, als men dit wil fixen, uitcommentarieer dan ook (1)
			AABB box0 = (AABB)shapes.get(0);
			AABB box1 = (AABB)shapes.get(1);
			double time0 = box0.getIntersectionTimeBox(rec.ray);
			double time1 = box1.getIntersectionTimeBox(rec.ray);
			if(time0 < time1){ //try box0 first, most likely to grant first intersection
				box0.intersect(rec);
				if(!rec.hit || rec.time > time1){ //potentially intersection with box1 that happens before this intersection
					ShadeRec rec2 = new ShadeRec(rec.ray);
					box1.intersect(rec2);
					if(rec2.hit && rec2.time < rec.time) rec.copyFrom(rec2); //if intersection with second box yields earlier intersection point, return those ShadeRec values.
				} // else rec contains the correct values
			} else {
				box1.intersect(rec);
				if(!rec.hit || rec.time > time0){ //potentially intersection with box0 that happens before this intersection with box1
					ShadeRec rec2 = new ShadeRec(rec.ray);
					box0.intersect(rec2);
					if(rec2.hit && rec2.time < rec.time) rec.copyFrom(rec2); //if intersection with second box yields earlier intersection point, return those ShadeRec values.
				} // else rec contains the correct values
			}
		} else {
			/** calculate object that is intersected first */
			ShadeRec firstRec = new ShadeRec(rec.ray);
			firstRec.time = Double.POSITIVE_INFINITY;
			for (Shape shape : shapes){
				shape.intersect(rec);
				if (rec.hit && rec.time < firstRec.time){ //belangrijk rec.hit want we herbruiken dezelfde rec.
					firstRec.copyFrom(rec);
				}
			}
			rec.copyFrom(firstRec);
		}
		
	}
	
	/**
	 * Returns the time until this AABB itself is intersected. The time until intersection with any of the objects inside is
	 * always bigger if the ray started outside the AABB.
	 */
	private double getIntersectionTimeBox(Ray ray) {
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
		return t;
	}
	
	/**
	 * Returns time until intersection with first object in this AABB.
	 */
	public void intersectT(ShadeRec rec){
		rec.counter = rec.counter + 1; //intersected.
		Ray ray = rec.ray;
		if(intersectedBy(ray)){
			double t = Double.POSITIVE_INFINITY;
			for (Shape shape : shapes){
				shape.intersectT(rec);
				if (rec.time < t){
					t = rec.time;
				}
			}
			rec.time = t;
		} else {
			rec.time = Double.POSITIVE_INFINITY;
		}
	}
	
	/**
	 * Returns whether this AABB itself is intersected by the given ray or not. If this function returns false, none of the objects 
	 * enclosed by this AABB are intersected. If this function returns true, objects inside this AABB might be intersected by the given ray.
	 */
	private boolean intersectedBy(Ray ray){
		Point e = ray.origin;
		Vector d = ray.direction;
		double txmin, txmax, tymin, tymax, tzmin, tzmax, tmin, tmax;
		if(d.x > 0){
			txmin = (pmin.x - e.x)/d.x;
			txmax = (pmax.x - e.x)/d.x;
		} else {
			txmax = (pmin.x - e.x)/d.x;
			txmin = (pmax.x - e.x)/d.x;
		}
		if(d.y > 0){
			tymin = (pmin.y - e.y)/d.y;
			tymax = (pmax.y - e.y)/d.y;
		} else {
			tymax = (pmin.y - e.y)/d.y;
			tymin = (pmax.y - e.y)/d.y;
		}
		//Doorsnede tussen deze twee intervallen
		tmin = Utility.max(txmin, tymin);
		tmax = Utility.min(txmax, tymax);
		if((tmin > tmax)||(tmax<0)) return false; //eventueel minder werk
		if(d.z > 0){
			tzmin = (pmin.z - e.z)/d.z;
			tzmax = (pmax.z - e.z)/d.z;
		} else {
			tzmax = (pmin.z - e.z)/d.z;
			tzmin = (pmax.z - e.z)/d.z;
		}
		tmin = Utility.max(tmin, tzmin);
		tmax = Utility.min(tmax, tzmax);
		return ((tmin <= tmax)&&(tmax>0));
	}
	
	private void log(String string){
		if (LOGGING)
			System.out.println(string);
	}
}

/**
 * Creates a bounding box with only the given shape in it
 * @param shape
 */
//public AABB(Shape shape){
//	
//}

/**
 * 	public void split(int maxShapesInLeaf){
		if(shapes.size() <= maxShapesInLeaf) return; //Te Weinig Shapes om te splitsen.
		//finding the longest axis
		Vector diff = pmax.subtract(pmin);
		double splitplane;
		AABB box1 = new AABB();
		AABB box2 = new AABB();
		if((diff.x > diff.y) && (diff.x > diff.z)){
			Sortable[] sortables = new Sortable[shapes.size()];
			int i = 0;
			for (Shape shape : shapes) {
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.x + tempmax.x)/2;
				sortables[i++] = new Sortable(center, shape);
			}
			Sortable.sort(sortables);
			Sortable center = sortables[shapes.size()/2];
			
			//splitplane = pmin.x + diff.x/2;
			for (Shape shape : shapes){
				//Determine in which box the new shapes will come
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.x + tempmax.x)/2;
				if (center < splitplane){ //to Box 1
					box1.add(shape);
				} else { //to Box 2
					box2.add(shape);
				}
	        }
		} else if(diff.y > diff.z) {
			TreeMap<Double, Shape> treemap = new TreeMap<Double, Shape>();
			for (Shape shape : shapes){
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.y + tempmax.y)/2;
				treemap.put(center, shape);
			}
			double[] mapKeys = new double[treemap.size()];
			int pos = 0;
			for (double key : treemap.keySet()) {
			    mapKeys[pos++] = key;
			}
			splitplane = mapKeys[treemap.size()/2];
			//splitplane = pmin.y + diff.y/2;
			for (Shape shape : shapes){
				//Determine in which box the new shapes will come
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.y + tempmax.y)/2;
				if (center < splitplane){ //to Box 1
					box1.add(shape);
				} else { //to Box 2
					box2.add(shape);
				}
	        }
		} else {
			TreeMap<Double, Shape> treemap = new TreeMap<Double, Shape>();
			for (Shape shape : shapes){
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.z + tempmax.z)/2;
				treemap.put(center, shape);
			}
			double[] mapKeys = new double[treemap.size()];
			int pos = 0;
			for (double key : treemap.keySet()) {
			    mapKeys[pos++] = key;
			}
			splitplane = mapKeys[treemap.size()/2];
			splitplane = pmin.z + diff.z/2;
			for (Shape shape : shapes){
				//Determine in which box the new shapes will come
				Point tempmin = shape.getMin();
				Point tempmax = shape.getMax();
				double center = (tempmin.z + tempmax.z)/2;
				if (center < splitplane){ //to Box 1
					box1.add(shape);
				} else { //to Box 2
					box2.add(shape);
				}
	        }
		}
		shapes = new ArrayList<Shape>();
		shapes.add(box1);
		shapes.add(box2);
		System.out.println("split done. it: " + maxShapesInLeaf + ", box1: " + box1.shapes.size() + " box2: " + box2.shapes.size());
		box1.split(maxShapesInLeaf-1);
		box2.split(maxShapesInLeaf-1);
	}
	
	public void intersectOLD(ShadeRec rec){		
		if(!intersectedBy(rec.ray)){
			rec.hit = false;
			return;
		}
		
		double t = Double.POSITIVE_INFINITY;
		double temp;
		Shape firstShape = null;
		for (Shape shape : shapes){
			shape.intersectT(rec);
			temp = rec.time;
			if (temp < t){
				firstShape = shape;
				t = temp;
			}
		}
		if(firstShape == null){
			rec.hit = false;
		} else {
			firstShape.intersect(rec);
		}
	}
	**/
