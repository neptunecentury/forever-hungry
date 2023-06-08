package tf.ssf.sfort.eternaleats.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Item.class)
public class Instant {
	@Inject(method="getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", at=@At(value="HEAD"), cancellable = true)
	private void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		if (stack.getItem().isFood()) info.setReturnValue(Objects.requireNonNull(stack.getItem().getFoodComponent()).isSnack()? Config.eatDurationY : Config.eatDurationX);
	}
}
