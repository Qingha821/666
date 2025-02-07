package cn.zbx1425.resourcepackupdater.gui.gl;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

import java.io.IOException;
import java.io.InputStream;

public class PreloadTextureResource extends Resource {
    private final ResourceLocation resourceLocation;
        public PreloadTextureResource(ResourceLocation resourceLocation, PackResources packResources) {
            super(packResources, () -> InputStream.nullInputStream());
            this.resourceLocation = resourceLocation;
        }

        @Override
        public InputStream open() {
            return getClass().getResourceAsStream("/assets/" + resourceLocation.getNamespace() + "/" + resourceLocation.getPath());
        }
    }