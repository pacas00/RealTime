package net.petercashel.RealTime;

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

public class RealTimeCommand extends CommandBase
{
	private mod_RealTime mod_RealTime;

	public RealTimeCommand(mod_RealTime mod_RealTime) {
		super();
		this.mod_RealTime = mod_RealTime;
	}


	public String getCommandName()
	{
		return "RealTime";
	}


	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "commands.RealTime.usage";
	}


	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	public void processCommand(ICommandSender sender, String[] args)
	{
		SyncTime(sender, args);
	}

	private void SyncTime(ICommandSender sender, String[] args)
	{
		long time = noSee(sender.getEntityWorld().getWorldTime());
		
		int days = (int) Math.floor((sender.getEntityWorld().getWorldTime() / 1728000L));
		long days_ticks = days * 1728000L;
		long currtime = sender.getEntityWorld().getWorldTime() - days_ticks;
		
		if (time > currtime) {
			sender.getEntityWorld().setWorldTime(sender.getEntityWorld().getWorldTime() + (time - currtime));
			return;
		} else {
			sender.getEntityWorld().setWorldTime(days_ticks + 1728000L + time);
		}
	}
	
	public static int DateEngine(int output) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(net.petercashel.RealTime.mod_RealTime.tzName));
		return cal.get(output);
	}

	private long noSee(long n) {
		int Hours = DateEngine(java.util.Calendar.HOUR_OF_DAY);
		int Minutes = DateEngine(java.util.Calendar.MINUTE);
		//		float total_sec = (Hours*60*60)+(Minutes*60); //0 - 86400 seconds 0-24000 ticks
		//		return ((total_sec/86400)*24000);

		// 1728000 = 1 REAL DAY
		// 72000  = 1 hour
		// 24000 = 20 min
		// 1200 = 1 min


		// From Offical Wiki
		// Minecraft time is exactly 72 times faster than normal time.
		// This can be easily calculated as the proportion 1440/20 = 72,
		// as there are 1440 minutes in a real day (60 * 24)
		// and 20 minutes in a full Minecraft day

		long l = 1728000L;
		int m = (HourSwitch(Hours) * 60) + Minutes;
		int t = (int) (l / 1440);
		
		int var4;
		
		var4 = (int)((t*m) % l);
		
		FMLLog.log("RealTime", Level.INFO, "Var4 " + var4);
		return var4;
	}

	private static int HourSwitch(int hours) {
		switch(hours) {
		case 6: return 0;
		case 7: return 1;
		case 8: return 2;
		case 9: return 3;
		case 10: return 4;
		case 11: return 5;
		case 12: return 6;
		case 13: return 7;
		case 14: return 8;
		case 15: return 9;
		case 16: return 10;
		case 17: return 11;
		case 18: return 12;
		case 19: return 13;
		case 20: return 14;
		case 21: return 15;
		case 22: return 16;
		case 23: return 17;
		case 0: return 18;
		case 1: return 19;
		case 2: return 20;
		case 3: return 21;
		case 4: return 22;
		case 5: return 23;
		}
		return 0;
	}
}