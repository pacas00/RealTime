package net.petercashel.RealTime.TileEntities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.petercashel.RealTime.mod_RealTime;

public class TileEntityweatherMan extends TileEntity {
	
	private int time = 0;
	public void updateEntity() {

		
	}
	
	public class WeatherTask extends Thread {

		private World wor;
	    public WeatherTask(World worl) {
			wor = worl;
		}
		public void run() {
	        URL url = null;
			try {
				url = new URL("http://api.worldweatheronline.com/free/v1/weather.ashx?key="+mod_RealTime.WeatherAPIKEY+"&num_of_days=1&q="+"&format=json");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String json = "";
			try {
				BufferedReader reader = new BufferedReader (new InputStreamReader(url.openStream()));
				BufferedWriter writer = new BufferedWriter (new FileWriter("data.txt"));
				String line;
				while ((line = reader.readLine()) != null) {
					json += line;
					writer.write(line);
					writer.newLine();
				}
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JsonObject gson = new JsonObject();
			gson = new GsonBuilder().create().fromJson(json, JsonObject.class);
			
			
			int weatherCode = gson.getAsJsonObject().
					getAsJsonObject("data").
					getAsJsonArray("current_condition").
					get(0).
					getAsJsonObject().
					get("weatherCode").getAsInt();
			int[] array = { 395, 392, 389, 386, 377, 374, 371, 368, 365, 362, 359, 356, 353, 350, 338, 335, 332, 329, 326, 314, 311, 308, 305, 302, 299, 284, 281 };
			if (Arrays.asList(array).contains(weatherCode)) {
				wor.getWorldInfo().setRaining(true);
			} else {
				wor.getWorldInfo().setRaining(false);
			}
	    }

	}

}
