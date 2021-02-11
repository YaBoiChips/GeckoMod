package yaboichips.geckomod.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.entities.GeckoBossEntity;
import yaboichips.geckomod.common.entities.GeckoEntity;
import yaboichips.geckomod.common.entities.NetherGeckoEntity;

import java.util.ArrayList;
import java.util.List;

public class GEntities {
    public static List<EntityType<?>> entities = new ArrayList<>();

    public static final EntityType<GeckoEntity> GECKO = createEntity("gecko", EntityType.Builder.create(GeckoEntity::new, EntityClassification.AMBIENT).build("gecko"));
    public static final EntityType<NetherGeckoEntity> NETHERGECKO = createEntity("nethergecko", EntityType.Builder.create(NetherGeckoEntity::new, EntityClassification.AMBIENT).build("nethergecko"));
    public static final EntityType<GeckoBossEntity> GECKOBOSSJUNGLE = createEntity("gecko_boss_jungle", EntityType.Builder.create(GeckoBossEntity::new, EntityClassification.MONSTER).size(7f, 3f).build("gecko_boss_jungle"));

    public static <E extends Entity, ET extends EntityType<E>> ET createEntity(String id, ET entityType) {
        entityType.setRegistryName(new ResourceLocation(GeckoMod.MOD_ID, id));
        entities.add(entityType);
        return entityType;
    }

    public static void init() {
    }
}

