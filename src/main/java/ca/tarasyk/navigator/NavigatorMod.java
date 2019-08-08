package ca.tarasyk.navigator;

import ca.tarasyk.navigator.pathfinding.algorithm.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.goals.GoalXZ;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;

@Mod(modid="navigator", name="Navigator", version="1.0")
public class NavigatorMod
{
    private static Logger logger;

    private int nTicks = 0;

    // this is all very temporary and dumb still
    PathNode start = new PathNode(23, 4, -9);
    Optional<Path<BetterBlockPos>> foundPath = Optional.ofNullable(null);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    private void renderParticlesAtBlock(float x, float y, float z) {
        Random rand = new Random();
        NavigatorProvider.getWorld().spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x + 0.5, y + 0.5, z + 0.5, rand.nextGaussian() * 0.01f + - 0.01f/2, rand.nextGaussian() * 0.03f - 0.03/2, rand.nextGaussian() * 0.01 - 0.01/2);
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent e) {
        if (e.getMessage().equals("start")) {
            start = new PathNode(new BetterBlockPos(NavigatorProvider.getPlayer().getPosition()));
        } else if (e.getMessage().equals("end")) {
            AStarPathFinder pathFinder = new AStarPathFinder(new GoalXZ(new BetterBlockPos(Minecraft.getMinecraft().player.getPosition())));
            foundPath = pathFinder.search(start);
        }
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent  evt)
    {
        if (nTicks % 250 == 0) {
            if (foundPath.isPresent()) {
                for (BetterBlockPos pos : foundPath.get().getNodes()) {
                    renderParticlesAtBlock((float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
                }
            }
        }
        nTicks++;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
