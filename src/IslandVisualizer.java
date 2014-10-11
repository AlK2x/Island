import java.awt.*;

public class IslandVisualizer {

    // delay in miliseconds (controls animation speed)
    private static final int DELAY = 500;

    // draw N-by-N percolation system
    public static void draw(Island island, int N) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, N);
        StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);

        for (int row = 1; row <= N; row++) {
            for (int col = 1; col <= N; col++) {
                TerrainField terrain = island.getTerrainField(row, col);
                if (terrain.getTerrainType() == TerrainField.WATER) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                }
                else if (terrain.getTerrainType() == TerrainField.MOUNT) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                }
                else if (terrain.getTerrainType() == TerrainField.MEADOW) {
                    // change meadow color, in order to juiciness
                    int juice = terrain.getJuiciness();
                    StdDraw.setPenColor(new Color(0, 100 + juice * 31, 0));
                }
                StdDraw.filledSquare(col - 0.5, N - row + 0.5, 0.45);
            }
        }
    }

    public static void main(String[] args) {

        // turn on animation mode
        StdDraw.show(0);

        // repeatedly read in sites to open and draw resulting system
        Island island = new Island();
        island.setLifeTime(42);
        draw(island, 10);
        StdDraw.show(0);
        StdDraw.show(DELAY);
        while (!island.isEndOfTime()) {
            island.tickTack();
            draw(island, 10);
            StdDraw.show(DELAY);
        }
    }
}