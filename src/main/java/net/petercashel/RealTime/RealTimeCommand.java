package net.petercashel.RealTime;

import static io.netty.buffer.Unpooled.buffer;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

public class RealTimeCommand extends CommandBase
{

	public RealTimeCommand() {
		super();
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


	private void SyncTime(ICommandSender sender, String[] args)
	{
		mod_RealTime.ServerNoSpamCounter = 501;
	}
	
	public static int DateEngine(int output) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(net.petercashel.RealTime.mod_RealTime.tzName));
		return cal.get(output);
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


	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if (args.length < 1) throw new WrongUsageException(("/RealTime {sync|offset|off|on}"));

		String cmd = args[0];

		if (cmd.equalsIgnoreCase("sync")) SyncTime(sender, args);

		if (cmd.equalsIgnoreCase("offset")) offset(server, sender, args);
		
		
		if (cmd.equalsIgnoreCase("on")) ChangeState(server, sender, args, true);
		if (cmd.equalsIgnoreCase("off")) ChangeState(server, sender, args, false);

		
	}
	
	/**
	 * Adds the strings available in this command to the given list of tab completion options.
	 */
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender par1ICommandSender, String[] args, @Nullable BlockPos pos)
    {
    	if (args.length == 2) {
			if (args[0].equalsIgnoreCase("offset")) {
				String[] subcmd = {"GMT","GMT+10","GMT-10","UTC"};
				return args.length == 2 ? getListOfStringsMatchingLastWord(args, subcmd) : null;
			}
		} else if (args.length == 1) {
			String[] subcmd = {"sync","offset","off","on"};
			return args.length == 1 ? getListOfStringsMatchingLastWord(args, subcmd) : null;
		}
		return null;
	}

	private void ChangeState(MinecraftServer server, ICommandSender sender, String[] args, boolean b) {
		mod_RealTime.RealTimeEnabled = b;
		
		ByteBuf bb = buffer(128);
		bb.writeBoolean(mod_RealTime.RealTimeEnabled);
		FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bb), "RealTimeLogin");
		mod_RealTime.ChannelLogin.sendToAll(pkt);
		mod_RealTime.ServerNoSpamCounter = 490;
		
	}


	private void offset(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2) throw new WrongUsageException(("/RealTime offset {GMT+10:00|GMT-10:00}"));

		mod_RealTime.tzName = args[1];
		
		if (args[1].contains("gmt") || args[1].contains("utc")) mod_RealTime.tzName = args[1].toUpperCase();
		
		
		ByteBuf bb = buffer(128);
		bb.writeBoolean(mod_RealTime.RealTimeEnabled);
		FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bb), "RealTimeLogin");
		mod_RealTime.ChannelLogin.sendToAll(pkt);
		mod_RealTime.ServerNoSpamCounter = 490;
		
		if (mod_RealTime.RealTimeEnabled) SyncTime(sender,args); 
		
	}
}