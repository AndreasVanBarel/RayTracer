package main;

import scene.Item;
import scene.Scene;
import shape.Shape;
import material.Material;
import math.Color;
import math.Point;
import math.Ray;
import math.UV;
import math.Vector;

public class ShadeRec {
	public boolean hit = false;
	public Point hitpoint = null;
	//public Point localhitpoint = null;
	public Vector normalvector = null;
	public Material material = null;
	public Ray ray = null;
	public Scene scene = null;
	public Item item = null; //the item that was hit if hit
	public UV uv = null; //als triangle mesh dan krijgt dit een value
	public double time = 0;
	public int counter = 0;	
	public int recursionDepth = 0;
	
	//public Shape shape = null; //the shape that was hit. Class invariant: this shape is either the shape stored in item, or a subshape of that shape.
	//public Ray effectiveRay = null; //hoort bij de shape om deze daarna nog te kunnen renderen.
	
	public ShadeRec(Ray ray){
		this.ray = ray;
	}
	
	public ShadeRec(){}

	/**
	 * Copies the ShadeRec
	 */
	public ShadeRec(ShadeRec rec) {
		copyFrom(rec);
	}
	
	/**
	 * Copies all values of provided ShadeRec to this ShadeRec.
	 */
	public void copyFrom(ShadeRec rec) {
		hit = rec.hit;
		hitpoint = rec.hitpoint;
		normalvector = rec.normalvector;
		material = rec.material;
		ray = rec.ray;
		scene = rec.scene;
		item = rec.item;
		uv = rec.uv;
		time = rec.time;
		//counter = rec.counter;
		//recursionDepth = rec.recursionDepth;
	}
	
	public Color shade(){
		if (hit) {
			return material.shade(this);
		} else {
			return scene.shade(this); //achtergrondkleur bepaald door scene
		}
	}
	
	public Color shadeNormal(){
		if (hit) {
			return material.shadeNormal(this);
		} else {
			return Color.BLACK; //achtergrondkleur
		}
	}

	/**returns normalvector pointing towards side the ray was incoming*/
	public Vector getGoodNormal(){
		if(ray.direction.cos(normalvector) > 0){
			return normalvector.scale(-1);
		} else {
			return normalvector;
		}
	}
	
	/**returns a hitpoint that can be used to trace other rays from above the surface hit*/
	public Point getSafeHitpointOutside(){
		return hitpoint.add(normalvector.scale(item.getEps()));
	}

	/**returns a hitpoint that can be used to trace other rays from under the surface hit*/
	public Point getSafeHitpointInside() {
		return hitpoint.add(normalvector.scale(-item.getEps()));
	}
	
	/**returns a hitpoint that can be used to trace other rays from above the surface hit*/
	public Point getSafeHitpointAbove(){
		return hitpoint.add(ray.direction.normalize().scale(-item.getEps()));
	}
	
	/**returns a hitpoint that can be used to trace other rays from above the surface hit*/
	public Point getSafeHitpointUnder(){
		return hitpoint.add(ray.direction.normalize().scale(item.getEps()));
	}
	
	public double getDistance(){
		if(hit){
			return hitpoint.subtract(ray.origin).length();
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}
}
