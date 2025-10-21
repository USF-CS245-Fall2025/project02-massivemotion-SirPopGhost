import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.net.URL;


public final class MassiveMotion extends JPanel implements ActionListener {

    // ----- Types -----
    /** Simulation body categories. */
    private enum Kind { STAR, COMET }

    /**
     * A single drawable/movable body in the simulation.
     */
    private static final class Body {
        Kind kind;
        int cx, cy;     // center position
        int vx, vy;     // pixels per tick
        int radius;     // draw radius
        Color color;

        /**
         * Constructs a Body
         */
        Body(Kind kind, int cx, int cy, int vx, int vy, int radius, Color color) {
            this.kind = kind;
            this.cx = cx; this.cy = cy;
            this.vx = vx; this.vy = vy;
            this.radius = radius;
            this.color = color;
        }

        /**
         * Advances the body's position by one tick using its velocity.
         */
        void step() { cx += vx; cy += vy; }

        /**
         * Renders the body as a filled circle centered at (cx, cy).
         */
        void paint(Graphics g) {
            g.setColor(color);
            g.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
        }

        /**
         * Determines whether this body lies entirely outside the viewport.
         */
        boolean offScreen(int w, int h) {
            return cx + radius < 0 || cx - radius > w || cy + radius < 0 || cy - radius > h;
        }
    }

    // ----- Config (defaults exactly per handout) -----
    private int timerDelay;
    private String listType;
    private int winW, winH;

    private int starX, starY; // treated as center
    private int starSize;
    private int starVx, starVy;

    private double genX; // probability to spawn along top/bottom each tick
    private double genY; // probability to spawn along left/right each tick
    private int bodySize;
    private int bodyVelMax;

    // ----- State -----
    private final Random rng = new Random();
    private final List<Body> bodies;
    private final Timer timer;

    // ----- Init -----
    /**
     * Reads an integer property, falling back to a default if missing or invalid.
     */
    private static int getInt(Properties p, String k, int dflt) {
        String v = p.getProperty(k);
        if (v == null) return dflt;
        try { return (int) Math.round(Double.parseDouble(v.trim())); }
        catch (Exception e) { return dflt; }
    }

    /**
     * Reads a double property, falling back to a default if missing or invalid.
     */
    private static double getDouble(Properties p, String k, double dflt) {
        String v = p.getProperty(k);
        if (v == null) return dflt;
        try { return Double.parseDouble(v.trim()); }
        catch (Exception e) { return dflt; }
    }

    /**
     * Loads all configuration values from a classpath resource path.
     * Uses documented defaults when properties are missing or malformed
    */
    private void loadConfig(String propPath) {
       Properties p = new Properties();
        try {
            // the configuration file name
            ClassLoader classLoader = MassiveMotion.class.getClassLoader();

            // Make sure that the configuration file exists
            URL res = Objects.requireNonNull(classLoader.getResource(propPath),
                "Can't find configuration file app.config");

            java.io.InputStream is = new java.io.FileInputStream(res.getFile());

            // load the properties file
            p.load(is);

            timerDelay = getInt(p, "timer_delay", 75);

            listType = p.getProperty("list", "arraylist"); // parsed; factory may use it later

            winW = getInt(p, "window_size_x", 1024);
            winH = getInt(p, "window_size_y", 768);

            starX = getInt(p, "star_position_x", 512);
            starY = getInt(p, "star_position_y", 384);
            starSize = getInt(p, "star_size", 30);
            starVx = getInt(p, "star_velocity_x", 0);
            starVy = getInt(p, "star_velocity_y", 0);

            genX = getDouble(p, "gen_x", 0.06);
            genY = getDouble(p, "gen_y", 0.06);

            bodySize = getInt(p, "body_size", 10);
            bodyVelMax = Math.max(1, getInt(p, "body_velocity", 3));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs the panel, loads config, seeds the star, and starts the timer.
     *
     */
    public MassiveMotion(String propPath) {
        loadConfig(propPath);
        setPreferredSize(new Dimension(winW, winH));

        bodies = ListFactory.fromProperty(listType);

        bodies.add(new Body(Kind.STAR, starX, starY, starVx, starVy, starSize, Color.RED));

        timer = new Timer(timerDelay, this);
        timer.start();
    }

    // ----- Spawning -----
    /**
     * Returns a non-zero integer velocity uniformly sampled from  maxAbs, maxAbs.
     *
     */
    private int randVelNZ(int maxAbs) {
        int v;
        do { v = rng.nextInt(maxAbs * 2 + 1) - maxAbs; } while (v == 0);
        return v;
    }

    /**
     * With probability genX, spawns a COMET at the top or bottom edge with velocity nudged inward.
     *
     */
    private void maybeSpawnTopBottom() {
        if (rng.nextDouble() >= genX) return;
        boolean fromTop = rng.nextBoolean();
        int cx = rng.nextInt(winW);
        int cy = fromTop ? -bodySize : winH + bodySize;
        int vx = randVelNZ(bodyVelMax);
        int vy = randVelNZ(bodyVelMax);
        if (fromTop && vy <= 0) vy = Math.abs(vy);
        if (!fromTop && vy >= 0) vy = -Math.abs(vy);
        bodies.add(new Body(Kind.COMET, cx, cy, vx, vy, bodySize, Color.BLACK));
    }

    /**
     * With probability genY, spawns a COMET at the left or right edge with velocity nudged inward.
     */
    private void maybeSpawnLeftRight() {
        if (rng.nextDouble() >= genY) return;
        boolean fromLeft = rng.nextBoolean();
        int cx = fromLeft ? -bodySize : winW + bodySize;
        int cy = rng.nextInt(winH);
        int vx = randVelNZ(bodyVelMax);
        int vy = randVelNZ(bodyVelMax);
        if (fromLeft && vx <= 0) vx = Math.abs(vx);
        if (!fromLeft && vx >= 0) vx = -Math.abs(vx);
        bodies.add(new Body(Kind.COMET, cx, cy, vx, vy, bodySize, Color.BLACK));
    }

    /**
     * Paints the current frame: clears to white, then draws all bodies.
     *
     */
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, winW, winH);
        for (int i = 0; i < bodies.size(); i++) bodies.get(i).paint(g);
    }

    /**
     * One timer tick: step all bodies, maybe spawn new ones, remove off-screen comets, request repaint.
     */
    @Override public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < bodies.size(); i++) bodies.get(i).step();

        maybeSpawnTopBottom();  
        maybeSpawnLeftRight(); 

        for (int i = bodies.size() - 1; i >= 0; i--) {
            Body b = bodies.get(i);
            if (b.kind == Kind.STAR) continue;
            if (b.offScreen(winW, winH)) bodies.remove(i);
        }

        repaint();
    }

    /**
     * Creates the window and starts the simulation.
     */
    public static void main(String[] args) {
        String propPath = args[0];

        MassiveMotion panel = new MassiveMotion(propPath);
        JFrame jf = new JFrame("Massive Motion");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(panel.winW, panel.winH); // spec: size from configuration
        jf.add(panel);
        jf.setVisible(true);
    }
}
