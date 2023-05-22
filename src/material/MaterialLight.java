package material;

import main.ShadeRec;
import math.Color;

public class MaterialLight extends Material {

	private Color color;
	
	public MaterialLight(Color color){
		this.color = color;
	}
	
	/**
	 * Shade in one constant color
	 */
	@Override
	public Color shade(ShadeRec rec){
		return color;
	}
}
