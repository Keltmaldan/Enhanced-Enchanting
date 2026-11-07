package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber
public class BlockBrokenProcedure {
    private static final ThreadLocal<Boolean> EXTRA_BREAK =
        ThreadLocal.withInitial(() -> false);

    private static final int MAX_EXTRA_BLOCKS = 128;
    private static final TagKey<Block> COMMON_ORES = TagKey.create(Registries.BLOCK, Identifier.parse("c:ores"));

    private record OreNode(BlockPos pos, BlockState state) { }

    private static int levelOf(ItemStack stack, RegistryLookup<Enchantment> registry, String id) {
        if (stack.isEmpty()) return 0;
        return stack.getEnchantmentLevel(registry.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse(id))));
    }

    private static TagKey<Block> appropriateMineableTag(ItemStack tool) {
        if (tool.is(ItemTags.PICKAXES)) return BlockTags.MINEABLE_WITH_PICKAXE;
        if (tool.is(ItemTags.AXES)) return BlockTags.MINEABLE_WITH_AXE;
        if (tool.is(ItemTags.SHOVELS)) return BlockTags.MINEABLE_WITH_SHOVEL;
        if (tool.is(ItemTags.HOES)) return BlockTags.MINEABLE_WITH_HOE;
        return null;
    }

    private static boolean validExtra(ServerLevel world, BlockPos pos, TagKey<Block> mineable) {
        if (!world.isInWorldBounds(pos) || !world.hasChunkAt(pos)) return false;
        BlockState state = world.getBlockState(pos);
        return !state.isAir() && state.getDestroySpeed(world, pos) >= 0 && state.is(mineable);
    }

    private static boolean isOre(BlockState state) {
        String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        return state.is(COMMON_ORES) || path.endsWith("_ore") || path.equals("ancient_debris");
    }

    private static String oreKind(BlockState state) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String path = id.getPath();
        if (path.startsWith("deepslate_")) path = path.substring("deepslate_".length());
        return id.getNamespace() + ":" + path;
    }

    private static boolean breakExtraBlock(ServerPlayer player, ServerLevel world, BlockPos pos) {
        if (EXTRA_BREAK.get()) return false;
        EXTRA_BREAK.set(true);
        try {
            return player.gameMode.destroyBlock(pos);
        } finally {
            EXTRA_BREAK.set(false);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        if (event.isCanceled() || EXTRA_BREAK.get()) return;
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel world)) return;
        if (player.getAbilities().instabuild) return;

        ItemStack originalTool = player.getMainHandItem();
        if (originalTool.isEmpty()) return;

        RegistryLookup<Enchantment> registry = world.registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT);

        int bigHole = levelOf(originalTool, registry, "enhanced_enchanting:big_hole");
        int veinMiner = levelOf(originalTool, registry, "enhanced_enchanting:vein_miner");
        int treeBreaker = levelOf(originalTool, registry, "enhanced_enchanting:tree_breaker");
        if (bigHole == 0 && veinMiner == 0 && treeBreaker == 0) return;
        	BlockPos origin = event.getPos().immutable();
        	BlockState originState = event.getState();
        TagKey<Block> mineable = appropriateMineableTag(originalTool);

        // Delay one tick: the original block must actually have broken before we expand. This avoids mining extras when a later listener cancels
        EnhancedEnchantingMod.queueServerWork(1, () -> {
            if (player.isRemoved() || player.level() != world) return;
            if (player.getMainHandItem() != originalTool || originalTool.isEmpty()) return;
            if (!world.hasChunkAt(origin) || world.getBlockState(origin).equals(originState)) return;

            if (mineable != null && (bigHole > 0 || veinMiner > 0)) {
                expandMining(world, player, origin, originState, originalTool,
                    mineable, bigHole > 0, veinMiner > 0);
            }
            if (treeBreaker > 0) {
                scheduleTreeBreaker(world, player, origin, originState, originalTool,
                    1, treeBreaker * 2);
            }
        });
    }

    private static void expandMining(ServerLevel world, ServerPlayer player, BlockPos origin, BlockState originState, ItemStack tool, TagKey<Block> mineable, boolean bigHole, boolean veinMiner) {
        LinkedHashSet<BlockPos> targets = new LinkedHashSet<>();
        ArrayDeque<OreNode> oreFrontier = new ArrayDeque<>();
        Set<BlockPos> visitedOres = new HashSet<>();

        boolean canVeinMine = veinMiner && tool.is(ItemTags.PICKAXES);
        if (canVeinMine && isOre(originState)) {
            oreFrontier.addLast(new OreNode(origin, originState));
            visitedOres.add(origin);
        }

        if (bigHole) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos pos = origin.offset(dx, dy, dz);
                        if (!validExtra(world, pos, mineable)) continue;
                        targets.add(pos);
                        	BlockState state = world.getBlockState(pos);
                        if (canVeinMine && isOre(state) && visitedOres.add(pos)) {
                            oreFrontier.addLast(new OreNode(pos, state));
                        }
                    }
                }
            }
        }

        // Expand every ore seeded by the original block and/or the Big Hole
        // Six-face adjacency, bounded to avoid large recursive lag spikes
        while (canVeinMine && !oreFrontier.isEmpty()
            && targets.size() < MAX_EXTRA_BLOCKS) {

            OreNode node = oreFrontier.removeFirst();
            String kind = oreKind(node.state());
            for (Direction direction : Direction.values()) {
                BlockPos next = node.pos().relative(direction);
                if (visitedOres.contains(next) || !validExtra(world, next, mineable)) continue;
                	BlockState state = world.getBlockState(next);
                if (!isOre(state) || !kind.equals(oreKind(state))) continue;

                visitedOres.add(next);
                targets.add(next);
                oreFrontier.addLast(new OreNode(next, state));
                if (targets.size() >= MAX_EXTRA_BLOCKS) break;
            }
        }

        for (BlockPos pos : targets) {
            if (player.isRemoved() || player.level() != world || player.getMainHandItem() != tool || tool.isEmpty()) break;
            if (validExtra(world, pos, mineable)) breakExtraBlock(player, world, pos);
        }
    }

    private static void scheduleTreeBreaker(ServerLevel world, ServerPlayer player,
        BlockPos origin, BlockState originalState, ItemStack tool,
        int step, int maximum) {
        if (step > maximum) return;
        EnhancedEnchantingMod.queueServerWork(1, () -> {
            if (player.isRemoved() || player.level() != world || player.getMainHandItem() != tool || tool.isEmpty()) return;
            BlockPos pos = origin.above(step);
            if (!world.isInWorldBounds(pos) || !world.hasChunkAt(pos)) return;
            if (!world.getBlockState(pos).equals(originalState)) return;
            if (!breakExtraBlock(player, world, pos)) return;
            scheduleTreeBreaker(world, player, origin, originalState, tool, step + 1, maximum);
        });
    }
}