package net.petercashel.RealTime.RealWeather;

import net.petercashel.RealTime.RealWeather.RealWeather.moonPhase;

public class WeatherData {
		//Weather Values
		public moonPhase moon = moonPhase.Full_Moon;
		public int moonInt = 0;

		public boolean raining = false;
		public boolean thunder = false;

		public float rainStr = 0.0f;
		public float rainStrPrev = 0.0f;
		public float thundStr = 0.0f;
		public float thundStrPrev = 0.0f;

		public int rainTime = 0;
		public int thundTime = 0;	

		//Rains
		public float norain = 0.0f;
		public float vrylightrain = 0.2f;
		public float lightrain = 0.35f;
		public float mediumrain = 0.8f;
		public float heavyrain = 1.2f;
		public float stormrain = 2.0f;
		
		//Rains
		public float noThund = 0.0f;
		public float halfThund = 0.5f;
		public float oneThund = 1.0f;
		public float onehalfThund = 1.5f;
		public float twoThund = 2.0f;
		
		//Snow
		public boolean snowing = false;

		//Water freezing
		public boolean freezing = false;

		public void StoreMoon() {
			moonInt = moon.ordinal();
			
		}

		public void RestoreMoon() {
			moon = moon.set(moonInt);			
		}

}
