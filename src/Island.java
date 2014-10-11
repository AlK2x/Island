
public class Island {

    private int N = 10;
    private TerrainField[][] island;

    public Island() {
        this.island = new TerrainField[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                this.island[i][j] = new TerrainField(StdRandom.uniform(1, 4));
            }
        }
        // is it possible to have this island, if not - repair
        this.checkIsland();
    }

    public TerrainField getTerrainField(int i, int j) {
        return island[i - 1][j - 1];
    }

    public void tickTack() {

    }

    private void checkIsland() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (island[i][j].getTerrainType() == TerrainField.WATER) {
                    try {
                        island[i + 1][j].setTerrainType(TerrainField.MEADOW);
                    } catch (IndexOutOfBoundsException e) { }
                    try {
                        island[i][j + 1].setTerrainType(TerrainField.MEADOW);
                    } catch (IndexOutOfBoundsException e) { }
                    try {
                        island[i - 1][j].setTerrainType(TerrainField.MEADOW);
                    } catch (IndexOutOfBoundsException e) { }
                    try {
                        island[i][j - 1].setTerrainType(TerrainField.MEADOW);
                    } catch (IndexOutOfBoundsException e) { }
                }
            }
        }
    }
}
