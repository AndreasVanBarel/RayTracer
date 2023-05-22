package light;

import main.ShadeRec;
import math.Color;
import math.Point;
import math.Ray;
import math.Vector;

public class DirectionalLight extends Light {
	public Vector direction; //is always normalized
	
	public DirectionalLight(Vector direction, Color color, double L_intensity){
		this.direction = direction.normalize();
		this.color = color;
		this.L = L_intensity;
	}
	
	public DirectionalLight(Vector direction, Color color){
		this(direction, color, 1);
	}
	
	public DirectionalLight(Vector direction){
		this(direction, Color.WHITE);
	}

	@Override
	public Vector getDirection(ShadeRec rec) {
		return direction;
	}
	
	@Override
	public boolean castsShadow(ShadeRec rec){
		//construct shadow ray
		Point origin = rec.hitpoint.add(getDirection(rec).scale(rec.item.getEps()));
		Ray shadowRay = new Ray(origin, getDirection(rec));
		return rec.scene.traceRay(shadowRay).hit;
	}
	
	public double getL(ShadeRec rec){
		return L;
	}

}
