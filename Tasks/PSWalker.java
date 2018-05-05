package BananaPicker.Tasks;

import BananaPicker.BananaPicker;
import BananaPicker.Task;
import org.powerbot.script.rt4.ClientContext;

public class PSWalker extends Task{
    private final int bananaId = 1963;
    private final int coinsId = 995;

    public PSWalker(ClientContext ctx) { super(ctx); }

    @Override
    public boolean activate() {
        return BananaPicker.areaPS.contains(ctx.players.local())
                && ((BananaPicker.pathToBank.end().distanceTo(ctx.players.local().tile()) > 5
                && ctx.inventory.select().id(bananaId).count() > 0)
                || BananaPicker.pathToBoatPS.end().distanceTo(ctx.players.local().tile()) > 5
                && ctx.inventory.select().id(bananaId).count() == 0);
    }

    @Override
    public void execute() {
        if (ctx.inventory.select().id(bananaId).count() == 0
                && BananaPicker.pathToBoatPS.end().distanceTo(ctx.players.local().tile()) > 5) {

            if (ctx.inventory.select().id(coinsId).count(true) < 60) {
                ctx.controller.stop();
            }

            BananaPicker.status = "Travelling to ship...";
            BananaPicker.pathToBoatPS.traverse();
        }
        if (ctx.inventory.select().id(bananaId).count() > 0
                && BananaPicker.pathToBank.end().distanceTo(ctx.players.local().tile()) > 5) {
            BananaPicker.status = "Travelling to deposit box...";
            BananaPicker.pathToBank.traverse();
        }
    }
}
