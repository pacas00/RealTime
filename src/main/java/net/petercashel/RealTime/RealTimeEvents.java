package net.petercashel.RealTime;

import static io.netty.buffer.Unpooled.buffer;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

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
				FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bb), "RealTimeConnect");
				mod_RealTime.ChannelConnect.sendToServer(pkt);
			}
		};

		Timer ConnectPacketTimer = new Timer(true);
		ConnectPacketTimer.schedule(ConnectPacket, 10000);
	}


}

