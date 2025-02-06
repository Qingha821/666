package cn.zbx1425.resourcepackupdater.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ClientVersionC2SPacket implements FabricPacket {

    public static final PacketType<ClientVersionC2SPacket> TYPE = PacketType.create(
            new ResourceLocation("zbx_rpu", "client_version"), ClientVersionC2SPacket::new);

    public final String clientVersion;

    public ClientVersionC2SPacket(String serverLockKey) {
        this.clientVersion = serverLockKey;
    }

    public ClientVersionC2SPacket(FriendlyByteBuf buf) {
        this.clientVersion = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(clientVersion);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
