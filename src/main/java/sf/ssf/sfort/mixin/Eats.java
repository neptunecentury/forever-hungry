package sf.ssf.sfort.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;

@Mixin(PlayerEntity.class)
public class Eats{
	@Dynamic
	byte foodDelay = 0;
	@Shadow
	protected HungerManager hungerManager = new HungerManager();
	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo info) {
		if (foodDelay > 0) {
			--foodDelay;
		}
	}
	@Inject(method = "canConsume", at = @At("HEAD"), cancellable = true)
	public void canConsume(boolean ignoreHunger, CallbackInfoReturnable info) {
		if (foodDelay==0 && !hungerManager.isNotFull()){
			info.setReturnValue(true);
			foodDelay = 65;
			info.cancel();
		}
	}
}
