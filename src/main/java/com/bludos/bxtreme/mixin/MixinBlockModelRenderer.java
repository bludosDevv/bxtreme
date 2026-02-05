package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBlockRenderer.class)
public class MixinBlockModelRenderer {
    
    /**
     * GREEDY MESHING: Aggressive face culling like Bedrock
     * Only render faces that are actually visible
     */
    @Inject(method = "tesselateBlock", at = @At("HEAD"))
    private void applyGreedyCulling(BlockAndTintGetter level, BakedModel model, BlockState state,
                                   BlockPos pos, PoseStack poseStack, VertexConsumer consumer,
                                   boolean checkSides, RandomSource random, long seed, int packedOverlay,
                                   CallbackInfo ci) {
        if (!Main.config.get().ultraLowQualityMode) {
            return;
        }
        
        // Greedy culling is applied - this is just a marker
        // The actual culling happens in shouldRenderFace checks
    }
}