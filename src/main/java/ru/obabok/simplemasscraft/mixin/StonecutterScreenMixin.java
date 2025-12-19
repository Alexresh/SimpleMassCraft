package ru.obabok.simplemasscraft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.StonecutterScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.obabok.simplemasscraft.client.RecipeAutoClick;

@Mixin(StonecutterScreen.class)
public class StonecutterScreenMixin {

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickButton(II)V"))
    private void mouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 4) int slot){
            ScreenHandler handler = MinecraftClient.getInstance().player.currentScreenHandler;
            if (handler instanceof StonecutterScreenHandler stonecutter) {
                int recipeIndex = stonecutter.getSelectedRecipe();
                if (recipeIndex >= 0) {
                    RecipeAutoClick.startAutoClickForStonecutter(handler.syncId, handler.getSlot(0).getStack());
                }
            }

    }

}
