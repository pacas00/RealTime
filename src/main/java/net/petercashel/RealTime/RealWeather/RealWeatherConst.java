package net.petercashel.RealTime.RealWeather;

// Holds weather data for RealTime and RealWeather
public class RealWeatherConst {
	
	//Weather Values
	public static moonPhase moon = moonPhase.Full_Moon;
	
	public static boolean needsUpdate = false;
	public static boolean raining = false;
	public static boolean thunder = false;
	
	public static float rainStr = 0.0f;
	public static float rainStrPrev = 0.0f;
	public static float thundStr = 0.0f;
	public static float thundStrPrev = 0.0f;
	
	public static int rainTime = 0;
	public static int thundTime = 0;	
	
	//Rains
	public static float norain = 0.0f;
	public static float vrylightrain = 0.2f;
	public static float lightrain = 0.35f;
	public static float mediumrain = 0.8f;
	public static float heavyrain = 1.2f;
	public static float stormrain = 2.0f;
	
	
	public static enum moonPhase {
		Full_Moon(0), Waxing_Gibbus(1), First_Quarter(2), Waxing_Cresent(3), New_Moon(4), Waning_Cresent(5), Last_Quarter(6), Waning_Gibbus(7);
		
		private int value;
        private moonPhase(int value) {
                this.value = value;
        }
	}
}
