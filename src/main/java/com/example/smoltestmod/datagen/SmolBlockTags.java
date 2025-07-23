package com.example.smoltestmod.datagen;

import com.example.smoltestmod.Registration;
import com.example.smoltestmod.SmolTestMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class SmolBlockTags extends BlockTagsProvider {

    public SmolBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SmolTestMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.SIMPLE_BLOCK.get())
                .add(Registration.COMPLEX_BLOCK.get())
                .add(Registration.GENERATOR_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.SIMPLE_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(Registration.COMPLEX_BLOCK.get())
                .add(Registration.GENERATOR_BLOCK.get());
        tag(Tags.Blocks.OBSIDIAN) // Forge tag!
                .add(Registration.COMPLEX_BLOCK.get());
    }
}
