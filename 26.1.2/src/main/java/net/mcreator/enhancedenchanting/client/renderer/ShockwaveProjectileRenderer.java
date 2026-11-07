package net.mcreator.enhancedenchanting.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;

import net.mcreator.enhancedenchanting.entity.ShockwaveProjectileEntity;
import net.mcreator.enhancedenchanting.client.model.Modelchockwave;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class ShockwaveProjectileRenderer extends EntityRenderer<ShockwaveProjectileEntity, LivingEntityRenderState> {
	private static final Identifier texture = Identifier.parse("enhanced_enchanting:textures/entities/shockwave_.png");
	private final Modelchockwave model;

	public ShockwaveProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new Modelchockwave(context.bakeLayer(Modelchockwave.LAYER_LOCATION));
	}

	@Override
	public void submit(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90));
		poseStack.mulPose(Axis.ZP.rotationDegrees(90 + state.xRot));
		model.setupAnim(state);
		submitNodeCollector.submitModel(this.model, state, poseStack, texture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
		poseStack.popPose();
		super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(ShockwaveProjectileEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
	}
}