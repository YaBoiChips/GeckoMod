package yaboichips.geckomod;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yaboichips.geckomod.client.entity.renderers.*;
import yaboichips.geckomod.common.containers.screens.TerrariumScreen;
import yaboichips.geckomod.common.entities.EndGeckoEntity;
import yaboichips.geckomod.common.entities.GeckoBossEntity;
import yaboichips.geckomod.common.entities.GeckoEntity;
import yaboichips.geckomod.common.entities.NetherGeckoEntity;
import yaboichips.geckomod.core.*;
import yaboichips.geckomod.util.GKeyBinds;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GeckoMod.MOD_ID)
public class GeckoMod {

    public static final String MOD_ID = "geckomod";

    private static final Logger LOGGER = LogManager.getLogger();

    public GeckoMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        GTileEntities.TILE_ENTITY_TYPES.register(modEventBus);
        GContainers.CONTAINER_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM GECKOS");
        GlobalEntityTypeAttributes.put(GEntities.GECKO, GeckoEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.NETHERGECKO, NetherGeckoEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.GECKOBOSSJUNGLE, GeckoBossEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.ENDGECKO, EndGeckoEntity.setCustomAttributes().create());

        LOGGER.info("GECKO COMMON DONE");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKO, GeckoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.NETHERGECKO, NetherGeckoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKOBOSSJUNGLE, GeckoBossJungleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKOSPIT, GeckoSpitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.ENDGECKO, EndGeckoRenderer::new);
        ScreenManager.registerFactory(GContainers.TERRARIUM_CONTAINER.get(), TerrariumScreen::new);
        GKeyBinds.register();
        LOGGER.info("GECKO CLIENT DONE");

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(GeckoMod.MOD_ID, "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }


    public static @Nonnull
    ResourceLocation createResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }


    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class GRegistryEvents {
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            LOGGER.info("HELLO from Register Block");
            GBlocks.init();
            GBlocks.blocks.forEach(block -> event.getRegistry().register(block));
            GBlocks.blocks.clear();
            GBlocks.blocks = null;
            LOGGER.info("GeckoMod: Blocks registered!");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.debug("GeckoMod: Registering items...");
            GItems.init();
            GItems.items.forEach(item -> event.getRegistry().register(item));
            GItems.items.clear();
            GItems.items = null;
            LOGGER.info("GeckoMod: Items registered!");
        }

        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
            LOGGER.debug("Preparing Gecko Entities");
            GEntities.init();
            GEntities.entities.forEach(entityType -> event.getRegistry().register(entityType));
            GEntities.entities.clear();
            GEntities.entities = null;
            LOGGER.info("Geckos registered!!");
        }
    }
}
