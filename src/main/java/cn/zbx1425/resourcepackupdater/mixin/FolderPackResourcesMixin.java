package cn.zbx1425.resourcepackupdater.mixin;

import cn.zbx1425.resourcepackupdater.ResourcePackUpdater;
import cn.zbx1425.resourcepackupdater.drm.AssetEncryption;
import cn.zbx1425.resourcepackupdater.drm.ServerLockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(PathPackResources.class)
public abstract class FolderPackResourcesMixin extends AbstractPackResources {

    @Unique
    private File canonicalFile;

    @Shadow
    private File file; // 使用 @Shadow 注解访问 PathPackResources 中的 file 字段

    @Unique
    private File getCanonicalFile() {
        if (canonicalFile == null) {
            try {
                canonicalFile = file.getCanonicalFile();
            } catch (IOException e) {
                canonicalFile = file;
            }
        }
        return canonicalFile;
    }

    // 修复 AbstractPackResources 的构造函数调用
    private FolderPackResourcesMixin(String name, boolean isBuiltin) {
        super(name, isBuiltin);
    }

    @Shadow
    private File getFile(String string) { return null; }

    @Inject(method = "getResource", at = @At("HEAD"), cancellable = true)
    void getResource(String resourcePath, CallbackInfoReturnable<InputStream> cir) throws IOException {
        if (getCanonicalFile().equals(ResourcePackUpdater.CONFIG.packBaseDirFile.value)) {
            File file = this.getFile(resourcePath);
            FileInputStream fis = new FileInputStream(file);
            cir.setReturnValue(AssetEncryption.wrapInputStream(fis));
            cir.cancel();
        }
    }

    @Inject(method = "hasResource", at = @At("HEAD"), cancellable = true)
    void hasResource(String resourcePath, CallbackInfoReturnable<Boolean> cir) {
        if (getCanonicalFile().equals(ResourcePackUpdater.CONFIG.packBaseDirFile.value)) {
            // 修复：检查具体的 resourcePath 而不是 null
            if (ServerLockRegistry.shouldRefuseProvidingFile(resourcePath)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "getResources", at = @At("HEAD"), cancellable = true)
#if MC_VERSION >= "11900"
    void getResources(PackType type, String namespace, String path, Predicate<ResourceLocation> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir) {
#else
    void getResources(PackType type, String namespace, String path, int maxDepth, Predicate<ResourceLocation> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir) {
#endif
        if (getCanonicalFile().equals(ResourcePackUpdater.CONFIG.packBaseDirFile.value)) {
            // 修复：需要根据具体的资源路径进行判断，这里假设需要根据 namespace 和 path 构造
            String resourcePath = namespace + "/" + path;
            if (ServerLockRegistry.shouldRefuseProvidingFile(resourcePath)) {
                cir.setReturnValue(Collections.emptyList());
                cir.cancel();
            }
        }
    }

    @Inject(method = "getNamespaces", at = @At("HEAD"), cancellable = true)
    void getNamespaces(PackType type, CallbackInfoReturnable<Set<String>> cir) {
        if (getCanonicalFile().equals(ResourcePackUpdater.CONFIG.packBaseDirFile.value)) {
            // 修复：需要根据具体的资源路径进行判断，这里假设需要根据 type 构造一个合适的路径
            String resourcePath = type.getDirectory() + "/";
            if (ServerLockRegistry.shouldRefuseProvidingFile(resourcePath)) {
                cir.setReturnValue(Collections.emptySet());
                cir.cancel();
            }
        }
    }
}
