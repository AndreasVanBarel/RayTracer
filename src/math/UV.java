package math;

public class UV {
	public final double u;
	public final double v;
	
//	public UV(int u, int v){
//		this.u = u;
//		this.v = v;
//	}
	
	// NOTE PAS DIT NOG AAN ZEKER!!!
	public UV(double u, double v){
		this.u = u;
		this.v = v;
	}
	
	public UV scale(double scalar){
		return new UV(u*scalar, v*scalar);
	}
	
	public UV add(UV uv){
		return new UV(u + uv.u, v + uv.v);
	}
	
	public String toString(){
		return "(u = " + u +", v = " + v + ")";
	}
}
