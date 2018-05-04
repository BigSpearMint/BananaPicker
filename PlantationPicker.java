package BananaPicker;

import org.powerbot.Con;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class PlantationPicker extends Task {
    private final Tile exitTile = new Tile(2920, 3151);
    private final int[] treeIds = {2073, 2074, 2075, 2076, 2077};

    public PlantationPicker (ClientContext ctx) { super(ctx); }

    @Override
    public boolean activate() {
        return BananaPicker.areaPlantation.contains(ctx.players.local())
                && ctx.inventory.select().count() < 28;
    }

    @Override
    public void execute() {
        BananaPicker.status = "Picking Bananas...";
        while (ctx.inventory.select().count() < 28) {
            final GameObject activeTree = ctx.objects.select(10).id(treeIds).nearest().poll();
            ctx.camera.turnTo(activeTree);
            if (ctx.camera.pitch() < 90) {
                ctx.camera.pitch(Random.nextInt(90,99));
            }

            activeTree.interact("Pick");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !activeTree.valid();
                }
            },100,50);
            if (activeTree.valid()) {
                ctx.objects.id(treeIds).nearest().poll();
            }

            Condition.sleep(Random.nextInt(225,300));
        }
        Condition.sleep(Random.nextInt(150, 625));

        BananaPicker.status = "Travelling to ship...";
        ctx.movement.step(exitTile.derive(Random.nextInt(-1,1),Random.nextInt(-1,1)));
    }
}
