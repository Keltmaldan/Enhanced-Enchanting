package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class LapisLazuliCauldronEntityFallsOnTheBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		if (entity instanceof ItemEntity itemEntity) {

			if (((world.getBlockState(BlockPos.containing(x, y, z))).getBlock().getStateDefinition().getProperty("lapis") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1) != 64) {
			return;
		}

			int value = 0;
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockState state = world.getBlockState(pos);

			var property = state.getBlock().getStateDefinition().getProperty("lapis");

			// ⚡ Lightning
			if (world instanceof ServerLevel level) {
				LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
				lightning.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
				lightning.setVisualOnly(true);
				level.addFreshEntity(lightning);
			}

if (property instanceof IntegerProperty prop) {
	if (prop.getPossibleValues().contains(value)) {
		world.setBlock(pos, state.setValue(prop, value), 3);
	}
}

			ItemStack item = itemEntity.getItem();

						if (!(("").equals(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("lapis_enchant")))) {
			return;
			}

			if (world instanceof ServerLevel level && !item.isEmpty()) {

				var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

// 🔹 récupérer tous les enchantements
List<Holder<Enchantment>> allEnchantments = new ArrayList<>();
registry.holders().forEach(allEnchantments::add);

// 🔹 filtrer compatibles
List<Holder<Enchantment>> validEnchantments = new ArrayList<>();

for (Holder<Enchantment> ench : allEnchantments) {
	if (ench.value().canEnchant(item)) {
		validEnchantments.add(ench);
	}
}

				if (!validEnchantments.isEmpty()) {

					Random random = new Random();

					Holder<Enchantment> randomEnchant = validEnchantments.get(random.nextInt(validEnchantments.size()));

					int maxLevel = randomEnchant.value().getMaxLevel();
					int levelEnchant = 1 + random.nextInt(maxLevel);

					// ✅ Appliquer enchant
					item.enchant(randomEnchant, levelEnchant);

					// ✅ ID enchant
					ResourceLocation id = registry.getKey(randomEnchant.value());

					if (id != null) {

						final String tagName = "lapis_enchant";
						final String tagValue = id.toString(); // ✅ FIX

						// ✅ VRAI "NBT-like"
						CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> {
							tag.putString(tagName, tagValue);
						});
					}
				}
			}
		}
	}
}