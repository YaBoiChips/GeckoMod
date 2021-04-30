package yaboichips.geckomod.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import yaboichips.geckomod.core.GItems;

public class JungleGeckoBossEntity extends GeckoBossEntity {

    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public JungleGeckoBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void livingTick() {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        this.bossInfo.setName(new TranslationTextComponent("Gecko Jungle Boss"));
        super.livingTick();
    }

    @Override
    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(GItems.GIANT_GECKO_SCALE);
        if (itementity != null) {
            itementity.setNoDespawn();
        }
    }
}
