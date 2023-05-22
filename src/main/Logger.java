package main;

public class Logger {
	public static final boolean enableOldMethodWarnings = true;
	
	public static void logOld(String string){
		if (enableOldMethodWarnings) System.out.println(string);
	}
	
	public static void logOld(){
		logOld("Debug warning: An old method was used.");
	}
}
