package net.petercashel.RealTime;

import static io.netty.buffer.Unpooled.buffer;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class ServerPacketHandlerConnect {

	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {
		ByteBuf bb = buffer(128);
		bb.writeInt(mod_RealTime.RealTimeZone);
		bb.writeBoolean(mod_RealTime.RealTimeEnabled);
		FMLProxyPacket pkt = new FMLProxyPacket(bb, "RealTimeLogin");
		mod_RealTime.ChannelLogin.sendToAll(pkt);
		mod_RealTime.ServerNoSpamCounter = 490;

	}
}
