package yaboichips.geckomod.client.worldrenderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import yaboichips.geckomod.core.GBlocks;

import java.util.Arrays;

public class GCutOutTextures {

    public static void renderCutOuts() {
        Block[] blocks = {
                GBlocks.JUNGLE_BOSS_SPAWNER.getBlock(),
                GBlocks.ICE_BOSS_SPAWNER.getBlock(),
                GBlocks.FIRE_BOSS_SPAWNER.getBlock()

        };
        Arrays.stream(blocks).forEach((block) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutoutMipped()));
        Arrays.stream(blocks).forEach((block) -> RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }
}
