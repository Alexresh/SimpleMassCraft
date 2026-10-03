package ru.obabok.simplemasscraft.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.obabok.simplemasscraft.client.RecipeAutoClick;


@Mixin(RecipeBookComponent.class)
public class RecipeBookWidgetMixin {
    //@Shadow private @Nullable NetworkRecipeId selectedRecipeId;
    //@Shadow protected MinecraftClient client;
    //@Shadow @Final private GhostRecipe ghostRecipe;

    @Shadow
    protected Minecraft minecraft;

    @Shadow
    @Final
    private GhostSlots ghostSlots;

    @Shadow
    private @Nullable RecipeDisplayId lastRecipe;

    @Inject(method = "tryPlaceRecipe", at = @At("HEAD"), cancellable = true)
    private void select(RecipeCollection recipeCollection, RecipeDisplayId recipe, boolean useMaxItems, CallbackInfoReturnable<Boolean> cir){
        if (recipeCollection.isCraftable(recipe)
                && minecraft.hasAltDown()) {
            lastRecipe = recipe;
            ghostSlots.clear();
            RecipeAutoClick.startAutoClick(recipe, minecraft.player.containerMenu.containerId);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
