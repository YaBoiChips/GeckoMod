package yaboichips.geckomod.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import yaboichips.geckomod.common.entities.GeckoEntity;
import yaboichips.geckomod.core.GBlocks;

import javax.annotation.Nullable;
import java.util.List;

public class GeckoPouchItem extends BasicItem {
    public GeckoPouchItem(Properties properties) {
        super(properties);
    }
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();
        World worldIn = context.getWorld();
        ItemStack stack = context.getItem();
        if (player.getEntityWorld().isRemote) return ActionResultType.FAIL;
            if (!containsEntity(stack)) return ActionResultType.FAIL;
            if (worldIn.getBlockState(pos) != GBlocks.TERRARIUM_BLOCK.getDefaultState()) {
                Entity entity = getEntityFromStack(stack, worldIn, true);
                BlockPos blockPos = pos.offset(facing);
                entity.setPositionAndRotation(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
                stack.setTag(new CompoundNBT());
                worldIn.addEntity(entity);
            }
        return ActionResultType.SUCCESS;
    }



    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return ActionResultType.FAIL;
            if (!(target instanceof GeckoEntity) || !target.isNonBoss() || !target.isAlive()) return ActionResultType.FAIL;
                if (containsEntity(stack)) return ActionResultType.FAIL;
                CompoundNBT nbt = new CompoundNBT();
                nbt.putString("entity", EntityType.getKey(target.getType()).toString());
                target.writeWithoutTypeId(nbt);
                stack.setTag(nbt);
                playerIn.swingArm(hand);
                playerIn.setHeldItem(hand, stack);
                target.remove(true);
        return ActionResultType.SUCCESS;
    }



    public static boolean containsEntity(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains("entity");
    }

    @Override
    public boolean hasTooltipDetails(@Nullable Key key) {
        return key == null;
    }

    @Override
    public void addTooltipDetails(@Nullable Key key, ItemStack stack, List<ITextComponent> tooltip, boolean advanced) {
        super.addTooltipDetails(key, stack, tooltip, advanced);
        if (containsEntity(stack)) {
            tooltip.add(new StringTextComponent("Mob: " + getID(stack)));
            tooltip.add(new StringTextComponent("Health: " + stack.getTag().getDouble("Health")));
        }
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

    public String getID(ItemStack stack) {
        return stack.getTag().getString("entity");
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (!containsEntity(stack))
            return new TranslationTextComponent(super.getTranslationKey(stack));
        return new TranslationTextComponent(super.getTranslationKey(stack)).appendString(" (" + getID(stack) + ")");
    }


}

