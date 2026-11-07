package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class OGSGUITaken01Procedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu) {
			ItemStack _setstack0 = new ItemStack(Blocks.AIR).copy();
			_setstack0.setCount(1);
			_menu.getSlots().get(0).set(_setstack0);
			_player.containerMenu.broadcastChanges();
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.grindstone.use")), SoundSource.BLOCKS, 1, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.grindstone.use")), SoundSource.BLOCKS, 1, 1, false);
			}
		}
	}
}