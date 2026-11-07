package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import javax.annotation.Nullable;

@EventBusSubscriber
public class EnchantmentsCatalystDoProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, Entity sourceentity, double amount) {
		execute(null, world, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity sourceentity, double amount) {
		if (sourceentity == null)
			return;
		double increasing = 0;
		increasing = 1;
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:enchantments_catalyst")))) != 0) {
			{
				ItemStack _redwire$itemstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
				ItemEnchantments _appliedEnchantments = _redwire$itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
				for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
					String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
					increasing = increasing + 0.05 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:enchantments_catalyst"))));
				}
			}
			if (event instanceof LivingIncomingDamageEvent _event) {
				_event.setAmount((float) (amount * increasing));
			}
		}
	}
}