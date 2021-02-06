package yaboichips.geckomod.common.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import yaboichips.geckomod.core.GEntities;
import yaboichips.geckomod.core.GItems;
import yaboichips.geckomod.util.Maths;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.UUID;

public class NetherGeckoEntity extends TameableEntity {
    private static final DataParameter<Integer> FLAGS = EntityDataManager.createKey(NetherGeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ARMOR_TYPE = EntityDataManager.createKey(NetherGeckoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> STANDING = EntityDataManager.createKey(NetherGeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(NetherGeckoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(NetherGeckoEntity.class, DataSerializers.VARINT);
    public boolean forcedSit = false;
    private int underWaterTicks;


    public NetherGeckoEntity(EntityType<? extends NetherGeckoEntity> type, World worldIn) {
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
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(0, new SitGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1.5D, 10.0F, 2.0F, true));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(10, new LeapAtTargetGoal(this, 0.4f));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }
    
    @Override
    protected void updateAITasks() {
        if (this.isInWaterOrBubbleColumn() && this.getArmor() <=0) {
            ++this.underWaterTicks;
        } else {
            this.underWaterTicks = 0;
        }
        if (this.underWaterTicks > 5) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }
        if (this.world.isRaining() && this.getArmor() <=0){
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }
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

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }



    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        ActionResultType type = super.func_230254_b_(player, hand);

        if (this.world.isRemote) {
            boolean flag = this.isOwner(player) || this.isTamed() || item == GItems.SPICY_GECKO_FOOD && !this.isTamed();
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
            } else if (item == GItems.SPICY_GECKO_FOOD) {
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
            if (type != ActionResultType.SUCCESS && isTamed() && isOwner(player) && !isBreedingItem(stack)) {
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
            }

            if (type != ActionResultType.SUCCESS && item == GItems.IRON_GECKO_ARMOR && this.isTamed()) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(1);
                if (this.getArmor() == 1){
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
                    this.setHealth(20.0F);
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            }
            if (type != ActionResultType.SUCCESS && item == GItems.GOLD_GECKO_ARMOR && this.isTamed()) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(2);
                if (this.getArmor() == 2) {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(9.0D);
                    this.setHealth(18.0F);
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            } if (type != ActionResultType.SUCCESS && item == GItems.DIAMOND_GECKO_ARMOR && this.isTamed()) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(3);
                if (this.getArmor() == 3) {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0D);
                    this.setHealth(24.0F);
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            } if (type != ActionResultType.SUCCESS && item == GItems.NETHERITE_GECKO_ARMOR && this.isTamed()) {
                player.sendStatusMessage(new TranslationTextComponent("Armor Applied"), true);
                this.setArmor(4);
                if (this.getArmor() == 4){
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
                    this.setHealth(30.0F);
                } else {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                    this.setHealth(16.0F);
                }
            }
        }
        return type;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable
    ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT tag) {
        setSkinColor(getRandomGeckoColor(rand));
        return super.onInitialSpawn(world, difficultyIn, reason, spawnData == null ? new AgeableEntity.AgeableData(1.0F) : spawnData, tag);
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

    public void setSitting(boolean sit) {
        this.dataManager.set(SITTING, sit);
    }

    public boolean isSitting() {
        return this.dataManager.get(SITTING);
    }


    public static NetherGeckoEntity.SkinColors getRandomGeckoColor(@Nonnull Random random) {
        int i = random.nextInt(2);

        if (i <= 1) {
            return SkinColors.REDNETHER;
        } else {
            return SkinColors.ORANGENETHER;

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

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable NetherGeckoEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
        @Nullable NetherGeckoEntity gecko = GEntities.NETHERGECKO.create(world);
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
        if (flag && this.getHeldItemMainhand().isEmpty() && entityIn instanceof LivingEntity && entityIn != this.getOwner()) {
            float f = this.world.getDifficultyForLocation(this.getPosition()).getAdditionalDifficulty();
            entityIn.setFire(8);
        }

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
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putInt("GeckoCommand", this.getCommand());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.setRawFlag(compound.getInt("Flag"));
        this.setArmor(compound.getInt("Armor"));
        this.setSitting(compound.getBoolean("GeckoSitting"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.setCommand(compound.getInt("GeckoCommand"));
    }

    public NetherGeckoEntity.SkinColors getSkinColor() {
        return NetherGeckoEntity.SkinColors.byIndex((getRawFlag() >> 16) & Byte.MAX_VALUE);
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
        setRawFlag(
                (color.ordinal() & Byte.MAX_VALUE) << 16 |
                        (climbing ? 1 : 0) << 8
        );
    }

    public void setRawFlag(int flag) {
        dataManager.set(FLAGS, flag);
    }

    // End write to nbt


    public enum SkinColors {
        REDNETHER(),
        ORANGENETHER();

        public static SkinColors byIndex(int index) {
            return Maths.get(SkinColors.values(), index);
        }
    }
}
