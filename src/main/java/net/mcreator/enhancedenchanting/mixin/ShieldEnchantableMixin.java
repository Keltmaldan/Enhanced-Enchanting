package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.item.ItemStack;

@Mixin(Item.class)
public class ShieldEnchantableMixin {
	
    @Inject(
        method = "getEnchantmentValue",
        at = @At("HEAD"),
        cancellable = true
    )
    private void modifyEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
    	//System.out.println("Item enchantability checked: " + this);
    	Item item = (Item)(Object)this;
        if (item == Items.SHIELD || item == Items.ELYTRA)
    		cir.setReturnValue(10);
	}
}