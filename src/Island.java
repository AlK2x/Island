
public class Island {

    private int N;
    private TerrainField[][] island;
    private int time;

    public Island(int N) {
        this.N = N;
        this.island = new TerrainField[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {

                this.island[i][j] = new TerrainField( this.getRandTerrainType() );
            }
        }
    }

    public TerrainField getTerrainField(int i, int j) {
        return island[i - 1][j - 1];
    }

    public void setLifeTime(int time) {
        this.time = time;
    }

    public boolean isEndOfTime() {
        return this.time == 0;
    }

    public void tickTack() {
        --time;
        this.updateIsland();
    }

    /**
     * Returns randomized TerrainType with an increased probability of occurrence MEADOW
     *
     * @return int TerrainType
     */
    private int getRandTerrainType()
    {
        int terrainType = StdRandom.uniform(1, 10);
        if (terrainType > TerrainField.MEADOW)
        {
            terrainType = TerrainField.MEADOW;
        }
        return terrainType;
    }

    private void updateIsland() {
        for (int i = 0; i < this.N; ++i) {
            for (int j = 0; j < this.N; ++j) {
                //System.out.println("[" + Integer.toString(i) + "," + Integer.toString(j) + "] " + Boolean.toString(isNearWater(i, j)) + " type=" + Integer.toString(island[i][j].getTerrainType()));
                island[i][j].updateTerrain( isNearWater(i, j) );
            }
        }
    }

    private boolean isNearWater(int row, int col) {
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = col - 1; j <= col + 1; ++j) {
                if ( !((i == row) && (j == col)) && (i >= 0) && (i < this.N) && (j >= 0) && (j < this.N)) {
                    if (island[i][j].getTerrainType() == TerrainField.WATER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
