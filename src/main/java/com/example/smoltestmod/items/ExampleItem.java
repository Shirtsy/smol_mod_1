package com.example.smoltestmod.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class ExampleItem extends Item {

    public ExampleItem() {
        super(new Item.Properties().food(
                new FoodProperties.Builder()
                        .alwaysEat()
                        .nutrition(1)
                        .saturationMod(2f)
                        .build()
        ));
    }
}
