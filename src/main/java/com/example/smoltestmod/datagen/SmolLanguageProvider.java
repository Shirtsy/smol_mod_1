package com.example.smoltestmod.datagen;

import com.example.smoltestmod.Registration;
import com.example.smoltestmod.SmolTestMod;
import com.example.smoltestmod.blocks.ComplexBlock;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class SmolLanguageProvider extends LanguageProvider {

    public SmolLanguageProvider(PackOutput output, String locale) {
        super(output, SmolTestMod.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(Registration.SIMPLE_BLOCK.get(), "Smol's Simple Block");
        add(Registration.COMPLEX_BLOCK.get(), "Smol's Complex Block");
        add(Registration.EXAMPLE_ITEM.get(), "Smol's Item");
        add(ComplexBlock.SCREEN_TUTORIAL_COMPLEX, "Super epic and super cool block");
        add(Registration.GENERATOR_BLOCK.get(), "Generator Block");
    }

}
