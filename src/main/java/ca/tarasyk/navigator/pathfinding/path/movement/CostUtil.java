package ca.tarasyk.navigator.pathfinding.path.movement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;

public class CostUtil {
    public static double digCost(InventoryPlayer inventory, IBlockState state) {
        double cost = inventory.getDestroySpeed(state);
        return cost;
    }
}
