package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ImmortalityDoProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null)
			return;
		double armor_piece = 0;
		Entity chicken = null;
		ItemStack stack = ItemStack.EMPTY;
		boolean pheonix = false;
		boolean chicken_master = false;
		boolean immortality = false;
		if (!entity.isAlive()) {
			for (int _i1 = 0; _i1 < 4; _i1++) {
				stack = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
					public static EquipmentSlot armorSlotByIndex(int _slotindex) {
						for (EquipmentSlot _slot : EquipmentSlot.values()) {
							if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
								return _slot;
							}
						}
						throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
					}
				}.armorSlotByIndex((int) armor_piece)) : ItemStack.EMPTY).copy();
				pheonix = stack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:pheonix")))) != 0;
				chicken_master = stack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:chicken_master")))) != 0;
				immortality = stack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:immortality")))) != 0;
				if (pheonix || chicken_master || immortality) {
					if (entity instanceof Player _player)
						_player.closeContainer();
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1, false, true));
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0, false, true));
					{
						Entity _entity = entity;
						if (_entity instanceof Player _player) {
							_player.getInventory().armor.set((int) armor_piece, new ItemStack(Blocks.AIR));
							_player.getInventory().setChanged();
						} else if (_entity instanceof LivingEntity _living) {
							_living.setItemSlot(new Object() {
								public static EquipmentSlot armorSlotByIndex(int _slotindex) {
									for (EquipmentSlot _slot : EquipmentSlot.values()) {
										if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
											return _slot;
										}
									}
									throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
								}
							}.armorSlotByIndex((int) armor_piece), new ItemStack(Blocks.AIR));
						}
					}
					if (world.isClientSide())
						Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
					if (pheonix || chicken_master) {
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(EnhancedEnchantingModMobEffects.INVINCIBILITY, 2, 0, true, false));
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1, false, true));
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 900, 0, false, true));
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 250, 3, false, true));
						if (world.isClientSide())
							Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
						if (pheonix) {
							if (world instanceof Level _level && !_level.isClientSide())
								_level.explode(null, x, y, z, 4, Level.ExplosionInteraction.MOB);
							if (world instanceof ServerLevel _level)
								_level.sendParticles(ParticleTypes.FLAME, x, y, z, 64, 3, 3, 3, 1);
						} else if (chicken_master) {
							if (sourceentity != null)
								if (!world.isClientSide()) {
									for (int _i2 = 0; _i2 < 4; _i2++) {
										chicken = world instanceof ServerLevel _level18 ? EntityType.CHICKEN.spawn(_level18, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED) : null;
										chicken.getPersistentData().putString("explosive_target", (sourceentity.getStringUUID()));
									}
								}
						}
						if (entity instanceof LivingEntity _entity)
							_entity.setHealth(2);
						if (event instanceof ICancellableEvent _cancellable) {
							_cancellable.setCanceled(true);
						}
					} else if (immortality) {
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1, false, true));
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, (y + 1), z, 5, 0, 0, 0, 1);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.totem.use")), SoundSource.PLAYERS, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.totem.use")), SoundSource.PLAYERS, 1, 1, false);
							}
						}
					}
					if (entity instanceof LivingEntity _entity)
						_entity.setHealth(2);
					Minecraft.getInstance().execute(() -> {
						Minecraft mc = Minecraft.getInstance();
						if (mc.player != null && mc.screen != null) {
							System.out.println(Thread.currentThread().getName());
							mc.setScreen(null);
						}
					});
					return;
				}
				armor_piece = armor_piece + 1;
			}
		}
	}
}