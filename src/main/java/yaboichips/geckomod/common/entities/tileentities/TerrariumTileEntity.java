package yaboichips.geckomod.common.entities.tileentities;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import yaboichips.geckomod.core.GTileEntities;

public class TerrariumTileEntity extends TileEntity implements ITickableTileEntity {
    public TerrariumTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    public TerrariumTileEntity(){
        this(GTileEntities.TERRARIUM.get());
    }



    @Override
    public void tick() {
        if (this.getTileData().contains("entity")){
            World world = this.world;
            world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.AMBIENT, 1.0f, 1.0f, false);
            System.out.println("poggies");
        }
    }
}
