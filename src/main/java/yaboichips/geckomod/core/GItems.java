package yaboichips.geckomod.core;

import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.common.items.GeckoArmorItem;
import yaboichips.geckomod.common.items.GeckoPouchItem;

import java.util.ArrayList;
import java.util.List;

public class GItems {
    public static List<Item> items = new ArrayList<>();

    public static final ItemGroup GECKO_TAB = new ItemGroup(GeckoMod.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(GItems.GECKO_STAFF);
        }
        @Override
        public boolean hasScrollbar() {
            return true;
        }
        @Override
        public ResourceLocation getBackgroundImage() {
            return new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tab_items.png");
        }
    };

    public static final Item GECKO_SPAWN_EGG = createItem(new SpawnEggItem(GEntities.GECKO, 81763, 91239231, (new Item.Properties().group(GECKO_TAB))), "gecko_spawn_egg");
    public static final Item NETHER_GECKO_SPAWN_EGG = createItem(new SpawnEggItem(GEntities.NETHERGECKO, 9123931, 2134, (new Item.Properties().group(GECKO_TAB))), "nether_gecko_spawn_egg");
    public static final Item GECKO_STAFF = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "gecko_staff");
    public static final Item GECKO_POUCH = createItem(new GeckoPouchItem(new Item.Properties().group(GECKO_TAB)), "gecko_pouch");

    //Blocks
    public static final Item GECKO_EGG = createItem(new BlockItem(GBlocks.GECKO_EGG_BLOCK, new Item.Properties().group(GECKO_TAB)), Registry.BLOCK.getKey(GBlocks.GECKO_EGG_BLOCK));
    public static final Item TERRARIUM_BLOCK = createItem(new BlockItem(GBlocks.TERRARIUM_BLOCK, new Item.Properties().group(GECKO_TAB)), Registry.BLOCK.getKey(GBlocks.TERRARIUM_BLOCK));


    //Armors
    public static final Item IRON_GECKO_ARMOR = createItem(new GeckoArmorItem(5, "iron", (new Item.Properties().group(GECKO_TAB))), "iron_gecko_armor");
    public static final Item DIAMOND_GECKO_ARMOR = createItem(new GeckoArmorItem(11, "diamond", (new Item.Properties().group(GECKO_TAB))), "diamond_gecko_armor");
    public static final Item GOLD_GECKO_ARMOR = createItem(new GeckoArmorItem(7, "gold", (new Item.Properties().group(GECKO_TAB))), "gold_gecko_armor");
    public static final Item NETHERITE_GECKO_ARMOR = createItem(new GeckoArmorItem(13, "netherite", (new Item.Properties().group(GECKO_TAB))), "netherite_gecko_armor");

    //Gecko Foods
    public static final Item GECKO_FOOD = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "gecko_food");
    public static final Item SPICY_GECKO_FOOD = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "spicy_gecko_food");
    public static final Item END_GECKO_FOOD = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "end_gecko_food");
    public static final Item GIANT_GECKO_SERUM = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "giant_gecko_serum");

    //Boss Drops
    public static final Item GIANT_GECKO_SCALE = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "giant_gecko_scale");
    public static final Item GIANT_GECKO_TOOTH = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "giant_gecko_tooth");
    public static final Item GIANT_GECKO_EYE = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "giant_gecko_eye");
    public static final Item GIANT_GECKO_HEART = createItem(new Item(new Item.Properties().group(GECKO_TAB)), "giant_gecko_heart");






    public static Item createItem(Item item, String id) {
        return createItem(item, GeckoMod.createResource(id));
    }

    public static Item createItem(Item item, ResourceLocation id) {
        if (id != null && !id.equals(new ResourceLocation("minecraft:air"))) {
            item.setRegistryName(id);

            items.add(item);

            return item;
        } else return null;
    }

    public static void init() {
    }
}
