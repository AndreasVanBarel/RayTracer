package material;

import main.ShadeRec;
import math.Color;
import math.Utility;
import math.Vector;

public abstract class Material {
	
	public static final double STANDARD_kd = 0.45;
	public static final double STANDARD_ka = 0.1;
	public static final double STANDARD_ks = 0.45;
	public static final double STANDARD_phongcoef = 50;
	
	public int maxRecursionDepth = 5;
	
	public abstract Color shade(ShadeRec rec);

	public Color shadeNormal(ShadeRec rec){
		Vector normal = rec.normalvector.normalize();
		return new Color(Utility.abs(normal.x), Utility.abs(normal.y), Utility.abs(normal.z));
	}
}

