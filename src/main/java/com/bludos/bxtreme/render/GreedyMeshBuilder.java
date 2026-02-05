package com.bludos.bxtreme.render;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * GREEDY MESHING - Like Bedrock Edition!
 * Combines adjacent identical block faces into single quads
 * MASSIVE FPS boost by reducing vertex count
 */
public class GreedyMeshBuilder {
    
    /**
     * Checks if two block states can be merged in greedy meshing
     */
    public static boolean canMerge(BlockState state1, BlockState state2, Direction face) {
        if (state1 == null || state2 == null) {
            return false;
        }
        
        // Must be same block type
        if (!state1.is(state2.getBlock())) {
            return false;
        }
        
        // Must have same properties
        if (!state1.equals(state2)) {
            return false;
        }
        
        // Can't merge transparent blocks (causes visual artifacts)
        if (!state1.canOcclude()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Calculates if a face should be rendered (culling)
     * Bedrock-style aggressive culling
     */
    public static boolean shouldRenderFace(BlockState current, BlockState neighbor, Direction face) {
        // Air always renders faces
        if (current.isAir()) {
            return false;
        }
        
        // If neighbor is air, render face
        if (neighbor.isAir()) {
            return true;
        }
        
        // If neighbor is same block, don't render (internal face)
        if (current.is(neighbor.getBlock())) {
            return false;
        }
        
        // If neighbor is opaque, don't render
        if (neighbor.canOcclude()) {
            return false;
        }
        
        // Otherwise render
        return true;
    }
}