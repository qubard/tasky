package ca.tarasyk.navigator;

import ca.tarasyk.navigator.pathfinding.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.Heuristic;
import ca.tarasyk.navigator.pathfinding.IPathFinder;
import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@Mod(modid="navigator", name="Navigator", version="1.0")
public class NavigatorMod
{
    private static Logger logger;

    private int nTicks = 0;

    ArrayList<BetterBlockPos> poses = new ArrayList<>();
    Optional<Path<BetterBlockPos>> foundPath = Optional.ofNullable(null);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    private void renderBlockAt(BlockPos pos) {
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(pos);

        double renderPosX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double renderPosY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double renderPosZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
        //GlStateManager.disableDepth();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
        GlStateManager.color(1, 1, 1, 1);
        brd.renderBlockBrightness(state, 1.0F);

        GlStateManager.color(0F, 1F, 0F, 1F);
        //GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    private void renderParticlesAtBlock(float x, float y, float z) {
        Random rand = new Random();
        Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x, y + 0.1, z  + rand.nextGaussian() * 0.015f - 0.015f/2, rand.nextGaussian() * 0.05f + - 0.05f/2, rand.nextGaussian() * 0.03f - 0.03/2, rand.nextGaussian() * 0.05 - 0.03/2);
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent  evt)
    {
        if (nTicks % 250 == 0) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


            if (foundPath.isPresent()) {
                for (BetterBlockPos pos : foundPath.get().getNodes()) {
                    renderParticlesAtBlock((float) pos.getX(), (float) pos.getY(), (float) pos.getZ() + 0.5f);
                }
                System.out.println("Path is present.");
            }

            GL11.glPopMatrix();
        }
        logger.info(Minecraft.getMinecraft().world.getBlockState(new BlockPos(111,66,-15)).isFullBlock());
        nTicks++;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);
        IPathFinder pathFinder = new AStarPathFinder();
        foundPath = pathFinder.search(new PathNode(0, 0, 0), new PathNode(300, 51, -20), Heuristic.BLOCKNODE_EUCLIDEAN_DISTANCE);
    }
}
