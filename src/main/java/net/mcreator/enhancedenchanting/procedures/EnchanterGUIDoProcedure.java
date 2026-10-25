package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class EnchanterGUIDoProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		ItemStack item = ItemStack.EMPTY;
		ItemStack old = ItemStack.EMPTY;
		double time = 0;
		double h = 0;
		if (((entity instanceof Player _entity0 && _entity0.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(2, "level", 0.0) : 0.0) != 0) {
			if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu1 ? _menu1.getSlots().get(2).getItem() : ItemStack.EMPTY)
					.getCount() >= ((entity instanceof Player _entity3 && _entity3.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu3) ? _menu3.getMenuState(2, "level", 0.0) : 0.0)
					&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu4 ? _menu4.getSlots().get(1).getItem() : ItemStack.EMPTY)
							.getCount() >= ((entity instanceof Player _entity6 && _entity6.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu6) ? _menu6.getMenuState(2, "level", 0.0) : 0.0)) {
				old = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu7 ? _menu7.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
				item = (EnchantmentHelper.enchantItem(world.getRandom(), old,
						(int) ((entity instanceof Player _entity8 && _entity8.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu8) ? _menu8.getMenuState(2, "level", 0.0) : 0.0),
						(true)
								? world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).holders().map(reference -> (Holder<Enchantment>) reference)
								: world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getTag(EnchantmentTags.IN_ENCHANTING_TABLE).get().stream()))
						.copy();
				if (!(item.isEnchanted() || item.isEnchantable())) {
					return;
				}
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.enchantment_table.use")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.enchantment_table.use")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				for (int _i1 = 0; _i1 < (int) ((entity instanceof Player _entity11 && _entity11.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu11) ? _menu11.getMenuState(2, "level", 0.0) : 0.0); _i1++) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
						}
					}
				}
				if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu) {
					_menu.getSlots().get(1).remove((int) ((entity instanceof Player _entity13 && _entity13.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu13) ? _menu13.getMenuState(2, "level", 0.0) : 0.0));
					_menu.getSlots().get(2).remove((int) ((entity instanceof Player _entity15 && _entity15.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu15) ? _menu15.getMenuState(2, "level", 0.0) : 0.0));
					ItemStack _setstack17 = new ItemStack(Blocks.AIR).copy();
					_setstack17.setCount(0);
					_menu.getSlots().get(0).set(_setstack17);
					_player.containerMenu.broadcastChanges();
				}
				EnhancedEnchantingMod.queueServerWork(1, () -> {
					if (entity instanceof Player _player)
						_player.closeContainer();
				});
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, (y + 1), z, item);
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				while (h < 2) {
					h = h + 0.1;
					while (time < 360) {
						time = time + 1;
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + Math.cos(time) + 0.5), (y + h), (z + Math.sin(time) + 0.5), 1, 0, 0, 0, 0);
					}
					time = 0;
				}
			}
		}
	}
}