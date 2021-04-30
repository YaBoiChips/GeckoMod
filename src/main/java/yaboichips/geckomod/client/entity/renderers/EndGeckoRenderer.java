package yaboichips.geckomod.client.entity.renderers;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.GeckoModel;
import yaboichips.geckomod.common.entities.EndGeckoEntity;

import javax.annotation.Nonnull;
import java.util.Map;

public class EndGeckoRenderer<T extends EndGeckoEntity> extends AbstractRenderer<T, GeckoModel<T>> {

    public static final Map<EndGeckoEntity.SkinColors, ResourceLocation> GECKO_TEXTURE = Util.make(Maps.newEnumMap(EndGeckoEntity.SkinColors.class), (map) -> {
        map.put(EndGeckoEntity.SkinColors.BLACKEND, createTexture("black_end"));
        map.put(EndGeckoEntity.SkinColors.PURPLEEND, createTexture("white_end"));

    });

    public EndGeckoRenderer(EntityRendererManager manager) {
        super(manager, new GeckoModel<>(), 1.0f);
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return GECKO_TEXTURE.get(entity.getEndSkinColor());
    }

    private static @Nonnull
    ResourceLocation createTexture(String name) {
        return GeckoMod.createResource("textures/entity/gecko/" + name + "_gecko.png");
    }
}
