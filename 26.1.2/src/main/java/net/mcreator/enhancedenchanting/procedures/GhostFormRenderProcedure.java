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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModRenderStateModifiers;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import javax.annotation.Nullable;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(Dist.CLIENT)
public class GhostFormRenderProcedure {
	@SubscribeEvent
	public static void onEntityRendered(RenderLivingEvent.Pre event) {
		Entity entity = (Entity) event.getRenderState().getRenderData(EnhancedEnchantingModRenderStateModifiers.LIVING_ENTITY);
		execute(event, entity);
	}

	public static void renderEntity(RenderLivingEvent entityRenderEvent, EntityModel model, RenderType renderType, LivingEntityRenderState state) {
		PoseStack poseStack = entityRenderEvent.getPoseStack();
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.bodyRot));
		if (state.deathTime > 0) {
			float f = ((float) state.deathTime + entityRenderEvent.getPartialTick() - 1f) / 20f * 1.6f;
			f = Mth.sqrt(f);
			if (f > 1f) {
				f = 1f;
			}
			poseStack.mulPose(Axis.ZP.rotationDegrees(f * 90f));
		}
		poseStack.scale(-1, -1, 1);
		poseStack.translate(0.0D, -1.501, 0.0D);
		model.setupAnim(state);
		model.renderToBuffer(poseStack, Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType), state.lightCoords, LivingEntityRenderer.getOverlayCoords(state, 0));
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