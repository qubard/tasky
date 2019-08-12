package ca.tarasyk.navigator;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public final class BetterBlockPos extends BlockPos {
    private final int x;
    private final int y;
    private final int z;

    public BetterBlockPos(int x, int y, int z) {
        super(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BetterBlockPos(double x, double y, double z) {
        this(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public BetterBlockPos(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + "," + z + ">";
    }

    @Override
    public int hashCode() {
        long hash = 3241;
        hash = 3457689L * hash + x;
        hash = 8734625L * hash + y;
        hash = 2873465L * hash + z;
        return (int) hash;
    }

    public int hashCodeXZ() {
        long hash = 3241;
        hash = 3457689L * hash + x;
        hash = 2873465L * hash + z;
        return (int) hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof BetterBlockPos) {
            BetterBlockPos oth = (BetterBlockPos) o;
            return oth.x == x && oth.y == y && oth.z == z;
        }
        BlockPos oth = (BlockPos) o;
        return oth.getX() == x && oth.getY() == y && oth.getZ() == z;
    }

    public BetterBlockPos add(BetterBlockPos other) {
        return new BetterBlockPos(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    @Override
    public BetterBlockPos up() {
        return new BetterBlockPos(x, y + 1, z);
    }

    @Override
    public BetterBlockPos up(int amt) {
        return amt == 0 ? this : new BetterBlockPos(x, y + amt, z);
    }

    @Override
    public BetterBlockPos down() {
        return new BetterBlockPos(x, y - 1, z);
    }

    @Override
    public BetterBlockPos down(int amt) {
        return amt == 0 ? this : new BetterBlockPos(x, y - amt, z);
    }

    @Override
    public BetterBlockPos offset(EnumFacing dir) {
        Vec3i vec = dir.getDirectionVec();
        return new BetterBlockPos(x + vec.getX(), y + vec.getY(), z + vec.getZ());
    }

    @Override
    public BetterBlockPos offset(EnumFacing dir, int dist) {
        if (dist == 0) {
            return this;
        }
        Vec3i vec = dir.getDirectionVec();
        return new BetterBlockPos(x + vec.getX() * dist, y + vec.getY() * dist, z + vec.getZ() * dist);
    }

    @Override
    public BetterBlockPos north() {
        return new BetterBlockPos(x, y, z - 1);
    }

    @Override
    public BetterBlockPos north(int amt) {
        return amt == 0 ? this : new BetterBlockPos(x, y, z - amt);
    }

    @Override
    public BetterBlockPos south() {
        return new BetterBlockPos(x, y, z + 1);
    }

    @Override
    public BetterBlockPos south(int amt) {
        return amt == 0 ? this : new BetterBlockPos(x, y, z + amt);
    }

    @Override
    public BetterBlockPos east() {
        return new BetterBlockPos(x + 1, y, z);
    }

    @Override
    public BetterBlockPos east(int amt) {
        return amt == 0 ? this : new BetterBlockPos(x + amt, y, z);
    }

    @Override
    public BetterBlockPos west() {
        return new BetterBlockPos(x - 1, y, z);
    }

    @Override
    public BetterBlockPos west(int amt) {
        return amt == 0 ? this : new BetterBlockPos(x - amt, y, z);
    }
}