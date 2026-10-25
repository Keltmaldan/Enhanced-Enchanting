package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.Comparator;

@EventBusSubscriber(Dist.CLIENT)
public class PrecisionDoProcedure {
	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		PacketDistributor.sendToServer(new PrecisionDoMessage());
		execute(event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	@EventBusSubscriber
	public record PrecisionDoMessage() implements CustomPacketPayload {
		public static final Type<PrecisionDoMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "procedure_precision_do"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PrecisionDoMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PrecisionDoMessage message) -> {
		}, (RegistryFriendlyByteBuf buffer) -> new PrecisionDoMessage());

		@Override
		public Type<PrecisionDoMessage> type() {
			return TYPE;
		}

		public static void handleData(final PrecisionDoMessage message, final IPayloadContext context) {
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
			EnhancedEnchantingMod.addNetworkMessage(PrecisionDoMessage.TYPE, PrecisionDoMessage.STREAM_CODEC, PrecisionDoMessage::handleData);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Vec3 vector = Vec3.ZERO;
		double d = 0;
		double xe = 0;
		double ye = 0;
		double ze = 0;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:precision")))) != 0) {
			vector = (entity.getLookAngle()).normalize();
			while (d < (entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(Attributes.ENTITY_INTERACTION_RANGE) ? _livingEntity4.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue() : 0) - 0.1) {
				xe = x + d * vector.x();
				ye = entity.getEyeHeight() / 2 + y + d * vector.y();
				ze = z + d * vector.z();
				if ((world.getBlockState(BlockPos.containing(xe, ye, ze))).isCollisionShapeFullBlock(world, BlockPos.containing(0, 0, 0))) {
					return;
				}
				{
					final Vec3 _center = new Vec3(xe, ye, ze);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator != null && !(entityiterator == entity)) {
							entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.PLAYER_ATTACK), entity), (float) ((entity instanceof Player _plr ? _plr.getAttackStrengthScale(0) : 0)
									* (entity instanceof LivingEntity _livingEntity14 && _livingEntity14.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity14.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0)));
							return;
						}
					}
				}
				d = d + 0.1;
			}
		}
	}
}