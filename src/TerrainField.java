import org.omg.PortableInterceptor.INACTIVE;

public class TerrainField {

    public static final int WATER = 1;
    public static final int MOUNT = 2;
    public static final int MEADOW = 3;
    public static final int MAX_SUN = 3;
    public static final int MAX_RAIN = 3;
    public static final int MAX_JUICINESS = 5;
    public static final int MAX_RABBITS = 3;
    public static final int MAX_HUNTERS = 3;
    public static final int MAX_WOLVES = 3;


    private int terrainType;
    private int juiciness;
    private int rain;
    private int sun;
    private int rabbits;
    private int hunters;
    private int wolves;

    private int deltaJuiciness;
    public int deltaRabbit;
    public int deltaHunter;
    public int deltaWolf;

    public TerrainField(int type) {
        this.terrainType = type;
        this.resetMembers();
        this.resetDeltas();
        this.changeWeather();
    }

    public void updateTerrain(boolean isNearWater) {
        if (this.getTerrainType() == TerrainField.MEADOW)
        {
            this.changeMeadow( isNearWater );
            this.changeWeather();

        }
    }

    public int getRabbits() { return this.rabbits; }

    public int getHunters() { return this.hunters; }

    public int getWolves() { return this.wolves; }

    public int getJuiciness() {
        return this.juiciness;
    }

    public int getRain() { return this.rain; }

    public int getSun() { return this.sun; }

    public int getTerrainType() {
        return this.terrainType;
    }

    public void setTerrainType(int type) {
        this.terrainType = type;
        if ( type != TerrainField.MEADOW ) {
            this.resetMembers();
        }
    }

    public void resetMembers() {
        this.juiciness = 0;
        this.sun = 0;
        this.rain = 0;
        this.rabbits = 0;
        this.hunters = 0;
        this.wolves = 0;
        this.resetDeltas();
    }


    public void resetDeltas()
    {
        this.deltaJuiciness = 0;
        this.deltaRabbit = 0;
        this.deltaHunter = 0;
        this.deltaWolf = 0;
    }


    public void setJuiciness(int i) {
        if (i >= 0) {
            this.juiciness = Math.min(i, TerrainField.MAX_JUICINESS);
        }
    }

    public void setRabbits(int i) {
        if ((i >= 0) && (this.getTerrainType() == TerrainField.MEADOW)) {
            this.rabbits = Math.min(i, TerrainField.MAX_RABBITS);
        }
    }

    public void setHunters(int i) {
        if (i >= 0 && this.getTerrainType() == TerrainField.MEADOW) {
            this.hunters = Math.min(i, TerrainField.MAX_HUNTERS);
        }
    }

    public void setWolves(int i) {
        if (i >= 0 && this.getTerrainType() == TerrainField.MEADOW) {
            this.wolves = Math.min(i, TerrainField.MAX_WOLVES);
        }
    }


    public void setSun(int i) {
        if (i >= 0 && this.getTerrainType() == TerrainField.MEADOW) {
            this.sun = Math.min(i, TerrainField.MAX_SUN);
        }
    }

    public void setRain(int i) {
        if (i >= 0 && this.getTerrainType() == TerrainField.MEADOW) {
            this.rain = Math.min(i, TerrainField.MAX_RAIN);
        }
    }

    public int rabbitsNeedMoveCount()
    {
        int hungryRabbits = this.getRabbits() - this.getJuiciness();
        return hungryRabbits;
    }

    public boolean isAvailableForRabbit()
    {
        int rabbitCountTomorrow = (this.getRabbits() + this.deltaRabbit);
        boolean isFoodAvailable = (this.getJuiciness() - rabbitCountTomorrow) > 0;
        boolean isMaxRabbitCountAchieved = rabbitCountTomorrow < MAX_RABBITS;
        if ( (this.getTerrainType() == MEADOW) && (this.getJuiciness() > 0) && isFoodAvailable && isMaxRabbitCountAchieved )
        {
            return true;
        }
        return false;
    }

    public int getUnsatisfiedHunters()
    {
        if (this.getWolves() > 0)
        {
            return 0; // if wolves exists, rabbits must live
        }
        int unsatisfiedHunters = this.getHunters() - this.getRabbits();
        this.huntersKillsRabbits();
        return unsatisfiedHunters;
    }

    public boolean isAvailableForHunter()
    {
        int hunterCountTomorrow = (this.getHunters() + this.deltaHunter);

        boolean isMaxHunterCountAchieved = hunterCountTomorrow < MAX_RABBITS;
        if ( (this.getTerrainType() == MEADOW) && isMaxHunterCountAchieved )
        {
            return true;
        }
        return false;
    }

    public void rabbitsEatsGrass()
    {
        for (int i = 1; i <= this.getRabbits(); ++i)
        {
            this.grassDie(); System.out.println("grass die");
        }
    }

    public void rabbitsReproduction()
    {
        if (this.getRabbits() == 2)
        {
            this.addRabbit();
        }
    }

    public void applyDeltas()
    {
        this.applyDeltaJuiciness();
        this.applyDeltaRabbit();
        this.applyDeltaHunter();
        this.applyDeltaWolves();
    }

    private void applyDeltaJuiciness()
    {
        this.addJuiciness( this.deltaJuiciness );
    }

    private void applyDeltaRabbit() { this.addRabbit( this.deltaRabbit ); }

    private void applyDeltaHunter() { this.addHunters( this.deltaHunter ); }

    private void applyDeltaWolves()
    {
        this.addWolves( this.deltaWolf );
    }

    private void addJuiciness( int delta )
    {
        this.juiciness += delta;
        if (this.juiciness < 0) this.juiciness = 0;
        if (this.juiciness > MAX_JUICINESS) this.juiciness = MAX_JUICINESS;
    }

    private void addRabbit( int delta )
    {
        this.rabbits += delta;
        if (this.rabbits < 0) this.rabbits = 0;
        if (this.rabbits > MAX_RABBITS) this.rabbits = MAX_RABBITS;
    }

    private void addHunters( int delta) {
        this.hunters += delta;
        if (this.hunters < 0) this.hunters = 0;
        if (this.hunters > MAX_HUNTERS) this.hunters = MAX_HUNTERS;
    }

    private void addWolves( int delta) {
        this.wolves += delta;
        if (this.wolves < 0) this.wolves = 0;
        if (this.wolves > MAX_WOLVES) this.wolves = MAX_WOLVES;
    }

    private void changeWeather() {
        if (this.getTerrainType() == TerrainField.MEADOW) {
            this.generateWeather();
        }
    }

    private void grassDie() {
        --this.deltaJuiciness;
    }

    public void grassGrow() {
        ++this.deltaJuiciness;
    }

    public void dieRabbit() { --this.deltaRabbit; }

    public void addRabbit()
    {
        ++this.deltaRabbit;
    }

    public void removeHunter() { --this.deltaHunter; }

    public void addHunter() { ++this.deltaHunter; }

    private void huntersKillsRabbits()
    {
        this.deltaRabbit -= Math.min(this.getRabbits(), this.getHunters());
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
