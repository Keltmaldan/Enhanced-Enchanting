package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import javax.annotation.Nullable;

@EventBusSubscriber
public class EnchantmentsCatalystDoProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double increasing = 0;
		increasing = 1;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:enchantments_catalyst")))) != 0) {
			{
				ItemStack _redwire$itemstack = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
				ItemEnchantments _appliedEnchantments = _redwire$itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
				for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
					String stringiterator = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
					increasing = increasing + 0.05 * (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:enchantments_catalyst"))));
					if (entity instanceof LivingEntity _entity) {
						AttributeModifier modifier = new AttributeModifier(Identifier.parse("enhanced_enchanting:enchantments_catalyst_dmgboost"), increasing, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
						if (!_entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(modifier.id())) {
							_entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(modifier);
						}
					}
				}
			}
		} else {
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(Identifier.parse("enhanced_enchanting:enchantments_catalyst_dmgboost"));
			}
		}
	}
}