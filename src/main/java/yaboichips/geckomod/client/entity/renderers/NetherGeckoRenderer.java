package yaboichips.geckomod.client.entity.renderers;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.NetherGeckoModel;
import yaboichips.geckomod.common.entities.NetherGeckoEntity;

import javax.annotation.Nonnull;
import java.util.Map;

public class NetherGeckoRenderer<T extends NetherGeckoEntity> extends AbstractRenderer<T, NetherGeckoModel<T>> {
    public static final Map<NetherGeckoEntity.SkinColors, ResourceLocation> GECKO_TEXTURE = Util.make(Maps.newEnumMap(NetherGeckoEntity.SkinColors.class), (map) -> {
        map.put(NetherGeckoEntity.SkinColors.ORANGENETHER, createTexture("orange_nether"));
        map.put(NetherGeckoEntity.SkinColors.REDNETHER, createTexture("red_nether"));

    });

    public NetherGeckoRenderer(EntityRendererManager manager) {

        super(manager, new NetherGeckoModel<>(), 1.0F);
    }


    @Override
    public @Nonnull
    ResourceLocation getEntityTexture(@Nonnull T entity) {
        return GECKO_TEXTURE.get(entity.getNetherSkinColor());
    }

    private static @Nonnull
    ResourceLocation createTexture(String name) {
        return GeckoMod.createResource("textures/entity/gecko/" + name + "_gecko.png");
    }
}

