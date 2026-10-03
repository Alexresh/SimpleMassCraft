package ru.obabok.simplemasscraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

public class RecipeAutoClick {
    private static boolean autoClickActive = false;
    private static RecipeDisplayId autoClickRecipeId = null;
    private static int currentHandlerSyncId = -1;
    private static int timer;
    private static int currentTime = 0;
    private static ItemStack expectedInput = ItemStack.EMPTY;
    private static int recipeIndex;


    public static void tick(Minecraft client){
        if (autoClickActive && client.player != null && client.gameMode != null) {
            if (!client.hasAltDown()) {
                autoClickActive = false;
                return;
            }

            AbstractContainerMenu handler = client.player.containerMenu;
            if (handler.containerId != currentHandlerSyncId) {
                autoClickActive = false;
                return;
            }
            if (handler instanceof StonecutterMenu stonecutter) {
                if (!client.hasAltDown()) {
                    return;
                }


                ItemStack inputSlot = handler.getSlot(0).getItem();
                if (inputSlot.isEmpty() && !expectedInput.isEmpty()) {

                    int totalSlots = handler.slots.size();
                    int inventoryStart = Math.max(0, totalSlots - 36);

                    for (int guiSlot = inventoryStart; guiSlot < totalSlots; guiSlot++) {
                        ItemStack stack = handler.getSlot(guiSlot).getItem();
                        if (!stack.isEmpty() && ItemStack.isSameItem(stack, expectedInput)) {
                            client.gameMode.handleContainerInput(currentHandlerSyncId, guiSlot, 0, ContainerInput.PICKUP, client.player);
                            client.gameMode.handleContainerInput(currentHandlerSyncId, 0, 0, ContainerInput.PICKUP, client.player);

                            if (!handler.getCarried().isEmpty()) { //check!
                                client.gameMode.handleContainerInput(currentHandlerSyncId, -999, 0, ContainerInput.PICKUP, client.player);
                            }
                            break;
                        }
                    }
                }


                recipeIndex = stonecutter.getSelectedRecipeIndex() == -1 ? recipeIndex : stonecutter.getSelectedRecipeIndex();
                //if (recipeIndex < 0) return;
                client.gameMode.handleInventoryButtonClick(currentHandlerSyncId, recipeIndex);

                ItemStack result = handler.getSlot(1).getItem();
                if (result.isEmpty()) return;
                client.gameMode.handleContainerInput(currentHandlerSyncId, 1, 1, ContainerInput.THROW, client.player);
                return;
            }
            if(currentTime >= timer){
                int resultSlotId = findResultSlot(handler);
                if (resultSlotId == -1) return;
                client.gameMode.handlePlaceRecipe(currentHandlerSyncId, autoClickRecipeId, true);
                client.gameMode.handleContainerInput(currentHandlerSyncId, resultSlotId, 1, ContainerInput.THROW, client.player);
                currentTime = 0;
            }
            currentTime++;
        }
    }

    private static int findResultSlot(AbstractContainerMenu handler){
        for (int i = 0; i < handler.slots.size(); i++) {
            if (handler.slots.get(i) instanceof ResultSlot) {
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

    public static void startAutoClick(RecipeDisplayId recipeId, int syncId) {
        autoClickActive = true;
        autoClickRecipeId = recipeId;
        currentHandlerSyncId = syncId;
        timer = SimpleMassCraftConfig.get().massCraftDelay;
    }
}
