package yaboichips.geckomod.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import yaboichips.geckomod.core.GItems;

import java.util.List;

public class FlowerGeckoBossEntity extends GeckoBossEntity {

    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public FlowerGeckoBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void livingTick() {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        this.bossInfo.setName(new TranslationTextComponent("Gecko Flower Boss"));
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.getPosition())).grow(4).expand(0.0D, this.world.getHeight(), 0.0D);
        List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb);
        for (PlayerEntity playerentity : list) {
            if (this.getTicksNearBoss() >= 15) {
                playerentity.addPotionEffect(new EffectInstance(Effects.POISON, 30 , 1));
            }
        }
        super.livingTick();
    }

    @Override
    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(GItems.GIANT_GECKO_EYE);
        if (itementity != null) {
            itementity.setNoDespawn();
        }
    }
}