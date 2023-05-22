package material;

import scene.Scene;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Vector;

public class MaterialPhong extends Material{
	public Color color;
	public Color specular;
	public double phongcoef = STANDARD_phongcoef;
	
	public double kd = STANDARD_kd; //diffuse reflection coefficient
	public double ka = STANDARD_ka; //ambient reflection coefficient
	public double ks = STANDARD_ks; //specular reflection coefficient
	
	public MaterialPhong(Color color, Color specular, double kd, double ka, double ks, double phongcoef){
		this.color = color;
		this.specular = specular;
		this.kd = kd;
		this.ka = ka;
		this.ks = ks;
		this.phongcoef = phongcoef;
	}
	
	public MaterialPhong(Color color, Color specular){
		this.color = color;
		this.specular = specular;
	}
	
	/**
	 * Shade in true color with ambient, diffuse and phong shading.
	 */
	@Override
	public Color shade(ShadeRec rec){
		Scene scene = rec.scene;
		Color L = Color.BLACK;
		Vector normal = rec.getGoodNormal();
		
		for (Light light : scene.lights){
			for(int i = 0; i < light.nbSamples(); i++){
				light.cycleSample();
				if (!light.castsShadow(rec)){
					Vector dir = light.getDirection(rec);
					double Llight = light.getL(rec);
					Color cLight = light.color;
					double cosine = dir.cos(normal);
					if (cosine > 0){
						//Diffuse
						Color temp = cLight.multiply(this.color).scale(kd*Llight*cosine/light.nbSamples()); //delen door Math.Pi nog
						L = L.add(temp);	

						//Phong
						Vector mirrored = normal.mirror(rec.ray.direction.scale(-1));
						cosine = dir.cos(mirrored);
						double brdf = ks*Math.pow(cosine, phongcoef);
						temp = cLight.multiply(this.specular).scale(Llight*brdf/light.nbSamples());
						L = L.add(temp);
					}
				}
			} 
		}
		//ambient light
		L = L.add(color.scale(ka));
		return L;
	}
	
}
