package yaboichips.geckomod.common.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class GeckoBossEntity extends MonsterEntity implements IRangedAttackMob {
    protected int ticksNearBoss;
    private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(GeckoBossEntity.class, DataSerializers.VARINT);
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    private static final Predicate<LivingEntity> NOT_UNDEAD = (mob) -> {
        return mob.getCreatureAttribute() != CreatureAttribute.UNDEAD && mob.attackable();
    };
    private static final EntityPredicate ENEMY_CONDITION = (new EntityPredicate()).setDistance(20.0D).setCustomPredicate(NOT_UNDEAD);


    public GeckoBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));    }

    public static @Nonnull
    AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 242.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0F)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 22.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET, 0);
    }

    public int getTicksNearBoss(){
        return this.ticksNearBoss;
    }

    @Override
    public void livingTick() {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.getPosition())).grow(4).expand(0.0D, this.world.getHeight(), 0.0D);
        List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb);
        for (PlayerEntity playerentity : list) {
            ++this.ticksNearBoss;
            if (getTicksNearBoss() >= 80){
                float f7 = playerentity.rotationYaw;
                float f = playerentity.rotationPitch;
                float f1 = -MathHelper.sin(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
                float f2 = -MathHelper.sin(f * ((float)Math.PI / 180F));
                float f3 = MathHelper.cos(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
                float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 4.2F;
                f1 = f1 * (f5 / f4);
                f3 = f3 * (f5 / f4);
                playerentity.addVelocity(-f1 ,2, -f3);
                this.ticksNearBoss = 0;
            }
        }
        super.livingTick();
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(new TranslationTextComponent("Gecko Boss"));
    }


    void launchProjectileToEntity(LivingEntity target) {
        GeckoSpitEntity geckoSpitEntity = new GeckoSpitEntity(this.world, this);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - geckoSpitEntity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        geckoSpitEntity.shoot(d0, d1 + (double)f, d2, 1.5F, 10.0F);
        if (!this.isSilent()) {
            this.world.playSound((PlayerEntity)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }

        this.world.addEntity(geckoSpitEntity);
    }


    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.launchProjectileToEntity(target);
    }




    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

   @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean isNonBoss() {
        return false;
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
