package me.zero.threedshareware.registry.block.renderer;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.phys.Vec2;

public class BarrelBlockEntityRenderer implements BlockEntityRenderer<BarrelBlockEntity> {
    @Override
    public void render(BarrelBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // GlStateManager.pushMatrix();
        poseStack.pushPose();
        poseStack.translate(blockEntity.getBlockPos().getX() + 0.5F, blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ() + 0.5F);
        // GlStateManager.translatef((float)var2 + 0.5F, (float)var4, (float)var6 + 0.5F);
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        Minecraft var10 = Minecraft.getInstance();
        Entity var11 = var10.getCameraEntity();
        Vec2 var12 = Vec2.ZERO;
        if (var11 != null) {
            var12 = var11.getRotationVector();
        }

        poseStack.mulPose(Vector3f.YP.rotationDegrees(-var12.y));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        //GlStateManager.rotatef(-var12.y, 0.0F, 1.0F, 0.0F);
        //GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        TextureAtlasSprite var13 = atlas.getSprite(new ResourceLocation("block/barrel_side"));
        TextureAtlasSprite var14 = atlas.getSprite(new ResourceLocation("block/fire_0"));
        Tesselator var15 = Tesselator.getInstance();
        BufferBuilder var16 = var15.getBuilder();
        var16.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        var16.vertex(-0.5D, 0.0D, 0.0D).uv(var13.getU0(), var13.getV1()).color(255, 255, 255, 255).endVertex();
        var16.vertex(0.5D, 0.0D, 0.0D).uv(var13.getU1(), var13.getV1()).color(255, 255, 255, 255).endVertex();
        var16.vertex(0.5D, -1.0D, 0.0D).uv(var13.getU1(), var13.getV0()).color(255, 255, 255, 255).endVertex();
        var16.vertex(-0.5D, -1.0D, 0.0D).uv(var13.getU0(), var13.getV0()).color(255, 255, 255, 255).endVertex();
        var16.vertex(0.0D, -1.0D, 0.0D);
        var16.vertex(-0.5D, 0.0D, 0.0D).uv(var14.getU0(), var14.getV1()).color(255, 255, 255, 255).endVertex();
        var16.vertex(0.5D, 0.0D, 0.0D).uv(var14.getU1(), var14.getV1()).color(255, 255, 255, 255).endVertex();
        var16.vertex(0.5D, -1.0D, 0.0D).uv(var14.getU1(), var14.getV0()).color(255, 255, 255, 255).endVertex();
        var16.vertex(-0.5D, -1.0D, 0.0D).uv(var14.getU0(), var14.getV0()).color(255, 255, 255, 255).endVertex();
        var16.vertex(0.0D, 0.0D, 0.0D);
        var15.end();
        poseStack.popPose();
        //GlStateManager.popMatrix();
    }
}
