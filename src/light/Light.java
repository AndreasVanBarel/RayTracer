package light;

import main.ShadeRec;
import math.*;

public abstract class Light {
	public Color color;
	public double L; //intensity
	
	public abstract Vector getDirection(ShadeRec rec);
	public abstract boolean castsShadow(ShadeRec rec);
	
	public abstract double getL(ShadeRec rec);
	public boolean isAreaLight(){
		return false;
	}
	public void cycleSample(){
	}
	public int nbSamples(){
		return 1;
	}
}
