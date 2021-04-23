package yaboichips.geckomod.client.worldrenderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import yaboichips.geckomod.core.GBlocks;

import java.util.Arrays;

public class GCutOutTextures {

    public static void renderCutOuts() {
        Block[] blocks = {
                GBlocks.JUNGLE_BOSS_SPAWNER.getBlock()
        };
        Arrays.stream(blocks).forEach((block) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutoutMipped()));
    }
}
