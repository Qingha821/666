package cn.zbx1425.resourcepackupdater.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ServerLockS2CPacket implements FabricPacket {

    public static final PacketType<ServerLockS2CPacket> TYPE = PacketType.create(
            new ResourceLocation("zbx_rpu", "server_lock"), ServerLockS2CPacket::new);

    public final String serverLockKey;

    public ServerLockS2CPacket(String serverLockKey) {
        this.serverLockKey = serverLockKey;
    }

    public ServerLockS2CPacket(FriendlyByteBuf buf) {
        this.serverLockKey = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(serverLockKey);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
