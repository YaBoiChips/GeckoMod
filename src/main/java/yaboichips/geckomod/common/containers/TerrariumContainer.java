package yaboichips.geckomod.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fml.common.Mod;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.entities.tileentities.TerrariumTileEntity;
import yaboichips.geckomod.core.GBlocks;
import yaboichips.geckomod.core.GContainers;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = GeckoMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerrariumContainer extends Container {

    public final TerrariumTileEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;


    public TerrariumContainer( final int windowId, final PlayerInventory playerInv, final TerrariumTileEntity tileEntityIn){
        super(GContainers.TERRARIUM_CONTAINER.get(), windowId);
        this.tileEntity = tileEntityIn;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());

        this.addSlot(new Slot(tileEntityIn, 0, 81, 36));

        // Main Inventory
        int startX = 8;
        int startY = 84;
        int slotSizePlus2 = 18;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInv, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
                        startY + (row * slotSizePlus2)));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInv, column, startX + (column * slotSizePlus2), 142));
        }
    }

    public TerrariumContainer( final int windowId, final PlayerInventory playerInv, final PacketBuffer data){
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    private static TerrariumTileEntity getTileEntity ( final PlayerInventory playerInv, final PacketBuffer data){
        Objects.requireNonNull(playerInv, "playerInv cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (tileAtPos instanceof TerrariumTileEntity) {
            return (TerrariumTileEntity) tileAtPos;
        }
        throw new IllegalStateException("TileEntity is not correct " + tileAtPos);
    }

    @Override
    public boolean canInteractWith (PlayerEntity playerIn){
        return isWithinUsableDistance(canInteractWithCallable, playerIn, GBlocks.TERRARIUM_BLOCK);
    }


    @Override
    public ItemStack transferStackInSlot (PlayerEntity playerIn, int index){
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 1) {
                if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}

