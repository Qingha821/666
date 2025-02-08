package cn.zbx1425.resourcepackupdater.mixin;

import cn.zbx1425.resourcepackupdater.ResourcePackUpdater;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {

    @Inject(
        method = "updateResourcePacks",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/Options;save()V"
        )
    )
    private void updatePackList(PackRepository packRepository, CallbackInfo ci) {
        ResourcePackUpdater.modifyPackList();
    }
}
