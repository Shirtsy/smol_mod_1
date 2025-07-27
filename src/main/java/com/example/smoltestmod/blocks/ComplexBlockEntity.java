package com.example.smoltestmod.blocks;

import com.example.smoltestmod.tools.AbstractBlockEntity;
import com.example.smoltestmod.tools.AdaptedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.example.smoltestmod.Registration.COMPLEX_BLOCK_ENTITY;

public class ComplexBlockEntity extends AbstractBlockEntity {

    public static final String ITEMS_TAG = "Inventory";

    public static int SLOT_COUNT = 1;
    public static int SLOT = 0;

    public boolean extractable = false;

    private final ItemStackHandler items = createItemHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(
            () -> new AdaptedItemHandler(items) {
                @Override
                public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                    if (!extractable) {
                        return ItemStack.EMPTY;
                    }
                    if (!simulate) {
                        extractable = false;
                    }
                    return super.extractItem(slot, amount, simulate);
                }
            }
    );

    public ComplexBlockEntity(BlockPos pos, BlockState state) {
        super(COMPLEX_BLOCK_ENTITY.get(), pos, state);
    }

    public void setLit(boolean lit) {
        if (this.level == null || this.level.isClientSide) {
            return;
        }

        BlockState currentState = this.getBlockState();
        if (currentState.getValue(ComplexBlock.LIT) != lit) {
            this.level.setBlock(
                    this.worldPosition,
                    currentState.setValue(ComplexBlock.LIT, lit),
                    Block.UPDATE_ALL
            );
        }
    }

    public boolean getLit() {
        return this.getBlockState().getValue(ComplexBlock.LIT);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                assert level != null;
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), SimpleBlock.UPDATE_ALL);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.isDamageableItem() && stack.getDamageValue() > 0;
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    protected void saveClientData(CompoundTag tag) {
        tag.put(ITEMS_TAG, items.serializeNBT());
    }

    protected void loadClientData(CompoundTag tag) {
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public void tickServer() {
        assert level != null;
        ItemStack stack = items.getStackInSlot(SLOT);

        if (stack.isEmpty()) {
            this.extractable = false;
            this.setLit(false);
            return;
        }

        if (!stack.isDamageableItem()) {
            return;
        }

        int value = stack.getDamageValue();
        if (value > 0) {
            this.extractable = false;
            if (level.getGameTime() % 20 == 0) { stack.setDamageValue(value - 1); }
            this.setLit(true);
        } else {
            this.extractable = true;
            this.setLit(false);
        }
    }

    private void ejectItem() {
        BlockPos pos = worldPosition.relative(Direction.UP);
        assert level != null;
        Block.popResource(level, pos, items.extractItem(SLOT, 1, false));
    }

}
