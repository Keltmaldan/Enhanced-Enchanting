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

import net.mcreator.enhancedenchanting.procedures.EnchantmentRemoverGUISlot0ChangesProcedure;
import net.mcreator.enhancedenchanting.procedures.EnchantmentRemoverBooksRemovesEnchantProcedure;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public record EnchantmentRemoverGUISlotMessage(int slotID, int x, int y, int z, int changeType, int meta) implements CustomPacketPayload {
	public static final Type<EnchantmentRemoverGUISlotMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "enchantment_remover_gui_slots"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentRemoverGUISlotMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, EnchantmentRemoverGUISlotMessage message) -> {
		buffer.writeInt(message.slotID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
		buffer.writeInt(message.changeType);
		buffer.writeInt(message.meta);
	}, (RegistryFriendlyByteBuf buffer) -> new EnchantmentRemoverGUISlotMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<EnchantmentRemoverGUISlotMessage> type() {
		return TYPE;
	}

	public static void handleData(final EnchantmentRemoverGUISlotMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleSlotAction(context.player(), message.slotID, message.changeType, message.meta, message.x, message.y, message.z)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleSlotAction(Player entity, int slot, int changeType, int meta, int x, int y, int z) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (slot == 0 && changeType == 0) {

			EnchantmentRemoverGUISlot0ChangesProcedure.execute(world, entity, slot);
		}
		if (slot == 0 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverGUISlot0ChangesProcedure.execute(world, entity, slot);
		}
		if (slot == 1 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 1 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 2 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 2 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 3 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 3 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 4 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 4 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 5 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 5 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 6 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 6 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 7 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 7 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 8 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 8 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 9 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 9 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 10 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 10 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 11 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 11 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 12 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 12 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 13 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 13 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 14 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 14 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 15 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 15 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 16 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 16 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 17 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 17 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 18 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 18 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 19 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 19 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 20 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 20 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 21 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 21 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 22 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 22 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 23 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 23 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 24 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 24 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 25 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 25 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 26 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 26 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 27 && changeType == 1) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
		if (slot == 27 && changeType == 2) {
			int amount = meta;

			EnchantmentRemoverBooksRemovesEnchantProcedure.execute(world, entity, slot);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EnhancedEnchantingMod.addNetworkMessage(EnchantmentRemoverGUISlotMessage.TYPE, EnchantmentRemoverGUISlotMessage.STREAM_CODEC, EnchantmentRemoverGUISlotMessage::handleData);
	}
}