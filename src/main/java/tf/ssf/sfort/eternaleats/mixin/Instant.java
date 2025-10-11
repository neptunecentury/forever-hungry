package tf.ssf.sfort.eternaleats.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Item.class)
public class Instant {
	@Inject(method="getMaxUseTime(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)I", at=@At(value="HEAD"), cancellable = true)
	private void getMaxUseTime(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> info) {
		ConsumableComponent foodComponent = (ConsumableComponent)stack.get(DataComponentTypes.CONSUMABLE);
		if (foodComponent != null) info.setReturnValue(foodComponent.consumeSeconds() <= 1.55f ? Config.eatDurationY : Config.eatDurationX);
	}
}
