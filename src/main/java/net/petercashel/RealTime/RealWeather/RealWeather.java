
package net.petercashel.RealTime.RealWeather;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;
import net.petercashel.RealTime.mod_RealTime;

public class RealWeather {

	// changeState1 changes when weather does
	// changeState2 is set to changeState1 when updates are sent to client
	public static boolean changeState1 = false;
	public static boolean changeState2 = false;

	public static void updateWeatherBody(WorldProvider provider, WorldInfo worldInfo, Random rand, float thunderingStrength, float rainingStrength, float prevThunderingStrength, float prevRainingStrength, boolean isRemote) {

		if (!mod_RealTime.RealWeatherEnabled) {
			VanillaupdateWeatherBody(provider, worldInfo, rand, thunderingStrength, rainingStrength, prevThunderingStrength, prevRainingStrength, isRemote);
		} else {
			// WorldProvider provider, WorldInfo worldInfo,
			// Random rand, float thunderingStrength, float rainingStrength,
			// float prevThunderingStrength, float prevRainingStrength, boolean isRemote


			if (!provider.hasNoSky)
			{
				if (provider.dimensionId == 0) {

					if (!isRemote) //Server
					{						
						if (RealWeatherConst.needsUpdate == true) {
							worldInfo.setRaining(RealWeatherConst.raining);
							provider.worldObj.rainingStrength = RealWeatherConst.rainStr;
							provider.worldObj.prevRainingStrength = RealWeatherConst.rainStrPrev;
							worldInfo.setRainTime(RealWeatherConst.rainTime);
							worldInfo.setThundering(RealWeatherConst.thunder);
							provider.worldObj.thunderingStrength = RealWeatherConst.thundStr;
							provider.worldObj.prevThunderingStrength = RealWeatherConst.thundStrPrev;
							worldInfo.setThunderTime(RealWeatherConst.thundTime);

							changeState1 = !changeState1;
							RealWeatherConst.needsUpdate = false;
						}

						if (changeState1 != changeState2) {
							System.out.println("Sending weather update to clients");
							//Send changes to client

							changeState2 = changeState1;
						}


					} else {
						//Client
						worldInfo.setRaining(RealWeatherConst.raining);
						provider.worldObj.rainingStrength = RealWeatherConst.rainStr;
						provider.worldObj.prevRainingStrength = RealWeatherConst.rainStrPrev;
						worldInfo.setRainTime(RealWeatherConst.rainTime);
						worldInfo.setThundering(RealWeatherConst.thunder);
						provider.worldObj.thunderingStrength = RealWeatherConst.thundStr;
						provider.worldObj.prevThunderingStrength = RealWeatherConst.thundStrPrev;
						worldInfo.setThunderTime(RealWeatherConst.thundTime);

					}
				} else {
					VanillaupdateWeatherBody(provider, worldInfo, rand, thunderingStrength, rainingStrength, prevThunderingStrength, prevRainingStrength, isRemote);
				}
			}
		}
	}

	public static void VanillaupdateWeatherBody(WorldProvider provider, WorldInfo worldInfo, Random rand, float thunderingStrength, float rainingStrength, float prevThunderingStrength, float prevRainingStrength, boolean isRemote) {
		//Vanilla Weather Code
		if (!provider.hasNoSky)
		{
			if (!isRemote)
			{
				int i = worldInfo.getThunderTime();

				if (i <= 0)
				{
					if (worldInfo.isThundering())
					{
						worldInfo.setThunderTime(rand.nextInt(12000) + 3600);
					}
					else
					{
						worldInfo.setThunderTime(rand.nextInt(168000) + 12000);
					}
				}
				else
				{
					--i;
					worldInfo.setThunderTime(i);

					if (i <= 0)
					{
						worldInfo.setThundering(!worldInfo.isThundering());
					}
				}

				prevThunderingStrength = thunderingStrength;

				if (worldInfo.isThundering())
				{
					thunderingStrength = (float)((double)thunderingStrength + 0.01D);
				}
				else
				{
					thunderingStrength = (float)((double)thunderingStrength - 0.01D);
				}

				thunderingStrength = MathHelper.clamp_float(thunderingStrength, 0.0F, 1.0F);
				int j = worldInfo.getRainTime();

				if (j <= 0)
				{
					if (worldInfo.isRaining())
					{
						worldInfo.setRainTime(rand.nextInt(12000) + 12000);
					}
					else
					{
						worldInfo.setRainTime(rand.nextInt(168000) + 12000);
					}
				}
				else
				{
					--j;
					worldInfo.setRainTime(j);

					if (j <= 0)
					{
						worldInfo.setRaining(!worldInfo.isRaining());
					}
				}

				prevRainingStrength = rainingStrength;

				if (worldInfo.isRaining())
				{
					rainingStrength = (float)((double)rainingStrength + 0.01D);
				}
				else
				{
					rainingStrength = (float)((double)rainingStrength - 0.01D);
				}

				rainingStrength = MathHelper.clamp_float(rainingStrength, 0.0F, 1.0F);
			}
		}
	}

	public static void SelfCallForLoading() {
		changeState1 = true;
	}
}
