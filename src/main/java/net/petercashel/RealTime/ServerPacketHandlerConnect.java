package net.petercashel.RealTime;

import static io.netty.buffer.Unpooled.buffer;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class ServerPacketHandlerConnect {

	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {
		ByteBuf bb = buffer(128);
		bb.writeBoolean(mod_RealTime.RealTimeEnabled);
		FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bb), "RealTimeLogin");
		mod_RealTime.ChannelLogin.sendToAll(pkt);
		mod_RealTime.ServerNoSpamCounter = 490;

	}
}
