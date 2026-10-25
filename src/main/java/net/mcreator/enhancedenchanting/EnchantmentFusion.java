package net.mcreator.enhancedenchanting;

import net.neoforged.neoforge.network.PacketDistributor;
import net.mcreator.enhancedenchanting.network.FusionAdvancementPacketMessage;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.world.level.Level;

public record EnchantmentFusion(String element_1, String element_2, String result, @Nullable String advancement) {
	public static final Codec<EnchantmentFusion> CODEC = RecordCodecBuilder.create(instance -> 
		instance.group(
			Codec.STRING.fieldOf("element_1").forGetter(EnchantmentFusion::element_1),
			Codec.STRING.fieldOf("element_2").forGetter(EnchantmentFusion::element_2),
			Codec.STRING.fieldOf("result").forGetter(EnchantmentFusion::result),
			Codec.STRING.optionalFieldOf("advancement", null).forGetter(EnchantmentFusion::advancement)
		).apply(instance, EnchantmentFusion::new)
	);

	public static Map<String, EnchantmentFusion> fusions = Map.of();
	
	public void trigger(ItemStack itemstack, Entity entity) {
		Level world = entity.level();
		EnchantmentHelper.updateEnchantments(itemstack, mutableEnchantments -> mutableEnchantments
				.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(this.element_1))))));
		EnchantmentHelper.updateEnchantments(itemstack, mutableEnchantments -> mutableEnchantments
				.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(this.element_2))))));
		itemstack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(result))), 1);
		if (advancement != null)
			PacketDistributor.sendToServer(new FusionAdvancementPacketMessage(this.advancement));
	}
}