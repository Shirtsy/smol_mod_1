package com.example.smoltestmod.datagen;

import com.example.smoltestmod.SmolTestMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataGeneration {

    public static void generate(GatherDataEvent event) {
        SmolTestMod.LOGGER.info("Generating Data...");
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new SmolBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new SmolItemModels(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new SmolLanguageProvider(packOutput, "en_us"));

        SmolBlockTags blockTags = new SmolBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new SmolItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new SmolRecipes(packOutput));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(SmolLootTables::new, LootContextParamSets.BLOCK))));
    }

}
