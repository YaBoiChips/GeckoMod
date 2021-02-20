package yaboichips.geckomod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import yaboichips.geckomod.common.entities.GeckoEntity;
import yaboichips.geckomod.core.GEntities;

import java.util.Random;

public class GeckoEggBlock extends Block {
    private static final VoxelShape GECKO_EGG_SHAPE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D);
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH_0_2;

    public GeckoEggBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(HATCH, Integer.valueOf(0)));
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (this.canGrow(worldIn)) {
            int i = state.get(HATCH);
            if (i < 2) {
                worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldIn.setBlockState(pos, state.with(HATCH, i + 1), 2);
            } else {
                worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldIn.removeBlock(pos, false);
                    worldIn.playEvent(2001, pos, Block.getStateId(state));
                    GeckoEntity geckoentity = GEntities.GECKO.create(worldIn);
                    geckoentity.setGrowingAge(-24000);
                    geckoentity.setLocationAndAngles((double)pos.getX() + 0.3D , pos.getY(), (double)pos.getZ() + 0.3D, 0.0F, 0.0F);
                    worldIn.addEntity(geckoentity);
                }
            }
        }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return GECKO_EGG_SHAPE;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HATCH);
    }

    private boolean canGrow(World worldIn) {
        float f = worldIn.func_242415_f(1.0F);
        if ((double)f < 0.69D && (double)f > 0.65D) {
            return true;
        } else {
            return worldIn.rand.nextInt(500) == 0;
        }
    }

}
