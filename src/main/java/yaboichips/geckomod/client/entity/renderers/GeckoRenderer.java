package yaboichips.geckomod.client.entity.renderers;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.GeckoModel;
import yaboichips.geckomod.common.entities.GeckoEntity;

import javax.annotation.Nonnull;
import java.util.Map;

public class GeckoRenderer<T extends GeckoEntity> extends AbstractRenderer<T, GeckoModel<T>> {
    public static final Map<GeckoEntity.SkinColors, ResourceLocation> GECKO_TEXTURE = Util.make(Maps.newEnumMap(GeckoEntity.SkinColors.class), (map) -> {
        map.put(GeckoEntity.SkinColors.BLUE,   createTexture("blue"));
        map.put(GeckoEntity.SkinColors.WHITE,  createTexture("white"));
        map.put(GeckoEntity.SkinColors.GREEN,  createTexture("green"));
        map.put(GeckoEntity.SkinColors.BROWN,  createTexture("brown"));
        map.put(GeckoEntity.SkinColors.BLACK,  createTexture("black"));
        map.put(GeckoEntity.SkinColors.RED,    createTexture("red"));
        map.put(GeckoEntity.SkinColors.ORANGE, createTexture("orange"));
    });

    public GeckoRenderer(EntityRendererManager manager) {
        super(manager, new GeckoModel<>(), 1.0F);
    }

    @Override
    public @Nonnull
    ResourceLocation getEntityTexture(@Nonnull T entity) {
        return GECKO_TEXTURE.get(entity.getSkinColor());
    }

    private static @Nonnull ResourceLocation createTexture(String name) {
        return GeckoMod.createResource("textures/entity/gecko/" + name + "_gecko.png");
    }
}