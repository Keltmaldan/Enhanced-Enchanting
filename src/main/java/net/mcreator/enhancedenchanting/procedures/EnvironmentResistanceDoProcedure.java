package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

@EventBusSubscriber
public class EnvironmentResistanceDoProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getSource(), event.getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, DamageSource damagesource, Entity entity, double amount) {
		execute(null, world, damagesource, entity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, DamageSource damagesource, Entity entity, double amount) {
		if (damagesource == null || entity == null)
			return;
		double armor_slot = 0;
		double reduction = 0;
		if (!damagesource.is(DamageTypes.GENERIC_KILL)) {
			if (damagesource.getEntity() == null || !((damagesource.getEntity()) instanceof LivingEntity)) {
				for (int _i1 = 0; _i1 < 4; _i1++) {
					if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
						public static EquipmentSlot armorSlotByIndex(int _slotindex) {
							for (EquipmentSlot _slot : EquipmentSlot.values()) {
								if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
									return _slot;
								}
							}
							throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
						}
					}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:environment_resistance")))) != 0) {
						reduction = reduction + 0.03 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
							public static EquipmentSlot armorSlotByIndex(int _slotindex) {
								for (EquipmentSlot _slot : EquipmentSlot.values()) {
									if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
										return _slot;
									}
								}
								throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
							}
						}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY)
								.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:environment_resistance"))));
					}
					armor_slot = armor_slot + 1;
				}
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount * (1 - reduction)));
				}
			}
		}
	}
}