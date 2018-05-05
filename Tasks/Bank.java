package BananaPicker.Tasks;

import BananaPicker.BananaPicker;
import BananaPicker.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.DepositBox;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Bank extends Task {
    private final int depositId = 26254;
    private final int bananaId = 1963;

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.objects.select(10).id(depositId).isEmpty()
                && ctx.inventory.select().id(bananaId).count() > 0;
    }

    @Override
    public void execute() {
        BananaPicker.status = "Banking...";
        GameObject deposit = ctx.objects.select().id(depositId).poll();

            ctx.camera.turnTo(deposit);

        if (!ctx.depositBox.opened()) {
            deposit.interact("Deposit");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.depositBox.opened();
                }
            },200,10);
        }

        ctx.depositBox.deposit(bananaId,DepositBox.Amount.ALL);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().id(bananaId).count() == 0;
            }
        },250,8);

        Condition.sleep(Random.nextInt(100,500));
        if (Random.nextInt(0,100) > 11) {    // Simulates actually closing bank vs clicking out of bank on map
            ctx.depositBox.close(true);
        }
    }
}

