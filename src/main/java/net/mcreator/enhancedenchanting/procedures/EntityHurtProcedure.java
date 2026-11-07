package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModParticleTypes;

import javax.annotation.Nullable;

@EventBusSubscriber
public class EntityHurtProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity(),
					event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
		execute(null, world, x, y, z, damagesource, entity, immediatesourceentity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
		if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		double armor_slot = 0;
		double deflect = 0;
		double dmg_keeper = 0;
		double T = 0;
		double a = 0;
		double b = 0;
		double c = 0;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		ChockwaveShootProcedure.execute(world, x, y, z, sourceentity);
		armor_slot = -1;
		if (!damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("enhanced_enchanting:too_much_damages_kept")))) {
			for (int _i1 = 0; _i1 < 4; _i1++) {
				armor_slot = armor_slot + 1;
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
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:damage_keeper")))) != 0) {
					dmg_keeper = dmg_keeper + 1;
				}
			}
			if (dmg_keeper != 0) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount / (dmg_keeper + 1)));
				}
				armor_slot = -1;
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.ENCHANT, x, y, z, 5, 1, 2, 1, 0);
				for (int _i1 = 0; _i1 < 4; _i1++) {
					armor_slot = armor_slot + 1;
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
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:damage_keeper")))) != 0) {
						{
							final String _tagName = "damage_keeper";
							final double _tagValue = ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
								public static EquipmentSlot armorSlotByIndex(int _slotindex) {
									for (EquipmentSlot _slot : EquipmentSlot.values()) {
										if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
											return _slot;
										}
									}
									throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
								}
							}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("damage_keeper") + 1);
							CustomData.update(DataComponents.CUSTOM_DATA, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
								public static EquipmentSlot armorSlotByIndex(int _slotindex) {
									for (EquipmentSlot _slot : EquipmentSlot.values()) {
										if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
											return _slot;
										}
									}
									throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
								}
							}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
							public static EquipmentSlot armorSlotByIndex(int _slotindex) {
								for (EquipmentSlot _slot : EquipmentSlot.values()) {
									if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
										return _slot;
									}
								}
								throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
							}
						}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("damage_keeper") > 5
								* (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
									public static EquipmentSlot armorSlotByIndex(int _slotindex) {
										for (EquipmentSlot _slot : EquipmentSlot.values()) {
											if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
												return _slot;
											}
										}
										throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
									}
								}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:damage_keeper"))))) {
							{
								final String _tagName = "damage_keeper";
								final double _tagValue = 0;
								CustomData.update(DataComponents.CUSTOM_DATA, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(new Object() {
									public static EquipmentSlot armorSlotByIndex(int _slotindex) {
										for (EquipmentSlot _slot : EquipmentSlot.values()) {
											if (_slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && _slot.getIndex() == _slotindex) {
												return _slot;
											}
										}
										throw new IllegalArgumentException("Invalid slot index: " + _slotindex);
									}
								}.armorSlotByIndex((int) armor_slot)) : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
							}
							entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("enhanced_enchanting:too_much_damages_kept")))), 6);
						}
					}
				}
			}
		}
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:poison_touch")))) != 0) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.POISON, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:poison_touch")))) * 60, 1));
		}
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:vampire")))) != 0) {
			if (world instanceof ServerLevel _level)
				_level.sendParticles((SimpleParticleType) (EnhancedEnchantingModParticleTypes.VAMPIRE_BLOOD_PARTICLE.get()), x, (y + 1), z, 15, 0.5, 0.5, 0.5, 0);
			if (sourceentity instanceof LivingEntity _entity)
				_entity.setHealth((float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:vampire")))) * 0.025 * amount));
		}
		if (!damagesource.is(DamageTypes.GENERIC_KILL)) {
			if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
					.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:zone_damage")))) != 0) {
				for (Entity entityiterator : world.getEntities(sourceentity, new AABB((x - 5), (y - 5), (z - 5), (x + 5), (y + 5), (z + 5)))) {
					entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC_KILL), sourceentity), (float) (amount / (6 - (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:zone_damage")))))));
				}
			}
		}
		if (immediatesourceentity instanceof Arrow) {
			if (immediatesourceentity.getY() <= y + 0.3125) {
				if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect")))) != 0) {
					if (Mth.nextInt(RandomSource.create(), 1, 6 - (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect"))))) == 1) {
						if (event instanceof ICancellableEvent _cancellable) {
							_cancellable.setCanceled(true);
						}
						if (!immediatesourceentity.level().isClientSide())
							immediatesourceentity.discard();
						if (world instanceof ServerLevel projectileLevel) {
							Projectile _entityToSpawn = initArrowProjectile(new Arrow(projectileLevel, 0, 0, 0, new Arrow(EntityType.ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, 1, (byte) 0)),
									entity, 1, false, false, false, AbstractArrow.Pickup.DISALLOWED);
							_entityToSpawn.setPos(x, (immediatesourceentity.getY()), z);
							_entityToSpawn.shoot((sourceentity.getX() - x), (sourceentity.getY() - y), (sourceentity.getZ() - z), 5, (float) 0.25);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.SWEEP_ATTACK, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 1, 0, 0, 0, 0);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")),
										SoundSource.PLAYERS, 1, 1);
							} else {
								_level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")), SoundSource.PLAYERS,
										1, 1, false);
							}
						}
					}
				}
			} else if (immediatesourceentity.getY() <= y + 1) {
				if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect")))) != 0) {
					if (Mth.nextInt(RandomSource.create(), 1, 6 - (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect"))))) == 1) {
						if (event instanceof ICancellableEvent _cancellable) {
							_cancellable.setCanceled(true);
						}
						if (!immediatesourceentity.level().isClientSide())
							immediatesourceentity.discard();
						if (world instanceof ServerLevel projectileLevel) {
							Projectile _entityToSpawn = initArrowProjectile(new Arrow(projectileLevel, 0, 0, 0, new Arrow(EntityType.ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, 1, (byte) 0)),
									entity, 1, false, false, false, AbstractArrow.Pickup.DISALLOWED);
							_entityToSpawn.setPos(x, (immediatesourceentity.getY()), z);
							_entityToSpawn.shoot((sourceentity.getX() - x), (sourceentity.getY() - y), (sourceentity.getZ() - z), 5, (float) 0.25);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.SWEEP_ATTACK, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 1, 0, 0, 0, 0);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")),
										SoundSource.PLAYERS, 1, 1);
							} else {
								_level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")), SoundSource.PLAYERS,
										1, 1, false);
							}
						}
					}
				}
			} else if (immediatesourceentity.getY() <= y + 1.5) {
				if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect")))) != 0) {
					if (Mth.nextInt(RandomSource.create(), 1, 6 - (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect"))))) == 1) {
						if (event instanceof ICancellableEvent _cancellable) {
							_cancellable.setCanceled(true);
						}
						if (!immediatesourceentity.level().isClientSide())
							immediatesourceentity.discard();
						if (world instanceof ServerLevel projectileLevel) {
							Projectile _entityToSpawn = initArrowProjectile(new Arrow(projectileLevel, 0, 0, 0, new Arrow(EntityType.ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, 1, (byte) 0)),
									entity, 1, false, false, false, AbstractArrow.Pickup.DISALLOWED);
							_entityToSpawn.setPos(x, (immediatesourceentity.getY()), z);
							_entityToSpawn.shoot((sourceentity.getX() - x), (sourceentity.getY() - y), (sourceentity.getZ() - z), 5, (float) 0.25);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.SWEEP_ATTACK, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 1, 0, 0, 0, 0);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")),
										SoundSource.PLAYERS, 1, 1);
							} else {
								_level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")), SoundSource.PLAYERS,
										1, 1, false);
							}
						}
					}
				}
			} else if (immediatesourceentity.getY() <= y + 2) {
				if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect")))) != 0) {
					if (Mth.nextInt(RandomSource.create(), 1, 6 - (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:deflect"))))) == 1) {
						if (event instanceof ICancellableEvent _cancellable) {
							_cancellable.setCanceled(true);
						}
						if (!immediatesourceentity.level().isClientSide())
							immediatesourceentity.discard();
						if (world instanceof ServerLevel projectileLevel) {
							Projectile _entityToSpawn = initArrowProjectile(new Arrow(projectileLevel, 0, 0, 0, new Arrow(EntityType.ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, 1, (byte) 0)),
									entity, 1, false, false, false, AbstractArrow.Pickup.DISALLOWED);
							_entityToSpawn.setPos(x, (immediatesourceentity.getY()), z);
							_entityToSpawn.shoot((sourceentity.getX() - x), (sourceentity.getY() - y), (sourceentity.getZ() - z), 5, (float) 0.25);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.SWEEP_ATTACK, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 1, 0, 0, 0, 0);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")),
										SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.land")), SoundSource.NEUTRAL,
										1, 1, false);
							}
						}
					}
				}
			}
		}
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:back_stabber")))) != 0) {
			if (!BackstabberCanSeeLogicProcedure.execute(sourceentity, entity, 120)) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (EnhancedEnchantingModParticleTypes.BACK_STAB_PARTICLE.get()), x, y, z, 5, 2, 2, 2, 1);
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount(
							(float) (2
									* (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
											.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:back_stabber"))))
									+ amount));
				}
			}
		}
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:weakness")))) != 0) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 15 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:weakness")))), 1));
		}
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:double_edged")))) != 0) {
			if (sourceentity instanceof LivingEntity _entity)
				_entity.setHealth(
						(float) ((1 + 0.1 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getArmorValue() : 0))
								* ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) - amount * Math.pow(
										(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
												.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:double_edged")))) + 1,
										-1))));
			if (event instanceof LivingIncomingDamageEvent _event) {
				_event.setAmount(
						(float) (amount
								+ amount * (1 - Math.pow(
										(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
												.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:double_edged")))) + 1,
										-1))));
			}
		}
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid")))) != 0) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 2d) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.squid.squirt")), SoundSource.PLAYERS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.squid.squirt")), SoundSource.PLAYERS, 1, 1, false);
					}
				}
				for (Entity entityiterator : world.getEntities(entity,
						new AABB(
								(x + (-2) * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))),
								(y + (-2) * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))),
								(z + (-2) * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))),
								(x + 2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))),
								(y + 2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))),
								(z + 2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid")))))))) {
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 5));
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));
					sx = (-2) * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))));
					while (sx <= 2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))) {
						sy = (-2) * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
								.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))));
						while (sy <= 2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
								.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))) {
							sz = (-2) * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
									.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))));
							while (sz <= 2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
									.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))) {
								if (world instanceof ServerLevel _level)
									_level.sendParticles(ParticleTypes.SQUID_INK, (x + sx), (y + sy), (z + sz), 1, 0, 0, 0, 0);
								sz = sz + (2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
										.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))) / 5d;
							}
							sy = sy + (2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
									.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))) / 5d;
						}
						sx = sx + (2 * (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
								.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:stressed_squid"))))) / 5d;
					}
				}
			}
		}
	}

	private static AbstractArrow initArrowProjectile(AbstractArrow entityToSpawn, Entity shooter, float damage, boolean silent, boolean fire, boolean particles, AbstractArrow.Pickup pickup) {
		entityToSpawn.setOwner(shooter);
		entityToSpawn.setBaseDamage(damage);
		if (silent)
			entityToSpawn.setSilent(true);
		if (fire)
			entityToSpawn.igniteForSeconds(100);
		if (particles)
			entityToSpawn.setCritArrow(true);
		entityToSpawn.pickup = pickup;
		return entityToSpawn;
	}

	private static ItemStack createArrowWeaponItemStack(Level level, int knockback, byte piercing) {
		ItemStack weapon = new ItemStack(Items.ARROW);
		if (knockback > 0)
			weapon.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), knockback);
		if (piercing > 0)
			weapon.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PIERCING), piercing);
		return weapon;
	}
}