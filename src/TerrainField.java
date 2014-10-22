
public class TerrainField {

    public static final int WATER = 1;
    public static final int MOUNT = 2;
    public static final int MEADOW = 3;

    private int terrainType;
    private int juiciness;
    private int rain;
    private int sun;
    private int rabbits;

    public TerrainField(int type) {
        this.terrainType = type;
        this.juiciness = 0;
        this.rain = 0;
        this.sun = 0;
        this.rabbits = 0;
        this.changeWeather();
    }

    public void updateTerrain(boolean isNearWater) {
        if (this.getTerrainType() == TerrainField.MEADOW)
        {
            this.changeMeadow( isNearWater );
            this.changeWeather();
        }
    }

    public void updateRabbits(RandomizedQueue<TerrainField> neighbourFields) {
        if (this.isNeedUpdateRabbits()) {
            if (this.rabbits > this.juiciness) {
                // need code for migrate to neighbourFields;
                this.dieRabbit();
            }
            if (this.rabbits >= 2) {
                this.addRabbit();
            }
        }
    }

    public int getRabbits() {
        return this.rabbits;
    }

    public int getRain() {
        return this.rain;
    }

    public int getSun() {
        return this.sun;
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

    private boolean isNeedUpdateRabbits() {
        return (this.rabbits >= 2) || (this.rabbits > this.juiciness);
    }

    private void changeWeather() {
        if (this.getTerrainType() == TerrainField.MEADOW) {
            this.generateWeather();
        }
    }

    private void grassDie() {
        if (this.juiciness > 0) {
            --this.juiciness;
        }
    }

    public void grassGrow() {
        if (this.juiciness < 5) {
            ++this.juiciness;
        }
    }

    private void addRabbit()
    {
        ++this.rabbits;
    }

    private void dieRabbit()
    {
        if (this.rabbits > 0)
        {
            --this.rabbits;
        }
    }

    private void changeMeadow(boolean isNearWater) {
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
            else if (isNearWater) {
                this.grassGrow();
            }
        }
        else if (this.sun == 2) {
            if ( (this.rain > 0) || (isNearWater) ) {
                this.grassGrow();
            }
        }
        else if (this.sun == 3) {
            if ( isNearWater ) {
                this.grassGrow();
            }
            else if (this.rain == 0) {
                this.grassDie();
            }
        }
    }

    private void generateWeather() {
        this.sun = StdRandom.uniform(4);
        this.rain = StdRandom.uniform(4);
        if (this.rain == 3) {
            this.sun = 0;
        }
        else if (this.sun == 3) {
            this.rain = 0;
        }
    }
}
