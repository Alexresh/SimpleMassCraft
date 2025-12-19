package ru.obabok.simplemasscraft.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.SlotActionType;

public class RecipeAutoClick {
    private static boolean autoClickActive = false;
    private static NetworkRecipeId autoClickRecipeId = null;
    private static int currentHandlerSyncId = -1;
    private static int timer;
    private static int currentTime = 0;
    private static ItemStack expectedInput = ItemStack.EMPTY;
    private static int recipeIndex;


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
            if (handler instanceof StonecutterScreenHandler stonecutter) {
                if (!Screen.hasAltDown()) {
                    return;
                }


                ItemStack inputSlot = handler.getSlot(0).getStack();
                if (inputSlot.isEmpty() && !expectedInput.isEmpty()) {

                    int totalSlots = handler.slots.size();
                    int inventoryStart = Math.max(0, totalSlots - 36);

                    for (int guiSlot = inventoryStart; guiSlot < totalSlots; guiSlot++) {
                        ItemStack stack = handler.getSlot(guiSlot).getStack();
                        if (!stack.isEmpty() && ItemStack.areEqual(stack, expectedInput)) {
                            client.interactionManager.clickSlot(currentHandlerSyncId, guiSlot, 0, SlotActionType.PICKUP, client.player);
                            client.interactionManager.clickSlot(currentHandlerSyncId, 0, 0, SlotActionType.PICKUP, client.player);

                            if (!handler.getCursorStack().isEmpty()) {
                                client.interactionManager.clickSlot(currentHandlerSyncId, -999, 0, SlotActionType.PICKUP, client.player);
                            }
                            break;
                        }
                    }
                }


                recipeIndex = stonecutter.getSelectedRecipe() == -1 ? recipeIndex : stonecutter.getSelectedRecipe();
                //if (recipeIndex < 0) return;
                client.interactionManager.clickButton(currentHandlerSyncId, recipeIndex);

                ItemStack result = handler.getSlot(1).getStack();
                if (result.isEmpty()) return;
                client.interactionManager.clickSlot(
                        currentHandlerSyncId,
                        1,
                        1,
                        SlotActionType.THROW,
                        client.player
                );

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
    public static void startAutoClickForStonecutter(int syncId, ItemStack input) {
        autoClickActive = true;
        currentHandlerSyncId = syncId;
        expectedInput = input.isEmpty() ? ItemStack.EMPTY : input.copy();
    }

    public static void startAutoClick(NetworkRecipeId recipeId, int syncId) {
        autoClickActive = true;
        autoClickRecipeId = recipeId;
        currentHandlerSyncId = syncId;
        timer = SimpleMassCraftConfig.get().massCraftDelay;
    }
}
