package com.example.smoltestmod.datagen;

import com.example.smoltestmod.Registration;
import com.example.smoltestmod.SmolTestMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SmolItemModels extends ItemModelProvider {

    public SmolItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SmolTestMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        assert Registration.SIMPLE_BLOCK.getId() != null;
        withExistingParent(Registration.SIMPLE_BLOCK.getId().getPath(), modLoc("block/simple_block"));
        assert Registration.COMPLEX_BLOCK.getId() != null;
        withExistingParent(Registration.COMPLEX_BLOCK.getId().getPath(), modLoc("block/complex_block"));
    }
}