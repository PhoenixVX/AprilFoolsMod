package me.zero.threedshareware.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zero.aprilfools.AprilFoolsMod;
import me.zero.threedshareware.ThreeDSharewareMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow
    protected abstract PlayerEntity getCameraPlayer ();
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow @Final
    private MinecraftClient client;
    @Shadow @Final private static Identifier WIDGETS_TEXTURE;
    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);
    @Shadow @Final private Random random;
    @Shadow public abstract int getTicks ();
    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private ItemStack currentStack;
    @Shadow private int heldItemTooltipFade;
    private static final Identifier HOT_BAR_TEXTURE = new Identifier(ThreeDSharewareMod.MOD_ID, "textures/gui/hotbar.png");

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled) {
            ci.cancel();
            PlayerEntity playerEntity = this.getCameraPlayer();
            if (playerEntity == null) {
                return;
            }

            int width = this.scaledWidth / 2;
            int hotBarWidth = width - 128;
            int height = this.scaledHeight - 64;
            RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, HOT_BAR_TEXTURE);
            //this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
            float offset = this.getZOffset();
            this.setZOffset(-660);
            this.drawTexture(matrices, hotBarWidth, height, 0, 64, 256, 64);
            matrices.push();
            matrices.translate(0.0f, 0.0f, -200.0f);
            int time = (int)(Util.getMeasuringTimeMs() / 2000L % 4L);
            float[] ticks = new float[]{-20.0f, 0.0f, 20.0f, 0.0f};
            if (this.client.getEntityRenderDispatcher().camera != null) {
                InventoryScreen.drawEntity(hotBarWidth + 128, height + 135, 64, ticks[time], 0.0f, playerEntity);
            }
            matrices.pop();
            RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, HOT_BAR_TEXTURE);
            //this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
            this.setZOffset(-90);
            this.drawTexture(matrices, hotBarWidth, height, 0, 0, 256, 64);
            RenderSystem.setShaderTexture(1, WIDGETS_TEXTURE);
            //this.client.getTextureManager().bindTexture(WIDGETS_TEXTURE);
            int slotX = playerEntity.getInventory().selectedSlot % 3;
            int slotY = playerEntity.getInventory().selectedSlot / 3;
            this.drawTexture(matrices,hotBarWidth + 194 + slotX * 20 - 1, height + 2 + slotY * 20 - 1, 0, 22, 24, 22);
            this.setZOffset((int) offset);
            //RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            DiffuseLighting.enableGuiDepthLighting(); // TODO: enableForItems()?
            for (int i = 0; i < 9; ++i) {
                int var12 = hotBarWidth + 194 + i % 3 * 20 + 2;
                int var13 = height + 2 + i / 3 * 20 + 2;
                this.renderHotbarItem(var12, var13, tickDelta, playerEntity, playerEntity.getInventory().main.get(i), i);
            }
            if (this.client.options.getAttackIndicator().getValue().equals(AttackIndicator.HOTBAR)) {
                float var15 = this.client.player.getAttackCooldownProgress(0.0F);
                if (var15 < 1.0F) {
                    int var12 = height + 2 + slotY * 20;
                    int var13 = hotBarWidth + 194 + slotX * 22;
                    RenderSystem.setShaderTexture(2, DrawableHelper.GUI_ICONS_TEXTURE);
                    //this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
                    int var14 = (int)(var15 * 19.0F);
                    RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.drawTexture(matrices, var13, var12, 0, 94, 18, 18);
                    this.drawTexture(matrices, var13, var12 + 18 - var14, 18, 112 - var14, 18, var14);
                }
            }

            DiffuseLighting.disableGuiDepthLighting();
            //DiffuseLighting.disable();
            //RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
        }
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled) {
            ci.cancel();
            PlayerEntity player = this.getCameraPlayer();
            if (player != null) {
                int health = MathHelper.ceil(player.getHealth());
                this.random.setSeed(this.getTicks() * 312871L);
                HungerManager var3 = player.getHungerManager();
                int foodLevel = var3.getFoodLevel();
                int width = this.scaledWidth / 2;
                int posX = width + 91;
                int height = this.scaledHeight - 74;
                int absorptionAmount = MathHelper.ceil(player.getAbsorptionAmount());
                this.client.getProfiler().push("armor");
                int armor = player.getArmor();
                String formattedArmorString = String.format("%02d%%", armor * 100 / 20);
                int widthOfArmorString = this.getTextRenderer().getWidth(formattedArmorString);
                int var13 = width - 128;
                int var14 = this.scaledHeight - 64;
                this.getTextRenderer().draw(matrices, formattedArmorString, (float)(var13 + 25 + (23 - widthOfArmorString)), (float)(var14 + 41), -1);
                this.client.getProfiler().swap("health");
                float var15 = player.getMaxHealth();
                String var16 = String.format("%02.0f%%", (float)(health + absorptionAmount) / var15 * 100.0F);
                int var17 = this.getTextRenderer().getWidth(var16);
                this.getTextRenderer().draw(matrices, var16, (float)(var13 + 25 + (23 - var17)), (float)(var14 + 8), absorptionAmount > 0 ? -256 : -65536);
                RenderSystem.setShaderTexture(0, HOT_BAR_TEXTURE);
                //this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
                int posY = height - 10;
                this.client.getProfiler().swap("food");
                int var19 = 32 * foodLevel / 20;
                this.drawTexture(matrices, var13 + 161, var14 + 14, 0, 128, 32, var19);
                posY -= 10;
                RenderSystem.setShaderTexture(1, DrawableHelper.GUI_ICONS_TEXTURE);
                //this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
                this.client.getProfiler().swap("air");
                int currentAir = player.getAir();
                int maximumAir = player.getMaxAir();
                if (player.isSubmergedIn(FluidTags.WATER) || currentAir < maximumAir) {
                    int var23 = MathHelper.ceil((double)(currentAir - 2) * 10.0D / (double)maximumAir);
                    int var24 = MathHelper.ceil((double)currentAir * 10.0D / (double)maximumAir) - var23;

                    for(int var25 = 0; var25 < var23 + var24; ++var25) {
                        if (var25 < var23) {
                            this.drawTexture(matrices, posX - var25 * 8 - 9, posY + 15, 16, 18, 9, 9);
                        } else {
                            this.drawTexture(matrices, posX - var25 * 8 - 9, posY + 15, 25, 18, 9, 9);
                        }
                    }
                }

                this.client.getProfiler().pop();
            }
        }
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    public void renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled) {
            ci.cancel();

            this.client.getProfiler().push("selectedItemName");
            if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
                MutableText mutableText = Text.empty().append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
                if (this.currentStack.hasCustomName()) {
                    mutableText.formatted(Formatting.ITALIC);
                }

                int i = this.getTextRenderer().getWidth(mutableText);
                int j = (this.scaledWidth - i) / 2;
                int k = this.scaledHeight - 84;
                if (!this.client.interactionManager.hasStatusBars()) {
                    k += 14;
                }

                int l = (int) ((float) this.heldItemTooltipFade * 256.0F / 10.0F);
                if (l > 255) {
                    l = 255;
                }

                if (l > 0) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    fill(matrices, j - 2, k - 2, j + i + 2, k + 9 + 2, this.client.options.getTextBackgroundColor(0));
                    this.getTextRenderer().drawWithShadow(matrices, mutableText, j, k, 16777215 + (l << 24));
                    RenderSystem.disableBlend();
                }
            }

            this.client.getProfiler().pop();
        }
    }

    @ModifyArg(
            method = "renderExperienceBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
            index = 2
    )
    private int modifyYPosition(int original) {
        return AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled ? this.scaledHeight - 70 : original;
    }

    @ModifyArg(
            method = "renderExperienceBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"),
            index = 3
    )
    private float modifyYPositionOfExperienceLevel(float original) {
        return AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled ? this.scaledHeight - 80.0F : original;
    }
}