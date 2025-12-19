package ru.obabok.simplemasscraft.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.SlotActionType;

public class RecipeAutoClick {
    private static boolean autoClickActive = false;
    private static NetworkRecipeId autoClickRecipeId = null;
    private static int currentHandlerSyncId = -1;
    private static int timer;
    private static int currentTime = 0;

    public static void tick(MinecraftClient client){
        if (autoClickActive && client.player != null && client.interactionManager != null) {
            if (!Screen.hasAltDown()) {
                autoClickActive = false;
                return;
            }
            ScreenHandler handler = client.player.currentScreenHandler;
            if (handler.syncId != currentHandlerSyncId) {
                autoClickActive = false;
                return;
            }
            if(currentTime >= timer){
                int resultSlotId = findResultSlot(handler);
                if (resultSlotId == -1) return;
                client.interactionManager.clickRecipe(currentHandlerSyncId, autoClickRecipeId, true);
                client.interactionManager.clickSlot(currentHandlerSyncId, resultSlotId, 1, SlotActionType.THROW, client.player);
                currentTime = 0;
            }
            currentTime++;
        }
    }

    private static int findResultSlot(ScreenHandler handler){
        for (int i = 0; i < handler.slots.size(); i++) {
            if (handler.slots.get(i) instanceof CraftingResultSlot) {
                return i;
            }
        }
        return -1;
    }

    public static void startAutoClick(NetworkRecipeId recipeId, int syncId) {
        autoClickActive = true;
        autoClickRecipeId = recipeId;
        currentHandlerSyncId = syncId;
        timer = SimpleMassCraftConfig.get().massCraftDelay;
    }
}
