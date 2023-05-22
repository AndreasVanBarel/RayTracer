package texture;

import java.awt.image.BufferedImage;

import math.Color;
import math.Ray;
import math.UV;
import math.Vector;

public class ImageTexture extends Texture {

	public BufferedImage image;
	
	public ImageTexture(BufferedImage image) {
		this.image = image;
	}
	
	/*interprets this image as a texture*/
	@Override
	public Color getColor(UV uv) {
		int x = getX(uv);
		int y = getY(uv);
		java.awt.Color c = new java.awt.Color(image.getRGB(x, y));
		//System.out.println("Color at uv (" + rec.uv.u +", "+rec.uv.v+") and image coords (" + x +", "+y+") is " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		double red = ((double)c.getRed())/255;
		double green = ((double)c.getGreen())/255;
		double blue = ((double)c.getBlue())/255;
		Color color = new Color(red, green, blue);
		return color;
	} 
	
	/*interprets this image as normal map*/
	@Override
	public Vector getNMap(UV uv) {
		int x = getX(uv);
		int y = getY(uv);
		java.awt.Color c = new java.awt.Color(image.getRGB(x, y));
		double vx = ((double)c.getRed())/255 -0.5;
		double vy = ((double)c.getGreen())/255 -0.5;
		double vz = ((double)c.getBlue())/255 -0.5;
		Vector vector = new Vector(vx,vy,vz);
		return vector;
	}
	
	private int getX(UV uv){
		int width = image.getWidth();
		return (int) Math.round((uv.u) * (width-1));
	}
	
	private int getY(UV uv){
		int height = image.getHeight();
		return (int) (int) Math.round((1-uv.v) * (height-1));
	}

}
