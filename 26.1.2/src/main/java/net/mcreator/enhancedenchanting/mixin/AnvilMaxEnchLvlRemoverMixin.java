package net.mcreator.enhancedenchanting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMaxEnchLvlRemoverMixin {
    @ModifyExpressionValue(
        method = "createResultInternal",
        at = @At(value = "CONSTANT", args = "intValue=100"),
        require = 0
    )
    private int enhancedEnchanting$removeTooExpensiveLimit(int threshold) {
        return Integer.MAX_VALUE;
    }

    @Inject(method = "createResultInternal", at = @At("TAIL"))
    private void enhancedEnchanting$upgradeResultLevels(CallbackInfo ci) {
        AnvilMenu anvil = (AnvilMenu) (Object) this;
        ItemStack left = anvil.getSlot(0).getItem();
        ItemStack right = anvil.getSlot(1).getItem();
        ItemStack output = anvil.getSlot(2).getItem();
        if (left.isEmpty() || right.isEmpty() || output.isEmpty()) return;

        ItemEnchantments leftEnchants = left.is(Items.ENCHANTED_BOOK)
            ? left.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
            : left.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments rightEnchants = right.is(Items.ENCHANTED_BOOK)
            ? right.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
            : right.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments resultEnchants = output.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (ItemEnchantments inputEnchants : new ItemEnchantments[] { rightEnchants, leftEnchants }) {
            for (Holder<Enchantment> enchantment : inputEnchants.keySet()) {
                if (enchantment.value().getMaxLevel() == 1) continue;
                int level = inputEnchants.getLevel(enchantment);
                if (leftEnchants.getLevel(enchantment) == level && rightEnchants.getLevel(enchantment) == level) {
                    output.enchant(enchantment, level + 1);
                } else if (resultEnchants.getLevel(enchantment) < level) {
                    output.enchant(enchantment, level);
                }
            }
        }
    }
}