package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.NeutralMob;

@Mixin(Entity.class)
public class OmniscientDo2Mixin {
	@Inject(
		method = "getTeamColor",
		at = @At("RETURN"),
		cancellable = true
	)
	private void change(CallbackInfoReturnable<Integer> cir) {
		Entity entity = (Entity) (Object) this;
		if (entity instanceof LivingEntity living) {
			if (living.hasEffect(MobEffects.GLOWING))
				return;
			if (living instanceof Monster)
				cir.setReturnValue(0xff0000);
			else if (living instanceof Player || living instanceof NeutralMob)
				cir.setReturnValue(0xffff00);
			else
				cir.setReturnValue(0x00ff00);
		}
	}
}