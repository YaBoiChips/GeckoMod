package yaboichips.geckomod.common.entities;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import yaboichips.geckomod.core.GBlocks;
import yaboichips.geckomod.core.GEntities;
import yaboichips.geckomod.core.GItems;
import yaboichips.geckomod.core.GSounds;
import yaboichips.geckomod.util.GKeyBinds;
import yaboichips.geckomod.util.Maths;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.UUID;

public class GeckoEntity extends TameableEntity implements IRideable{
    private static final DataParameter<Integer> FLAGS = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ARMOR_TYPE = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> STANDING = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GIANT = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SETGIANT = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.createKey(GeckoEntity.class, DataSerializers.BOOLEAN);
    public static final EntitySize GECKO_SIZE = EntitySize.flexible(0.4f,0.4f);
    public static final EntitySize GIANT_SIZE = EntitySize.flexible(1.9f,1.2f);
    private final BoostHelper boostHelper = new BoostHelper(this.dataManager, COMMAND, GIANT);
    public boolean forcedSit = false;
    public boolean forcedGiant = false;

    public GeckoEntity(EntityType<? extends GeckoEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static @Nonnull
    AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 4.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.33F)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D);
    }



    // Initialise Gecko

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(FLAGS, 0);
        this.dataManager.register(ARMOR_TYPE, 0);
        this.dataManager.register(COMMAND, 0);
        this.dataManager.register(SETGIANT, 0);
        this.dataManager.register(HAS_EGG, false);
        this.dataManager.register(GIANT, Boolean.FALSE);
        this.dataManager.register(STANDING, Boolean.FALSE);
        this.dataManager.register(SITTING, Boolean.FALSE);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new GeckoEntity.MateGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new GeckoEntity.LayEggGoal(this, 1.0D));
        this.goalSelector.addGoal(0, new SitGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1.5D, 10.0F, 2.0F, true));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(10, new LeapAtTargetGoal(this, 0.4f));
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, BeeEntity.class, false));
    }

    @Override
    public boolean isElytraFlying() {
        return this.isGiant() && !this.isOnGround();
    }

    public boolean hasEgg() {
        return this.dataManager.get(HAS_EGG);
    }

    void setHasEgg(boolean hasEgg) {
        this.dataManager.set(HAS_EGG, hasEgg);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
            if (this.isGiant() && this.isLiving()) {
                return GIANT_SIZE;
            } else {
                return GECKO_SIZE;
            }
    }
    @Override
    public void livingTick() {
        if (this.world.isRemote) {
            if (this.isGiant() && this.isBeingRidden()) {
                if (GKeyBinds.GECKO_FLY_KEY.isPressed()) {
                    Vector3d vec = this.getMotion();
                    this.setVelocity(vec.x, 0.5, vec.z);
                }
            }
            if (this.isGiant()) {
                this.recalculateSize();
            }
            if (!this.isGiant()) {
                this.recalculateSize();
            }
            if (this.isPotionActive(Effects.POISON)) {
                this.removePotionEffect(Effects.POISON);
            }
        } super.livingTick();
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
            return false;
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return super.canBeLeashedTo(player);
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

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }


    public Item getTameItem(){
        return GItems.GECKO_FOOD;
    }



    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        ActionResultType type = super.func_230254_b_(player, hand);

        if (this.world.isRemote) {
            boolean flag = this.isOwner(player) || this.isTamed() || item == this.getTameItem() && !this.isTamed();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        } else {
            if (this.isTamed()) {
                if (this.isBreedingItem(stack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    this.heal((float) item.getFood().getHealing());
                    return ActionResultType.SUCCESS;
                }
            } else if (item == this.getTameItem()) {
                if (!player.abilities.isCreativeMode) {
                    stack.shrink(1);
                }
                if (this.rand.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget((LivingEntity) null);
                    this.func_233687_w_(true);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.world.setEntityState(this, (byte) 6);
                }
                return ActionResultType.SUCCESS;
            }
            if (type != ActionResultType.SUCCESS && isTamed() && isOwner(player) && !isBreedingItem(stack) && item != GItems.GECKO_STAFF) {
                if (!player.isSneaking()) {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() == 2) {
                        this.setCommand(0);
                    }
                    player.sendStatusMessage(new TranslationTextComponent("entity.gecko.command_" + this.getCommand(), this.getName()), true);
                    boolean sit = this.getCommand() == 1;
                    if (sit) {
                        this.forcedSit = true;
                        this.setSitting(true);
                        return ActionResultType.SUCCESS;
                    } else {
                        this.forcedSit = false;
                        this.setSitting(false);
                        return ActionResultType.SUCCESS;
                    }
                }
            } if (type != ActionResultType.SUCCESS && isTamed() && isOwner(player) && item == GItems.GIANT_GECKO_SERUM) {
                if (player.isSneaking()) {
                    this.setSetGiant(this.getSetGiant() + 1);
                    player.sendStatusMessage(new TranslationTextComponent("Made Giant"), true);
                    if (this.getSetGiant() == 2)
                        this.setSetGiant(0);
                }

                boolean giant = this.getSetGiant() == 1;
                if (giant) {
                    this.forcedGiant = true;
                    this.setGiant(true);
                    return ActionResultType.SUCCESS;
                } else {
                    this.forcedGiant = false;
                    this.setGiant(false);
                    return ActionResultType.SUCCESS;
                }
            }

            if (type != ActionResultType.SUCCESS && item == GItems.IRON_GECKO_ARMOR && this.isTamed() && this.getArmor() < 1) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(1);
                if (this.getArmor() == 1) {
                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
                        this.setHealth(20.0F);
                    }
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            }
            if (type != ActionResultType.SUCCESS && item == GItems.GOLD_GECKO_ARMOR && this.isTamed() && this.getArmor() < 2) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(2);
                if (this.getArmor() == 2) {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(9.0D);
                    this.setHealth(18.0F);
                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            }
            if (type != ActionResultType.SUCCESS && item == GItems.DIAMOND_GECKO_ARMOR && this.isTamed() && this.getArmor() < 3) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(3);
                if (this.getArmor() == 3) {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0D);
                    this.setHealth(24.0F);
                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            }
            if (type != ActionResultType.SUCCESS && item == GItems.NETHERITE_GECKO_ARMOR && this.isTamed() && this.getArmor() < 4) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(4);
                if (this.getArmor() == 4) {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
                    this.setHealth(30.0F);
                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            }
            if (type != ActionResultType.SUCCESS && item == GItems.GECKO_STAFF && this.isTamed() && !this.isBeingRidden() && this.isGiant()){
                if (!this.world.isRemote) {
                    player.startRiding(this, true);
                }
            }
        }
            return type;
    }

    @Override
    public Vector3d func_230268_c_(LivingEntity livingEntity) {
        Direction direction = this.getAdjustedHorizontalFacing();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.func_230268_c_(livingEntity);
        } else {
            int[][] aint = TransportationHelper.func_234632_a_(direction);
            BlockPos blockpos = this.getPosition();
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

            for(Pose pose : livingEntity.getAvailablePoses()) {
                AxisAlignedBB axisalignedbb = livingEntity.getPoseAABB(pose);

                for(int[] aint1 : aint) {
                    blockpos$mutable.setPos(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.world.func_242403_h(blockpos$mutable);
                    if (TransportationHelper.func_234630_a_(d0)) {
                        Vector3d vector3d = Vector3d.copyCenteredWithVerticalOffset(blockpos$mutable, d0);
                        if (TransportationHelper.func_234631_a_(this.world, livingEntity, axisalignedbb.offset(vector3d))) {
                            livingEntity.setPose(pose);
                            return vector3d;
                        }
                    }
                }
            }
            return super.func_230268_c_(livingEntity);
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable
    ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT tag) {
        setSkinColor(getRandomGeckoColor(rand));
        return super.onInitialSpawn(world, difficultyIn, reason, spawnData == null ? new AgeableEntity.AgeableData(1.0F) : spawnData, tag);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GSounds.GECKO_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return GSounds.GECKO_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return GSounds.GECKO_DEATH;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason spawnReason) {
        return super.canSpawn(world, spawnReason);
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 12;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return !this.isTamed();
    }

    public int getArmor() {
       return this.dataManager.get(ARMOR_TYPE);}

    public void setArmor(int armor) {
        this.dataManager.set(ARMOR_TYPE, armor);
    }

    public void setCommand(int command) {
        this.dataManager.set(COMMAND, command);
    }

    public int getCommand() {
        return this.dataManager.get(COMMAND);
    }

    public void setSetGiant(int setGiant) {
        this.dataManager.set(SETGIANT, setGiant);
    }

    public int getSetGiant() {
        return this.dataManager.get(SETGIANT);
    }

    public void setSitting(boolean sit) {
        this.dataManager.set(SITTING, sit);
    }

    public boolean isSitting() {
        return this.dataManager.get(SITTING);
    }

    public void setGiant(boolean giant) {
        this.dataManager.set(GIANT, giant);
    }

    public boolean isGiant() {
        return this.dataManager.get(GIANT);
    }

    public static SkinColors getRandomGeckoColor(@Nonnull Random random) {
        int i = random.nextInt(13);

        if (i <= 0) {
            return SkinColors.GREEN;
        } else if (i <= 3) {
            return SkinColors.WHITE;
        } else if (i <= 5) {
            return SkinColors.BLACK;
        } else if (i <= 7) {
            return SkinColors.ORANGE;
        } else if (i <= 9){
            return SkinColors.BLUE;
        } else if (i <= 10) {
            return SkinColors.RED;
        } else {
            return SkinColors.BROWN;
        }
    }

    // End initialising

    @Override
    public void tick() {
        super.tick();
        setClimbing(!world.isRemote && collidedHorizontally);

    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        return stack.getItem() == Items.APPLE;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof PlayerEntity)) {
            super.collideWithEntity(entityIn);
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable GeckoEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
        @Nullable GeckoEntity gecko = GEntities.GECKO.create(world);
        UUID uuid = this.getOwnerId();
        if (uuid != null) {
            gecko.setOwnerId(uuid);
            gecko.setTamed(true);
        }
        if (gecko != null) gecko.setSkinColor(getRandomGeckoColor(gecko.rand));

        return gecko;
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)getAttributeValue(Attributes.ATTACK_DAMAGE)));

        if (flag) applyEnchantments(this, entityIn);

        return flag;
    }

    @Override
    public boolean isOnLadder() {
        return isClimbing();
    }

    // Write to Nbt

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Flag", this.getRawFlag());
        compound.putInt("Armor", this.getArmor());
        compound.putBoolean("GeckoSitting", this.isSitting());
        compound.putBoolean("GeckoGiant", this.isGiant());
        compound.putBoolean("HasEgg", this.hasEgg());
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putInt("GeckoCommand", this.getCommand());
        compound.putInt("SetGiant", this.getSetGiant());
        compound.putBoolean("ForcedGiant", this.forcedGiant);

    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.setRawFlag(compound.getInt("Flag"));
        this.setArmor(compound.getInt("Armor"));
        this.setSitting(compound.getBoolean("GeckoSitting"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.forcedGiant = compound.getBoolean("ForcedGiant");
        this.setHasEgg(compound.getBoolean("HasEgg"));
        this.setGiant(compound.getBoolean("GeckoGiant"));
        this.setCommand(compound.getInt("GeckoCommand"));
        this.setSetGiant(compound.getInt("SetGiant"));
    }

    public SkinColors getSkinColor() {
        return SkinColors.byIndex((getRawFlag() >> 16) & Byte.MAX_VALUE);
    }

    public boolean isClimbing() {
        return ((getRawFlag() >> 8) & Byte.MAX_VALUE) > 0;
    }

    public void setSkinColor(@Nonnull SkinColors color) {
        setFlags(color, isClimbing());
    }

    public void setClimbing(boolean value) {
        setFlags(getSkinColor(), value);
    }

    public int getRawFlag() {
        return dataManager.get(FLAGS);
    }

    public void setFlags(@Nonnull SkinColors color, boolean climbing) {
        setRawFlag((color.ordinal() & Byte.MAX_VALUE) << 16 | (climbing ? 1 : 0) << 8);
    }

    public void setRawFlag(int flag) {
        dataManager.set(FLAGS, flag);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        Entity entity = this.getControllingPassenger();
        if (!(entity instanceof PlayerEntity)) {
            return false;
        } else {
            PlayerEntity playerentity = (PlayerEntity)entity;
            return playerentity.getHeldItemMainhand().getItem() == GItems.GECKO_STAFF || playerentity.getHeldItemOffhand().getItem() == GItems.GECKO_STAFF;
        }
    }

    public void travel(Vector3d travelVector) {
        this.ride(this, this.boostHelper, travelVector);
    }

    @Override
    public boolean boost() {
        return false;
    }

    @Override
    public void travelTowards(Vector3d travelVec) {
        super.travel(travelVec);
    }

    @Override
    public float getMountedSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.525F;
    }


    // End write to nbt


    public enum SkinColors {
        WHITE(),
        BLUE(),
        GREEN(),
        BROWN(),
        BLACK(),
        RED(),
        ORANGE();

        public static SkinColors byIndex(int index) {
            return Maths.get(SkinColors.values(), index);
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
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.targetMate, (AgeableEntity)null);
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
            world.setBlockState(blockpos, GBlocks.GECKO_EGG_BLOCK.getDefaultState());
            this.gecko.setHasEgg(false);
            this.gecko.setInLove(600);
        }

        @Override
        protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
            return !worldIn.isAirBlock(pos.up());
        }
    }
}