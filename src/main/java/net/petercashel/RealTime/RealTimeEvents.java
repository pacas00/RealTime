package net.petercashel.RealTime;

import static io.netty.buffer.Unpooled.buffer;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class RealTimeEvents {

	@SubscribeEvent
	public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
	{
		mod_RealTime.ClientTimeEnabled = false;
		mod_RealTime.RealTimeZone = mod_RealTime.RealTimeZoneOriginal;
		TimerTask ConnectPacket = new TimerTask(){

			public void run() {
				ByteBuf bb = buffer(128);
				bb.writeBoolean(true);
				FMLProxyPacket pkt = new FMLProxyPacket(bb, "RealTimeConnect");
				mod_RealTime.ChannelConnect.sendToServer(pkt);
			}
		};

		Timer ConnectPacketTimer = new Timer(true);
		ConnectPacketTimer.schedule(ConnectPacket, 750);
	}


}

