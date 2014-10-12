import java.awt.*;

public class IslandVisualizer {

    // delay in miliseconds (controls animation speed)
    private static final int DELAY = 1000;

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

    public static void draw(Island island, int N) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, N);
        StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);

        for (int row = 1; row <= N; row++) {
            for (int col = 1; col <= N; col++) {
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
            }
        }
    }

    public static void main(String[] args) {

        // turn on animation mode
        //StdDraw.show(0);

        // repeatedly read in sites to open and draw resulting system
        int N = 4;
        Island island = new Island(N);
        island.setLifeTime(42);
        //StdDraw.show(0);


        StdDraw.setCanvasSize(800, 800);
        draw(island, N);


        //StdDraw.show(DELAY);
        /*while (!island.isEndOfTime()) {
            island.tickTack();
            draw(island, N);
            StdDraw.show(DELAY);
        }*/

        while (true) {
            if (StdDraw.mousePressed()) {

                island.tickTack();
                draw(island, N);
            }
            StdDraw.show(250);
        }
    }
}