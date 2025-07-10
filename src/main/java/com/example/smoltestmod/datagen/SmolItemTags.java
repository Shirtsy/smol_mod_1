package com.example.smoltestmod.datagen;

import com.example.smoltestmod.SmolTestMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SmolItemTags extends ItemTagsProvider {

    public SmolItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), SmolTestMod.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    }

}
