package net.petercashel.RealTime;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class ClientPacketHandlerLogin {

	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) {
		ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());

		try {
			mod_RealTime.RealTimeOffset = bbis.readInt();
			mod_RealTime.ClientTimeEnabled = bbis.readBoolean();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
