package net.petercashel.RealTime;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = "mod_realtime", name = "RealTime", version = "ver.@@@.@@@.@@@.@@@")
public class mod_RealTime {

	public static final String CATEGORY_GENERAL = "general";
	
	//RealTime Stuff

	public static boolean RealTimeEnabled;
	public static int RealTimeOffset = 0;

	public void RealTimeInit() {
	//Not Needing to do anything yet
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		RealTimeInit();
		System.out.println("[RealTime] Loaded.");
		FMLLog.log("RealTime", Level.INFO, "Mod Has Loaded [RealTime]");
	}


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try {
			cfg.load();
			RealTimeEnabled = cfg.get(CATEGORY_GENERAL,"ReadTimeEnabled", false).getBoolean(false);
			RealTimeOffset = cfg.get(CATEGORY_GENERAL,"RealTimeOffset", 0).getInt();


		} catch (Exception e) {
			System.out.println("[RealTime] Error Loading Config");
		} finally {
			cfg.save();
		}
	}

}