package net.petercashel.RealTime.RealWeather;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.petercashel.RealTime.mod_RealTime;

public class RealWeatherCommand extends CommandBase
{
	public RealWeatherCommand(mod_RealTime mod_RealTime) {
		super();
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


	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		int commandIndex = 0;

		FMLLog.log("RealWeather", Level.INFO, args[commandIndex]);

		switch (args[commandIndex]) {

		case "set": { 
			setWeather(sender, args);
			}
		default: { }
		}
	}





}