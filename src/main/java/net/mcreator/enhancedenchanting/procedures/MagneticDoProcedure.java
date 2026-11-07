package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class MagneticDoProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double lvl = 0;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		for (byte i = 0; i <= 3; i++) {
			if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
				public static EquipmentSlot armorSlotByIndex(int _slotindex) {
					for (EquipmentSlot _slot : EquipmentSlot.values()) {
						if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
							return _slot;
						}
					}
					throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
				}
			}.armorSlotByIndex((int) i)) : ItemStack.EMPTY)
					.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:magnetic")))) != 0) {
				lvl = Math.max(lvl, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
					public static EquipmentSlot armorSlotByIndex(int _slotindex) {
						for (EquipmentSlot _slot : EquipmentSlot.values()) {
							if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
								return _slot;
							}
						}
						throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
					}
				}.armorSlotByIndex((int) i)) : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:magnetic")))));
			}
		}
		if (lvl == 0) {
			return;
		}
		lvl = 1 + Math.log(lvl);
		float factor = (!entity.onGround()) ? 4f : 1f;
		factor = (entity.isShiftKeyDown()) ? 0.01f : factor;
		sx = (int) (-3 * lvl);
		for (int _i1 = 0; _i1 < (int) (int) (6 * lvl); _i1++) {
			sy = (int) (-3 * lvl);
			for (int _i2 = 0; _i2 < (int) (int) (6 * lvl); _i2++) {
				sz = (int) (-3 * lvl);
				for (int _i3 = 0; _i3 < (int) (int) (6 * lvl); _i3++) {
					if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).is(BlockTags.create(ResourceLocation.parse("enhanced_enchanting:magnetic_tag")))) {
						entity.push(sx * factor / 100 * lvl, sy * factor / 150 * lvl, sz * factor / 150 * lvl);
						if (sy > 0 && (!entity.isShiftKeyDown())) {
							entity.fallDistance = (float) (entity.fallDistance * 0.9);
						}
					}
					sz++;
				}
				sy++;
			}
			sx++;
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 * lvl / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof ItemEntity) {
					entityiterator.push((x - entityiterator.getX()), (y - entityiterator.getY()), (z - entityiterator.getZ()));
				}
			}
		}
	}
}