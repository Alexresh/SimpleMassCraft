package ru.obabok.simplemasscraft.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SimpleMassCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(SimpleMassCraftConfig.class, JanksonConfigSerializer::new);
        ClientTickEvents.END_CLIENT_TICK.register(RecipeAutoClick::tick);
    }
}
