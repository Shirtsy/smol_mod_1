package com.example.smoltestmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SimpleBlock extends Block {

    public SimpleBlock() {
        super(Properties.of()
                .strength(3.5f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
                .randomTicks()
        );
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        level.addParticle(
                ParticleTypes.SMOKE,
                pos.getX() + 0.5,
                pos.getY() + 1.5,
                pos.getZ() + 0.5,
                0,
                0.1,
                0
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }
        level.explode(
                null,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                10f,
                true,
                Level.ExplosionInteraction.MOB
        );
        return InteractionResult.SUCCESS;
    }

}
