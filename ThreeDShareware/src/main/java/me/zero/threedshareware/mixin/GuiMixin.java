package me.zero.threedshareware.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.zero.aprilfools.AprilFoolsMod;
import me.zero.threedshareware.ThreeDSharewareMod;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public abstract class GuiMixin extends GuiComponent {

    private static final ResourceLocation HOT_BAR_TEXTURE = new ResourceLocation(ThreeDSharewareMod.MOD_ID, "textures/gui/hotbar.png");
    @Shadow
    @Final
    private static ResourceLocation WIDGETS_LOCATION;
    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private RandomSource random;
    @Shadow
    private ItemStack lastToolHighlight;
    @Shadow
    private int toolHighlightTimer;

    @Shadow
    protected abstract Player getCameraPlayer();

    @Shadow
    public abstract int getGuiTicks();

    @Shadow
    public abstract Font getFont();

    @Shadow @Final private ItemRenderer itemRenderer;

    @Shadow public abstract void tick(boolean bl);

    @Shadow protected abstract void renderSlot(int i, int j, float f, Player player, ItemStack itemStack, int k);

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void renderHotbar(float tickDelta, PoseStack poseStack, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled) {
            ci.cancel();
            Player player = this.getCameraPlayer();
            if (player == null) {
                return;
            }

            int width = this.screenWidth / 2;
            int hotBarWidth = width - 128;
            int height = this.screenHeight - 64;
            RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, HOT_BAR_TEXTURE);
            float offset = this.getBlitOffset();
            this.setBlitOffset(-660);
            this.blit(poseStack, hotBarWidth, height, 0, 64, 256, 64);
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, -200.0f);
            int time = (int) (Util.getMillis() / 2000L % 4L);
            float[] ticks = new float[]{-20.0f, 0.0f, 20.0f, 0.0f};
            if (this.minecraft.getEntityRenderDispatcher().camera != null) {
                InventoryScreen.renderEntityInInventory(hotBarWidth + 128, height + 135, 64, ticks[time], 0.0f, player);
            }
            poseStack.popPose();
            RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, HOT_BAR_TEXTURE);
            this.setBlitOffset(-90);
            this.blit(poseStack, hotBarWidth, height, 0, 0, 256, 64);
            RenderSystem.setShaderTexture(1, WIDGETS_LOCATION);
            int slotX = player.getInventory().selected % 3;
            int slotY = player.getInventory().selected / 3;
            this.blit(poseStack, hotBarWidth + 194 + slotX * 20 - 1, height + 2 + slotY * 20 - 1, 0, 22, 24, 22);
            this.setBlitOffset((int) offset);
            //RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Lighting.setupFor3DItems();
            for (int slotIndex = 0; slotIndex < 9; ++slotIndex) {
                int var12 = hotBarWidth + 194 + slotIndex % 3 * 20 + 2;
                int var13 = height + 2 + slotIndex / 3 * 20 + 2;
                this.renderSlot(var12, var13, tickDelta, player, player.getInventory().items.get(slotIndex), slotIndex);
            }
            if (this.minecraft.options.attackIndicator().get().equals(AttackIndicatorStatus.HOTBAR)) {
                float attackStrength = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (attackStrength < 1.0F) {
                    int var12 = height + 2 + slotY * 20;
                    int var13 = hotBarWidth + 194 + slotX * 22;
                    RenderSystem.setShaderTexture(2, GuiComponent.GUI_ICONS_LOCATION);
                    int var14 = (int) (attackStrength * 19.0F);
                    RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.blit(poseStack, var13, var12, 0, 94, 18, 18);
                    this.blit(poseStack, var13, var12 + 18 - var14, 18, 112 - var14, 18, var14);
                }
            }

            Lighting.setupForFlatItems();
            //DiffuseLighting.disable();
            //RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
        }
    }

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    private void renderStatusBars(PoseStack poseStack, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled) {
            ci.cancel();
            Player player = this.getCameraPlayer();
            if (player != null) {
                int health = Mth.ceil(player.getHealth());
                this.random.setSeed(this.getGuiTicks() * 312871L);
                FoodData var3 = player.getFoodData();
                int foodLevel = var3.getFoodLevel();
                int width = this.screenWidth / 2;
                int posX = width + 91;
                int height = this.screenHeight - 74;
                int absorptionAmount = Mth.ceil(player.getAbsorptionAmount());
                this.minecraft.getProfiler().push("armor");
                int armor = player.getArmorValue();
                String formattedArmorString = String.format("%02d%%", armor * 100 / 20);
                int widthOfArmorString = this.getFont().width(formattedArmorString);
                int x = width - 128;
                int y = this.screenHeight - 64;
                this.getFont().draw(poseStack, formattedArmorString, (x + 25 + (23 - widthOfArmorString)), (y + 41), -1);
                this.minecraft.getProfiler().popPush("health");
                float playerMaxHealth = player.getMaxHealth();
                String var16 = String.format("%02.0f%%", (health + absorptionAmount) / playerMaxHealth * 100.0F);
                int var17 = this.getFont().width(var16);
                this.getFont().draw(poseStack, var16, (x + 25 + (23 - var17)), (y + 8), absorptionAmount > 0 ? -256 : -65536);
                RenderSystem.setShaderTexture(0, HOT_BAR_TEXTURE);
                int posY = height - 10;
                this.minecraft.getProfiler().popPush("food");
                int var19 = 32 * foodLevel / 20;
                this.blit(poseStack, x + 161, y + 14, 0, 128, 32, var19);
                posY -= 10;
                RenderSystem.setShaderTexture(1, GuiComponent.GUI_ICONS_LOCATION);
                this.minecraft.getProfiler().popPush("air");
                int currentAir = player.getAirSupply();
                int maximumAir = player.getMaxAirSupply();
                if (player.isEyeInFluid(FluidTags.WATER) || currentAir < maximumAir) {
                    int var23 = Mth.ceil((currentAir - 2) * 10.0D / maximumAir);
                    int var24 = Mth.ceil(currentAir * 10.0D / maximumAir) - var23;

                    for (int var25 = 0; var25 < var23 + var24; ++var25) {
                        if (var25 < var23) {
                            this.blit(poseStack, posX - var25 * 8 - 9, posY + 15, 16, 18, 9, 9);
                        } else {
                            this.blit(poseStack, posX - var25 * 8 - 9, posY + 15, 25, 18, 9, 9);
                        }
                    }
                }

                this.minecraft.getProfiler().pop();
            }
        }
    }

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"), cancellable = true)
    public void renderHeldItemTooltip(PoseStack poseStack, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled) {
            ci.cancel();

            this.minecraft.getProfiler().push("selectedItemName");
            if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
                MutableComponent mutableText = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color);
                if (this.lastToolHighlight.hasCustomHoverName()) {
                    mutableText.withStyle(ChatFormatting.ITALIC);
                }

                int textWidth = this.getFont().width(mutableText);
                int x = (this.screenWidth - textWidth) / 2;
                int y = this.screenHeight - 84;

                if (!this.minecraft.gameMode.canHurtPlayer()) {
                    y += 14;
                }

                int colorModifier = (int) (this.toolHighlightTimer * 256.0F / 10.0F);
                if (colorModifier > 255) {
                    colorModifier = 255;
                }

                if (colorModifier > 0) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    fill(poseStack, x - 2, y - 2, x + textWidth + 2, y + 9 + 2, this.minecraft.options.getBackgroundColor(0));
                    this.getFont().draw(poseStack, mutableText, x, y, 16777215 + (colorModifier << 24));
                    RenderSystem.disableBlend();
                }
            }

            this.minecraft.getProfiler().pop();
        }
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void disableRenderExperienceBar (PoseStack poseStack, int i, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled && !AprilFoolsMod.CONFIG.threeDSharewareConfig.xpBarEnabled) {
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "renderExperienceBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V"),
            index = 2
    )
    private int modifyYPosition(int original) {
        return AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled && AprilFoolsMod.CONFIG.threeDSharewareConfig.xpBarEnabled ? this.screenHeight - 70 : original;
    }

    @ModifyArg(
            method = "renderExperienceBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;draw(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/String;FFI)I"),
            index = 3
    )
    private float modifyYPositionOfExperienceLevel(float original) {
        return AprilFoolsMod.CONFIG.threeDSharewareConfig.sharewareHudEnabled && AprilFoolsMod.CONFIG.threeDSharewareConfig.xpBarEnabled ? this.screenHeight - 80.0F : original;
    }
}