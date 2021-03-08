package yaboichips.geckomod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import yaboichips.geckomod.common.entities.tileentities.TerrariumTileEntity;
import yaboichips.geckomod.core.GTileEntities;

public class TerrariumBlock extends Block {
    public TerrariumBlock(Properties builder) {
        super(builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return GTileEntities.TERRARIUM.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult result) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            ItemStack stack = player.getHeldItem(handIn);
            if (stack.hasTag()) {
                if (stack.getTag().contains("entity")) {
                    assert tile != null;
                    ((TerrariumTileEntity) tile).setInventorySlotContents(0, stack.copy());
                    stack.shrink(1);
                }
            }
             else{
                 System.out.println("pog");
                NetworkHooks.openGui((ServerPlayerEntity) player, (TerrariumTileEntity) tile, pos);
            }
            return ActionResultType.SUCCESS;
            }
        return ActionResultType.FAIL;
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TerrariumTileEntity) {
                InventoryHelper.dropItems(worldIn, pos, ((TerrariumTileEntity) te).getItems());
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
}
