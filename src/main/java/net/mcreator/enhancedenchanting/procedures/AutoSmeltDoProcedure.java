package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@EventBusSubscriber
public class AutoSmeltDoProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getState(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		execute(null, world, x, y, z, blockstate, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		String loot_table = "";
		String string1 = "";
		ItemStack item = ItemStack.EMPTY;
		ItemStack smelt = ItemStack.EMPTY;
		ArrayList<Object> useless = new ArrayList<>();
		if (false) {
			EnhancedEnchantingMod.LOGGER.info(blockstate);
		}
		if (!(entity instanceof Player _plr ? _plr.getAbilities().instabuild : false)) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
					.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:auto_smelt")))) != 0) {
				List<ItemStack> looting = Collections.emptyList();
				if (world instanceof ServerLevel serverLevel) {
					BlockPos pos = BlockPos.containing(x, y, z);
					LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
							.withParameter(LootContextParams.TOOL, entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY).withParameter(LootContextParams.THIS_ENTITY, entity)
							.withOptionalParameter(LootContextParams.BLOCK_ENTITY, serverLevel.getBlockEntity(pos));
					looting = blockstate.getDrops(builder);
				}
				for (ItemStack it : looting) {
					smelt = (world instanceof Level _lvlSmeltResult
							? _lvlSmeltResult.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(it), _lvlSmeltResult).map(recipe -> recipe.value().getResultItem(_lvlSmeltResult.registryAccess()).copy())
									.orElse(ItemStack.EMPTY)
							: ItemStack.EMPTY).copy();
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SMALL_FLAME, x, y, z, 8, 0.5, 0.5, 0.5, 0);
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, (smelt.getItem() == Blocks.AIR.asItem() ? it : smelt));
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
				} ;
				if (event instanceof ICancellableEvent _cancellable) {
					_cancellable.setCanceled(true);
				}
				if (world instanceof ServerLevel _level) {
					(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
					});
				}
				world.destroyBlock(BlockPos.containing(x, y, z), false);
			}
		}
	}
}