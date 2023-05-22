package texture;

import math.Color;
import math.UV;
import math.Vector;

public class ConstantTexture extends Texture {
	public Color color;
	
	public ConstantTexture(Color color){
		this.color = color;
	}
	
	@Override
	public Color getColor(UV uv){
		return color;
	}
	
	@Override
	public Vector getNMap(UV uv) {
		return new Vector(0,0,0);
	}
}
