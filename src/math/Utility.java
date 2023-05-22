package math;

public class Utility {
	public static double min(double a, double b){
		if (a<b)
			return a;
		return b;
	}
	
	public static double max(double a, double b){
		if (a>b)
			return a;
		return b;
	}
	
	public static double min(double a, double b, double c){
		return min(a, min(b,c));
	}
	
	public static double max(double a, double b, double c){
		return max(a, max(b,c));
	}
	
	public static double abs(double a){
		if (a<0)
			return -a;
		return a;
	}
}
