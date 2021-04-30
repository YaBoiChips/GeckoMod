package yaboichips.geckomod.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.entities.*;

import java.util.ArrayList;
import java.util.List;

public class GEntities {
    public static List<EntityType<?>> entities = new ArrayList<>();

    public static final EntityType<GeckoEntity> GECKO = createEntity("gecko", EntityType.Builder.create(GeckoEntity::new, EntityClassification.AMBIENT).build("gecko"));
    public static final EntityType<GeckoSpitEntity> GECKOSPIT = register("gecko_spit", EntityType.Builder.<GeckoSpitEntity>create(GeckoSpitEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).func_233608_b_(10), EntityType.Builder.<GeckoSpitEntity>create(GeckoSpitEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).func_233608_b_(10).build("geckospit"));
    public static final EntityType<NetherGeckoEntity> NETHERGECKO = createEntity("nethergecko", EntityType.Builder.create(NetherGeckoEntity::new, EntityClassification.AMBIENT).build("nethergecko"));
    public static final EntityType<EndGeckoEntity> ENDGECKO = createEntity("endgecko", EntityType.Builder.create(EndGeckoEntity::new, EntityClassification.AMBIENT).build("endgecko"));
    public static final EntityType<JungleGeckoBossEntity> GECKOBOSSJUNGLE = createEntity("gecko_boss_jungle", EntityType.Builder.create(JungleGeckoBossEntity::new, EntityClassification.MONSTER).size(6f, 12f).build("gecko_boss_jungle"));
    public static final EntityType<IceGeckoBossEntity> GECKOBOSSICE = createEntity("gecko_boss_ice", EntityType.Builder.create(IceGeckoBossEntity::new, EntityClassification.MONSTER).size(6f, 12f).build("gecko_boss_ice"));
    public static final EntityType<FireGeckoBossEntity> GECKOBOSSFIRE = createEntity("gecko_boss_fire", EntityType.Builder.create(FireGeckoBossEntity::new, EntityClassification.MONSTER).size(6f, 12f).build("gecko_boss_fire"));
    public static final EntityType<FlowerGeckoBossEntity> GECKOBOSSFLOWER = createEntity("gecko_boss_flower", EntityType.Builder.create(FlowerGeckoBossEntity::new, EntityClassification.MONSTER).size(6f, 12f).build("gecko_boss_flower"));

    public static <E extends Entity, ET extends EntityType<E>> ET createEntity(String id, ET entityType) {
        entityType.setRegistryName(new ResourceLocation(GeckoMod.MOD_ID, id));
        entities.add(entityType);
        return entityType;
    }

    private static <E extends Entity, ET extends EntityType<E>> ET register(String id, EntityType.Builder<E> builder, ET entityType) {
        entityType.setRegistryName(new ResourceLocation(GeckoMod.MOD_ID, id));
        entities.add(entityType);
        return entityType;
    }


    public static void init() {
    }
}