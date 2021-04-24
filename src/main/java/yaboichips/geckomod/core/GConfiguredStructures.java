package yaboichips.geckomod.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import yaboichips.geckomod.GeckoMod;

public class GConfiguredStructures {

    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_JUNGLE_BOSS_AREA = GStructures.JUNGLE_BOSS_AREA.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_ICE_BOSS_AREA = GStructures.ICE_BOSS_AREA.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_FIRE_BOSS_AREA = GStructures.FIRE_BOSS_AREA.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(GeckoMod.MOD_ID, "configured_jungle_boss_area"), CONFIGURED_JUNGLE_BOSS_AREA);
        Registry.register(registry, new ResourceLocation(GeckoMod.MOD_ID, "configured_ice_boss_area"), CONFIGURED_ICE_BOSS_AREA);
        Registry.register(registry, new ResourceLocation(GeckoMod.MOD_ID, "configured_fire_boss_area"), CONFIGURED_FIRE_BOSS_AREA);
        FlatGenerationSettings.STRUCTURES.put(GStructures.FIRE_BOSS_AREA.get(), CONFIGURED_FIRE_BOSS_AREA);
        FlatGenerationSettings.STRUCTURES.put(GStructures.JUNGLE_BOSS_AREA.get(), CONFIGURED_JUNGLE_BOSS_AREA);
        FlatGenerationSettings.STRUCTURES.put(GStructures.ICE_BOSS_AREA.get(), CONFIGURED_ICE_BOSS_AREA);

    }
}
