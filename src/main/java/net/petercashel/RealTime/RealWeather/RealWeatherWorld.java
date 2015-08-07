
package net.petercashel.RealTime.RealWeather;

import java.util.Random;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.storage.WorldInfo;
import net.petercashel.RealTime.mod_RealTime;

public class RealWeatherWorld {

	final static boolean isDebugEnvironment = Boolean.parseBoolean(System.getenv("JavaDebugEnvironment"));

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
						if (RealWeather.threadStartedServer == false) {
							RealWeather.threadStartedServer = true;
							RealWeather.createServerThread();
						}

						if (RealWeather.needsUpdate == true) {
							worldInfo.setRaining(RealWeather.WeatherData.raining);
							provider.worldObj.rainingStrength = RealWeather.WeatherData.rainStr;
							provider.worldObj.prevRainingStrength = RealWeather.WeatherData.rainStrPrev;
							worldInfo.setRainTime(RealWeather.WeatherData.rainTime);
							worldInfo.setThundering(RealWeather.WeatherData.thunder);
							provider.worldObj.thunderingStrength = RealWeather.WeatherData.thundStr;
							provider.worldObj.prevThunderingStrength = RealWeather.WeatherData.thundStrPrev;
							worldInfo.setThunderTime(RealWeather.WeatherData.thundTime);

							RealWeather.needsUpdate = false;
						}


					} else {
						//Client
						worldInfo.setRaining(RealWeather.WeatherData.raining);
						provider.worldObj.rainingStrength = RealWeather.WeatherData.rainStr;
						provider.worldObj.prevRainingStrength = RealWeather.WeatherData.rainStrPrev;
						worldInfo.setRainTime(RealWeather.WeatherData.rainTime);
						worldInfo.setThundering(RealWeather.WeatherData.thunder);
						provider.worldObj.thunderingStrength = RealWeather.WeatherData.thundStr;
						provider.worldObj.prevThunderingStrength = RealWeather.WeatherData.thundStrPrev;
						worldInfo.setThunderTime(RealWeather.WeatherData.thundTime);

						//Check if data was recived and call the updater
						if (RealWeather.needsUpdateClient) {
							RealWeather.needsUpdateClient = false;
							RealWeather.doClientUpdate();
							if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "Client is performing weather update");
						}

					}
				} else {
					VanillaupdateWeatherBody(provider, worldInfo, rand, thunderingStrength, rainingStrength, prevThunderingStrength, prevRainingStrength, isRemote);
				}
			}
		}
	}

	public static void SelfCallForLoading() {
		//Makes sure this class exists before world loads.
	}

	public static boolean canSnowAtBody(int p_147478_1_, int p_147478_2_, int p_147478_3_, boolean p_147478_4_, World world)
	{
		if (!mod_RealTime.RealWeatherEnabled) {
			return vanillaCanSnowAtBody(p_147478_1_, p_147478_2_, p_147478_3_, p_147478_4_, world);
		} else {
			return RealWeather.WeatherData.snowing;
		}		
	}
	
	/**
     * Returns true if the biome have snowfall instead a normal rain.
     */
    public static boolean getEnableSnow(BiomeGenBase b)
    {
    	if (!mod_RealTime.RealWeatherEnabled) {
			return vanillaGetEnableSnow(b);
		} else {
			return RealWeather.WeatherData.snowing;
		}
    }

	public static boolean canBlockFreezeBody(int p_72834_1_, int p_72834_2_, int p_72834_3_, boolean p_72834_4_, World world)
	{
		if (!mod_RealTime.RealWeatherEnabled) {
			return vanillaCanBlockFreezeBody(p_72834_1_, p_72834_2_, p_72834_3_, p_72834_4_, world);
		} else {

			if (!RealWeather.WeatherData.freezing)
			{
				return false;
			}
			else
			{
				if (p_72834_2_ >= 0 && p_72834_2_ < 256 && world.getSavedLightValue(EnumSkyBlock.Block, p_72834_1_, p_72834_2_, p_72834_3_) < 10)
				{
					Block block = world.getBlock(p_72834_1_, p_72834_2_, p_72834_3_);

					if ((block == Blocks.water || block == Blocks.flowing_water) && world.getBlockMetadata(p_72834_1_, p_72834_2_, p_72834_3_) == 0)
					{
						if (!p_72834_4_)
						{
							return true;
						}

						boolean flag1 = true;

						if (flag1 && world.getBlock(p_72834_1_ - 1, p_72834_2_, p_72834_3_).getMaterial() != Material.water)
						{
							flag1 = false;
						}

						if (flag1 && world.getBlock(p_72834_1_ + 1, p_72834_2_, p_72834_3_).getMaterial() != Material.water)
						{
							flag1 = false;
						}

						if (flag1 && world.getBlock(p_72834_1_, p_72834_2_, p_72834_3_ - 1).getMaterial() != Material.water)
						{
							flag1 = false;
						}

						if (flag1 && world.getBlock(p_72834_1_, p_72834_2_, p_72834_3_ + 1).getMaterial() != Material.water)
						{
							flag1 = false;
						}

						if (!flag1)
						{
							return true;
						}
					}
				}

				return false;
			}	
		}
	}

	//Vanilla Methods
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


	public static boolean vanillaCanSnowAtBody(int p_147478_1_, int p_147478_2_, int p_147478_3_, boolean p_147478_4_, World world)
	{
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(p_147478_1_, p_147478_3_);
		float f = biomegenbase.getFloatTemperature(p_147478_1_, p_147478_2_, p_147478_3_);

		if (f > 0.15F)
		{
			return false;
		}
		else if (!p_147478_4_)
		{
			return true;
		}
		else
		{
			if (p_147478_2_ >= 0 && p_147478_2_ < 256 && world.getSavedLightValue(EnumSkyBlock.Block, p_147478_1_, p_147478_2_, p_147478_3_) < 10)
			{
				Block block = world.getBlock(p_147478_1_, p_147478_2_, p_147478_3_);

				if (block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(world, p_147478_1_, p_147478_2_, p_147478_3_))
				{
					return true;
				}
			}

			return false;
		}
	}

	public static boolean vanillaCanBlockFreezeBody(int p_72834_1_, int p_72834_2_, int p_72834_3_, boolean p_72834_4_, World world)
	{
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(p_72834_1_, p_72834_3_);
		float f = biomegenbase.getFloatTemperature(p_72834_1_, p_72834_2_, p_72834_3_);

		if (f > 0.15F)
		{
			return false;
		}
		else
		{
			if (p_72834_2_ >= 0 && p_72834_2_ < 256 && world.getSavedLightValue(EnumSkyBlock.Block, p_72834_1_, p_72834_2_, p_72834_3_) < 10)
			{
				Block block = world.getBlock(p_72834_1_, p_72834_2_, p_72834_3_);

				if ((block == Blocks.water || block == Blocks.flowing_water) && world.getBlockMetadata(p_72834_1_, p_72834_2_, p_72834_3_) == 0)
				{
					if (!p_72834_4_)
					{
						return true;
					}

					boolean flag1 = true;

					if (flag1 && world.getBlock(p_72834_1_ - 1, p_72834_2_, p_72834_3_).getMaterial() != Material.water)
					{
						flag1 = false;
					}

					if (flag1 && world.getBlock(p_72834_1_ + 1, p_72834_2_, p_72834_3_).getMaterial() != Material.water)
					{
						flag1 = false;
					}

					if (flag1 && world.getBlock(p_72834_1_, p_72834_2_, p_72834_3_ - 1).getMaterial() != Material.water)
					{
						flag1 = false;
					}

					if (flag1 && world.getBlock(p_72834_1_, p_72834_2_, p_72834_3_ + 1).getMaterial() != Material.water)
					{
						flag1 = false;
					}

					if (!flag1)
					{
						return true;
					}
				}
			}

			return false;
		}
	}
	
	/**
     * Returns true if the biome have snowfall instead a normal rain.
     */
    public static boolean vanillaGetEnableSnow(BiomeGenBase b)
    {
        return b.func_150559_j();
    }

}
