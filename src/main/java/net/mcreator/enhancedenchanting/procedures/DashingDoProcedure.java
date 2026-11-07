package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import javax.annotation.Nullable;

@EventBusSubscriber(Dist.CLIENT)
public class DashingDoProcedure {
	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		PacketDistributor.sendToServer(new DashingDoMessage());
		execute(event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	@EventBusSubscriber
	public record DashingDoMessage() implements CustomPacketPayload {
		public static final Type<DashingDoMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "procedure_dashing_do"));
		public static final StreamCodec<RegistryFriendlyByteBuf, DashingDoMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, DashingDoMessage message) -> {
		}, (RegistryFriendlyByteBuf buffer) -> new DashingDoMessage());

		@Override
		public Type<DashingDoMessage> type() {
			return TYPE;
		}

		public static void handleData(final DashingDoMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.SERVERBOUND) {
				context.enqueueWork(() -> {
					if (!context.player().level().getChunkSource().hasChunk(SectionPos.blockToSectionCoord(context.player().getX()), SectionPos.blockToSectionCoord(context.player().getZ())))
						return;
					execute(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), context.player());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}

		@SubscribeEvent
		public static void registerMessage(FMLCommonSetupEvent event) {
			EnhancedEnchantingMod.addNetworkMessage(DashingDoMessage.TYPE, DashingDoMessage.STREAM_CODEC, DashingDoMessage::handleData);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof Player _plrCldCheck1 && _plrCldCheck1.getCooldowns().isOnCooldown((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()))
				&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:dashing")))) != 0) {
			if (entity instanceof Player _player)
				_player.getCooldowns().addCooldown((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(),
						(int) (20 / (entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(Attributes.ATTACK_SPEED) ? _livingEntity4.getAttribute(Attributes.ATTACK_SPEED).getValue() : 0)));
			entity.push(
					(((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:dashing")))) / 3d)
							* entity.getLookAngle().x),
					(!((world.getFluidState(BlockPos.containing(x, y + 1, z)).createLegacyBlock()).getBlock() == Blocks.AIR) && !((world.getFluidState(BlockPos.containing(x, y, z)).createLegacyBlock()).getBlock() == Blocks.AIR)
							|| entity instanceof LivingEntity _livEnt14 && _livEnt14.isFallFlying()
									? ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
											.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:dashing")))) / 3d)
											* entity.getLookAngle().y
									: 0),
					(((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:dashing")))) / 3d)
							* entity.getLookAngle().z));
		}
	}
}