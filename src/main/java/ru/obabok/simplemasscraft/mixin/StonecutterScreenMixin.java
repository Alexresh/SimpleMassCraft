package ru.obabok.simplemasscraft.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.obabok.simplemasscraft.client.RecipeAutoClick;

@Mixin(StonecutterScreen.class)
public class StonecutterScreenMixin {

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleInventoryButtonClick(II)V"))
    private void mouseClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir){
        AbstractContainerMenu handler = Minecraft.getInstance().player.containerMenu;
        if (handler instanceof StonecutterMenu stonecutter) {
            int recipeIndex = stonecutter.getSelectedRecipeIndex();
            if (recipeIndex >= 0) {
                RecipeAutoClick.startAutoClickForStonecutter(handler.containerId, handler.getSlot(0).getItem());
            }
        }

    }

}
