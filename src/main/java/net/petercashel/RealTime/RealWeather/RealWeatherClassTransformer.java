package net.petercashel.RealTime.RealWeather;

import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelaunchLog;


public class RealWeatherClassTransformer implements net.minecraft.launchwrapper.IClassTransformer {

	final static boolean isDebugEnvironment = Boolean.parseBoolean(System.getenv("JavaDebugEnvironment"));
	private static boolean debugTargetOnly = false;

	// Static record all the names of classes, methods and fields for ASM

	//Class:
	//World
	static String World = "net.minecraft.world.World";
	static String WorldOBF = "ahb";

	//Methods:
	//updateWeatherBody
	static String updateWeatherBody = "updateWeatherBody";
	static String updateWeatherBodyOBF  = updateWeatherBody; // Forge Added, obf matches deobf
	//Sig 
	static String updateWeatherBodySig = "()V";
	static String updateWeatherBodySigOBF = "()V";


	//Methods:
	//updateWeatherBody
	static String canSnowAtBody = "canSnowAtBody";
	static String canSnowAtBodyOBF  = canSnowAtBody; // Forge Added, obf matches deobf
	//Sig 
	static String canSnowAtBodySig = "(IIIZ)Z";
	static String canSnowAtBodySigOBF = "(IIIZ)Z";


	//Methods:
	//canBlockFreezeBody
	static String canBlockFreezeBody = "canBlockFreezeBody";
	static String canBlockFreezeBodyOBF  = canBlockFreezeBody; // Forge Added, obf matches deobf
	//Sig 
	static String canBlockFreezeBodySig = "(IIIZ)Z";
	static String canBlockFreezeBodySigOBF = "(IIIZ)Z";


	//Class:
	//BiomeGenBase
	static String BiomeGenBase = "net.minecraft.world.biome.BiomeGenBase";
	static String BiomeGenBaseOBF = "ahu";

	//Methods:
	//getEnableSnow
	static String getEnableSnow = "getEnableSnow";
	static String getEnableSnowOBF = "func_76746_c";
	//Sig 
	static String getEnableSnowSig = "()Z";
	static String getEnableSnowSigOBF = "()Z";

	// Static record all the names of classes, methods and fields for ASM 

	@Override
	public byte[] transform(String arg0, String arg1, byte[] arg2) {

		if (arg0.equals(WorldOBF)) {
			if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather INSIDE OBFUSCATED WORLD TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMWorld(arg0, arg2, true);
		}

		if (arg0.equals(World)) {
			if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather INSIDE WORLD TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMWorld(arg0, arg2, false);
		}
		
		
		if (arg0.equals(BiomeGenBaseOBF)) {
			if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather INSIDE OBFUSCATED WORLD TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMBiomeGenBase(arg0, arg2, true);
		}

		if (arg0.equals(BiomeGenBase)) {
			if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather INSIDE WORLD TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMBiomeGenBase(arg0, arg2, false);
		}

		return arg2;
	}

	//patchClassASM for World. Specifically named due to upcoming expansion to properly support weather.
	public byte[] patchClassASMWorld(String name, byte[] bytes, boolean obfuscated) {

		//updateWeatherBody
		String targetMethodName = "";
		String targetMethodSig = "";
		String calledSig = "";

		//canSnowAtBody
		String targetMethodName2 = "";
		String targetMethodSig2 = "";
		String calledSig2 = "";

		//canBlockFreezeBody
		String targetMethodName3 = "";
		String targetMethodSig3 = "";
		String calledSig3 = "";

		if(obfuscated == true) {
			// These link to static string references to the obfuscated method names. 
			// These are pulled from modding resources inside the MCP.
			// It's quite normal for obfuscated method names to have the same name
			// so long as the parameters are different.

			//updateWeatherBody
			targetMethodName = updateWeatherBodyOBF;
			targetMethodSig = updateWeatherBodySigOBF;
			calledSig = "(Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/storage/WorldInfo;Ljava/util/Random;FFFFZ)V";

			//canSnowAtBody
			targetMethodName2 = canSnowAtBodyOBF;
			targetMethodSig2 = canSnowAtBodySigOBF;
			calledSig2 = "(IIIZLnet/minecraft/world/World;)Z";

			//canBlockFreezeBody
			targetMethodName3 = canBlockFreezeBodyOBF;
			targetMethodSig3 = canBlockFreezeBodySigOBF;
			calledSig3 = "(IIIZLnet/minecraft/world/World;)Z";

		} else {
			targetMethodName = updateWeatherBody;
			targetMethodSig = updateWeatherBodySig;
			calledSig = "(Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/storage/WorldInfo;Ljava/util/Random;FFFFZ)V";

			//canSnowAtBody
			targetMethodName2 = canSnowAtBody;
			targetMethodSig2 = canSnowAtBodySig;
			calledSig2 = "(IIIZLnet/minecraft/world/World;)Z";

			//canSnowAtBody
			targetMethodName3 = canBlockFreezeBody;
			targetMethodSig3 = canBlockFreezeBodySig;
			calledSig3 = "(IIIZLnet/minecraft/world/World;)Z";

		}

		//set up ASM class manipulation stuff. Consult the ASM docs for details
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);



		//Now we loop over all of the methods declared inside the class until we get to the targetMethodName

		// find method to inject into
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();
			if (isDebugEnvironment) {
				if (!debugTargetOnly) {
					FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Method Name: "+m.name + " Desc:" + m.desc);
				} else {
					if (m.desc.equals(targetMethodSig)) {
						FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Method Name: "+m.name + " Desc:" + m.desc);
					}					
				}				
			}

			// updateWeatherBody
			if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodSig)) {
				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Inside target method!");

				InsnList toInject = new InsnList();
				if (!obfuscated) {
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "provider", "Lnet/minecraft/world/WorldProvider;"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "worldInfo", "Lnet/minecraft/world/storage/WorldInfo;"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "rand", "Ljava/util/Random;"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "thunderingStrength", "F"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "rainingStrength", "F"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "prevThunderingStrength", "F"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "prevRainingStrength", "F"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "isRemote", "Z"));
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "updateWeatherBody", calledSig, false));
					toInject.add(new InsnNode(Opcodes.RETURN));
				} else {
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_73011_w", "Laqo;")); //provider
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_72986_A", "Lays;")); //worldInfo
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_73012_v", "Ljava/util/Random;")); //rand
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_73017_q", "F")); //thunderingStrength
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_73004_o", "F")); //rainingStrength
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_73018_p", "F")); //prevThunderingStrength
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_73003_n", "F")); //prevRainingStrength
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "ahb", "field_72995_K", "Z")); //isRemote
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "updateWeatherBody", calledSig, false));
					toInject.add(new InsnNode(Opcodes.RETURN));
				}
				m.instructions.clear();
				m.instructions.add(toInject);


				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "RealTime RealWeather - updateWeatherBody Patching Complete!");
			}

			// canSnowAtBody
			if (m.name.equals(targetMethodName2) && m.desc.equals(targetMethodSig2)) {
				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Inside target method!");

				InsnList toInject = new InsnList();
				if (!obfuscated) {

					toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));

					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "canSnowAtBody", calledSig2, false));
					toInject.add(new InsnNode(Opcodes.IRETURN));
				} else {

					toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));

					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "canSnowAtBody", calledSig2, false));
					toInject.add(new InsnNode(Opcodes.IRETURN));
				}
				m.instructions.clear();
				m.instructions.add(toInject);

				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "RealTime RealWeather - canSnowAtBody Patching Complete!");
			}

			// canBlockFreezeBody
			if (m.name.equals(targetMethodName3) && m.desc.equals(targetMethodSig3)) {
				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Inside target method!");

				InsnList toInject = new InsnList();
				if (!obfuscated) {

					toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));

					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "canBlockFreezeBody", calledSig3, false));
					toInject.add(new InsnNode(Opcodes.IRETURN));
				} else {

					toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));

					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "canBlockFreezeBody", calledSig3, false));
					toInject.add(new InsnNode(Opcodes.IRETURN));
				}
				m.instructions.clear();
				m.instructions.add(toInject);

				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "RealTime RealWeather - canBlockFreezeBody Patching Complete!");
			}


		}

		//ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	//patchClassASM for BiomeGenBase. Specifically named due to upcoming expansion to properly support weather.
	public byte[] patchClassASMBiomeGenBase(String name, byte[] bytes, boolean obfuscated) {

		if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "* " + bytes.length);
		
		//getEnableSnow
		String targetMethodName = "";
		String targetMethodSig = "";
		String calledSig = "";


		if(obfuscated == true) {
			//getEnableSnow
			targetMethodName = getEnableSnowOBF;
			targetMethodSig = getEnableSnowSigOBF;
			calledSig = "(Lnet/minecraft/world/biome/BiomeGenBase;)Z";
		} else {
			//getEnableSnow
			targetMethodName = getEnableSnow;
			targetMethodSig = getEnableSnowSig;
			calledSig = "(Lnet/minecraft/world/biome/BiomeGenBase;)Z";
		}


		//set up ASM class manipulation stuff. Consult the ASM docs for details
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);



		//Now we loop over all of the methods declared inside the class until we get to the targetMethodName

		// find method to inject into
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();
			if (isDebugEnvironment) {
				if (!debugTargetOnly) {
					FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Method Name: "+m.name + " Desc:" + m.desc);
				} else {
					if (m.desc.equals(targetMethodSig)) {
						FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Method Name: "+m.name + " Desc:" + m.desc);
					}					
				}				
			}

			// getEnableSnow
			if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodSig)) {
				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "*********RealTime RealWeather Inside target method!");

				InsnList toInject = new InsnList();
				if (!obfuscated) {
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "getEnableSnow", calledSig, false));
					toInject.add(new InsnNode(Opcodes.IRETURN));
				} else {
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealWeather/RealWeatherWorld", "getEnableSnow", calledSig, false));
					toInject.add(new InsnNode(Opcodes.IRETURN));
				}
				m.instructions.clear();
				m.instructions.add(toInject);


				if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "RealTime RealWeather - getEnableSnow Patching Complete!");
			}
		}
		
		//ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		if (isDebugEnvironment) FMLLog.log("RealWeather", Level.INFO, "* " + writer.toByteArray().length);
		return writer.toByteArray();
	}

}
