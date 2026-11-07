package net.mcreator.enhancedenchanting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public final class ApotheosisGemRemover {
    private ApotheosisGemRemover() {}

    public static ItemStack removeGems(ItemStack stack) {
        ItemStack copy = stack.copy();
        BuiltInRegistries.DATA_COMPONENT_TYPE
            .get(Identifier.fromNamespaceAndPath("apotheosis", "socketed_gems"))
            .ifPresent(holder -> copy.remove(holder.value()));
        return copy;
    }
}