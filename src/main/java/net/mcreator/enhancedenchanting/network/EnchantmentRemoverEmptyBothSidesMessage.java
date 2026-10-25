package net.mcreator.enhancedenchanting.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.enhancedenchanting.procedures.EnchantmentRemoverSlotClearProcedure;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public record EnchantmentRemoverEmptyBothSidesMessage(String extradata) implements CustomPacketPayload {
	public static final Type<EnchantmentRemoverEmptyBothSidesMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "enchantment_remover_empty_both_sides"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentRemoverEmptyBothSidesMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, EnchantmentRemoverEmptyBothSidesMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new EnchantmentRemoverEmptyBothSidesMessage(buffer.readUtf()));

	@Override
	public Type<EnchantmentRemoverEmptyBothSidesMessage> type() {
		return TYPE;
	}

	public static void handleData(final EnchantmentRemoverEmptyBothSidesMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;
				if (!world.hasChunkAt(entity.blockPosition()))
					return;

				EnchantmentRemoverSlotClearProcedure.execute(entity);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;

				EnchantmentRemoverSlotClearProcedure.execute(entity);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EnhancedEnchantingMod.addNetworkMessage(EnchantmentRemoverEmptyBothSidesMessage.TYPE, EnchantmentRemoverEmptyBothSidesMessage.STREAM_CODEC, EnchantmentRemoverEmptyBothSidesMessage::handleData);
	}
}