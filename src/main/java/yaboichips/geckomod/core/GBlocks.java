package yaboichips.geckomod.core;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.blocks.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GBlocks {

    public static List<Block> blocks = new ArrayList<>();


    public static Block GECKO_EGG_BLOCK = createEggBlock("gecko_egg");
    public static Block NETHER_GECKO_EGG_BLOCK = createEggBlock("nether_gecko_egg");
    public static Block END_GECKO_EGG_BLOCK = createEggBlock("end_gecko_egg");
    public static Block TERRARIUM_BLOCK = createTerrariumBlock("terrarium_block");
    public static Block JUNGLE_BOSS_SPAWNER = createJungleSpawnerBlock("jungle_boss_spawner");
    public static Block ICE_BOSS_SPAWNER = createIceSpawnerBlock("ice_boss_spawner");
    public static Block FIRE_BOSS_SPAWNER = createFireSpawnerBlock("fire_boss_spawner");
    public static Block FLOWER_BOSS_SPAWNER = createFlowerSpawnerBlock("flower_boss_spawner");



    static @Nonnull Block createTerrariumBlock(String id) {
        Block createBlock = new TerrariumBlock(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(4.0f, 1.5f).sound(SoundType.GLASS));
        return registerBlock(id, createBlock);
    }

    static @Nonnull Block createJungleSpawnerBlock(String id) {
        Block createBlock = new JungleBossSpawnBlock(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.CHAIN).notSolid());
        return registerBlock(id, createBlock);
    }

    static @Nonnull Block createIceSpawnerBlock(String id) {
        Block createBlock = new IceBossSpawnBlock(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.CHAIN).notSolid());
        return registerBlock(id, createBlock);
    }

    static @Nonnull Block createFireSpawnerBlock(String id) {
        Block createBlock = new FireBossSpawnBlock(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.CHAIN).notSolid());
        return registerBlock(id, createBlock);
    }

    static @Nonnull Block createFlowerSpawnerBlock(String id) {
        Block createBlock = new FlowerBossSpawnBlock(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.CHAIN).notSolid());
        return registerBlock(id, createBlock);
    }

    static @Nonnull Block createEggBlock(String id) {
        Block createBlock = new GeckoEggBlock(AbstractBlock.Properties.create(Material.CORAL).sound(SoundType.HYPHAE).hardnessAndResistance(0.7f).tickRandomly().notSolid());
        return registerBlock(id, createBlock);
    }


    static @Nonnull
    <T extends Block> T registerBlock(String id, @Nonnull T block) {
        block.setRegistryName(GeckoMod.createResource(id));

        blocks.add(block);

        return block;
    }

    public static void init() {
    }
}

