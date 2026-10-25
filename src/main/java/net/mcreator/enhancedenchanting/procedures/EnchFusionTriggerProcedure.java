package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.neoforged.neoforge.network.PacketDistributor;
import net.mcreator.enhancedenchanting.network.FusionAdvancementPacketMessage;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.client.Minecraft;
import java.util.Map;

import net.mcreator.enhancedenchanting.EnchantmentFusion;
import net.mcreator.enhancedenchanting.EnchantmentFusionEvent;

import javax.annotation.Nullable;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(value = {Dist.CLIENT})
public class EnchFusionTriggerProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, Minecraft.getInstance().level, event.getItemStack(), event.getEntity());
	}

	public static void execute(LevelAccessor world, ItemStack itemstack, Entity entity) {
		execute(null, world, itemstack, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, ItemStack itemstack, Entity entity) {
		if (world == null) return;
		var registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		for (EnchantmentFusion fusion : EnchantmentFusion.fusions.values()) {
			if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(fusion.result())))) != 0)
				continue;
			if (
				(itemstack.getEnchantmentLevel(registry.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(fusion.element_1())))) != 0) &&
				(itemstack.getEnchantmentLevel(registry.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(fusion.element_2())))) != 0)){
					NeoForge.EVENT_BUS.post(new EnchantmentFusionEvent(itemstack, entity, fusion));
			}
		}
	}
}