package BananaPicker.Tasks;

import BananaPicker.BananaPicker;
import BananaPicker.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class KBoatHandler extends Task {
    private final Tile boatTilePS = new Tile(3032, 3217, 1);
    private final int bananaId = 1963;
    private final int customsOfficerId = 3648;
    private final int gangplankIdPS = 2084;
    private final String[] optionsK = {"Can I journey on this ship?", "Search away, I have nothing to hide.", "Ok."};

    public KBoatHandler(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return BananaPicker.areaK.contains(ctx.players.local().tile())
                && BananaPicker.pathToBoatK.end().distanceTo(ctx.players.local().tile()) < 5
                && ctx.inventory.select().id(bananaId).count() > 0;
    }

    @Override
    public void execute() {
        BananaPicker.status = "Sailing to Port Sarim...";
        Npc officer = ctx.npcs.select().id(customsOfficerId).poll();

        if(!officer.inViewport()) {
            ctx.camera.turnTo(officer);
        }

        officer.interact("Pay-Fare");

        Chatter chatter = new Chatter(ctx);
        chatter.chat(optionsK, boatTilePS, gangplankIdPS);
    }
}

