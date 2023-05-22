package material;

import scene.Scene;
import texture.ConstantTexture;
import texture.Texture;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Vector;

public class MaterialTextured extends Material {
	private Texture color;
	private Texture specular;
	private double phongcoef = STANDARD_phongcoef;
	
	public double kd = STANDARD_kd; //diffuse reflection coefficient
	public double ka = STANDARD_ka; //ambient reflection coefficient
	public double ks = STANDARD_ks; //specular reflection coefficient
	
	public MaterialTextured(Texture texture, Color specular, double kd, double ka, double ks, double phongcoef){
		this(texture,new ConstantTexture(specular),kd,ka,ks,phongcoef);
	}
	
	public MaterialTextured(Color color, Color specular, double kd, double ka, double ks, double phongcoef){
		this(new ConstantTexture(color), new ConstantTexture(specular), kd, ka, ks, phongcoef);
	}
	
	public MaterialTextured(Texture texture, Texture specular, Texture bumpMap){
		this.color = texture;
		this.specular = specular ;		
	}
	
	public MaterialTextured(Texture texture){
		this.color = texture;
		this.specular = texture ;		
	}
	
	public MaterialTextured(Texture texture, Texture specular, double kd, double ka, double ks, double phongcoef){
		this.color = texture;
		this.specular = specular ;		
		this.kd = kd;
		this.ka = ka;
		this.ks = ks;
		this.phongcoef = phongcoef;
	}
	
	/**
	 * Shade in true color with ambient, diffuse and phong shading.
	 */
	@Override
	public Color shade(ShadeRec rec){
		Scene scene = rec.scene;
		Color L = Color.BLACK;
		Vector normal = rec.normalvector;
		
		for (Light light : scene.lights){
			if (!light.castsShadow(rec)){
				Vector dir = light.getDirection(rec);
				double Llight = light.getL(rec);
				Color cLight = light.color;
				double cosine = dir.cos(normal);
				if (cosine > 0){
					//Diffuse
					Color temp = cLight.multiply(color.getColor(rec)).scale(kd*Llight*cosine); //delen door Math.Pi nog
					L = L.add(temp);	
					
					//Phong
					Vector mirrored = normal.mirror(rec.ray.direction.scale(-1));
					cosine = dir.cos(mirrored);
					double brdf = ks*Math.pow(cosine, phongcoef);
					temp = cLight.multiply(specular.getColor(rec)).scale(Llight*brdf);
					L = L.add(temp);
				}
			} 
		}
		//ambient light
		L = L.add(color.getColor(rec).scale(ka));
		return L;
	}
	
}
