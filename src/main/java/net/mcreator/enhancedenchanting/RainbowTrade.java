package net.mcreator.enhancedenchanting;

import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

@EventBusSubscriber
public class RainbowTrade {
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		if (!(event.getType() == VillagerProfession.LEATHERWORKER)) 
			return;
		Holder<Enchantment> rainbow = event.getRegistryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("enhanced_enchanting", "rainbow")));
		ItemStack cost = new ItemStack(Items.EMERALD, 10);
		ItemStack[] list = {new ItemStack(Items.LEATHER_HELMET),
							new ItemStack(Items.LEATHER_CHESTPLATE),
							new ItemStack(Items.LEATHER_LEGGINGS),
							new ItemStack(Items.LEATHER_BOOTS),
							new ItemStack(Items.LEATHER_HORSE_ARMOR),
							new ItemStack(Items.WOLF_ARMOR)
		};
		for (ItemStack stack : list) {
			stack.enchant(rainbow, 1);
			event.getTrades().get(5).add(new BasicItemListing(cost, stack, 10, 5, 0.05f));
		}
	}
}