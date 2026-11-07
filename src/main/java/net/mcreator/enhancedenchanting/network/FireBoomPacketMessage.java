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

import net.mcreator.enhancedenchanting.procedures.FireBoomPacketReceivedByClientProcedure;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public record FireBoomPacketMessage(String extradata) implements CustomPacketPayload {
	public static final Type<FireBoomPacketMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "fire_boom_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, FireBoomPacketMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, FireBoomPacketMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new FireBoomPacketMessage(buffer.readUtf()));

	@Override
	public Type<FireBoomPacketMessage> type() {
		return TYPE;
	}

	public static void handleData(final FireBoomPacketMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;

				FireBoomPacketReceivedByClientProcedure.execute(world, inboundString);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EnhancedEnchantingMod.addNetworkMessage(FireBoomPacketMessage.TYPE, FireBoomPacketMessage.STREAM_CODEC, FireBoomPacketMessage::handleData);
	}
}