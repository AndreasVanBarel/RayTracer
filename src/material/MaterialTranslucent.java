package material;

import scene.Scene;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Point;
import math.Ray;
import math.Vector;

//THIS IS A TEST MATERIAL, DON'T USE
public class MaterialTranslucent extends Material{
		
	public Color color;
	public Color specular;
	public double phongcoef = STANDARD_phongcoef;
	
	public double kd = STANDARD_kd; //diffuse reflection coefficient
	public double ka = STANDARD_ka; //ambient reflection coefficient
	public double ks = STANDARD_ks; //specular reflection coefficient
	
	public double n; // refractive index
	public int maxRecursionDepth = 5; //0 - no reflectance, 1 - only direct reflectance, etc.
	
	public MaterialTranslucent(Color color, Color specular, double kd, double ka, double ks, double phongcoef){
		this.color = color;
		this.specular = specular;
		this.kd = kd;
		this.ka = ka;
		this.ks = ks;
		this.phongcoef = phongcoef;
	}
	
	public MaterialTranslucent(Color color, Color specular, double n){
		this.color = color;
		this.specular = specular;
		this.n = n;
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

		//reflection, only if max recursion depth hasn't been reached yet.
		if(rec.recursionDepth < maxRecursionDepth){
			double transmittance = 0.9;
			L = L.scale(1-transmittance);
			
			Point transOrigin = rec.getSafeHitpointUnder(); //generate safe hitpoint at the other side
			Vector transDir = rec.ray.direction; //same direction
			Ray transRay = new Ray(transOrigin, transDir);
			ShadeRec transRec = new ShadeRec(transRay);
			transRec.recursionDepth = rec.recursionDepth + 1;
			rec.scene.traceRay(transRec);
			
			L = L.add(transRec.shade().scale(transmittance));
		}
		return L;
	}
	
}
