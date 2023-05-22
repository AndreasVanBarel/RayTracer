package material;

import scene.Scene;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Vector;

public class MaterialWardDuer extends Material{
	public Color color;
	public Color specular;
	public double alpha;
	
	//De kleur bepaalt deze coefficienten...
	public double kd = 1; //diffuse reflection coefficient
	public double ka = 0; //ambient reflection coefficient
	public double ks = 1; //specular reflection coefficient
	
	public static final MaterialWardDuer NICKEL = new MaterialWardDuer(new Color(0.0388, 0.0291, 0.0159), new Color(0.107,0.0828,0.054), 0.0424);
	public static final MaterialWardDuer ACRYLIC_WHITE = new MaterialWardDuer(new Color(0.312, 0.257, 0.155), new Color(0.00673,0.00478,0.00318), 0.0155);
	public static final MaterialWardDuer DELRIN = new MaterialWardDuer(new Color(0.292, 0.244, 0.146), new Color(0.0238,0.0198,0.0133), 0.146);
	public static final MaterialWardDuer ACRYLIC_GREEN = new MaterialWardDuer(new Color(0.0165, 0.064, 0.0239), new Color(0.0068,0.00548, 0.00335), 0.0137);

	
	public MaterialWardDuer(Color color, Color specular, double alpha){
		this.color = color;
		this.specular = specular;
		this.alpha = alpha;
	}
	
	/**
	 * Shade in true color with Ward-Duer shading
	 */
	@Override
	public Color shade(ShadeRec rec){
		Scene scene = rec.scene;
		Color shading = Color.BLACK;
		Vector N = rec.getGoodNormal().normalize(); //normal at same side as the one we are shading
		Vector V = rec.ray.direction.scale(-1).normalize();
		//N and V should be normalized vectors
		
		
		for (Light light : scene.lights){
			for(int i = 0; i < light.nbSamples(); i++){
				light.cycleSample();
				if (!light.castsShadow(rec)){
					Vector L = light.getDirection(rec).normalize();
					double Llight = light.getL(rec);
					Color cLight = light.color;
					double cosine = L.cos(N);
					if (cosine > 0){
						//Diffuse
						Color temp = cLight.multiply(this.color).scale(kd*Llight*cosine/light.nbSamples()); //delen door Math.Pi nog
						shading = shading.add(temp);	

						//WardDuer
						//Vector R = N.mirror(dirL);
						Vector H = L.add(V).scale(0.5);
						double delta = N.angle(H);
						double K = 1/(N.dot(L)*N.dot(V));
						double temp2 = Math.tan(delta)/(alpha);
						K = K*Math.exp(-temp2*temp2)/(4*Math.PI*alpha*alpha);
						temp = cLight.multiply(this.specular).scale(Llight*K/light.nbSamples());
						shading = shading.add(temp);
					}
				}
			} 
		}
		//ambient light
		shading = shading.add(color.scale(ka));
		return shading;
	}
	
}
