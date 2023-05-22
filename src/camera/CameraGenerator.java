package camera;

import math.Point;
import math.Vector;

public class CameraGenerator {
	
	public static Camera generateCamera(int i, int width, int height){
		switch (i) {
		case 12: return generateCamera12(width, height);
		case 13: return generateCamera13(width, height);
		case 20: return generateCamera20(width, height);
		default: return generateCameraDefault(width, height);
		}
	}
	
	public static Camera generateCameraDefault(int width, int height){
		return new PerspectiveCamera(width, height,
				new Point(0,0,0), new Vector(0, 0, 1), new Vector(0, 1, 0), 90);
	}
	
	public static Camera generateCamera13(int width, int height){
		return new PerspectiveCamera(width, height,
				new Point(0,0,0), new Vector(0, 0, 1), new Vector(0, 1, 0), 60);
	}
	
	public static Camera generateCamera20(int width, int height){
		return new DOFCamera(width, height, new Point(0,0,0), new Vector(0,0,20), new Vector(0,1,0), 60, 0.05);
	}
	
	public static Camera generateCamera12(int width, int height){
		return new PerspectiveCamera(width, height, new Point(0,0,0), new Vector(0,0,20), new Vector(0,1,0), 60);
	}
}
