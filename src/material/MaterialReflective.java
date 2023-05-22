package material;

import scene.Scene;
import texture.Texture;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Ray;
import math.Vector;

public class MaterialReflective extends Material{
		
	public Color color;
	public Color specular;
	public double phongcoef = STANDARD_phongcoef;
	private Texture envMap = null;
	
	public double kd = STANDARD_kd; //diffuse reflection coefficient
	public double ka = STANDARD_ka; //ambient reflection coefficient
	public double ks = STANDARD_ks; //specular reflection coefficient
	public double ka_after = 0; //for special effects
	
	public double r0; // reflectance coefficient between 0 and 1
	//public int maxRecursionDepth = 2; //0 - no refraction, 1 - only direct refraction, etc.
	
	public MaterialReflective(Color color, Color specular, double kd, double ka, double ks, double r0){
		this.color = color;
		this.specular = specular;
		this.kd = kd;
		this.ka = ka;
		this.ks = ks;
		this.r0 = r0;
	}
	
	public MaterialReflective(Color color, Color specular, double r0){
		this.color = color;
		this.specular = specular;
		this.r0 = r0;
	}
	
	public void setEnvMap(Texture envMap){
		this.envMap = envMap;
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
		//L = Color.BLACK;

		//reflection, only if max recursion depth hasn't been reached yet.
		if(rec.recursionDepth < maxRecursionDepth){
			double cosine = -rec.ray.direction.cos(normal);
			double reflectance = getReflectance(cosine);
			L = L.scale(1-reflectance); //niet gereflecteerde omlaag schalen
			
			//construct new ray
			Vector mirrored = normal.mirror(rec.ray.direction.scale(-1));
			Ray ray = new Ray(rec.getSafeHitpointAbove(), mirrored);
			Color mirrorL;
			if(envMap != null){
				mirrorL = envMap.getEnvColor(ray);
			} else {
				ShadeRec newRec = new ShadeRec(ray);
				newRec.recursionDepth = rec.recursionDepth + 1;
				//int oldRecDepth = newRec.recursionDepth;
				rec.scene.traceRay(newRec);
				//if(oldRecDepth != newRec.recursionDepth) System.out.println("error");
				mirrorL = newRec.shade();
			}
			//mirrorL = Color.MAGENTA;
			L = L.add(mirrorL.multiply(specular).scale(reflectance));

		}
		
		//ambient light
		L = L.add(color.scale(ka_after));
		
		return L;
	}

	/** geeft benadering van Fresnel reflectance uit de cosinus van de hoek tussen normaal en straal. */
	public double getReflectance(double cosine){
		//double r0 = (n-1)/(n+1)*(n-1)/(n+1);
		return r0 + (1-r0)*Math.pow(1-cosine, 5);
	}
	
}
