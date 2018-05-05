package BananaPicker.Tasks;

import BananaPicker.BananaPicker;
import BananaPicker.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ChatOption;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

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
        return (BananaPicker.areaK.contains(ctx.players.local().tile())
                && BananaPicker.pathToBoatK.end().distanceTo(ctx.players.local().tile()) < 5
                && ctx.inventory.select().id(bananaId).count() > 0);
    }

    @Override
    public void execute() {
        BananaPicker.status = "Sailing to Port Sarim...";
        Npc officer = ctx.npcs.select().id(customsOfficerId).poll();

        if(!officer.inViewport()) {
            ctx.camera.turnTo(officer);
        }

        officer.interact("Pay-Fare");

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
            else if (!ctx.chat.select().text(optionsK).isEmpty()) {
                final ChatOption choice = ctx.chat.select().text(optionsK).poll();

                if (choice.select(true)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !choice.valid();
                        }
                    }, 300, 10);
                }
            }
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().tile().equals(boatTilePS);
            }
        }, 800, 20);
        Condition.sleep(Random.nextInt(400, 1300));
        ctx.objects.select(5).id(gangplankIdPS).poll().interact("Cross");
        Condition.sleep(Random.nextInt(2000, 3500));
    }
}

