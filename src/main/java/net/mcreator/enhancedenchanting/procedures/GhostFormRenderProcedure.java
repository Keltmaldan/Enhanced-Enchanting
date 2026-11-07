package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.model.EntityModel;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import javax.annotation.Nullable;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(Dist.CLIENT)
public class GhostFormRenderProcedure {
	@SubscribeEvent
	public static void onEntityRendered(RenderLivingEvent.Pre event) {
		execute(event, event.getEntity());
	}

	public static void renderEntity(RenderLivingEvent entityRenderEvent, LivingEntity renderEntity, EntityModel model, VertexConsumer vertexConsumer) {
		PoseStack poseStack = entityRenderEvent.getPoseStack();
		entityRenderEvent.getRenderer().getModel().copyPropertiesTo(model);
		LivingEntity eventEntity_ = entityRenderEvent.getEntity();
		model.young = eventEntity_.isBaby();
		float partialTick = entityRenderEvent.getPartialTick();
		float limbSwing = eventEntity_.walkAnimation.position(partialTick);
		float limbSwingAmount = eventEntity_.walkAnimation.speed(partialTick);
		float ageInTicks = eventEntity_.tickCount + partialTick;
		float interpolatedBodyYaw = Mth.rotLerp(partialTick, eventEntity_.yBodyRotO, eventEntity_.yBodyRot);
		float interpolatedHeadYaw = Mth.rotLerp(partialTick, eventEntity_.yHeadRotO, eventEntity_.yHeadRot);
		float netHeadYaw = interpolatedHeadYaw - interpolatedBodyYaw;
		float headPitch = Mth.lerp(partialTick, eventEntity_.xRotO, eventEntity_.getXRot());
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - interpolatedBodyYaw));
		if (eventEntity_.deathTime > 0) {
			float f = ((float) eventEntity_.deathTime + partialTick - 1f) / 20f * 1.6f;
			f = Mth.sqrt(f);
			if (f > 1f) {
				f = 1f;
			}
			poseStack.mulPose(Axis.ZP.rotationDegrees(f * 90f));
		}
		poseStack.scale(-1, -1, 1);
		poseStack.translate(0.0D, -1.501, 0.0D);
		model.prepareMobModel(renderEntity, limbSwing, limbSwingAmount, partialTick);
		model.setupAnim(renderEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(poseStack, vertexConsumer, entityRenderEvent.getPackedLight(), LivingEntityRenderer.getOverlayCoords(eventEntity_, 0));
		poseStack.popPose();
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(EnhancedEnchantingModMobEffects.GHOST_FORM)) {
			if (event instanceof ICancellableEvent _cancellable) {
				_cancellable.setCanceled(true);
			}
		}
	}
}