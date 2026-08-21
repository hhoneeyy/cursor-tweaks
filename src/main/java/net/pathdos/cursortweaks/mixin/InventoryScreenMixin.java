package net.pathdos.cursortweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.pathdos.cursortweaks.config.Gui;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.DoubleBuffer;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryScreenMixin {

    private static float lastMouseX = -1;
    private static float lastMouseY = -1;
    private final DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
    private final DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);

    @Inject(method = "init", at = @At("HEAD"))
    private void injectInitHead(CallbackInfo ci) {
        if (!((Object) this instanceof InventoryScreen)) return;

        long windowHandle = Minecraft.getInstance().getWindow().handle();
        if (Gui.get().dropdownChoice == Gui.DropdownOption.cursormemory && lastMouseX != -1) {
            GLFW.glfwSetCursorPos(windowHandle, lastMouseX, lastMouseY);
        }
        if (Gui.get().dropdownChoice == Gui.DropdownOption.customposition) {
            GLFW.glfwSetCursorPos(windowHandle, Gui.get().xStaticPosition, Gui.get().yStaticPosition);
        }
    }

    @Inject(method = "containerTick", at = @At("TAIL"))
    private void injectContainerTick(CallbackInfo ci) {
        if (!((Object) this instanceof InventoryScreen)) return;

        xPos.rewind();
        yPos.rewind();

        long windowHandle = Minecraft.getInstance().getWindow().handle();
        GLFW.glfwGetCursorPos(windowHandle, xPos, yPos);
        if (Gui.get().recordCursorStaticPosition) {
            Gui.get().xStaticPosition = (int) xPos.get();
            Gui.get().yStaticPosition = (int) yPos.get();
            Gui.save();
            return;
        }
        lastMouseX = (float) xPos.get();
        lastMouseY = (float) yPos.get();
    }
}