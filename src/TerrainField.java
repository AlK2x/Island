
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

    public void updateTerrain() {
        this.changeWeather();
        this.changeMeadow();
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

    private void changeWeather() {
        this.generateWeather();
    }

    private void grassDie() {
        if (this.juiciness > 0) {
            --this.juiciness;
        }
    }

    private void grassGrow() {
        if (this.juiciness < 5) {
            ++this.juiciness;
        }
    }

    private void changeMeadow() {
        if (this.sun == 0) {
            if (this.rain == 3) {
                this.grassDie();
            }
        }
        else if (this.sun == 1) {
            if (this.rain == 1) {
                this.grassGrow();
            }
            else if (this.rain == 2) {
                this.grassGrow();
            }
        }
        else if (this.sun == 2) {
            if (this.rain > 0) {
                this.grassGrow();
            }
        }
        else if (this.sun == 3) {
            if (this.rain == 0) {
                this.grassDie();
            }
            else if (this.rain == 2) {
                this.grassGrow();
            }
            else if (this.rain == 3) {
                this.grassGrow();
            }
        }
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
