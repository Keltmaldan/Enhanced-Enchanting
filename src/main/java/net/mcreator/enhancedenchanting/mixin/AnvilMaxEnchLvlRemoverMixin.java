package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

@Mixin(AnvilMenu.class)
public class AnvilMaxEnchLvlRemoverMixin {

	@Inject(
		method = "createResult",
		at = @At("TAIL"),
		cancellable = false
	)
	private void change(CallbackInfo ci) {
		AnvilMenu anvil = (AnvilMenu) (Object) this;
		ItemStack leftItem = anvil.slots.get(0).getItem();
		ItemStack rightItem = anvil.slots.get(1).getItem();
		ItemStack oldResult = anvil.slots.get(2).getItem();

		if (anvil == null || leftItem == null || rightItem == null)
			return;
		ItemEnchantments enchsList[] = {rightItem.is(Items.ENCHANTED_BOOK) ? rightItem.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY) :
									 	rightItem.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY),
									 	leftItem.is(Items.ENCHANTED_BOOK) ? leftItem.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY) :
									 	leftItem.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY),
		};
		ItemEnchantments resultEnchs = oldResult.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		for (ItemEnchantments enchs : enchsList) {
			for (Holder<Enchantment> ench : enchs.keySet()) {
				if (ench.value().getMaxLevel() == 1)
					continue;
				int lvl = enchs.getLevel(ench);
				if (enchsList[1].getLevel(ench) == lvl && enchsList[0].getLevel(ench) == lvl) {
					lvl++;
					oldResult.enchant(ench, lvl);
				}
				else if (resultEnchs.getLevel(ench) < enchs.getLevel(ench))
					oldResult.enchant(ench, lvl);
			}
		}	
	}

	@ModifyExpressionValue(
		method = "createResult",
    	at = @At(
        value = "CONSTANT",
        	args = "intValue=40"
    	)
	)
	private int removeTooExpensiveLimit(int value) {
	    return Integer.MAX_VALUE;
	}
}