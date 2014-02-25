package net.petercashel.RealTime;

import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import static io.netty.buffer.Unpooled.*;


public class RealTime {


	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 * Or the system clock time with sunrise at 7am and sunset at 7pm.
	 */
	public static float calculateRealTime(long par1, float par3)
	{


		if (mod_RealTime.RealTimeEnabled) {
			if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {

				float Time = TimeCalculator() + 10000;
				float WorldTime = Time;
				mod_RealTime.ServerTime = Time;


				if (mod_RealTime.ServerNoSpamCounter < 500) {
					mod_RealTime.ServerNoSpamCounter++;
				} else {
					ByteBuf bb = buffer(128);
					bb.clear();
					bb.writeFloat(WorldTime);
					FMLProxyPacket pkt = new FMLProxyPacket(bb, "RealTime");
					mod_RealTime.Channel.sendToAll(pkt);
					mod_RealTime.ServerNoSpamCounter = 0;
				}

				return WorldTime;
			} else {
				if (mod_RealTime.ClientTimeEnabled) {
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

	public static float calculateRealTime()
	{
		return calculateRealTime(1L,1f);    		
	}

	public static int DateEngine(int output) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(mod_RealTime.tzName));
		return cal.get(output);
	}

	public static float TimeCalculator() {
		int Hours = DateEngine(java.util.Calendar.HOUR_OF_DAY);
		int Minutes = DateEngine(java.util.Calendar.MINUTE);
		float total_sec = (Hours*60*60)+(Minutes*60); //0 - 86400 seconds 0-24000 ticks
		return ((total_sec/86400)*24000);
	}


}