package yaboichips.geckomod.common.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import yaboichips.geckomod.core.GItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class GeckoBossEntity extends MonsterEntity implements IRangedAttackMob {
    private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(GeckoBossEntity.class, DataSerializers.VARINT);
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    private static final Predicate<LivingEntity> NOT_UNDEAD = (mob) -> {
        return mob.getCreatureAttribute() != CreatureAttribute.UNDEAD && mob.attackable();
    };
    private static final EntityPredicate ENEMY_CONDITION = (new EntityPredicate()).setDistance(20.0D).setCustomPredicate(NOT_UNDEAD);

    public boolean no = false;

    public GeckoBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, false, false, NOT_UNDEAD));
    }

    public static @Nonnull
    AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET, 0);
    }



    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(new TranslationTextComponent("Gecko Boss"));
    }


    private void launchProjectileToEntity(int head, PlayerEntity target) {
        this.launchProjectileToCoords(head, target.getPosX(), target.getPosY(), target.getPosZ(), head == 0 && this.rand.nextFloat() < 0.001F);
    }

    @Override
    public void livingTick() {
       PlayerEntity player = this.world.getClosestPlayer(this, 50D);
        this.setAttackTarget(player);
        super.livingTick();
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
        this.launchProjectileToEntity(0, (PlayerEntity)target);
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
