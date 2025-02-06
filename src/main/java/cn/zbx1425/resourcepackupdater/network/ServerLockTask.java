package cn.zbx1425.resourcepackupdater.network;

import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;

import java.util.function.Consumer;

public class ServerLockTask implements ConfigurationTask {

    public static final Type TYPE = new Type("zbx_rpu:server_lock");

    public final String serverLockKey;

    public ServerLockTask(String serverLockKey) {
        this.serverLockKey = serverLockKey;
    }

    @Override
    public void start(Consumer<Packet<?>> task) {
        task.accept(ServerConfigurationNetworking.createS2CPacket(new ServerLockS2CPacket(serverLockKey)));
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
