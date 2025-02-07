package com.momosoftworks.coldsweat.data.tag;

import com.momosoftworks.coldsweat.ColdSweat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public class ModEffectTags
{
    public static final TagKey<MobEffect> HEARTH_BLACKLISTED = createTag("hearth_blacklisted");

    private static TagKey<MobEffect> createTag(String name)
    {   return TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(ColdSweat.MOD_ID, name));
    }
}
