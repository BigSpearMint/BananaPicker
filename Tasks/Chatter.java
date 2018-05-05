package BananaPicker.Tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ChatOption;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Chatter extends ClientAccessor {

    public Chatter(ClientContext ctx) {
        super(ctx);
    }

    public void chat(final String[] prompts, final Tile boatTile, final int gangplankId) {
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
            else if (!ctx.chat.select().text(prompts).isEmpty()) {
                final ChatOption choice = ctx.chat.select().text(prompts).poll();

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
                return ctx.players.local().tile().equals(boatTile);
            }
        }, 1400, 20);

        ctx.objects.select(5).id(gangplankId).poll().interact("Cross");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().tile().equals(boatTile);
            }
        },2500,4);
    }
}
