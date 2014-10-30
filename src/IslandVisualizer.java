import java.awt.*;
import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;


public class IslandVisualizer {

    // delay in miliseconds (controls animation speed)
    private static final int DELAY = 1000;
    private static final int RESOLUTION = 800;
    private static boolean lockedMouse = false;

    // draw N-by-N percolation system
    public static void drawMountain(int col, int row, int N) {
        StdDraw.setPenColor(StdDraw.BLACK);
        double[] x = {col-0.9, col-0.8, col-0.7, col-0.55, col-0.4, col-0.35, col-0.15, col-0.9};
        double[] y = {N-row+0.1, N-row+0.5, N-row+0.4, N-row+0.85, N-row+0.6, N-row+0.7, N-row+0.1, N-row+0.1};
        StdDraw.setPenRadius(0.01);
        StdDraw.polygon(x, y);
    }

    public static void drawWeather(int col, int row, int rainState, int sunState, int N) {
        if (rainState > 0)
        {
            StdDraw.picture(col-0.75, N-row+0.75,"images/rain0" + Integer.toString(rainState) + ".png", 0.47, 0.47);
        }
        if (sunState > 0)
        {
            StdDraw.picture(col-0.25, N-row+0.75,"images/sun0" + Integer.toString(sunState) + ".png", 0.47, 0.47);
        }

    }

    private static void drawRabbit(int col, int row, TerrainField field, int N)
    {
        int rabbits = field.getRabbits();
        if (rabbits > 0)
        {
            StdDraw.picture(col-0.35, N-row+0.25, "images/rabbit0" + Integer.toString(rabbits) + ".png", 0.35 + rabbits * 0.07 , 0.35 + rabbits * 0.05 );
        }
    }

    public static void draw(Island island, int N) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, N);
        StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);

        for (int row = N; row >= 1; row--) {
            for (int col = N; col >= 1; col--) {
                TerrainField terrain = island.getTerrainField(row, col);
                String text = "";
                if (terrain.getTerrainType() == TerrainField.WATER) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                }
                else if (terrain.getTerrainType() == TerrainField.MOUNT) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                }
                else if (terrain.getTerrainType() == TerrainField.MEADOW) {
                    // change meadow color, in order to juiciness
                    int juice = terrain.getJuiciness();
                    if (juice > 0) {
                        StdDraw.setPenColor(new Color(0, 255 - (juice -1) * 45, 0));
                    }
                    else {
                        StdDraw.setPenColor(StdDraw.WHITE);
                    }
                }
                StdDraw.filledSquare(col - 0.5, N - row + 0.5, 0.48);
                StdDraw.setPenColor(StdDraw.BLACK);

                if (terrain.getTerrainType() == TerrainField.MOUNT)
                {
                    drawMountain(col, row, N);
                }

                drawWeather(col, row, terrain.getRain(), terrain.getSun(), N);
                drawRabbit(col, row, terrain, N);
            }
        }
    }

    private static void displayPopup(TerrainField field) {
        String[] items = {"ВОДА", "ГОРЫ", "ЛУГ"};
        JComboBox combo = new JComboBox(items);
        combo.setSelectedIndex(field.getTerrainType() - 1);
        JTextField field1 = new JTextField(String.valueOf(field.getJuiciness()));
        JTextField field2 = new JTextField(String.valueOf(field.getRabbits()));


        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(combo);
        panel.add(new JLabel("Трава:"));
        panel.add(field1);
        panel.add(new JLabel("Кролики:"));
        panel.add(field2);
        int result = JOptionPane.showConfirmDialog(null, panel, "Тонкая настройка",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            field.setTerrainType(combo.getSelectedIndex() + 1);
            field.setJuiciness(Integer.parseInt(field1.getText()));
            field.setRabbits(Integer.parseInt(field2.getText()));
        } else {
            System.out.println("Cancelled");
        }
    }

    private static TerrainField findTerrain(Island island, int x, int y, int N) {
        StdOut.println("find");
        StdOut.println(island.getTerrainField(y, x).getTerrainType());
        return island.getTerrainField(y, x);
    }

    private static int InvertCoord(int y, int N) {
        int arr[] = new int[N + 1];
        int st = N;
        for (int i = 1; i <= N; ++i) {
            arr[i] = st--;
        }

        return arr[y];
    }

    public static void main(String[] args) {

        // turn on animation mode
        //StdDraw.show(0);

        // repeatedly read in sites to open and draw resulting system
        int N = 6;
        Island island = new Island(N);
        island.setLifeTime(42);
        //StdDraw.show(0);


        StdDraw.setCanvasSize(RESOLUTION, RESOLUTION);
        draw(island, N);

        while (true) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                island.tickTack();
                draw(island, N);
            }

            if (StdDraw.mousePressed())
            {
                StdOut.println(StdDraw.mouseX());
                StdOut.println(StdDraw.mouseY());
                lockedMouse = true;
            }

            if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER) && lockedMouse)
            {
                try {
                    int x = (int) StdDraw.mouseX() + 1;
                    int y = InvertCoord((int) StdDraw.mouseY() + 1, N);
                    displayPopup(findTerrain(island, x, y, N));
                    draw(island, N);
                    lockedMouse = false;
                }
                catch (IndexOutOfBoundsException e) {
                    lockedMouse = false;
                    continue;
                }
            }

            StdDraw.show(250);
        }
    }
}