package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

import java.util.regex.Pattern;
import java.util.Optional;
import java.util.List;

@EventBusSubscriber(value = {Dist.CLIENT})
public class LapisItemRenderProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, Minecraft.getInstance().level, event.getItemStack(), event.getToolTip());
	}

	public static void execute(LevelAccessor world, ItemStack itemstack, List<Component> tooltip) {
		execute(null, world, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, ItemStack itemstack, List<Component> tooltip) {
		if (tooltip == null)
			return;
		double lenght = 0;
		double R = 0;
		double B = 0;
		double G = 0;
		double step = 0;
		MutableComponent returning_value = Component.empty();
		String name = "";
		String enchname = "";
		String enchid = "";
		if (("").equals(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("lapis_enchant"))) {
			return;
		}
		enchname = Component.translatable((("enchantment." + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("lapis_enchant")).replace(":", "."))).getString();
		enchid = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("lapis_enchant");
		Optional<Holder.Reference<Enchantment>> opt = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(ResourceKey.create(Registries.ENCHANTMENT, (ResourceLocation.parse(enchid))));
		Enchantment lapisench = opt.get().value();
		for (int iter = tooltip.size() - 1; iter >= 0; iter--) {
			if ((((tooltip.get(iter).getString()).replace(" ", "")).replace(Component
					.translatable(("enchantment.level." + (int) itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(enchid))))))
					.getString(), "")).equals(enchname.replace(" ", ""))) {
				tooltip.remove(iter);
				tooltip.add((int) iter,
						Component
								.literal(
										("\u00A79" + "Lapis Lazuli :" + enchname
												+ (lapisench.getMaxLevel() != 1
														? " " + Component
																.translatable(("enchantment.level." + (int) itemstack
																		.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(enchid))))))
																.getString()
														: ""))));
				return;
			}
		} ;
		if (EnhancedEnchantingModVariables.MapVariables.get(world).ModifyNameOfItem) {
			{
				final String _tagName = "has_rendered_name";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putBoolean(_tagName, _tagValue));
			}
			if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("has_rendered_name")) {
				return;
			}
			name = ((itemstack.getDisplayName().getString()).replace("]", "")).replace("[", "");
			lenght = (name).length();
			if (lenght == 0) {
				return;
			}
			R = ((((int) 0x2d3e78 >> 16) & 0xFF) - (((int) 0x5976be >> 16) & 0xFF)) / (lenght - 1);
			G = ((((int) 0x2d3e78 >> 8) & 0xFF) - (((int) 0x5976be >> 8) & 0xFF)) / (lenght - 1);
			B = (((int) 0x2d3e78 & 0xFF) - ((int) 0x5976be & 0xFF)) / (lenght - 1);
			int i = 0;
			String[] _array38 = name.split(Pattern.quote(""));
			if (_array38.length != 0) {
				for (String stringiterator : _array38) {
					step = (255 << 24) | ((int) (R * i) << 16) | ((int) (G * i) << 8) | (int) (B * i);
					returning_value.append(Component.literal(stringiterator).withColor((int) (0x5976BE + step) & 0xFFFFFF));
					i++;
				}
			}
			itemstack.set(DataComponents.ITEM_NAME, returning_value);
		}
	}
}