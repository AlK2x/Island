
public class TerrainField {

    public static final int WATER = 1;
    public static final int MOUNT = 2;
    public static final int MEADOW = 3;

    private int terrainType;
    private int juiciness;
    private int rain;
    private int sun;

    public TerrainField(int type) {
        this.terrainType = type;
        this.juiciness = 0;
    }

    public int getTerrainType() {
        return this.terrainType;
    }

    public void setTerrainType(int type) {
        this.terrainType = type;
    }

    public int getJuiciness() {
        return this.juiciness;
    }

    public void changeWeather() {
        this.generateWeather();
    }

    private void generateWeather() {
        this.sun = StdRandom.uniform(4);
        this.rain = StdRandom.uniform(4);
        if (this.sun == 3) {
            this.rain = 0;
        }
        else if (this.rain == 3) {
            this.sun = 0;
        }
    }
}
