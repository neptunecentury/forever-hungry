package sf.ssf.sfort.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;

@Mixin(PlayerEntity.class)
public class Eats extends LivingEntity {
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
	public void canConsume(boolean ignoreHunger, CallbackInfoReturnable info) {
		if ((!hungerManager.isNotFull() || sf.ssf.sfort.Eats.forceDelay) && foodDelay==0){
			info.setReturnValue(true);
			foodDelay = sf.ssf.sfort.Eats.delay;
			foodDelay += foodDelay*sf.ssf.sfort.Eats.difficultyScale*this.world.getDifficulty().getId();
			info.cancel();
		}
	}

	@Surrogate
	public Iterable<ItemStack> getArmorItems() { return null; }
	@Surrogate
	public ItemStack getEquippedStack(EquipmentSlot slot) { return null; }
	@Surrogate
	public void equipStack(EquipmentSlot slot, ItemStack stack) {}
	@Surrogate
	public Arm getMainArm() { return null; }
}
