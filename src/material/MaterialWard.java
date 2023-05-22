package material;

import scene.Scene;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Vector;

public class MaterialWard extends Material{
	public Color color;
	public Color specular;
	public double alpha;
	
	//De kleur bepaalt deze coefficienten...
	public double kd = 1; //diffuse reflection coefficient
	public double ka = 0; //ambient reflection coefficient
	public double ks = 1; //specular reflection coefficient
	
	public static final MaterialWard NICKEL = new MaterialWard(new Color(0.0221, 0.0178, 0.0107), new Color(0.204,0.154,0.0946), 0.0432);
	public static final MaterialWard ACRYLIC_WHITE = new MaterialWard(new Color(0.312, 0.257, 0.155), new Color(0.00928,0.00669,0.00447), 0.0158);
	public static final MaterialWard DELRIN = new MaterialWard(new Color(0.293, 0.244, 0.146), new Color(0.0348,0.0295,0.0202), 0.162);
	public static final MaterialWard FABRIC_BLUE = new MaterialWard(new Color(0.0163, 0.0153, 0.0277), new Color(0.0174,0.0159,0.018), 0.5);
	public static final MaterialWard ACRYLIC_GREEN = new MaterialWard(new Color(0.0164, 0.0641, 0.0238), new Color(0.0101,0.00793,0.00495), 0.0141);

	
	public MaterialWard(Color color, Color specular, double alpha){
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
						double K = 1/Math.sqrt(N.dot(L)*N.dot(V));
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