package net.petercashel.RealTime;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.petercashel.RealTime.RealWeather.RealWeatherClassTransformer;

@MCVersion(value = "1.10.2")
public class RealTimeFMLLoadingPlugin implements IFMLLoadingPlugin {

	//-Dfml.coreMods.load=net.petercashel.RealTime.RealTimeFMLLoadingPlugin
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{RealTimeClassTransformer.class.getName(),RealWeatherClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
