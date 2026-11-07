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
import net.minecraft.core.SectionPos;

import net.mcreator.enhancedenchanting.procedures.FireTrailEnchantmentUseOnKeyReleasedProcedure;
import net.mcreator.enhancedenchanting.procedures.FireTrailEnchantmentUseOnKeyPressProcedure;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public record FireTrailEnchantmentUseMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<FireTrailEnchantmentUseMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "key_fire_trail_enchantment_use"));
	public static final StreamCodec<RegistryFriendlyByteBuf, FireTrailEnchantmentUseMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, FireTrailEnchantmentUseMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new FireTrailEnchantmentUseMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<FireTrailEnchantmentUseMessage> type() {
		return TYPE;
	}

	public static void handleData(final FireTrailEnchantmentUseMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.pressedms);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (type == 0) {

			FireTrailEnchantmentUseOnKeyPressProcedure.execute(entity);
		}
		if (type == 1) {

			FireTrailEnchantmentUseOnKeyReleasedProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EnhancedEnchantingMod.addNetworkMessage(FireTrailEnchantmentUseMessage.TYPE, FireTrailEnchantmentUseMessage.STREAM_CODEC, FireTrailEnchantmentUseMessage::handleData);
	}
}