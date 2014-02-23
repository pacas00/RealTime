package net.petercashel.RealTime;

import java.util.Calendar;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;


@Mod(modid = "mod_realtime", name = "RealTime", version = "ver.@@@.@@@.@@@.@@@")
public class mod_RealTime {

	public static FMLEventChannel Channel;
	public static FMLEventChannel ChannelConnect;
	public static FMLEventChannel ChannelLogin;


	@SidedProxy(clientSide = "net.petercashel.RealTime.ClientProxy", serverSide = "net.petercashel.RealTime.CommonProxy")
	public static CommonProxy proxy;

	public static final String CATEGORY_GENERAL = "general";

	//RealTime Stuff

	public static boolean RealTimeEnabled;
	public static int RealTimeOffset = 0;

	public static float ClientTime = 3000F;
	public static boolean ClientTimeEnabled = false;

	// Used server side to not spam the client with packets from this mod.
	public static int ServerNoSpamCounter = 0;
	public static int RealTimeOffsetOriginal;

	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		System.out.println("[RealTime] Loaded.");
		FMLLog.log("RealTime", Level.INFO, "Mod Has Loaded [RealTime]");
	}


	@EventHandler
	public void ServerStopped(FMLServerStoppingEvent event) 
	{
		mod_RealTime.ClientTimeEnabled = false;
		mod_RealTime.RealTimeOffset = mod_RealTime.RealTimeOffsetOriginal;
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
			RealTimeOffset = cfg.get(CATEGORY_GENERAL,"RealTime_TimeZone_GMT", 0).getInt();
			RealTimeOffsetOriginal = RealTimeOffset;


		} catch (Exception e) {
			System.out.println("[RealTime] Error Loading Config");
		} finally {
			cfg.save();
		}
		FMLCommonHandler.instance().bus().register(new RealTimeEvents());

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