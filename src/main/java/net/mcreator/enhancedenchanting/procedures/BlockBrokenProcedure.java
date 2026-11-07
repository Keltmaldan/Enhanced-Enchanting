package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import javax.annotation.Nullable;

import java.util.function.Function;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HoeItem;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.Direction.Axis;

@EventBusSubscriber
public class BlockBrokenProcedure {
	
	private static int getLevel(ItemStack stack, RegistryLookup<Enchantment> ref, String id) {
		return stack.getEnchantmentLevel(ref.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(id))));
	}
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		BlockPos pos = event.getPos();
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		Player entity = event.getPlayer();
		if (entity == null)
			return;
		Level world = entity.level();
		ItemStack stack = entity.getMainHandItem();
		Item item = stack.getItem();
		RegistryLookup<Enchantment> ref = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		BlockState blockstate = world.getBlockState(pos);

		if (getLevel(stack, ref, "enhanced_enchanting:big_hole") != 0) {
			var tag = switch (item) {
				case AxeItem axe -> BlockTags.create(ResourceLocation.parse("mineable/axe".toLowerCase(java.util.Locale.ENGLISH)));
				case PickaxeItem pick -> BlockTags.create(ResourceLocation.parse("mineable/pickaxe".toLowerCase(java.util.Locale.ENGLISH)));
				case ShovelItem shovel -> BlockTags.create(ResourceLocation.parse("mineable/shovel".toLowerCase(java.util.Locale.ENGLISH)));
				case HoeItem hoe -> BlockTags.create(ResourceLocation.parse("mineable/hoe".toLowerCase(java.util.Locale.ENGLISH)));
				default -> null;
			};
			Axis axis = world.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(100)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getDirection().getAxis();
			if (tag != null)
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 || j != 0) {
						if (/*entity.getXRot() > 40 || entity.getXRot() < -40*/ axis == Direction.Axis.Y) {
							BlockPos _pos = BlockPos.containing(x + i, y, z + j);
							if ((world.getBlockState(_pos).is(tag))) {
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
								world.destroyBlock(_pos, false);
							}
						} else if (axis == Direction.Axis.Z) {
							BlockPos _pos = BlockPos.containing(x + i, y + j, z);
							if ((world.getBlockState(_pos).is(tag))) {		
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
								world.destroyBlock(_pos, false);
							}
						} else if (axis == Direction.Axis.X) {
							BlockPos _pos = BlockPos.containing(x, y + j, z + i);
							if ((world.getBlockState(_pos).is(tag))) {
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
								world.destroyBlock(_pos, false);
							}
						}
					}
				}
			}
		}
		int treeBrkrLvl = getLevel(stack, ref, "enhanced_enchanting:tree_breaker");
		if (treeBrkrLvl != 0) {
			createTimedLoop(2 * treeBrkrLvl, 1, _timedLoop -> {
						if ((world.getBlockState(BlockPos.containing(x, y + _timedLoop.iterator, z))) == blockstate) {
							{
								BlockPos _pos = BlockPos.containing(x, y + _timedLoop.iterator, z);
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y + _timedLoop.iterator, z), null);
								world.destroyBlock(_pos, false);
							}
						} else {
							if (true)
								return false;
						}
						return true;
					});
		}
	}

	private static class EnhancedEnchantingModTimedLoop {
		private int iterator = 0;
		private int ticks;
		private final int total;
		private final Function<EnhancedEnchantingModTimedLoop, Boolean> forEach;

		private EnhancedEnchantingModTimedLoop(int total, int ticks, Function<EnhancedEnchantingModTimedLoop, Boolean> forEach) {
			this.total = total;
			this.ticks = ticks;
			this.forEach = forEach;
			run();
		}

		private void run() {
			if (forEach.apply(this))
				EnhancedEnchantingMod.queueServerWork(ticks, this::next);
		}

		private void next() {
			if (total > ++iterator)
				run();
		}
	}

	private static EnhancedEnchantingModTimedLoop createTimedLoop(int total, int ticks, Function<EnhancedEnchantingModTimedLoop, Boolean> forEach) {
		return new EnhancedEnchantingModTimedLoop(total, ticks, forEach);
	}	
}