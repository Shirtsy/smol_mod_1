package com.example.smoltestmod.blocks;

import com.example.smoltestmod.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import static com.example.smoltestmod.blocks.ComplexBlockEntity.SLOT;
import static com.example.smoltestmod.blocks.ComplexBlockEntity.SLOT_COUNT;

public class ComplexContainer extends AbstractContainerMenu {

    private final BlockPos pos;

    public ComplexContainer(int windowId, Player player, BlockPos pos) {
        super(Registration.COMPLEX_CONTAINER.get(), windowId);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof ComplexBlockEntity be) {
            addSlot(new SlotItemHandler(be.getItems(), SLOT, 82, 24));
        }
        layoutPlayerInventorySlots(player.getInventory(), 10, 70);
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Inventory playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return itemstack;
        }

        ItemStack stack = slot.getItem();
        itemstack = stack.copy();
        if (index < SLOT_COUNT) {
            if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        }

        if (!this.moveItemStackTo(stack, SLOT, SLOT+1, false)) {
            if (index < 27 + SLOT_COUNT) {
                if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < Inventory.INVENTORY_SIZE + SLOT_COUNT && !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.COMPLEX_BLOCK.get());
    }

}
