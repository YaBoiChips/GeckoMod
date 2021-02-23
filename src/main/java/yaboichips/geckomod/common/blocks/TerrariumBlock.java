package yaboichips.geckomod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import yaboichips.geckomod.common.entities.tileentities.TerrariumTileEntity;
import yaboichips.geckomod.core.GItems;
import yaboichips.geckomod.core.GTileEntities;

import javax.annotation.Nullable;

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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TerrariumTileEntity){
            TerrariumTileEntity terrariumTileEntity = (TerrariumTileEntity)tileentity;
            ItemStack stack = player.getHeldItem(handIn);
            Item item = stack.getItem();
        if (item == GItems.GECKO_POUCH) {
            //if (stack.getTag().toString().equals("entity")) {
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.contains("entity");
                terrariumTileEntity.write(compoundNBT);
            //}
        }
    }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public String getID(ItemStack stack) {
        return stack.getTag().getString("entity");
    }

    @Nullable
    public Entity getEntityFromStack(ItemStack stack, World world, boolean withInfo) {
        EntityType type = EntityType.byKey(stack.getTag().getString("entity")).orElse(null);
        if (type != null) {
            Entity entity = type.create(world);
            if (withInfo) entity.read(stack.getTag());
            return entity;
        }
        return null;
    }
}
