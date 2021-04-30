package yaboichips.geckomod.common.entities;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import yaboichips.geckomod.core.GBlocks;
import yaboichips.geckomod.core.GEntities;
import yaboichips.geckomod.core.GItems;
import yaboichips.geckomod.util.Maths;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.UUID;

public class EndGeckoEntity extends GeckoEntity {
    private static final DataParameter<Integer> FLAGS = EntityDataManager.createKey(EndGeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ARMOR_TYPE = EntityDataManager.createKey(EndGeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> STANDING = EntityDataManager.createKey(EndGeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EndGeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EndGeckoEntity.class, DataSerializers.VARINT);
    private int underWaterTicks;

    public EndGeckoEntity(EntityType<? extends GeckoEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static @Nonnull
    AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 4.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.33F)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    // Initialise Gecko

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(FLAGS, 0);
        this.dataManager.register(ARMOR_TYPE, 0);
        this.dataManager.register(COMMAND, 0);
        this.dataManager.register(STANDING, Boolean.FALSE);
        this.dataManager.register(SITTING, Boolean.FALSE);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new SitGoal(this));
        this.goalSelector.addGoal(1, new EndGeckoEntity.MateGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new EndGeckoEntity.LayEggGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1.5D, 10.0F, 2.0F, true));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(10, new LeapAtTargetGoal(this, 0.4f));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        if (!this.isTamed()) {
            this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        }
    }

    @Override
    protected void updateAITasks() {
        if (this.isInWaterOrBubbleColumn() && this.getArmor() <= 0) {
            ++this.underWaterTicks;
        } else {
            this.underWaterTicks = 0;
        }
        if (this.underWaterTicks > 5) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }
        if (this.world.isRaining() && this.getArmor() <= 0) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }
    }


    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
            this.setHealth(16.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6.0D);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable
    ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT tag) {
        setSkinColor(getRandomEndGeckoColor(rand));
        return super.onInitialSpawn(world, difficultyIn, reason, spawnData == null ? new AgeableEntity.AgeableData(1.0F) : spawnData, tag);
    }


    public static EndGeckoEntity.SkinColors getRandomEndGeckoColor(@Nonnull Random random) {
        int i = random.nextInt(2);

        if (i <= 1) {
            return SkinColors.BLACKEND;
        } else {
            return SkinColors.PURPLEEND;

        }
    }

    // End initialising


    @Override
    public Item getTameItem() {
        return GItems.CHORUS_GECKO_FOOD;
    }


    @ParametersAreNonnullByDefault
    @Override
    public @Nullable
    EndGeckoEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
        @Nullable EndGeckoEntity gecko = GEntities.ENDGECKO.create(world);
        UUID uuid = this.getOwnerId();
        if (uuid != null) {
            gecko.setOwnerId(uuid);
            gecko.setTamed(true);
        }
        if (gecko != null) gecko.setSkinColor(getRandomEndGeckoColor(gecko.rand));

        return gecko;
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) applyEnchantments(this, entityIn);
        if (flag && this.getHeldItemMainhand().isEmpty() && entityIn instanceof LivingEntity && entityIn != this.getOwner()) {
            float f = this.world.getDifficultyForLocation(this.getPosition()).getAdditionalDifficulty();
            entityIn.teleportKeepLoaded(entityIn.getPosX() + 3, entityIn.getPosY(), entityIn.getPosZ() - 5);
        }

        return flag;
    }

    public EndGeckoEntity.SkinColors getEndSkinColor() {
        return EndGeckoEntity.SkinColors.byIndex((getRawFlag() >> 16) & Byte.MAX_VALUE);
    }

    public int getRawFlag() {
        return dataManager.get(FLAGS);
    }

    public void setFlags(@Nonnull EndGeckoEntity.SkinColors color, boolean climbing) {
        setRawFlag(
                (color.ordinal() & Byte.MAX_VALUE) << 16 |
                        (climbing ? 1 : 0) << 8
        );
    }

    public void setRawFlag(int flag) {
        dataManager.set(FLAGS, flag);
    }

    public void setSkinColor(@Nonnull SkinColors color) {
        setFlags(color, isClimbing());
    }


    public enum SkinColors {
        BLACKEND(),
        PURPLEEND();

        public static EndGeckoEntity.SkinColors byIndex(int index) {
            return Maths.get(EndGeckoEntity.SkinColors.values(), index);
        }
    }

    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }


    static class MateGoal extends BreedGoal {
        private final GeckoEntity gecko;

        MateGoal(GeckoEntity gecko, double speedIn) {
            super(gecko, speedIn);
            this.gecko = gecko;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !this.gecko.hasEgg();
        }

        protected void spawnBaby() {
            ServerPlayerEntity serverplayerentity = this.animal.getLoveCause();
            if (serverplayerentity == null && this.targetMate.getLoveCause() != null) {
                serverplayerentity = this.targetMate.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.addStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.targetMate, null);
            }

            this.gecko.setHasEgg(true);
            this.animal.resetInLove();
            this.targetMate.resetInLove();
            Random random = this.animal.getRNG();
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.addEntity(new ExperienceOrbEntity(this.world, this.animal.getPosX(), this.animal.getPosY(), this.animal.getPosZ(), random.nextInt(7) + 1));
            }
        }
    }

    static class LayEggGoal extends MoveToBlockGoal {
        private final GeckoEntity gecko;

        LayEggGoal(GeckoEntity gecko, double speedIn) {
            super(gecko, speedIn, 16);
            this.gecko = gecko;
        }

        public boolean shouldExecute() {
            return this.gecko.hasEgg();
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.gecko.hasEgg();
        }

        public void tick() {
            super.tick();
            BlockPos blockpos = this.gecko.getPosition();
            World world = this.gecko.world;
            world.setBlockState(blockpos, GBlocks.END_GECKO_EGG_BLOCK.getDefaultState());
            this.gecko.setHasEgg(false);
            this.gecko.setInLove(600);
        }

        @Override
        protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
            return !worldIn.isAirBlock(pos.up());
        }
    }
}
