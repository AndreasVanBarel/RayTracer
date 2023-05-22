package material;

import scene.Scene;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Vector;

public class MaterialBlinnPhong extends Material{
	public Color color;
	public Color specular;
	public double n;
	
	//De kleur bepaalt deze coefficienten...
	public double kd = 1; //diffuse reflection coefficient
	public double ka = 0; //ambient reflection coefficient
	public double ks = 1; //specular reflection coefficient
	
	public static final MaterialBlinnPhong NICKEL = new MaterialBlinnPhong(new Color(0.0301, 0.0257, 0.0173), new Color(0.0665,0.0486,0.0285), 1040);
	public static final MaterialBlinnPhong ACRYLIC_WHITE = new MaterialBlinnPhong(new Color(0.314, 0.259, 0.156), new Color(0.00103,0.000739,0.000481), 35600);
	public static final MaterialBlinnPhong DELRIN = new MaterialBlinnPhong(new Color(0.299, 0.249, 0.15), new Color(0.00755,0.00654,0.00461), 83.4);
	public static final MaterialBlinnPhong ACRYLIC_GREEN = new MaterialBlinnPhong(new Color(0.0183, 0.0657, 0.0248), new Color(0.00161, 0.00121, 0.000781), 28400);
	
	public MaterialBlinnPhong(Color color, Color specular, double n){
		this.color = color;
		this.specular = specular;
		this.n = n;
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
						double K = (n+2)/(2*Math.PI) * Math.pow(Math.cos(delta), n);
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