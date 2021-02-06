package yaboichips.geckomod.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import yaboichips.geckomod.core.GItems;

import javax.annotation.Nullable;

public class GeckoBossEntity extends MonsterEntity implements IRangedAttackMob {
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    public boolean no = false;

    public GeckoBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false, false));
    }



    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }


    private void launchProjectileToEntity(int head, LivingEntity target) {
        this.launchProjectileToCoords(head, target.getPosX(), target.getPosY() + (double)target.getEyeHeight() * 0.5D, target.getPosZ(), head == 0 && this.rand.nextFloat() < 0.001F);
    }


    private void launchProjectileToCoords(int head, double x, double y, double z, boolean invulnerable) {
        if (!this.isSilent()) {
            this.world.playEvent(null, 1024, this.getPosition(), 0);
        }
        WitherSkullEntity witherskullentity = new WitherSkullEntity(this.world, this, this.getPosX(), this.getPosY(), this.getPosZ());
        witherskullentity.setShooter(this);
        if (invulnerable) {
            witherskullentity.setSkullInvulnerable(true);
        }

        witherskullentity.setRawPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        this.world.addEntity(witherskullentity);
    }


    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.launchProjectileToEntity(0, target);
    }

    @Override
    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(GItems.GIANT_GECKO_SCALE);
        if (itementity != null) {
            itementity.setNoDespawn();
        }
    }

    @Override
    public boolean isNonBoss() {
        return no;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDespawnPeaceful()) {
            this.remove();
        } else {
            this.idleTime = 0;
        }
    }
}
