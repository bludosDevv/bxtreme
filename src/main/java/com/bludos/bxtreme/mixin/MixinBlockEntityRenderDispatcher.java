package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher {
    
    /**
     * Skip rendering distant tile entities (chests, furnaces, etc)
     * These are SUPER expensive on mobile
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void skipDistantTileEntities(BlockEntity blockEntity, float partialTick, 
                                        net.minecraft.client.renderer.MultiBufferSource bufferSource,
                                        CallbackInfo ci) {
        if (!Main.config.get().reduceTileEntityUpdates) {
            return;
        }
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        
        Vec3 playerPos = mc.player.position();
        BlockPos blockPos = blockEntity.getBlockPos();
        
        // Calculate distance squared
        double dx = blockPos.getX() - playerPos.x;
        double dy = blockPos.getY() - playerPos.y;
        double dz = blockPos.getZ() - playerPos.z;
        double distanceSq = dx * dx + dy * dy + dz * dz;
        
        // Don't render tile entities beyond 24 blocks
        if (distanceSq > 24 * 24) {
            ci.cancel();
        }
    }
}