package yaboichips.geckomod.common.world;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.core.GEntities;

@Mod.EventBusSubscriber(modid = GeckoMod.MOD_ID)
public class GEntitySpawning {

    public static void registerEntityWorldSpawns(BiomeLoadingEvent event, EntityType<?> entity, EntityClassification classification, Biome.Category...categories) {
    for (Biome.Category category : categories)
        if (category !=null) {
            event.getSpawns().withSpawner(classification, new MobSpawnInfo.Spawners(entity, 100, 1, 3));
        }
}

        @SubscribeEvent
        public static void createEntitySpawns(BiomeLoadingEvent event){
            registerEntityWorldSpawns(event, GEntities.GECKO, EntityClassification.AMBIENT, Biome.Category.SWAMP);
            //registerEntityWorldSpawns(event, GEntities.NETHERGECKO, EntityClassification.AMBIENT, Biome.Category.NETHER);
        }
}