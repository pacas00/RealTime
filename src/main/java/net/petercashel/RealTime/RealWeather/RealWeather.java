package net.petercashel.RealTime.RealWeather;

import static io.netty.buffer.Unpooled.buffer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.petercashel.RealTime.mod_RealTime;
import net.petercashel.RealTime.RealWeather.RealWeather.moonPhase;

// Holds weather data for RealTime and RealWeather
public class RealWeather {

	//Stores the last received weather data
	public static String weatherJSON = "";
	
	//Client sent data
	public static String weatherJSONClient = "";
	
	final static boolean isDebugEnvironment = Boolean.parseBoolean(System.getProperty("JavaDebugEnvironment"));
	
	public static boolean needsUpdate = false;
	public static boolean needsUpdateClient = false;

	public static boolean threadStartedServer = false;

	//Weather Data class (for Serials)
	public static WeatherData WeatherData = new WeatherData();

	public static Timer timer;
	

	public static enum moonPhase {
		Full_Moon(0), Waxing_Gibbus(1), First_Quarter(2), Waxing_Cresent(3), New_Moon(4), Waning_Cresent(5), Last_Quarter(6), Waning_Gibbus(7);

		private int value;
		private moonPhase(int value) {
			this.value = value;
		}
		
		public moonPhase set(int value) {
			this.value = value;
			return this;
		}
	}

	public static void processWeatherServer() {
		processWeatherJSONServer(weatherJSON);
		if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, weatherJSON);
		needsUpdate = true;	
		sendWeatherToClient();
	}
	
	public static void sendWeatherToClient() {
		Gson gson = new Gson();
		weatherJSONClient = gson.toJson(RealWeather.WeatherData);
		if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "sending weather data: " + RealWeather.weatherJSONClient);
		//Send data to client
		ByteBuf bb = buffer(8192);
		bb.writeInt(weatherJSONClient.getBytes(StandardCharsets.US_ASCII).length);
		bb.writeBytes(weatherJSONClient.getBytes(StandardCharsets.US_ASCII));
		FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bb), "RealWeather");
		mod_RealTime.ChannelWeather.sendToAll(pkt);
	}

	public static void processWeatherJSONClient(String s) {
		Gson gson = new Gson();
		RealWeather.WeatherData = gson.fromJson(s, WeatherData.class);
		RealWeather.WeatherData.RestoreMoon();
		
	}
	
	
	public static void processWeatherJSONServer(String s) {
		JsonObject gson = new JsonObject();
		gson = new GsonBuilder().create().fromJson(s, JsonObject.class);
		
		String moon = gson.getAsJsonObject().get("MoonPhase").getAsString();
		String weather = gson.getAsJsonObject().get("Weather").getAsString();
		
		// MOON LOGIC!
		if (moon.contains("Full")) {
			RealWeather.WeatherData.moon = moonPhase.Full_Moon;
		} else if (moon.contains("Blue")) {
			RealWeather.WeatherData.moon = moonPhase.Full_Moon;
		} else if (moon.contains("New")) {
			RealWeather.WeatherData.moon = moonPhase.New_Moon;
		} else if (moon.contains("First")) {
			RealWeather.WeatherData.moon = moonPhase.First_Quarter;
		} else if (moon.contains("Last")) {
			RealWeather.WeatherData.moon = moonPhase.Last_Quarter;
		} else {
			if (moon.contains("Waxing")) {
				if (moon.contains("Crescent")) {
					RealWeather.WeatherData.moon = moonPhase.Waxing_Cresent;
				} else if (moon.contains("Gibbus")) {
					RealWeather.WeatherData.moon = moonPhase.Waxing_Gibbus;
				}
			} else if (moon.contains("Waning")) {
				if (moon.contains("Crescent")) {
					RealWeather.WeatherData.moon = moonPhase.Waning_Cresent;
				} else if (moon.contains("Gibbus")) {
					RealWeather.WeatherData.moon = moonPhase.Waning_Gibbus;
				}
			}
		}
		RealWeather.WeatherData.StoreMoon();
		
		//Weather Logic //weather //
		if (weather.contains("Rain") || weather.contains("Showers")) {
			if (weather.contains("Chance")) {
				RealWeather.WeatherData.raining = false;
				RealWeather.WeatherData.rainStr = RealWeather.WeatherData.norain;
			} else {
				if (weather.contains("Light")) {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.lightrain;
				} 
				else if (weather.contains("Medium")) {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.mediumrain;
				} 
				else if (weather.contains("Heavy")) {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.heavyrain;
				} else {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.mediumrain;
				}
			}
		} else {
			RealWeather.WeatherData.raining = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.norain;
		}
		
		if (weather.contains("Storm") || weather.contains("storm")) {
			if (weather.contains("Chance")) {
			} else {
				if (weather.contains("Light")) {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.lightrain;
				} 
				else if (weather.contains("Medium")) {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.mediumrain;
				} 
				else if (weather.contains("Heavy")) {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.heavyrain;
				} else {
					RealWeather.WeatherData.raining = true;
					RealWeather.WeatherData.rainStr = RealWeather.WeatherData.heavyrain;
				}
			}
		}
		
		if (weather.contains("Thunder") || weather.contains("thunder")) {
			if (weather.contains("Chance")) {
			} else {
				if (weather.contains("Light")) {
					RealWeather.WeatherData.thunder = true;
					RealWeather.WeatherData.thundStr = 0.25f;
				} 
				else if (weather.contains("Medium")) {
					RealWeather.WeatherData.thunder = true;
					RealWeather.WeatherData.thundStr = 0.5f;
				} 
				else if (weather.contains("Heavy")) {
					RealWeather.WeatherData.thunder = true;
					RealWeather.WeatherData.thundStr = 1.0f;
				} else {
					RealWeather.WeatherData.thunder = true;
					RealWeather.WeatherData.thundStr = 0.5f;
				}
			}
		} else {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.thundStr = 0f;
		}
		
	}

	//Called to create a new thread to run the processing.
	public static void doClientUpdate() {
		Thread t = new Thread(new ClientWeatherUpdateThread());
		t.setDaemon(true);//success is here now
		t.start();

	}

	public static void createServerThread() {
		timer = new Timer("RealWeatherServerThread", true);
		
		ServerWeatherThread task = new ServerWeatherThread();
		
		timer.scheduleAtFixedRate(task, 0, 1000 * 60 * 15);		
	}
	
	static class ServerWeatherThread extends TimerTask implements Runnable {
		@Override
		public void run() {
			if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "Server is performing weather update");
			//Get weather data
			URL urlWeather = null;
			try {
				urlWeather = new URL(
						"http://api.wunderground.com/api/"
						+ mod_RealTime.WeatherAPIKEY
						+ "/conditions/q/"
						+ (mod_RealTime.WeatherLocationCountry).replace(" ", "%20")
						+ "/"
						+ (mod_RealTime.WeatherLocationCity).replace(" ", "%20")
						+ ".json");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String jsonWeather = "";
			try {
				BufferedReader reader = new BufferedReader (new InputStreamReader(urlWeather.openStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					jsonWeather += line;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JsonObject weathergson = new JsonObject();
			weathergson = new GsonBuilder().create().fromJson(jsonWeather, JsonObject.class);
			
			//Get Moon Data
			URL urlMoon = null;
			try {
				urlMoon = new URL(
						"http://api.wunderground.com/api/"
						+ mod_RealTime.WeatherAPIKEY
						+ "/astronomy/q/"
						+ (mod_RealTime.WeatherLocationCountry).replace(" ", "%20")
						+ "/"
						+ (mod_RealTime.WeatherLocationCity).replace(" ", "%20")
						+ ".json");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String jsonMoon = "";
			try {
				BufferedReader reader = new BufferedReader (new InputStreamReader(urlMoon.openStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					jsonMoon += line;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JsonObject moongson = new JsonObject();
			moongson = new GsonBuilder().create().fromJson(jsonMoon, JsonObject.class);
			
			JsonObject datagson = new JsonObject();
try {
	datagson.addProperty("MoonPhase", moongson.getAsJsonObject().getAsJsonObject("moon_phase").get("phaseofMoon").getAsString());
	datagson.addProperty("Weather", weathergson.getAsJsonObject().getAsJsonObject("current_observation").get("weather").getAsString());
	datagson.addProperty("Weathericon", weathergson.getAsJsonObject().getAsJsonObject("current_observation").get("icon").getAsString());
} catch (NullPointerException ex) { return;}
			
			RealWeather.weatherJSON = datagson.toString();
			RealWeather.processWeatherServer();			
		}				
	}
	
	static class ClientWeatherUpdateThread implements Runnable {

		@Override
		public void run() {
			if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "Client weather data: " + RealWeather.weatherJSONClient);
			RealWeather.processWeatherJSONClient(RealWeather.weatherJSONClient);
		}
		
	}
}
