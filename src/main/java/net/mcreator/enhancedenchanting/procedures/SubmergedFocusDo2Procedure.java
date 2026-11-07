package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SubmergedFocusDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceentity, double amount) {
		execute(null, world, x, y, z, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity sourceentity, double amount) {
		if (sourceentity == null)
			return;
		double armor_piece = 0;
		double level = 0;
		if ((world.getFluidState(BlockPos.containing(x, y, z)).createLegacyBlock()).getBlock() instanceof LiquidBlock) {
			for (int _i1 = 0; _i1 < 4; _i1++) {
				if ((sourceentity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
					public static EquipmentSlot armorSlotByIndex(int _slotindex) {
						for (EquipmentSlot _slot : EquipmentSlot.values()) {
							if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
								return _slot;
							}
						}
						throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
					}
				}.armorSlotByIndex((int) armor_piece)) : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:submerged_focus")))) != 0) {
					level = level + 0.1 * (sourceentity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
						public static EquipmentSlot armorSlotByIndex(int _slotindex) {
							for (EquipmentSlot _slot : EquipmentSlot.values()) {
								if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
									return _slot;
								}
							}
							throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
						}
					}.armorSlotByIndex((int) armor_piece)) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:submerged_focus"))));
				}
				armor_piece = armor_piece + 1;
			}
			if (level != 0) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount * (1 + level)));
				}
			}
		}
	}
}