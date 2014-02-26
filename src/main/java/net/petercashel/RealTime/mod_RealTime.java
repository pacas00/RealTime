package net.petercashel.RealTime;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = "mod_realtime", name = "RealTime")
public class mod_RealTime {
	
	@Instance("mod_RealTime")
	public static mod_RealTime instance;

	public static FMLEventChannel Channel;
	public static FMLEventChannel ChannelConnect;
	public static FMLEventChannel ChannelLogin;


	@SidedProxy(clientSide = "net.petercashel.RealTime.ClientProxy", serverSide = "net.petercashel.RealTime.CommonProxy")
	public static CommonProxy proxy;

	public static final String CATEGORY_GENERAL = "general";

	//RealTime Stuff

	public static boolean RealTimeEnabled;
	public static String tzName;
	public static int RealTimeZone = 0;
	public static int RealTimeZoneOriginal;
	public static String WeatherAPIKEY = "";
	public static String WeatherLocation = "";

	
	public static float ServerTime = 0F;
	public static float ClientTime = 3000F;
	public static boolean ClientTimeEnabled = false;

	// Used server side to not spam the client with packets from this mod.
	public static int ServerNoSpamCounter = 0;
	
	public static Block weatherMan;
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		GameRegistry.registerTileEntity(TileEntityweatherMan.class, "weatherManTE");
		System.out.println("[RealTime] Loaded.");
		FMLLog.log("RealTime", Level.INFO, "Mod Has Loaded [RealTime]");
	}


	@EventHandler
	public void ServerStopped(FMLServerStoppingEvent event) 
	{
		mod_RealTime.ClientTimeEnabled = false;
		mod_RealTime.RealTimeZone = mod_RealTime.RealTimeZoneOriginal;
	}


	@EventHandler
	public void ServerStarting(FMLServerStartingEvent event) 
	{
		mod_RealTime.ChannelConnect.register(new ServerPacketHandlerConnect());
	}


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try {
			cfg.load();
			RealTimeEnabled = cfg.get(CATEGORY_GENERAL,"ReadTimeEnabled", false).getBoolean(false);
			
			Calendar cal = Calendar.getInstance();
			TimeZone tz = TimeZone.getDefault();
			
			tzName = cfg.get(CATEGORY_GENERAL,"RealTime_TimeZone_Name", cal.getTimeZone().getDisplayName()).getString();;
			
			tz = TimeZone.getTimeZone(tzName);
			cal.setTimeZone(tz);
			
			RealTimeZone = cal.getTimeZone().getRawOffset();
			
			RealTimeZoneOriginal = RealTimeZone;

			WeatherAPIKEY = cfg.get(CATEGORY_GENERAL,"WeatherAPIKEY", "").getString();
			WeatherLocation = cfg.get(CATEGORY_GENERAL,"WeatherLocation", "").getString();
			
		} catch (Exception e) {
			System.out.println("[RealTime] Error Loading Config");
		} finally {
			cfg.save();
		}
		FMLCommonHandler.instance().bus().register(new RealTimeEvents());
		
		weatherMan = new BlockweatherMan();
		GameRegistry.registerBlock(weatherMan, "weatherMan");

	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealTime");
		ChannelConnect = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealTimeConnect");
		ChannelLogin = NetworkRegistry.INSTANCE.newEventDrivenChannel("RealTimeLogin");
		proxy.load();
		RealTimeInit();

		//Channel.sendToServer(FMLProxyPacket);
		//Channel.sendTo(FMLProxyPacket, EntityPlayerMP);
	}

	public void RealTimeInit() {
		//Not Needing to do anything yet
	}



}