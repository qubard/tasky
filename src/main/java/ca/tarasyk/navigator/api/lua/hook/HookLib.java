package ca.tarasyk.navigator.api.lua.hook;

import ca.tarasyk.navigator.api.lua.func.action.*;
import ca.tarasyk.navigator.api.lua.func.getter.*;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class HookLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue root = tableOf();
        LuaValue table = tableOf();
        env.set("hook", table);
        env.set("inventoryFull", new InventoryFull());
        env.set("containerFull", new ContainerFull());
        table.set("add", new HookAdd());
        env.set("setLoop", new SetLoop());
        env.set("player", new Player());
        env.set("attack", new LeftClick());
        env.set("countStacks", new CountStacks());
        env.set("enchantItem", new EnchantItem());
        env.set("setSlot", new SetSlot());
        env.set("findSlots", new FindSlots());
        env.set("moveStack", new MoveStack(750));
        env.set("consumeItem", new ConsumeItem());
        env.set("drop", new Drop());
        env.set("sleep", new Sleep());
        env.set("attackMouseover", new AttackMouseover());
        env.set("lookAt", new LookAt());
        env.set("itemStack", new ItemStack());
        env.set("leftClick", new LeftClick());
        env.set("inventoryHas", new InventoryHas());
        env.set("maxDurability", new MaxSlotDurability());
        env.set("durability", new SlotDurability());
        env.set("closeGUI", new CloseGUI());
        env.set("expLevel", new ExperienceLevel());
        env.set("blockAt", new BlockAt());
        env.set("harvestLevel", new HarvestLevel());
        env.set("cropAge", new CropAge());
        env.set("foodStats", new FoodStats());
        env.set("digBlock", new DigBlock());
        env.set("rightClick", new RightClick());
        env.set("clickBlock", new ClickBlock());
        env.set("countMobs", new CountMobs());
        env.set("putInContainer", new PutInContainer());
        env.set("printChat", new PrintChat());
        env.set("currentSlot", new CurrentSlot());
        env.set("moveTo", new MoveTo(30));
        env.set("moveToXZ", new MoveToXZ());
        env.set("rightClickBlock", new RightClickBlock(100));
        return root;
    }

    /**
     * Add the hooked LuaFunction to the hook table
     */
    static class HookAdd extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue hook, LuaValue func) {
            HookProvider.getProvider().addHook(Hook.hookForName(hook.checkjstring()).get(), func.checkfunction());
            return null;
        }
    }
}
