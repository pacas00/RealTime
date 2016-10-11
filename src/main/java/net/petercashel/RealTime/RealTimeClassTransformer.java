package net.petercashel.RealTime;

import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraftforge.fml.common.FMLLog;


public class RealTimeClassTransformer implements net.minecraft.launchwrapper.IClassTransformer {
	//-DJavaDebugEnvironment="true"
	final static boolean isDebugEnvironment = Boolean.parseBoolean(System.getProperty("JavaDebugEnvironment"));
	
	
	//mcp-notch.srg
	//USERFOLDER\.gradle\caches\minecraft\de\oceanlabs\mcp\mcp_snapshot\20160826\1.10.2\srgs
	

	// Static class to record all the names of classes, methods and fields for ASM

	//Class:
	//WorldProvider
	static String WorldProvider = "net.minecraft.world.WorldProvider";
	static String WorldProviderOBF = "atl";

	//Methods:
	//calculateCelestialAngle
	static String calculateCelestialAngle  = "calculateCelestialAngle";
	static String calculateCelestialAngleOBF  = "a";
	//Sig 
	static String calculateCelestialAngleSig = "(JF)F";

	//getMoonPhase
	static String getMoonPhase = "getMoonPhase";
	static String getMoonPhaseOBF = "a";
	//Sig
	static String getMoonPhaseSig = "(J)I";


	//Class:
	//World
	static String World = "net.minecraft.world.World";
	static String WorldOBF = "aid";

	//Methods:
	//getSunBrightnessFactor
	static String getSunBrightnessFactor = "getSunBrightnessFactor";
	static String getSunBrightnessFactorOBF  = getSunBrightnessFactor; // Forge Added, obf matches deobf
	//Sig 
	static String getSunBrightnessFactorSig = "(F)F";

	//getSunBrightnessFactor
	static String getSunBrightnessBody = "getSunBrightnessBody";
	static String getSunBrightnessBodyOBF  = getSunBrightnessBody; // Forge Added, obf matches deobf
	//Sig 
	static String getSunBrightnessBodySig = "(F)F";

	// Static class to record all the names of classes, methods and fields for ASM 

	@Override
	public byte[] transform(String arg0, String arg1, byte[] arg2) {

		if (arg0.equals(WorldProviderOBF)) {
			if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime INSIDE OBFUSCATED WORLDPROVIDER TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMWorldProvider(arg0, arg2, true);
		}

		if (arg0.equals(WorldProvider)) {
			if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime INSIDE WORLDPROVIDER TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMWorldProvider(arg0, arg2, false);
		}

		if (arg0.equals(WorldOBF)) {
			if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime INSIDE OBFUSCATED WORLD TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMWorld(arg0, arg2, true);
		}

		if (arg0.equals(World)) {
			if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime INSIDE WORLD TRANSFORMER ABOUT TO PATCH: " + arg0);
			return patchClassASMWorld(arg0, arg2, false);
		}

		return arg2;
	}

	//patchClassASM for WorldProvider. Specifically named due to upcoming expansion to properly support weather.
	public byte[] patchClassASMWorldProvider(String name, byte[] bytes, boolean obfuscated) {

		String targetMethodName = "";
		String targetMethodName2 = "";

		if(obfuscated == true) {
			// These link to static string references to the obfuscated method names. 
			// These are pulled from modding resources inside the MCP.
			// It's quite normal for obfuscated method names to have the same name
			// so long as the parameters are different.

			//calculateCelestialAngle
			targetMethodName = calculateCelestialAngleOBF;
			//getMoonPhase
			targetMethodName2 = getMoonPhaseOBF;

		} else {
			targetMethodName = calculateCelestialAngle;
			targetMethodName2 = getMoonPhase;
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
			if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime Method Name: "+m.name + " Desc:" + m.desc);

			// calculateCelestialAngle
			if (m.name.equals(targetMethodName) && m.desc.equals("(JF)F")) {
				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime Inside target method!");

				InsnList toInject = new InsnList();

				toInject.add(new VarInsnNode(Opcodes.LLOAD, 1));
				toInject.add(new VarInsnNode(Opcodes.FLOAD, 3));
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealTime", "calculateCelestialAngle", "(JF)F", false));
				toInject.add(new InsnNode(Opcodes.FRETURN));

				//	                    LLOAD 1
				//	                    FLOAD 3
				//	                    INVOKESTATIC net/petercashel/RealTime/RealTime.calculateRealTime (JF)F
				//	                    FRETURN

				m.instructions.clear();
				m.instructions.add(toInject);


				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "RealTime Patching Complete!");
			}

			// getMoonPhase
			if (m.name.equals(targetMethodName2) && m.desc.equals("(J)I")) {
				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime Inside target method!");

				InsnList toInject = new InsnList();

				toInject.add(new VarInsnNode(Opcodes.LLOAD, 1));
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealTime", "getMoonPhase", "(J)I", false));
				toInject.add(new InsnNode(Opcodes.IRETURN));

				//	                    LLOAD 1
				//	                    FLOAD 3
				//	                    INVOKESTATIC net/petercashel/RealTime/RealTime.getMoonPhase (J)I
				//	                    FRETURN

				m.instructions.clear();
				m.instructions.add(toInject);


				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "RealTime Patching Complete!");
			}


		}

		//ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}


	//patchClassASM for World.
	public byte[] patchClassASMWorld(String name, byte[] bytes, boolean obfuscated) {

		String targetMethodName = "";
		String targetMethodName2 = "";

		if(obfuscated == true) {
			// These link to static string references to the obfuscated method names. 
			// These are pulled from modding resources inside the MCP.
			// It's quite normal for obfuscated method names to have the same name
			// so long as the parameters are different.

			//calculateCelestialAngle
			targetMethodName = getSunBrightnessFactorOBF;
			targetMethodName2 = getSunBrightnessBodyOBF;

		} else {
			targetMethodName = getSunBrightnessFactor;
			targetMethodName2 = getSunBrightnessBody;
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
			if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime Method Name: "+m.name + " Desc:" + m.desc);

			// getSunBrightnessFactor
			if (m.name.equals(targetMethodName) && m.desc.equals("(F)F")) {
				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime Inside target method!");

				InsnList toInject = new InsnList();

				toInject.add(new VarInsnNode(Opcodes.FLOAD, 1));
				toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealTime", "getSunBrightnessFactor", "(FLnet/minecraft/world/World;)F", false));
				toInject.add(new InsnNode(Opcodes.FRETURN));

				m.instructions.clear();
				m.instructions.add(toInject);

				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "RealTime Patching Complete!");
			}

			// getSunBrightnessBody
			if (m.name.equals(targetMethodName2) && m.desc.equals("(F)F")) {
				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "*********RealTime Inside target method "+ targetMethodName2 +" !");

				InsnList toInject = new InsnList();

				toInject.add(new VarInsnNode(Opcodes.FLOAD, 1));
				toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/petercashel/RealTime/RealTime", "getSunBrightnessBody", "(FLnet/minecraft/world/World;)F", false));
				toInject.add(new InsnNode(Opcodes.FRETURN));

				m.instructions.clear();
				m.instructions.add(toInject);

				if (isDebugEnvironment) FMLLog.log("RealTime", Level.INFO, "RealTime Patching Complete!");
			}

		}

		//ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
