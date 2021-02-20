package yaboichips.geckomod.core;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.blocks.GeckoEggBlock;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GBlocks {

    public static List<Block> blocks = new ArrayList<>();


    public static Block GECKO_EGG_BLOCK = createEggBlock("gecko_egg");

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

