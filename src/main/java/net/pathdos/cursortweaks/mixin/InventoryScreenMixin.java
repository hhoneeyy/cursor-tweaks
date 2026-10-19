package net.pathdos.cursortweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.pathdos.cursortweaks.config.Gui;
import org.lwjgl.BufferUtils;
import org.lwjgl.sdl.SDLMouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryScreenMixin {

    private static float lastMouseX = -1;
    private static float lastMouseY = -1;
    // Minecraft 26.3 replaced GLFW with SDL; SDL reports/accepts the cursor position as floats.
    private final FloatBuffer xPos = BufferUtils.createFloatBuffer(1);
    private final FloatBuffer yPos = BufferUtils.createFloatBuffer(1);

    @Inject(method = "init", at = @At("HEAD"))
    private void injectInitHead(CallbackInfo ci) {
        if (!((Object) this instanceof InventoryScreen)) return;

        long windowHandle = Minecraft.getInstance().getWindow().handle();
        if (Gui.get().dropdownChoice == Gui.DropdownOption.cursormemory && lastMouseX != -1) {
            SDLMouse.SDL_WarpMouseInWindow(windowHandle, lastMouseX, lastMouseY);
        }
        if (Gui.get().dropdownChoice == Gui.DropdownOption.customposition) {
            SDLMouse.SDL_WarpMouseInWindow(windowHandle, Gui.get().xStaticPosition, Gui.get().yStaticPosition);
        }
    }

    @Inject(method = "containerTick", at = @At("TAIL"))
    private void injectContainerTick(CallbackInfo ci) {
        if (!((Object) this instanceof InventoryScreen)) return;

        xPos.clear();
        yPos.clear();

        SDLMouse.SDL_GetMouseState(xPos, yPos);
        if (Gui.get().recordCursorStaticPosition) {
            Gui.get().xStaticPosition = (int) xPos.get(0);
            Gui.get().yStaticPosition = (int) yPos.get(0);
            Gui.save();
            return;
        }
        lastMouseX = xPos.get(0);
        lastMouseY = yPos.get(0);
    }
}
