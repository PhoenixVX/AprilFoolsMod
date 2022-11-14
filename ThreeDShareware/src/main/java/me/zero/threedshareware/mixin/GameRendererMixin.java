package me.zero.threedshareware.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow public abstract Minecraft getMinecraft();

    private PostChain field_19253;

    public double field_19256 = 10.0D;
    private int field_19254 = 5;
    private int field_19255 = 0;

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"))
    public void injectShader (ResourceManager resourceManager, CallbackInfo ci) {
        if (this.field_19253 != null) {
            this.field_19253.close();
        }

        this.field_19253 = null;

        try {
            this.field_19253 = new PostChain(this.getMinecraft().getTextureManager(), resourceManager, this.getMinecraft().getMainRenderTarget(), new ResourceLocation("shaders/post/bits.json"));
            this.field_19253.resize(this.getMinecraft().getWindow().getWidth(), this.getMinecraft().getWindow().getHeight());
        } catch (Exception var3) {
            throw new IllegalStateException("Failed to load only shader that matters", var3);
        }
    }

    @Inject(method = "resize", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/LevelRenderer.resize(II)V"))
    public void resizeInjectedShader (int width, int height, CallbackInfo ci) {
        if (this.field_19253 != null) {
            this.field_19253.resize(width, height);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void injectClear (float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        GlStateManager._clear(16640, Minecraft.ON_OSX);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void injectShaderProcess (float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        if (this.getMinecraft().getOverlay() == null) {
            this.method_20281();
            if (this.field_19253 != null) {
                PoseStack poseStack = RenderSystem.getModelViewStack();
                poseStack.pushPose();
                poseStack.setIdentity();
                //GlStateManager.matrixMode(5890);
                //GlStateManager.pushMatrix();
                //GlStateManager.loadIdentity();
                this.field_19253.process(partialTicks);
                poseStack.popPose();
                //GlStateManager.popMatrix();
                //GlStateManager.matrixMode(5888);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/util/Mth.lerp(FFF)F"))
    public void injectMethod20280 (float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        this.method_20280();
    }

    private void method_20280() {
        GlStateManager._disableTexture();
        GlStateManager._enableDepthTest();
        GlStateManager._colorMask(false, false, false, false);
        int var1 = this.getMinecraft().getWindow().getGuiScaledWidth();
        int var2 = this.getMinecraft().getWindow().getGuiScaledHeight();
        Tesselator var3 = Tesselator.getInstance();
        BufferBuilder var4 = var3.getBuilder();
        //var4.begin(7, DefaultVertexFormat.POSITION_COLOR);
        var4.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        var4.vertex(this.field_19256, 10.0D + this.field_19256, 10.0D).color(0, 0, 0, 255).endVertex();
        var4.vertex(this.field_19256, (double)var2 - this.field_19256, 10.0D).color(0, 0, 0, 255).endVertex();
        var4.vertex((double)var1 - this.field_19256, (double)var2 - this.field_19256, 10.0D).color(0, 0, 0, 255).endVertex();
        var4.vertex((double)var1 - this.field_19256, 10.0D + this.field_19256, 10.0D).color(0, 0, 0, 255).endVertex();
        var3.end();
        GlStateManager._colorMask(true, true, true, true);
        GlStateManager._enableTexture();
        //this.getMinecraft().getTextureManager()._bind(Screen.BACKGROUND_LOCATION);
        RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        var4.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        var4.vertex(0.0D, var2, 0.0D).uv(0.0F, (var2 / 32.0F)).endVertex();
        var4.vertex(var1, var2, 0.0D).uv((var1 / 32.0F), (var2 / 32.0F + 0.0F)).endVertex();
        var4.vertex(var1, 0.0D, 0.0D).uv((var1 / 32.0F), 0.0F).endVertex();
        var4.vertex(0.0D, 0.0D, 0.0D).uv(0.0F, 0.0F).endVertex();
        var3.end();
        GlStateManager._disableDepthTest();
        GlStateManager._clear(256, Minecraft.ON_OSX);
    }


    private void method_20281() {
        // GlStateManager.disableLighting();
        Lighting.setupFor3DItems();
        int var1 = this.getMinecraft().getWindow().getGuiScaledWidth();
        Font var2 = this.getMinecraft().font;
        PoseStack poseStack = RenderSystem.getModelViewStack();
        Gui.fill(poseStack, 0, 0, var1, 9 + 4, -65536);
        if ((this.field_19255 & 16) != 0) {
            var2.draw(poseStack, "UNREGISTERED VERSION", (float)this.field_19255, 4.0F, -16711936);
        }

        if (this.field_19254++ > 10) {
            this.field_19254 = 0;
            this.field_19255 += 5;
            if (this.field_19255 > var1) {
                this.field_19255 = -var2.width("UNREGISTERED VERSION");
            }
        }

    }
}
