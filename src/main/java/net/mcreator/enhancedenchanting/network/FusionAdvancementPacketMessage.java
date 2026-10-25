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

import net.mcreator.enhancedenchanting.procedures.FusionAdvancementPacketReceivedByServerProcedure;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public record FusionAdvancementPacketMessage(String extradata) implements CustomPacketPayload {
	public static final Type<FusionAdvancementPacketMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "fusion_advancement_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, FusionAdvancementPacketMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, FusionAdvancementPacketMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new FusionAdvancementPacketMessage(buffer.readUtf()));

	@Override
	public Type<FusionAdvancementPacketMessage> type() {
		return TYPE;
	}

	public static void handleData(final FusionAdvancementPacketMessage message, final IPayloadContext context) {
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

				FusionAdvancementPacketReceivedByServerProcedure.execute(entity, inboundString);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EnhancedEnchantingMod.addNetworkMessage(FusionAdvancementPacketMessage.TYPE, FusionAdvancementPacketMessage.STREAM_CODEC, FusionAdvancementPacketMessage::handleData);
	}
}