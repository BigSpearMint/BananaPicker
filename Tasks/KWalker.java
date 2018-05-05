package BananaPicker;

import org.powerbot.script.rt4.ClientContext;

public class KWalker extends Task{
    private final int bananaId = 1963;

    public KWalker (ClientContext ctx) { super(ctx); }

    @Override
    public boolean activate() {
        return BananaPicker.areaK.contains(ctx.players.local())
                && ((BananaPicker.pathToBoatK.end().distanceTo(ctx.players.local().tile()) > 5
                && ctx.inventory.select().id(bananaId).count() > 0)
                || (BananaPicker.pathToPlantation.end().distanceTo(ctx.players.local().tile()) > 5
                && ctx.inventory.select().id(bananaId).count() == 0));
    }

    @Override
    public void execute() {
        if (ctx.inventory.select().id(bananaId).count() == 0
                && BananaPicker.pathToPlantation.end().distanceTo(ctx.players.local().tile()) > 5) {
            BananaPicker.status = "Travelling to plantation...";
            BananaPicker.pathToPlantation.traverse();
        }
        if (ctx.inventory.select().id(bananaId).count() > 0
                && BananaPicker.pathToBoatK.end().distanceTo(ctx.players.local().tile()) > 5) {
            BananaPicker.status = "Travelling to ship...";
            BananaPicker.pathToBoatK.traverse();
        }
    }
}
