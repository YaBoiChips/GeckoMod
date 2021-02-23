package yaboichips.geckomod.core;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.entities.tileentities.TerrariumTileEntity;

public class GTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(
            ForgeRegistries.TILE_ENTITIES, GeckoMod.MOD_ID);

    public static final RegistryObject<TileEntityType<TerrariumTileEntity>> TERRARIUM = TILE_ENTITY_TYPES
            .register("terrarium", ()-> TileEntityType.Builder
                    .create(TerrariumTileEntity::new, GBlocks.TERRARIUM_BLOCK).build(null));

}
