package ca.tarasyk.navigator.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;

public class ContainerUtil {

    // Lower bound of a container is always 0, but what is the upper bound?
    // See https://wiki.vg/Inventory
    public static int slotLimit(Container container) {
        if (container instanceof ContainerChest) {
            ContainerChest cont = (ContainerChest) container;
            return cont.getInventory().size();
        }
        return 0;
    }

}
