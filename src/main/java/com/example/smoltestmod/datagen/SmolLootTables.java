package com.example.smoltestmod.datagen;

import com.example.smoltestmod.Registration;
import com.example.smoltestmod.SmolTestMod;
import com.example.smoltestmod.blocks.ComplexBlockEntity;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.stream.Collectors;

public class SmolLootTables extends VanillaBlockLoot {

    @Override
    protected void generate() {
        dropSelf(Registration.SIMPLE_BLOCK.get());
        //createStandardTable(Registration.COMPLEX_BLOCK.get(), Registration.COMPLEX_BLOCK_ENTITY.get());
        dropSelf(Registration.COMPLEX_BLOCK.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(SmolTestMod.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void createStandardTable(Block block, BlockEntityType<?> type) {
        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy(ComplexBlockEntity.ITEMS_TAG, "BlockEntityTag." + ComplexBlockEntity.ITEMS_TAG, CopyNbtFunction.MergeStrategy.REPLACE))
                        .apply(SetContainerContents.setContents(type)
                                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                );
        add(block, LootTable.lootTable().withPool(builder));
    }

}
