package BananaPicker.Tasks;

import BananaPicker.BananaPicker;
import BananaPicker.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class PSBoatHandler extends Task {
    private final Tile boatTileK = new Tile(2956, 3143, 1);
    private final int bananaId = 1963;
    private final int gangplankIdK = 2082;
    private final int[] sailorsId = {3644, 3645, 3646};
    private final String[] optionsPS = {"Yes please."};

    public PSBoatHandler(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return BananaPicker.areaPS.contains(ctx.players.local().tile())
                && BananaPicker.pathToBoatPS.end().distanceTo(ctx.players.local().tile()) < 5
                && ctx.inventory.select().id(bananaId).count() == 0;
    }

    @Override
    public void execute() {
        BananaPicker.status = "Sailing to Karamja...";
        Npc sailor = ctx.npcs.select().id(sailorsId).nearest().poll();

        sailor.interact("Pay-Fare");

        Chatter chatter = new Chatter(ctx);
        chatter.chat(optionsPS, boatTileK, gangplankIdK);
    }
}

