package yaboichips.geckomod.core;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.containers.TerrariumContainer;

public class GContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, GeckoMod.MOD_ID);

    public static final RegistryObject<ContainerType<TerrariumContainer>> TERRARIUM_CONTAINER = CONTAINER_TYPES
            .register("terrarium_container", () -> IForgeContainerType.create(TerrariumContainer::new));
}
