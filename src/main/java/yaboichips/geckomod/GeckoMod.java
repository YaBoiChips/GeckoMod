package yaboichips.geckomod;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
import yaboichips.geckomod.client.worldrenderers.GCutOutTextures;
import yaboichips.geckomod.common.containers.screens.TerrariumScreen;
import yaboichips.geckomod.common.entities.*;
import yaboichips.geckomod.core.*;
import yaboichips.geckomod.util.GKeyBinds;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

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
        GStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM GECKOS");
        GlobalEntityTypeAttributes.put(GEntities.GECKO, GeckoEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.NETHERGECKO, NetherGeckoEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.GECKOBOSSJUNGLE, JungleGeckoBossEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.GECKOBOSSICE, IceGeckoBossEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(GEntities.ENDGECKO, EndGeckoEntity.setCustomAttributes().create());
        event.enqueueWork(() -> {
            GStructures.setupStructures();
            GConfiguredStructures.registerConfiguredStructures();
        });
        LOGGER.info("GECKO COMMON DONE");
    }

    public void biomeModification(final BiomeLoadingEvent event) {
        ResourceLocation biome = event.getName();
        Biome.Category category = event.getCategory();
        if (biome == null) return;
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (category == Biome.Category.JUNGLE)
        generation.getStructures().add(() -> GConfiguredStructures.CONFIGURED_JUNGLE_BOSS_AREA);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKO, GeckoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.NETHERGECKO, NetherGeckoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKOBOSSJUNGLE, JungleGeckoBossRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKOBOSSICE, IceGeckoBossRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.GECKOSPIT, GeckoSpitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GEntities.ENDGECKO, EndGeckoRenderer::new);
        ScreenManager.registerFactory(GContainers.TERRARIUM_CONTAINER.get(), TerrariumScreen::new);
        GKeyBinds.register();
        GCutOutTextures.renderCutOuts();
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
