package material;

import sampling.Sample;
import scene.Scene;
import texture.Texture;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.OrthonormalBasis;
import math.Point;
import math.Ray;
import math.Vector;

public class MaterialGlossy extends Material{
		
	public Color color;
	public Color specular;
	public double phongcoef = STANDARD_phongcoef;
	private Texture envMap = null;
	
	public double kd = STANDARD_kd; //diffuse reflection coefficient
	public double ka = STANDARD_ka; //ambient reflection coefficient
	public double ks = STANDARD_ks; //specular reflection coefficient
	
	//public int maxRecursionDepth = 2; //0 - no refraction, 1 - only direct refraction, etc.
	public int samples = 5; // number of random samples per glossy reflection
	public int nbGlossySamples = 10;
	
	public MaterialGlossy(Color color, Color specular, double kd, double ka, double ks, double phongcoef){
		this.color = color;
		this.specular = specular;
		this.kd = kd;
		this.ka = ka;
		this.ks = ks;
		this.phongcoef = phongcoef;
	}
	
	public MaterialGlossy(Color color, Color specular){
		this.color = color;
		this.specular = specular;
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
		Vector normal = rec.getGoodNormal();
		
		//Diffuse shading with the lights
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
					}
				}
			}
		}
		
		//ambient light
		L = L.add(color.scale(ka));
		
		//Glossy part by shooting rays (governed by ks)
		Vector mirrored = normal.mirror(rec.ray.direction.scale(-1));
		Vector sampleDir;
		Point sampleOrigin = rec.getSafeHitpointAbove();
		Color glossyL = Color.BLACK;
		for (int i = 0; i < nbGlossySamples; i++){
			OrthonormalBasis basis = new OrthonormalBasis(mirrored);
			sampleDir = generateSampleDir(phongcoef, basis);	
			if(sampleDir.cos(normal) > 0){ //could be a sample that points into the object again
				Ray ray = new Ray(sampleOrigin, sampleDir);
				if (envMap != null){
					glossyL = glossyL.add(envMap.getEnvColor(ray));
				} else {
					ShadeRec newRec = new ShadeRec(ray);
					newRec.recursionDepth = Integer.MAX_VALUE-10;
					rec.scene.traceRay(newRec);
					glossyL = glossyL.add(newRec.shade());
				}
			}
		}
		glossyL = glossyL.scale(ks/nbGlossySamples).multiply(specular);
		L = L.add(glossyL);
		return L;
	}
	
	//returns sample vector centered around basis.w such that density d of samples varies with theta such that d = cos(theta)^e
	public Vector generateSampleDir(double exp, OrthonormalBasis basis){
		Sample sample = Sample.getRandomHemisphere(exp);
		double phi = sample.x;
		double theta = sample.y;
		double pu = Math.sin(theta)*Math.cos(phi);
		double pv = Math.sin(theta)*Math.sin(phi);
		double pw = Math.cos(theta);
		return basis.u.scale(pu).add(basis.v.scale(pv)).add(basis.w.scale(pw));
	}
	
}
