package net.petercashel.RealTime.RealWeather;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.DimensionManager;
import net.petercashel.RealTime.mod_RealTime;

public class RealWeatherCommand extends CommandBase
{
	private mod_RealTime mod_RealTime;

	public RealWeatherCommand(mod_RealTime mod_RealTime) {
		super();
		this.mod_RealTime = mod_RealTime;
	}


	public String getCommandName()
	{
		return "RealWeather";
	}


	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "commands.RealWeather.usage";
	}


	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	public void processCommand(ICommandSender sender, String[] args)
	{
		int commandIndex = 0;

		FMLLog.log("RealWeather", Level.INFO, args[commandIndex]);

		switch (args[commandIndex]) {

		case "set": { 
			setWeather(sender, args);
			}
		default: { }
		}
	}

	public void setWeather(ICommandSender sender, String[] args) {
		int commandIndex = 1;
		
		FMLLog.log("RealWeather", Level.INFO, args[commandIndex]);

		if (args[commandIndex].equalsIgnoreCase("rain")) {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.mediumrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.noThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("lightrain")) {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.lightrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.noThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("heavyrain")) {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.heavyrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.noThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("snow")) {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = true;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.mediumrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.noThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("thunder")) {
			RealWeather.WeatherData.thunder = true;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.norain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.halfThund;
			
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("thunderstorm")) {
			RealWeather.WeatherData.thunder = true;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.stormrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.onehalfThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("blizzard")) {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = true;
			RealWeather.WeatherData.freezing = true;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.stormrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.noThund;
			RealWeather.needsUpdate = true;
		} else if ( (args[commandIndex].equalsIgnoreCase("SnowStorm")) || (args[commandIndex].equalsIgnoreCase("Snow Storm"))) {
			RealWeather.WeatherData.thunder = true;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = true;
			RealWeather.WeatherData.freezing = true;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.stormrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.halfThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("storm")) {
			RealWeather.WeatherData.thunder = true;
			RealWeather.WeatherData.raining = true;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.stormrain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.oneThund;
			RealWeather.needsUpdate = true;
		} else if (args[commandIndex].equalsIgnoreCase("clear")) {
			RealWeather.WeatherData.thunder = false;
			RealWeather.WeatherData.raining = false;
			RealWeather.WeatherData.snowing = false;
			RealWeather.WeatherData.freezing = false;
			RealWeather.WeatherData.rainStr = RealWeather.WeatherData.norain;
			RealWeather.WeatherData.thundStr = RealWeather.WeatherData.noThund;
			RealWeather.needsUpdate = true;
		} else {
			
		}
		RealWeather.sendWeatherToClient();
	}





}