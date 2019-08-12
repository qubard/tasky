package ca.tarasyk.navigator;

import ca.tarasyk.navigator.api.lua.Hook;
import ca.tarasyk.navigator.api.lua.HookLib;
import ca.tarasyk.navigator.api.lua.HookProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.algorithm.PathRunner;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.goals.GoalXZ;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Mod(modid="navigator", name="Navigator", version="1.0")
public class NavigatorMod
{
    private static Logger logger;

    private int nTicks = 0;

    // this is all very temporary and dumb still
    PathNode start = new PathNode(23, 4, -9);
    Future<Optional<BlockPosPath>> foundPath;
    public static ExecutorService executorService;

    public static LuaFunction chatFuncHook = null;

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
    public void onChat(LivingHurtEvent e) {
        HookProvider.getProvider().dispatch(Hook.ON_LIVING_HURT, e);
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent e) {
        if (e.getMessage().equals("start")) {
            start = new PathNode(new BetterBlockPos(NavigatorProvider.getPlayer().getPosition()));
        } else if (e.getMessage().equals("end")) {
            AStarPathFinder pathFinder = new AStarPathFinder(new GoalXZ(new BetterBlockPos(Minecraft.getMinecraft().player.getPosition())), (long) 3000);
            Future<Optional<BlockPosPath>> future = executorService.submit(() -> pathFinder.search(start));
            long start = System.currentTimeMillis();
            foundPath = future;
        } if (e.getMessage().equals("-load")) {
            System.out.println("Destroying");
            try {
                HookProvider.getProvider().unhook();
                Globals globals = JsePlatform.standardGlobals();
                globals.load(new HookLib());
                LuaValue chunk = globals.load(loadScript("tasky", "test.lua"));
                chunk.call();
                printDebugMessage("Successfully loaded test.lua!");
            } catch (LuaError | IOException err) {
                HookProvider.getProvider().unhook();
                String errMsg = err.toString().replace("\n", "").replace("\r", "");
                printDebugMessage("Failed to load: " + errMsg);
            }
        } else {
            HookProvider.getProvider().dispatch(Hook.ON_CHAT, e);
        }
        printDebugMessage(""+NavigatorProvider.getPlayer().posX +"," + NavigatorProvider.getPlayer().posZ);
    }

    public static void printDebugMessage(String msg) {
        String str = "\247d[Tasky] \2477" + msg;
        NavigatorProvider.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(str));
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String loadScript(String root, String name) throws IOException {
        String path = new StringBuilder().append(Minecraft.getMinecraft().mcDataDir.getPath()).append("/").append(root).append("/scripts/").append(name).toString();
        return readFile(path, StandardCharsets.UTF_8);
    }

    ArrayList<Integer> tickList = new ArrayList<>();

    private void prepopulateTickList() {
        for (int i = 0; i < 100; i++) {
            tickList.add((int) (Math.random() * 50) - 25);
        }
    }

    public static BlockPosPath path = null;

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt)
    {
        if (path != null) {
            BetterBlockPos bb = new BetterBlockPos(NavigatorProvider.getPlayer().getPosition());
            int i = 0;
            for (BetterBlockPos pos : path.pathNear(50.0, bb).getNodes()) {
                if (nTicks % (200 + tickList.get(i % tickList.size())) == 0) {
                    renderParticlesAtBlock((float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
                }
                i++;
            }
        }
        nTicks++;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.prepopulateTickList();
        executorService = Executors.newSingleThreadExecutor();
        MinecraftForge.EVENT_BUS.register(this);
        /*Thread th = new Thread(new Runnable(){
                @Override
                public void run() {
                    Globals globals = JsePlatform.standardGlobals();
                    globals.load(new HookLib());
                    LuaValue chunk = globals.loadfile("scripts/test.lua");
                    chunk.call();
                }
        });
        th.start();*/
    }
}
