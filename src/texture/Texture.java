package texture;

import main.ShadeRec;
import math.Color;
import math.Ray;
import math.UV;
import math.Vector;

public abstract class Texture {
	
	public abstract Color getColor(UV uv);

	public Color getColor(ShadeRec rec){
		return getColor(rec.uv);
	}
	
	public abstract Vector getNMap(UV uv);
	
	public Vector getNMap(ShadeRec rec){
		return getNMap(rec.uv);
	}
	
	public Color getEnvColor(Ray ray){
		double theta = ray.direction.getTheta();
		double phi = ray.direction.getPhi();
		UV uv = new UV(theta/(2*Math.PI), phi/(Math.PI) + 0.5);
		return getColor(uv);
	}
}
