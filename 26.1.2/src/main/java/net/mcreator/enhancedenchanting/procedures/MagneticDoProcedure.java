package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber
public class MagneticDoProcedure {

    private static final Identifier MAGNETIC_ID =
            Identifier.parse("enhanced_enchanting:magnetic");

    // All standard tagged blocks share one strength; iron ores are 1/9 as strong
    private static final TagKey<Block> MAGNETIC_BLOCKS =
            BlockTags.create(Identifier.parse("enhanced_enchanting:valid_magnetic_block"));
    private static final TagKey<Block> MINECRAFT_ORES =
            BlockTags.create(Identifier.parse("minecraft:ores"));
    private static final TagKey<Block> COMMON_ORES =
            BlockTags.create(Identifier.parse("c:ores"));
    private static final TagKey<Block> COMMON_IRON_ORES =
            BlockTags.create(Identifier.parse("c:ores/iron"));
    private static final TagKey<Block> EXTRA_IRON_ORES =
            BlockTags.create(Identifier.parse("enhanced_enchanting:magnetic_iron_ores"));

    private static final TagKey<Item> MAGNETIC_DROPS =
            TagKey.create(Registries.ITEM,
                    Identifier.parse("enhanced_enchanting:is_valid_magnetic_entity"));

    private static final TagKey<Item> LEGACY_MAGNETIC_DROPS =
            TagKey.create(Registries.ITEM,
                    Identifier.parse("enhanced_enchanting:magnetic_items"));

    private static final EquipmentSlot[] ARMOR = {
            EquipmentSlot.FEET, EquipmentSlot.LEGS,
            EquipmentSlot.CHEST, EquipmentSlot.HEAD
    };

    private static final int BLOCK_RADIUS = 6;
    private static final int BLOCK_VERTICAL_RADIUS = 3;
    private static final int BLOCK_SCAN_INTERVAL = 6;
    private static final double GOLEM_RADIUS = 7.0;
    private static final double BLOCK_PULL = 0.040;
    private static final double GOLEM_RELATIVE_PULL = 0.45;
    private static final double ORE_RELATIVE_PULL = 1.0 / 9.0;
    private static final double DROPPED_ITEM_PULL = 0.045;
    private static final double SPRINT_MULTIPLIER = 0.16;

    // Weak keys avoid retaining unloaded entities. Client and server entities are
    // separate keys; synchronization protects the shared static cache
    private static final Map<LivingEntity, BlockPos> CACHED_BLOCKS =
            Collections.synchronizedMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LivingEntity wearer)) return;
        if (!wearer.isAlive() || wearer.isSpectator()) return;

        Level level = wearer.level();

        // Also predict the movement of the local player, avoiding server-side
        // corrections overriding the feeling of magnetic attraction
        if (level.isClientSide()
                && (!(wearer instanceof Player player) || !player.isLocalPlayer())) {
            return;
        }

        var enchantment = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, MAGNETIC_ID));

        int highestLevel = 0;
        for (EquipmentSlot slot : ARMOR) {
            ItemStack armor = wearer.getItemBySlot(slot);
            highestLevel = Math.max(highestLevel, armor.getEnchantmentLevel(enchantment));
        }

        if (highestLevel <= 0) {
            CACHED_BLOCKS.remove(wearer);
            return;
        }
        
        double power = 1.0 + Math.log(highestLevel);

        if (wearer.tickCount % BLOCK_SCAN_INTERVAL == 0
                || !CACHED_BLOCKS.containsKey(wearer)) {
            CACHED_BLOCKS.put(wearer, findBestVisibleMagneticBlock(level, wearer));
        }

        Anchor anchor = findAnchor(level, wearer);
        if (anchor != null) {
            applyAnchorPull(wearer, anchor, power);
        }

        if (!(level instanceof ServerLevel server) || wearer.tickCount % 2 != 0) return;

        double itemRadius = Math.min(12.0, 6.0 + 2.0 * power);
        Vec3 destination = wearer.position().add(0, wearer.getBbHeight() * 0.45, 0);
        for (ItemEntity item : server.getEntitiesOfClass(ItemEntity.class,
                wearer.getBoundingBox().inflate(itemRadius),
                other -> other.isAlive() && !other.isSpectator()
                        && isMagneticItem(other.getItem()))) {
            if (item.distanceToSqr(wearer) > itemRadius * itemRadius) continue;
            pullToward(item, destination, DROPPED_ITEM_PULL * power, 0.50, 0.65);
        }
    }

    private record Anchor(Vec3 target, double relativeStrength, double stopDistance) {}

    private static Anchor findAnchor(Level level, LivingEntity wearer) {
        Anchor best = null;
        double bestScore = -1.0;

        BlockPos pos = CACHED_BLOCKS.get(wearer);
        if (pos != null && level.isLoaded(pos)
                && isMagneticBlock(level.getBlockState(pos))
                && isVisibleBlock(level, wearer, pos)) {
            Vec3 center = Vec3.atCenterOf(pos);
            double dx = center.x - wearer.getX();
            double dz = center.z - wearer.getZ();
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            double distance = wearer.position().distanceTo(center);

            if (horizontal > 0.50 && distance <= BLOCK_RADIUS + 1.0) {
                double relative = blockRelativeStrength(level.getBlockState(pos));
                best = new Anchor(center, relative, 0.30);
                bestScore = relative / (horizontal + 0.8);
            }
        }

        for (IronGolem golem : level.getEntitiesOfClass(IronGolem.class,
                wearer.getBoundingBox().inflate(GOLEM_RADIUS),
                other -> other.isAlive() && !other.isSpectator())) {
            double dx = golem.getX() - wearer.getX();
            double dz = golem.getZ() - wearer.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance > GOLEM_RADIUS || distance < 0.1) continue;

            double score = GOLEM_RELATIVE_PULL / (distance + 0.8);
            if (score > bestScore) {
                bestScore = score;
                best = new Anchor(golem.position(), GOLEM_RELATIVE_PULL, 0.75);
            }
        }

        return best;
    }

    private static void applyAnchorPull(LivingEntity wearer, Anchor anchor, double power) {
        Vec3 delta = anchor.target.subtract(wearer.position());
        Vec3 horizontal = new Vec3(delta.x, 0, delta.z);
        double distance = horizontal.length();
        if (distance <= anchor.stopDistance) return;

        Vec3 direction = horizontal.scale(1.0 / distance);
        boolean sprinting = wearer.isSprinting();
        double distanceFalloff = Math.min(1.0, 3.0 / Math.max(1.0, distance));
        double strength = BLOCK_PULL * power * anchor.relativeStrength * distanceFalloff;
        if (sprinting) {
            strength *= SPRINT_MULTIPLIER;
            Vec3 motion = wearer.getDeltaMovement();
            wearer.setDeltaMovement(motion.x * 0.99, motion.y, motion.z * 0.99);
        }

        double inwardSpeed = wearer.getDeltaMovement().dot(direction);
        double speedLimit = 0.15 + 0.025 * (power - 1.0);
        double impulse = Math.min(strength, speedLimit - inwardSpeed);
        if (impulse > 0.0) {
            wearer.push(direction.scale(impulse));
        }
    }

    private static BlockPos findBestVisibleMagneticBlock(Level level, LivingEntity wearer) {
        BlockPos origin = wearer.blockPosition();
        BlockPos selected = null;
        double bestScore = -1.0;

        for (int dx = -BLOCK_RADIUS; dx <= BLOCK_RADIUS; dx++) {
            for (int dy = -BLOCK_VERTICAL_RADIUS; dy <= BLOCK_VERTICAL_RADIUS; dy++) {
                for (int dz = -BLOCK_RADIUS; dz <= BLOCK_RADIUS; dz++) {
                    BlockPos pos = origin.offset(dx, dy, dz);
                    if (!level.isLoaded(pos)) continue;

                    BlockState state = level.getBlockState(pos);
                    if (!isMagneticBlock(state)) continue;

                    Vec3 center = Vec3.atCenterOf(pos);
                    double horizontal = Math.hypot(
                            center.x - wearer.getX(), center.z - wearer.getZ());
                    double full = wearer.position().distanceTo(center);
                    if (horizontal <= 0.50 || full > BLOCK_RADIUS + 1.0) continue;

                    double relative = blockRelativeStrength(state);
                    double score = relative / (horizontal + 0.8);
                    if (score <= bestScore) continue;

                    // This LOS test prevents solid walls / outer coverings
                    // from exposing an enclosed magnetic block to the wearer
                    if (!isVisibleBlock(level, wearer, pos)) continue;

                    bestScore = score;
                    selected = pos;
                }
            }
        }
        return selected;
    }

    private static boolean isVisibleBlock(Level level, LivingEntity wearer, BlockPos pos) {
        Vec3 center = Vec3.atCenterOf(pos);

        // Looking TOWARD the block isn't required. A visible physical face is
        // Test from eye level and torso height to handle low walls and ores
        Vec3 eyes = wearer.getEyePosition();
        Vec3 torso = wearer.position().add(0, wearer.getBbHeight() * 0.55, 0);
        return rayHitsBlock(level, wearer, eyes, center, pos)
                || rayHitsBlock(level, wearer, torso, center, pos);
    }

    private static boolean rayHitsBlock(Level level, LivingEntity wearer,
            Vec3 from, Vec3 to, BlockPos target) {
        BlockHitResult hit = level.clip(new ClipContext(
                from, to, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, wearer));
        return hit.getType() == HitResult.Type.BLOCK
                && hit.getBlockPos().equals(target)
                && !hit.isInside();
    }

    private static boolean isExplicitIronOre(BlockState state) {
        return state.is(BlockTags.IRON_ORES)
                || state.is(COMMON_IRON_ORES)
                || state.is(EXTRA_IRON_ORES);
    }

    private static boolean isIronOre(BlockState state) {
        if (isExplicitIronOre(state)) return true;

        // minecraft:ores is generic, not iron-only. Accept entries from this
        // broad tag only if ALSO magnetic-tagged or named as an iron ore
        if (state.is(MINECRAFT_ORES) || state.is(COMMON_ORES)) {
            String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
            return state.is(MAGNETIC_BLOCKS) || path.contains("iron");
        }
        return false;
    }

    private static boolean isMagneticBlock(BlockState state) {
        return state.is(MAGNETIC_BLOCKS)
                || state.is(Blocks.IRON_BLOCK)
                || isExplicitIronOre(state)
                || ((state.is(MINECRAFT_ORES) || state.is(COMMON_ORES))
                    && BuiltInRegistries.BLOCK.getKey(state.getBlock())
                            .getPath().contains("iron"));
    }

    private static double blockRelativeStrength(BlockState state) {
        return isIronOre(state) ? ORE_RELATIVE_PULL : 1.0;
    }

    private static boolean isMagneticItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.is(MAGNETIC_DROPS)
                || stack.is(LEGACY_MAGNETIC_DROPS)
                || stack.is(Items.IRON_INGOT)
                || stack.is(Items.IRON_NUGGET)
                || stack.is(Items.RAW_IRON)
                || stack.is(Items.IRON_BLOCK)
                || stack.is(Items.RAW_IRON_BLOCK)
                || stack.is(Items.IRON_SWORD)
                || stack.is(Items.IRON_PICKAXE)
                || stack.is(Items.IRON_AXE)
                || stack.is(Items.IRON_SHOVEL)
                || stack.is(Items.IRON_HOE)
                || stack.is(Items.IRON_HELMET)
                || stack.is(Items.IRON_CHESTPLATE)
                || stack.is(Items.IRON_LEGGINGS)
                || stack.is(Items.IRON_BOOTS);
    }

    private static void pullToward(Entity moving, Vec3 destination,
            double acceleration, double speedLimit, double stopDistance) {
        Vec3 difference = destination.subtract(moving.position());
        if (difference.lengthSqr() <= stopDistance * stopDistance) return;

        Vec3 direction = difference.normalize();
        double inwardSpeed = moving.getDeltaMovement().dot(direction);
        double impulse = Math.min(acceleration, speedLimit - inwardSpeed);
        if (impulse > 0.0) moving.push(direction.scale(impulse));
    }
}