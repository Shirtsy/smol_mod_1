package com.example.smoltestmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ComplexBlock extends Block implements EntityBlock {

    public static final String SCREEN_TUTORIAL_COMPLEX = "tutorial.screen.complex";
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public static final int LIT_BRIGHTNESS = 10;

    public ComplexBlock() {
        super(Properties.of()
                .strength(3.5f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
                .lightLevel((BlockState state) -> state.getValue(LIT) ? LIT_BRIGHTNESS : 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING);
        builder.add(LIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LIT, false);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ComplexBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ComplexBlockEntity bEntity) {
                bEntity.tickServer();
            }
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ComplexBlockEntity)) {
            throw new IllegalStateException("Our named container provider is missing!");
        }

        MenuProvider containerProvider = new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable(SCREEN_TUTORIAL_COMPLEX);
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                return new ComplexContainer(windowId, playerEntity, pos);
            }
        };

        NetworkHooks.openScreen((ServerPlayer) player, containerProvider, be.getBlockPos());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.is(newState.getBlock())) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            super.onRemove(state, level, pos, newState, movedByPiston);
            return;
        }

        LazyOptional<IItemHandler> itemHandlerOptional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        itemHandlerOptional.ifPresent(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        });
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
