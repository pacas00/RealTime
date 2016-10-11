
package net.petercashel.RealTime.RealWeather;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.WorldInfo;
import net.petercashel.RealTime.mod_RealTime;

public class RealWeatherWorld {

	final static boolean isDebugEnvironment = Boolean.parseBoolean(System.getProperty("JavaDebugEnvironment"));

	public static void updateWeatherBody(WorldProvider provider, WorldInfo worldInfo, Random rand, float thunderingStrength, float rainingStrength, float prevThunderingStrength, float prevRainingStrength, boolean isRemote) {

		if (!mod_RealTime.RealWeatherEnabled) {
			VanillaupdateWeatherBody(provider, worldInfo, rand, thunderingStrength, rainingStrength, prevThunderingStrength, prevRainingStrength, isRemote);
		} else {
			// WorldProvider provider, WorldInfo worldInfo,
			// Random rand, float thunderingStrength, float rainingStrength,
			// float prevThunderingStrength, float prevRainingStrength, boolean isRemote


			if (!provider.getHasNoSky())
			{
				if (provider.getDimension() == 0) {

					if (!isRemote) //Server
					{
						if (RealWeather.threadStartedServer == false) {
							RealWeather.threadStartedServer = true;
							RealWeather.createServerThread();
						}

						if (RealWeather.needsUpdate == true) {
							worldInfo.setRaining(RealWeather.WeatherData.raining);
							rainingStrength = RealWeather.WeatherData.rainStr;
							prevRainingStrength = RealWeather.WeatherData.rainStrPrev;
							worldInfo.setRainTime(RealWeather.WeatherData.rainTime);
							worldInfo.setThundering(RealWeather.WeatherData.thunder);
							thunderingStrength = RealWeather.WeatherData.thundStr;
							prevThunderingStrength = RealWeather.WeatherData.thundStrPrev;
							worldInfo.setThunderTime(RealWeather.WeatherData.thundTime);

							RealWeather.needsUpdate = false;
						}


					} else {
						//Client
						worldInfo.setRaining(RealWeather.WeatherData.raining);
						rainingStrength = RealWeather.WeatherData.rainStr;
						prevRainingStrength = RealWeather.WeatherData.rainStrPrev;
						worldInfo.setRainTime(RealWeather.WeatherData.rainTime);
						worldInfo.setThundering(RealWeather.WeatherData.thunder);
						thunderingStrength = RealWeather.WeatherData.thundStr;
						prevThunderingStrength = RealWeather.WeatherData.thundStrPrev;
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

	public static boolean canSnowAtBody(BlockPos pos, boolean checkLight, World world)
	{
		if (!mod_RealTime.RealWeatherEnabled) {
			return vanillaCanSnowAtBody(pos, checkLight, world);
		} else {
			return RealWeather.WeatherData.snowing;
		}		
	}
	
	/**
     * Returns true if the biome have snowfall instead a normal rain.
     */
    public static boolean getEnableSnow(Biome b)
    {
    	if (!mod_RealTime.RealWeatherEnabled) {
			return vanillaGetEnableSnow(b);
		} else {
			return RealWeather.WeatherData.snowing;
		}
    }

	public static boolean canBlockFreezeBody(BlockPos pos, boolean noWaterAdj, World world)
	{
		if (!mod_RealTime.RealWeatherEnabled) {
			return vanillaCanBlockFreezeBody(pos, noWaterAdj, world);
		} else {

			if (!RealWeather.WeatherData.freezing)
			{
				return false;
			}
			else
			{
				if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
	            {
	                IBlockState iblockstate = world.getBlockState(pos);
	                Block block = iblockstate.getBlock();

	                if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
	                {
	                    if (!noWaterAdj)
	                    {
	                        return true;
	                    }

	                    boolean flag = isWater(pos.west(), world) && isWater(pos.east(), world) && isWater(pos.north(), world) && isWater(pos.south(), world);

	                    if (!flag)
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
		if (!provider.getHasNoSky())
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


	public static boolean vanillaCanSnowAtBody(BlockPos pos, boolean checkLight, World world)
	{
		Biome biome = world.getBiome(pos);
        float f = biome.getFloatTemperature(pos);

        if (f > 0.15F)
        {
            return false;
        }
        else if (!checkLight)
        {
            return true;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate = world.getBlockState(pos);

                if (iblockstate.getBlock().isAir(iblockstate, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
                {
                    return true;
                }
            }

            return false;
        }
	}

	public static boolean vanillaCanBlockFreezeBody(BlockPos pos, boolean noWaterAdj, World world)
	{
		Biome biome = world.getBiome(pos);
        float f = biome.getFloatTemperature(pos);

        if (f > 0.15F)
        {
            return false;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate = world.getBlockState(pos);
                Block block = iblockstate.getBlock();

                if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                {
                    if (!noWaterAdj)
                    {
                        return true;
                    }

                    boolean flag = isWater(pos.west(), world) && isWater(pos.east(), world) && isWater(pos.north(), world) && isWater(pos.south(), world);

                    if (!flag)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
	}
	
    public static boolean vanillaGetEnableSnow(Biome b)
    {
    	return b.isSnowyBiome();
    }
	
    
    private static boolean isWater(BlockPos pos, World world)
    {
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }

}
