package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class CurseOfLaggingDoProcedure {
	@SubscribeEvent
	public static void onClientTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player == null)
			return;
		Level world = player.level();
		if (world == null)
			return;
		boolean lag = player.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:curse_of_lagging")))) != 0;
		if (lag)
			RenderSystem.limitDisplayFPS(20);
			
	}
}