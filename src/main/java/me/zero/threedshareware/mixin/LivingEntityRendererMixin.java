package me.zero.threedshareware.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.zero.aprilfools.AprilFoolsMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.isPassenger()Z", shift = At.Shift.AFTER))
    public void renderEntityAsBillBoardEntity (LivingEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!(entity instanceof Player) && AprilFoolsMod.CONFIG != null && AprilFoolsMod.CONFIG.threeDSharewareConfig.billBoardEntitiesEnabled) {
            this.method_20283(matrixStack, entityYaw, partialTicks, packedLight);
        }
    }

    protected void method_20283(PoseStack matrixStack, final double n, final double n2, final float n3) {
        final double n4 = Math.atan2(n2, n) / 3.141592653589793 * 180.0;
        final double n5 = Math.floor((n3 - n4) / 45.0) * 45.0;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) n4));
        // GlStateManager.rotatef((float)n4, 0.0f, 1.0f, 0.0f);
        matrixStack.scale(0.02F, 1.0F, 1.0F);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) n5));
        // GlStateManager.rotated(n5, 0.0, 1.0, 0.0);
    }
}
