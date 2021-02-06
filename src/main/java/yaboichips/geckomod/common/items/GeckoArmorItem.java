package yaboichips.geckomod.common.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GeckoArmorItem extends Item {

    private final int armorValue;
    private final ResourceLocation texture;

    public GeckoArmorItem(int armorValue, String tierArmor, Item.Properties builder) {
        this(armorValue, new ResourceLocation("textures/entity/gecko/armor/gecko_armor_" + tierArmor + ".png"), builder);
    }

    public GeckoArmorItem(int armorValue, ResourceLocation texture, Item.Properties builder) {
        super(builder);
        this.armorValue = armorValue;
        this.texture = texture;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getArmorTexture() {
        return texture;
    }

    public int getArmorValue() {
        return this.armorValue;
    }
}
