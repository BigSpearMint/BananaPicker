package BananaPicker;

import BananaPicker.Tasks.*;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.TilePath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(
        name = "BananaPicker",
        properties = "author=BigSpearMint; client=4;",
        description = "No-requirement Karamja Banana Picker" )

public class BananaPicker extends PollingScript<ClientContext> implements PaintListener, MessageListener {
    private final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    private final Color color1 = new Color(255, 255, 102, 174);
    private final Color color2 = new Color(255, 255, 0);
    private final Color color3 = new Color(51, 0, 51);
    private final Color color4 = new Color(102, 102, 102);

    private final BasicStroke stroke1 = new BasicStroke(2);
    private final Font font1 = new Font("Arial", 1, 20);
    private final Font font2 = new Font("Arial", 1, 12);

    private List<Task> taskList = new ArrayList<Task>();
    public static String status = "";
    private int bananasPicked = 0;
    private long time = 0;

    private final Tile[] pathPS = new Tile[]{
            new Tile(3044, 3236, 0), new Tile(3041, 3236, 0),
            new Tile(3038, 3236, 0), new Tile(3035, 3236, 0),
            new Tile(3032, 3236, 0), new Tile(3029, 3236, 0),
            new Tile(3028, 3233, 0), new Tile(3028, 3230, 0),
            new Tile(3028, 3227, 0), new Tile(3028, 3224, 0),
            new Tile(3028, 3221, 0)
    };
    private final Tile[] pathK = new Tile[]{
            new Tile(2950, 3147, 0), new Tile(2947, 3147, 0),
            new Tile(2944, 3147, 0), new Tile(2941, 3147, 0),
            new Tile(2938, 3147, 0), new Tile(2935, 3147, 0),
            new Tile(2932, 3148, 0), new Tile(2929, 3150, 0),
            new Tile(2926, 3152, 0), new Tile(2923, 3152, 0),
            new Tile(2920, 3152, 0), new Tile(2917, 3152, 0),
            new Tile(2916, 3153, 0), new Tile(2916, 3156, 0),
    };
    public static final Area areaPS = new Area(
            new Tile(3031, 3215),
            new Tile(3025, 3215),
            new Tile(3025, 3238),
            new Tile(3048, 3238),
            new Tile(3049, 3232),
            new Tile(3031, 3233)
    );
    public static final Area areaK = new Area(
            new Tile(2956, 3144),
            new Tile(2940, 3144),
            new Tile(2912, 3149),
            new Tile(2912, 3154),
            new Tile(2926, 3154),
            new Tile(2937, 3151),
            new Tile(2958, 3149)
);
    public static final Area areaPlantation = new Area(
            new Tile(2916, 3154),
            new Tile(2934, 3152),
            new Tile(2934, 3163),
            new Tile(2927, 3177),
            new Tile(2911, 3174),
            new Tile(2904, 3162),
            new Tile(2909, 3156)
    );

    public static TilePath pathToBoatPS, pathToBoatK, pathToPlantation, pathToBank;

    @Override
    public void start() {
        pathToBoatPS = ctx.movement.newTilePath(pathPS);
        pathToBank = ctx.movement.newTilePath(pathPS).reverse();
        pathToBoatK = ctx.movement.newTilePath(pathK).reverse();
        pathToPlantation = ctx.movement.newTilePath(pathK);
        taskList.addAll(Arrays.asList(new Bank(ctx), new KBoatHandler(ctx), new PSBoatHandler(ctx),
                new KWalker(ctx), new PSWalker(ctx), new PlantationPicker(ctx)));
    }


    @Override
    public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
                Condition.sleep(500);
            }
        }
    }

    public void drawMouse(Graphics g) {
        g.setColor(Color.RED);
        Point p = ctx.input.getLocation();
        g.drawLine(p.x - 6, p.y, p.x + 6, p.y);
        g.drawLine(p.x, p.y - 6, p.x, p.y + 6);
    }

    @Override
    public void repaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHints(antialiasing);
        time = getRuntime();

        drawMouse(g);
        g.setColor(color1);
        g.fillRect(249, 11, 260, 62);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(249, 11, 260, 62);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("BananaPicker", 319, 35);
        g.setFont(font2);
        g.setColor(color4);
        g.drawString("Run Time: " + formatTime(time), 262, 50);
        g.drawString("Bananas Picked: " + bananasPicked, 381, 50);
        g.drawString("Status: " + status, 263, 67);
    }

    @Override
    public void messaged(MessageEvent e) {
        final String msg = e.text().toLowerCase();
        if (msg.equals("you pick a banana.")) {
            bananasPicked++;
        }
    }

    public String formatTime (long time) {
        final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
                + (s < 10 ? "0" + s : s);
    }
}
