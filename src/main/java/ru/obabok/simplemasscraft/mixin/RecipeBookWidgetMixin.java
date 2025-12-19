package ru.obabok.simplemasscraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.recipe.NetworkRecipeId;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.obabok.simplemasscraft.client.RecipeAutoClick;


@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {
    @Shadow private @Nullable NetworkRecipeId selectedRecipeId;
    @Shadow protected MinecraftClient client;
    @Shadow @Final private GhostRecipe ghostRecipe;

    @Inject(method = "select", at = @At("HEAD"), cancellable = true)
    private void select(RecipeResultCollection results, NetworkRecipeId recipeId, CallbackInfoReturnable<Boolean> cir){
        if (results.isCraftable(recipeId)
                && Screen.hasAltDown()) {
            selectedRecipeId = recipeId;
            ghostRecipe.clear();
            RecipeAutoClick.startAutoClick(recipeId, client.player.currentScreenHandler.syncId);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
