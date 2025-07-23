package com.example.smoltestmod.datagen;

import com.example.smoltestmod.Registration;
import com.example.smoltestmod.SmolTestMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SmolBlockStates extends BlockStateProvider {

    public SmolBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SmolTestMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.SIMPLE_BLOCK.get());
        horizontalBlock(Registration.COMPLEX_BLOCK.get(), models().getExistingFile(modLoc("block/complex_block")));
        horizontalBlock(Registration.GENERATOR_BLOCK.get(), models().getExistingFile(modLoc("block/generator_block")));
    }
}
