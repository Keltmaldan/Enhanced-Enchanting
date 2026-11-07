package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.block.LapisLazuliCauldronBlock;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.phys.Vec3;

public class LapisLazuliCauldronEntityFallsOnTheBlockProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (!(world instanceof ServerLevel level)) return;
        if (!(entity instanceof ItemEntity dropped) || dropped.isRemoved()) return;

        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof LapisLazuliCauldronBlock)) return;
        if (state.getValue(LapisLazuliCauldronBlock.BLOCKSTATE) != 1) return;

        ItemStack original = dropped.getItem();
        if (original.isEmpty()) return;
        String existingTag = original.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("lapis_enchant", "");
        if (!existingTag.isEmpty()) return;

        boolean isBook = original.is(Items.BOOK);
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        var currentlyApplied = original.getAllEnchantments(registry);
        List<Holder.Reference<Enchantment>> valid = registry.listElements()
            .filter(candidate -> candidate.value().getMaxLevel() >= 1)
            .filter(candidate -> isBook || original.supportsEnchantment(candidate))
            .filter(candidate -> currentlyApplied.getLevel(candidate) == 0)
            .filter(candidate -> currentlyApplied.keySet().stream()
                .allMatch(existing -> Enchantment.areCompatible(candidate, existing))).toList();

        if (valid.isEmpty()) return;
        Holder.Reference<Enchantment> selected = valid.get(level.getRandom().nextInt(valid.size()));
        int enchantLevel = 1 + level.getRandom().nextInt(selected.value().getMaxLevel());
        ItemStack result = isBook ? new ItemStack(Items.ENCHANTED_BOOK) : original.copyWithCount(1);
        result.enchant(selected, enchantLevel);
        CustomData.update(DataComponents.CUSTOM_DATA, result, tag -> tag.putString("lapis_enchant", selected.key().identifier().toString()));

        if (original.getCount() > 1) {
            ItemStack rest = original.copy();
            rest.shrink(1);
            dropped.setItem(rest);
            ItemEntity enchantedDrop = new ItemEntity(level, dropped.getX(), dropped.getY() + 0.15, dropped.getZ(), result);
            enchantedDrop.setPickUpDelay(20);
            enchantedDrop.setDeltaMovement(0, 0.18, 0);
            level.addFreshEntity(enchantedDrop);
        } else {
            dropped.setItem(result);
            dropped.setPickUpDelay(20);
        }

        level.setBlock(pos, state.setValue(LapisLazuliCauldronBlock.LAPIS, 0).setValue(LapisLazuliCauldronBlock.BLOCKSTATE, 0), 3);
        	LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level,EntitySpawnReason.TRIGGERED);
			if (lightning != null) {
    			lightning.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
    			lightning.setVisualOnly(true);
    			level.addFreshEntity(lightning);
		}
    }
}