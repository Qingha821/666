public class OptionsScreenMixin {

    @Inject(
        method = "m_232823_", // 这是混淆后的方法名，对应 updatePackList
        // 或者使用 "updatePackList" 如果你使用的是 Yarn mappings
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;m_92147_()V" // 混淆后的 save() 方法
            // 或者使用 "Lnet/minecraft/client/Options;save()V" 如果使用 Yarn mappings
        )
    )
    private void onUpdatePackList(PackRepository packRepository, CallbackInfo ci) {
        ResourcePackUpdater.modifyPackList();
    }
}
