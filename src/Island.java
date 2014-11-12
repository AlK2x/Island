import java.util.Set;

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

    private RandomizedQueue<TerrainField> getNeighboringFields(int i, int j) {
        RandomizedQueue<TerrainField> neighboringFields = new RandomizedQueue<TerrainField>();
        TerrainField tempField;
        try {
            if (this.getTerrainField(i + 1, j).getTerrainType() == TerrainField.MEADOW) {
                neighboringFields.enqueue(this.getTerrainField(i + 1, j));
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (this.getTerrainField(i - 1, j).getTerrainType() == TerrainField.MEADOW) {
                neighboringFields.enqueue(this.getTerrainField(i - 1, j));
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (this.getTerrainField(i, j + 1).getTerrainType() == TerrainField.MEADOW) {
                neighboringFields.enqueue(this.getTerrainField(i, j + 1));
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (this.getTerrainField(i, j - 1).getTerrainType() == TerrainField.MEADOW) {
                neighboringFields.enqueue(this.getTerrainField(i, j - 1));
            }
        } catch (IndexOutOfBoundsException e) {}

        return neighboringFields;
    }

    private void updateIsland() {
        for (int i = 0; i < this.N; ++i) {
            for (int j = 0; j < this.N; ++j) {
                //System.out.println("[" + Integer.toString(i) + "," + Integer.toString(j) + "] " + Boolean.toString(isNearWater(i, j)) + " type=" + Integer.toString(island[i][j].getTerrainType()));
                island[i][j].updateTerrain( isNearWater(i, j) );
                island[i][j].updateRabbits(this.getNeighboringFields(i, j));
                if (island[i][j].getHunters() > 0) {
                    island[i][j].updateHunters(this.getNeighboringFields(i, j));
                }
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
