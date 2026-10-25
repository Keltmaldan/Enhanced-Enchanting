package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class VeinMinerDoProcedure {
	private static final int MAX = 100;
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		LivingEntity entity = event.getPlayer();
		Level world = entity.level();
		if (entity.getMainHandItem()
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:vein_miner")))) != 0) {
			execute(event, world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getState(), null, 0);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		execute(null, world, x, y, z, blockstate, null, 0);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, @Nullable Direction dir, int i) {
		if ((BuiltInRegistries.BLOCK.getKey(blockstate.getBlock()).toString()).contains("ore")) {
			if (i <= MAX)
			for (Direction directioniterator : Direction.values()) {
				if (directioniterator.equals(dir))
					continue;
				double xs = x + directioniterator.getStepX();
				double ys = y + directioniterator.getStepY();
				double zs = z + directioniterator.getStepZ();
				BlockState bs = (world.getBlockState(BlockPos.containing(xs, ys, zs)));
				if (bs.getBlock() == blockstate.getBlock()) {
					{
						BlockPos _pos = BlockPos.containing(xs, ys, zs);
						Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(xs, ys, zs), null);
						world.destroyBlock(_pos, false);
						execute(null, world, xs, ys, zs, bs, directioniterator.getOpposite(), i + 1);
					}
				}
			}
		}
	}
}