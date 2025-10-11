package tf.ssf.sfort.eternaleats.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class Eats extends LivingEntity {
	@Dynamic
	int foodDelay = 0;
	@Shadow
	protected HungerManager hungerManager = new HungerManager();

	protected Eats(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo info) {
		if (foodDelay > 0) {
			--foodDelay;
		}
	}
	@Inject(method = "canConsume", at = @At("HEAD"), cancellable = true)
	public void canConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> info) {
		if ((!hungerManager.isNotFull() || tf.ssf.sfort.eternaleats.mixin.Config.forceDelay) && foodDelay==0){
			info.setReturnValue(true);
			foodDelay = tf.ssf.sfort.eternaleats.mixin.Config.delay;
			foodDelay += foodDelay* tf.ssf.sfort.eternaleats.mixin.Config.difficultyScale*this.getWorld().getDifficulty().getId();
			info.cancel();
		}
	}
}
