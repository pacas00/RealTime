package net.petercashel.RealTime;

import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.petercashel.RealTime.RealWeather.RealWeather;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import static io.netty.buffer.Unpooled.*;


public class RealTime {
	public static float WorldTime;
	public static float SunAngle = 0.5F;
	
	public static int getMoonPhase(long par1) {
		if (mod_RealTime.RealTimeEnabled) { //(int)(par1 / 1728000L % 8L + 8L) % 8
			if (!mod_RealTime.RealWeatherEnabled)
				return (int)(par1 / 1728000L % 8L + 8L) % 8;
			else
				return RealWeather.WeatherData.moon.ordinal();
		} else {
			return (int)(par1 / 24000L % 8L + 8L) % 8;
		}
	}
	
	//Lighting Brightness
	public static float getSunBrightnessFactor(float p_72971_1_, World world) {
		if (mod_RealTime.RealTimeEnabled) {
			//Modded Code
			
			float f1 = world.getCelestialAngle(p_72971_1_);
	        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.2F);

	        if (f2 < 0.0F)
	        {
	            f2 = 0.0F;
	        }

	        if (f2 > 1.0F)
	        {
	            f2 = 1.0F;
	        }

	        f2 = 1.0F - f2;
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
	        return f2 * 0.8F + 0.2F;
	        
		} else {
			//Vanilla
			float f1 = world.getCelestialAngle(p_72971_1_);
	        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.2F);

	        if (f2 < 0.0F)
	        {
	            f2 = 0.0F;
	        }

	        if (f2 > 1.0F)
	        {
	            f2 = 1.0F;
	        }

	        f2 = 1.0F - f2;
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
	        return f2 * 0.8F + 0.2F;
		}
	}
	
	//Rendered Brightness
	public static float getSunBrightnessBody(float p_72967_1_, World world) {
		if (mod_RealTime.RealTimeEnabled) {
			//Modded Code
			
			float f1 = world.getCelestialAngle(p_72967_1_);
	        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);

	        if (f2 < 0.0F)
	        {
	            f2 = 0.0F;
	        }

	        if (f2 > 1.0F)
	        {
	            f2 = 1.0F;
	        }

	        f2 = 1.0F - f2;
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getRainStrength(p_72967_1_) * 5.0F) / 16.0D));
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getThunderStrength(p_72967_1_) * 5.0F) / 16.0D));
	        
	        
	        
	        return f2;
		} else {
			//Vanilla
			float f1 = world.getCelestialAngle(p_72967_1_);
	        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);

	        if (f2 < 0.0F)
	        {
	            f2 = 0.0F;
	        }

	        if (f2 > 1.0F)
	        {
	            f2 = 1.0F;
	        }

	        f2 = 1.0F - f2;
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getRainStrength(p_72967_1_) * 5.0F) / 16.0D));
	        f2 = (float)((double)f2 * (1.0D - (double)(world.getThunderStrength(p_72967_1_) * 5.0F) / 16.0D));
	        return f2;
		}
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 * Or the system clock time with sunrise at 6am and sunset at 6pm.
	 */
	public static float calculateCelestialAngle(long par1, float par3)
	{
		// par1 (long) is the total number of ticks for all days
		// par3 (float) is the percent of the current day?
		//FMLLog.log("RealTime", Level.INFO, par1);
		//FMLLog.log("RealTime", Level.INFO, par3);

		if (mod_RealTime.RealTimeEnabled) {
			if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {

				float Time = TimeCalculator(par1, par3);
				SunAngle = Time;
				if (Time > WorldTime) {
					if ((Time - WorldTime) > 25F) {
						mod_RealTime.ServerNoSpamCounter = 501;
						FMLLog.log("RealTime", Level.INFO, "Time was too Big " + Time + " "  + WorldTime);
					}					
				} else {
					if ((WorldTime - Time) > 25F) {
						mod_RealTime.ServerNoSpamCounter = 501;
						FMLLog.log("RealTime", Level.INFO, "WorldTime was too Big " + WorldTime + " "  + Time);
					}
				}
				WorldTime = Time;
				mod_RealTime.ServerTime = Time;

				if (mod_RealTime.ServerNoSpamCounter < 500) {
					mod_RealTime.ServerNoSpamCounter++;
				} else {
					ByteBuf bb = buffer(128);
					bb.clear();
					bb.writeFloat(Time);
					FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bb), "RealTime");
					mod_RealTime.Channel.sendToAll(pkt);
					mod_RealTime.ServerNoSpamCounter = 0;
				}
				
				return Time;
			} else {
				if (mod_RealTime.ClientTimeEnabled) {
					SunAngle = mod_RealTime.ClientTime;
					return mod_RealTime.ClientTime;

				} else {
					int var4 = (int)(par1 % 24000L);
					float var5 = ((float)var4 + par3) / 24000.0F - 0.25F;

					if (var5 < 0.0F)
					{
						++var5;
					}

					if (var5 > 1.0F)
					{
						--var5;
					}

					float var6 = var5;
					var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
					var5 = var6 + (var5 - var6) / 3.0F;
					return var5;				
				}
			}
		}
		else
		{
            int var4 = (int)(par1 % 24000L);
			float var5 = ((float)var4 + par3) / 24000.0F - 0.25F;

			if (var5 < 0.0F)
			{
				++var5;
			}

			if (var5 > 1.0F)
			{
				--var5;
			}

			float var6 = var5;
			var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
			var5 = var6 + (var5 - var6) / 3.0F;
			return var5;
		}

	}

	public static int DateEngine(int output) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(mod_RealTime.tzName));
		return cal.get(output);
	}

	public static float TimeCalculator(long par1, float par3) {
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
		
		if (mod_RealTime.EnforceSync == false) {
			var4 = (int)(par1 % l);
		} else {
			var4 = (int)((t*m) % l);
		}
		
		float var5 = ((float)var4 + par3) / ((float)l) - 0.25F;

		if (var5 < 0.0F)
		{
			++var5;
		}

		if (var5 > 1.0F)
		{
			--var5;
		}

		float var6 = var5;
		var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		
		if (var5 < 0F || var5 > 1F) {
			FMLLog.log("RealTime", Level.INFO, "INVALID ANGLE CALCULATED " + var5);
		}
		return var5;
		
		
		
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