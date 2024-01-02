package net.pinero.simpledeserteagle.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.pinero.simpledeserteagle.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Author: MrCrayfish
 */
@Mixin(LivingEntityRenderer.class)
public class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
{
    private T entity;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void capture(T entity, float entityYaw, float deltaTicks, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci, boolean shouldSit, float f, float f1, float f2, float f6, float f7, float f8, float f5)
    {
        this.entity = entity;
        this.limbSwing = f5;
        this.limbSwingAmount = f8;
        this.ageInTicks = f7;
        this.netHeadYaw = f2;
        this.headPitch = f6;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    public void fireRenderPlayer(M model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha)
    {
        Hooks.fireRenderPlayer(model, poseStack, consumer, light, overlay, red, green, blue, alpha, this.entity, this.limbSwing, this.limbSwingAmount, this.ageInTicks, this.netHeadYaw, this.headPitch);
    }
}
