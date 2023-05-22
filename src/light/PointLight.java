package light;

import main.ShadeRec;
import math.Color;
import math.Point;
import math.Ray;
import math.Vector;

public class PointLight extends Light {

	public Point position;
	
	public PointLight(Point position, Color color, double L_intensity){
		this.position = position;
		this.color = color;
		this.L = L_intensity;
	}
	
	public PointLight(Point position, Color color){
		this(position, color, 1);
	}
	
	public PointLight(Point position){
		this(position, Color.WHITE);
	}

	@Override
	public Vector getDirection(ShadeRec rec) {
		return position.subtract(rec.hitpoint);
	}
	
	@Override
	public boolean castsShadow(ShadeRec rec){
		//construct shadow ray
		Point origin = rec.hitpoint.add(getDirection(rec).normalize().scale(rec.item.getEps()));
		Ray shadowRay = new Ray(origin, getDirection(rec));
		return rec.scene.traceShadow(shadowRay,1); //1 eigenlijk niet exact ma bon.
	}
	
	public double getL(ShadeRec rec){
		return L/position.subtract(rec.hitpoint).lengthSquared();
	}

}
