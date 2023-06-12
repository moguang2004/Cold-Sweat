package dev.momostudios.coldsweat.util.registries;

import dev.momostudios.coldsweat.ColdSweat;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.dimension.DimensionType;

public class ModTags
{
    public static class Items
    {
        public static final TagKey<Item> BOILER_VALID = createTag("boiler_valid");
        public static final TagKey<Item> ICEBOX_VALID = createTag("icebox_valid");
        public static final TagKey<Item> CHAMELEON_TAMING = createTag("chameleon/taming");
        public static final TagKey<Item> CHAMELEON_HOT = createTag("chameleon/find_hot_biomes");
        public static final TagKey<Item> CHAMELEON_COLD = createTag("chameleon/find_cold_biomes");
        public static final TagKey<Item> CHAMELEON_HUMID = createTag("chameleon/find_humid_biomes");

        private static TagKey<Item> createTag(String name)
        {   return ItemTags.create(new ResourceLocation(ColdSweat.MOD_ID, name));
        }
    }

    public static class Dimensions
    {
        public static final TagKey<DimensionType> SOUL_LAMP_VALID = createTag("soulspring_lamp_valid");

        private static TagKey<DimensionType> createTag(String name)
        {   return TagKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(ColdSweat.MOD_ID, name));
        }
    }
}