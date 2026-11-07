package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Blocks;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.ArrayList;

// Alters the real, already-calculated vanilla drops rather than cancelling
// the block break. Works for single blocks, Big Hole, and Vein Miner alike
@EventBusSubscriber
public class AutoSmeltDoProcedure {
    private static final ResourceKey<Enchantment> AUTO_SMELT = ResourceKey.create(
        Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:auto_smelt"));

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (event.isCanceled()) return;
        if (!(event.getBreaker() instanceof ServerPlayer player)) return;
        if (player.getAbilities().instabuild) return;

        ServerLevel world = event.getLevel();
        ItemStack tool = event.getTool();
        if (tool.isEmpty()) return;
        RegistryLookup<Enchantment> registry = world.registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT);
        if (tool.getEnchantmentLevel(registry.getOrThrow(AUTO_SMELT)) <= 0) return;

        // Operate on a snapshot because extra output stacks may be appended
        for (ItemEntity drop : new ArrayList<>(event.getDrops())) {
            ItemStack input = drop.getItem();
            	if (input.isEmpty()) continue;
            		ItemStack output = smeltOne(world, input);
            	if (output.isEmpty()) continue;

            long total = (long) output.getCount() * input.getCount();
            	if (total <= 0) continue;
            int first = (int) Math.min(total, output.getMaxStackSize());
            drop.setItem(output.copyWithCount(first));
            total -= first;

            while (total > 0) {
                int amount = (int) Math.min(total, output.getMaxStackSize());
                ItemEntity extra = new ItemEntity(world, drop.getX(),
                    drop.getY(), drop.getZ(), output.copyWithCount(amount));
                extra.setPickUpDelay(10);
                event.getDrops().add(extra);
                total -= amount;
            }
        }
    }

    private static ItemStack smeltOne(ServerLevel world, ItemStack input) {
        if (input.is(Blocks.RAW_IRON_BLOCK.asItem())) return new ItemStack(Blocks.IRON_BLOCK);
        if (input.is(Blocks.RAW_GOLD_BLOCK.asItem())) return new ItemStack(Blocks.GOLD_BLOCK);
        if (input.is(Blocks.RAW_COPPER_BLOCK.asItem())) return new ItemStack(Blocks.COPPER_BLOCK);

        SingleRecipeInput recipeInput = new SingleRecipeInput(input.copyWithCount(1));
        return world.recipeAccess().getRecipeFor(RecipeType.SMELTING, recipeInput, world)
            .map(recipe -> recipe.value().assemble(recipeInput))
            .orElse(ItemStack.EMPTY);
    }
}