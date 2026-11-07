package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class EnchUpgraderButtonDoDebugProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (Items.ECHO_SHARD == (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get(1).getItem() : ItemStack.EMPTY).getItem()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu2 ? _menu2.getSlots().get(1).getItem() : ItemStack.EMPTY).getCount() >= 32
				|| entity instanceof Player _plr4 && _plr4.gameMode() == GameType.CREATIVE) {
			if (entity instanceof ServerPlayer _player && _player.level() instanceof ServerLevel _level) {
				AdvancementHolder _adv = _level.getServer().getAdvancements().get(Identifier.parse("enhanced_enchanting:upgrading"));
				if (_adv != null) {
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						for (String criteria : _ap.getRemainingCriteria())
							_player.getAdvancements().award(_adv, criteria);
					}
				}
			}
			EnchantmentsUpgraderGUIButtonDoProcedure.execute(world, entity);
			if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu) {
				_menu.getSlots().get(1).remove(32);
				_player.containerMenu.broadcastChanges();
			}
			if (world instanceof ServerLevel _level) {
				LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level, EntitySpawnReason.TRIGGERED);
				entityToSpawn.snapTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
				entityToSpawn.setVisualOnly(true);
				_level.addFreshEntity(entityToSpawn);
			}
		}
	}
}