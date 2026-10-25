package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

@EventBusSubscriber
public class CurseOfIrreparabilityDoProcedure {
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		execute(event, event.getPlayer().level(), event.getLeft(),event.getRight(), event.getOutput());
	}

	public static void execute(LevelAccessor world, ItemStack leftItem, ItemStack rightItem, ItemStack output) {
		execute(null, world, leftItem, rightItem, output);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, ItemStack leftItem, ItemStack rightItem, ItemStack output) {
		ItemStack new_slot = ItemStack.EMPTY;
		if (leftItem.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:curse_of_irreparability")))) != 0
				&& leftItem.getItem().isValidRepairItem(leftItem, rightItem)) {
			if (event instanceof ICancellableEvent _cancellable) {
				_cancellable.setCanceled(true);
			}
		}
	}
}