package net.momostudios.coldsweat.core.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.momostudios.coldsweat.ColdSweat;
import net.momostudios.coldsweat.common.container.BoilerContainer;
import net.momostudios.coldsweat.common.container.HearthContainer;
import net.momostudios.coldsweat.common.container.IceboxContainer;
import net.momostudios.coldsweat.common.container.SewingContainer;

public class ContainerInit
{
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ColdSweat.MOD_ID);

    public static final RegistryObject<ContainerType<BoilerContainer>> BOILER_CONTAINER_TYPE =
            CONTAINER_TYPES.register("boiler", () -> IForgeContainerType.create(BoilerContainer::new));

    public static final RegistryObject<ContainerType<IceboxContainer>> ICEBOX_CONTAINER_TYPE =
        CONTAINER_TYPES.register("ice_box", () -> IForgeContainerType.create(IceboxContainer::new));

    public static final RegistryObject<ContainerType<SewingContainer>> SEWING_CONTAINER_TYPE =
        CONTAINER_TYPES.register("sewing_table", () -> IForgeContainerType.create(SewingContainer::new));

    public static final RegistryObject<ContainerType<HearthContainer>> HEARTH_CONTAINER_TYPE =
        CONTAINER_TYPES.register("hearth", () -> IForgeContainerType.create(HearthContainer::new));
}