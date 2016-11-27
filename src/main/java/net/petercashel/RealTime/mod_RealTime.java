package net.petercashel.RealTime;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.petercashel.RealTime.RealWeather.RealWeather;
import net.petercashel.RealTime.RealWeather.RealWeatherCommand;
import net.petercashel.RealTime.RealWeather.RealWeatherWorld;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;


@Mod(modid = "mod_realtime", name = "RealTime")
public class mod_RealTime {
	
	@Instance("mod_RealTime")
	public static mod_RealTime instance;

	public static FMLEventChannel Channel;
	public static FMLEventChannel ChannelConnect;
	public static FMLEventChannel ChannelLogin;
	public static FMLEventChannel ChannelWeather;


	@SidedProxy(clientSide = "net.petercashel.RealTime.ClientProxy", serverSide = "net.petercashel.RealTime.CommonProxy")
	public static CommonProxy proxy;

	public static final String CATEGORY_GENERAL = "general";

	//RealTime Stuff

	public static boolean RealTimeEnabled;
	public static boolean RealWeatherEnabled = false;
	public static String tzName;
	public static String WeatherAPIKEY = "";
	public static String WeatherLocationCity = "";
	public static String WeatherLocationCountry = "";
	
	public static boolean EnforceSync = true;
	
	public static float ServerTime = 0F;
	public static float ClientTime = 3000F;
	public static boolean ClientTimeEnabled = false;

	// Used server side to not spam the client with packets from this mod.
	public static int ServerNoSpamCounter = 0;
	
	private RealTimeCommand RealTimeCMD;
	private RealWeatherCommand RealWeatherCMD;

	private MinecraftServer server;
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		System.out.println("[RealTime+RealWeather] Loaded.");
		FMLLog.log("RealTime+RealWeather", Level.INFO, "Mod Has Loaded [RealTime]");
	}


	@EventHandler
	public void ServerStopped(FMLServerStoppingEvent event) 
	{
		mod_RealTime.ClientTimeEnabled = false;
	}


	@EventHandler
	public void ServerStarting(FMLServerStartingEvent event) 
	{
		mod_RealTime.ChannelConnect.register(new ServerPacketHandlerConnect()); //Sends packet on channel RealTimeLogin
		
		server = event.getServer();

		RealTimeCMD = new RealTimeCommand();
		event.registerServerCommand(RealTimeCMD);
		RealWeatherCMD = new RealWeatherCommand(this);
		event.registerServerCommand(RealWeatherCMD);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try {
			cfg.load();
			RealTimeEnabled = cfg.get(CATEGORY_GENERAL,"RealTimeEnabled", false).getBoolean(false);
			
			Calendar cal = Calendar.getInstance();
			TimeZone tz = TimeZone.getDefault();
			
			tzName = cfg.get(CATEGORY_GENERAL,"RealTime_TimeZone_Name", displayTimeZone(cal.getTimeZone())).getString();;
			
			tz = TimeZone.getTimeZone(tzName);
			cal.setTimeZone(tz);
			
			RealWeatherEnabled = cfg.get(CATEGORY_GENERAL,"RealWeatherEnabled", false).getBoolean(false);
			WeatherAPIKEY = cfg.get(CATEGORY_GENERAL,"RealWeather_APIKEY", "").getString();
			WeatherLocationCity = cfg.get(CATEGORY_GENERAL,"RealWeather_City", "").getString();
			WeatherLocationCountry = cfg.get(CATEGORY_GENERAL,"RealWeather_Country", "").getString();
			
		} catch (Exception e) {
			System.out.println("[RealTime] Error Loading Config");
		} finally {
			cfg.save();
		}
		MinecraftForge.EVENT_BUS.register(new RealTimeEvents());
		RealWeatherWorld.SelfCallForLoading();

	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealTime"); // RealTime / L165
		ChannelConnect = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealTimeConnect"); //(RealTimeEvents) Client sends on connect to server, trigger ServerPacketHandlerConnect
		ChannelLogin = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealTimeLogin"); //Sent by server- ServerPacketHandlerConnect to client ClientPacketHandlerLogin
		ChannelWeather = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealWeather"); // RealWeather L82
		proxy.load();
		RealTimeInit();

		//Channel.sendToServer(FMLProxyPacket);
		//Channel.sendTo(FMLProxyPacket, EntityPlayerMP);
	}

	public void RealTimeInit() {
		//Not Needing to do anything yet
	}

	private static String displayTimeZone(TimeZone tz) {

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
	    long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) 
	                              - TimeUnit.HOURS.toMinutes(hours);
	    // avoid -4:-30 issue
	    minutes = Math.abs(minutes);

	    String result = "";
	    if (hours > 0) {
	        result = String.format("GMT+%d:%02d", hours, minutes);
	    } else {
	        result = String.format("GMT%d:%02d", hours, minutes);
	    }

	    return result;

	}
}