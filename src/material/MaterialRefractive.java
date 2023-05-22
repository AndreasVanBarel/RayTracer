package material;

import scene.Scene;
import texture.Texture;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Point;
import math.Ray;
import math.Vector;

public class MaterialRefractive extends Material{
		
	public Color filterColor; // = exp(-s) with s the attenuation coefficient for each color. L = L_0 * exp(s*d) with d distance travelled.
	public double n; // refractive index
	public final int maxRecursionDepth = 4; //0 - no reflectance, 1 - only direct reflectance, etc.
	private Texture envMap = null;
	
	public MaterialRefractive(Color filterColor, double n){
		this.filterColor = filterColor;
		this.n = n;
	}
	
	public void setEnvMap(Texture envMap){
		this.envMap = envMap;
	}
	
	/**
	 * Shade in true color with ambient, diffuse and phong shading.
	 */
	@Override
	public Color shade(ShadeRec rec){
		Vector normal = rec.normalvector;
		Color L;

		//only if max recursion depth hasn't been reached yet.
		if(rec.recursionDepth < maxRecursionDepth){
			double cosi = rec.ray.direction.cos(normal);			
			boolean fromOutside;
			double eta;
			if(cosi < 0){
				fromOutside = true;
				eta = n;
				cosi = -cosi;
			} else {
				fromOutside = false;
				eta = 1/n;
			}
			if(!(cosi > 0 && cosi < 1)) System.out.println(cosi);
			// cosi (positive), eta, fromOutside are now correct

			//calculating reflection (either total internal or normal reflection)			
			Point mirrorOrigin = rec.getSafeHitpointAbove(); //mirror origin is same side
			Vector mirrorDir = normal.mirror(rec.ray.direction.scale(-1));
			Ray mirrorRay = new Ray(mirrorOrigin, mirrorDir);
			if(envMap != null && fromOutside){
				L = envMap.getEnvColor(mirrorRay);
			} else {
				ShadeRec mirrorRec = new ShadeRec(mirrorRay);
				mirrorRec.recursionDepth = rec.recursionDepth + 1;
				rec.scene.traceRay(mirrorRec);
				L = mirrorRec.shade();
				if(!fromOutside){ //reflected ray went through refractive object //Beers Law
					double distance = mirrorRec.getDistance();
					L = L.multiply(filterColor.exp(distance));
				}
			}
			//L = Color.MAGENTA;
			
			double costSquared = 1-1/(eta*eta)*(1-cosi*cosi);
			if(costSquared < 0){ // total internal, do nothing more (does not occur for spheres)
				//L = Color.MAGENTA;
			} else {
				double cost = Math.sqrt(costSquared);
				Point transOrigin = rec.getSafeHitpointUnder();
				Vector transDir = rec.ray.direction.normalize().scale(1/eta).subtract(rec.getGoodNormal().scale(+cost - cosi/eta));
				//System.out.println(transDir.cos(rec.ray.direction)); //PRINT STATEMENT
				
				Ray transRay = new Ray(transOrigin, transDir);
				
				Color transL;
				if(envMap != null && !fromOutside){
					transL = envMap.getEnvColor(transRay);
				} else {
					ShadeRec transRec = new ShadeRec(transRay);
					transRec.recursionDepth = rec.recursionDepth + 1;
					rec.scene.traceRay(transRec);
								
					transL = transRec.shade();	
					if(fromOutside){ //refracted ray went through refractive object //Beers Law
						double distance = transRec.getDistance();
						transL = transL.multiply(filterColor.exp(distance));
					}				
				}
				//transL = Color.BLACK;	
				double reflectance = getReflectance(cosi, cost, eta);
				transL = transL.scale(1-reflectance);
				L = L.scale(reflectance);
				L = L.add(transL);
			}
		} else {
			L = Color.BLACK; //default color if recursion depth is depleted.
		}
		return L;
	}
	
	/**Fresnel transmittance*/
	public double getReflectance(double cosi, double cost, double eta){
		double r_par = (eta*cosi - cost)/(eta*cosi + cost);
		double r_perp = (cosi - eta*cost)/(cosi + eta*cost);
		return 0.5*(r_par*r_par + r_perp*r_perp);
		//return 0;
	}	
}