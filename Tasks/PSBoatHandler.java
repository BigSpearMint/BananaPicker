package BananaPicker.Tasks;

import BananaPicker.BananaPicker;
import BananaPicker.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ChatOption;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class PSBoatHandler extends Task {
    private final Tile boatTileK = new Tile(2956, 3143, 1);
    private final int bananaId = 1963;
    private final int gangplankIdK = 2082;
    private final int[] sailorsId = {3644, 3645, 3646};
    private final String optionsPS = "Yes please.";

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

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.chatting();
            }
        }, 220, 10);

        while (ctx.chat.chatting()) {

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.chat.canContinue();
                }
            },Random.nextInt(500,775), 2);

            if (ctx.chat.canContinue()) {
                ctx.chat.clickContinue(true);
            }
            else if (!ctx.chat.select().text(optionsPS).isEmpty()) {
                final ChatOption choice = ctx.chat.select().text(optionsPS).poll();

                if (choice.select(true)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !choice.valid();
                        }
                    }, 220, 10);
                }
            }
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().tile().equals(boatTileK);
            }
        }, 800, 20);
        Condition.sleep(Random.nextInt(400, 1300));
        ctx.objects.select(5).id(gangplankIdK).poll().interact("Cross");
        Condition.sleep(Random.nextInt(2000, 3500));
    }
}

