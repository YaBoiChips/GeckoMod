package yaboichips.geckomod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import yaboichips.geckomod.common.entities.GeckoBossEntity;
import yaboichips.geckomod.core.GEntities;

public class FireBossSpawnBlock extends Block {
    public FireBossSpawnBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        GeckoBossEntity gecko = GEntities.GECKOBOSSFIRE.create(worldIn);
        worldIn.addEntity(gecko);
        gecko.setPosition(pos.getX(), pos.getY(), pos.getZ());
        worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.AMBIENT, 1.0f, 1.0f);
        worldIn.removeBlock(pos, false);
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
