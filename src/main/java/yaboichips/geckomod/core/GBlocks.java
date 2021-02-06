package yaboichips.geckomod.core;

import net.minecraft.block.Block;
import yaboichips.geckomod.GeckoMod;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GBlocks {

    public static List<Block> blocks = new ArrayList<>();















    static @Nonnull
    <T extends Block> T registerBlock(String id, @Nonnull T block) {
        block.setRegistryName(GeckoMod.createResource(id));

        blocks.add(block);

        return block;
    }

    public static void init() {
    }
}

