import org.omg.PortableInterceptor.INACTIVE;

public class TerrainField {

    public static final int WATER = 1;
    public static final int MOUNT = 2;
    public static final int MEADOW = 3;
    public static final int MAX_JUCINESS = 5;
    public static final int MAX_RABBITS = 3;
    public static final int MAX_HUNTERS = 3;


    private int terrainType;
    private int juiciness;
    private int rain;
    private int sun;
    private int rabbits;
    private int hunters;

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
                int refugees = Math.abs(this.rabbits - this.juiciness);
                for (TerrainField field : neighbourFields)
                {
                    if (field.hasPlaceForRabbit())
                    {
                        int numPlacesForRabbit = 3 - field.getRabbits();
                        int diff = refugees - numPlacesForRabbit;
                        if (diff <= 0)
                        {
                            field.setRabbits(field.getRabbits() + refugees);
                            this.juiciness -= refugees;
                            break;
                        }
                        else
                        {
                            field.setRabbits(field.getRabbits() + diff);
                            this.juiciness -= diff;
                            refugees = diff;
                        }
                    }
                }
                if (refugees > 0)
                {
                    while (refugees != 0)
                    {
                        this.dieRabbit();
                        --refugees;
                    }
                }
            }
            this.rabbitEatTheGrass();
            if (this.rabbits == 2) {
                this.addRabbit();
            }
        }
    }

    public boolean hasPlaceForRabbit()
    {
        return this.juiciness > 0 && this.rabbits < 3;
    }

    public int getRabbits() {
        return this.rabbits;
    }

    public void updateHunters(RandomizedQueue<TerrainField> neighbourFields) {
        if (this.hunters == 0) {
            return;
        }

        int hungryHunters = this.hunters;
        for (int i = 0; i < this.rabbits && i < this.hunters; ++i) {
            this.dieRabbit();
            --hungryHunters;
        }

        if (hungryHunters > 0) {
            this.moveHunterToOtherTerrain(neighbourFields, hungryHunters);
        }
    }

    private void moveHunterToOtherTerrain(RandomizedQueue<TerrainField> neighbourFields, int hungryHunters) {
        int infinityLoopProtection = 123;
        StdRandom.setSeed(System.currentTimeMillis());
        while (hungryHunters > 0 && infinityLoopProtection > 0) {
            --infinityLoopProtection;
            int len = neighbourFields.size();
            if (len == 0) {
                return;
            }
            StdOut.println("len: " + Integer.toString(StdRandom.uniform(len)));
            TerrainField field = neighbourFields.getItem(StdRandom.uniform(len));
            if (field.getHunters() >= 3) {
                continue;
            }

            int freePlacesForHunters = 3 - field.getHunters();
            if (hungryHunters <= freePlacesForHunters) {
                this.hunters -= hungryHunters;
                field.addHunters(hungryHunters);
                break;
            } else {
                this.hunters -= freePlacesForHunters;
                hungryHunters -= freePlacesForHunters;
                field.addHunters(freePlacesForHunters);
            }
        }


/*        for (TerrainField field : neighbourFields) {
            if (field.getHunters() >= 3 || field.getTerrainType() != TerrainField.MEADOW) {
                continue;
            }

            int freePlacesForHunters = 3 - field.getHunters();
            if (hungryHunters <= freePlacesForHunters) {
                this.hunters -= hungryHunters;
                field.addHunters(hungryHunters);
                break;
            } else {
                this.hunters -= freePlacesForHunters;
                hungryHunters -= freePlacesForHunters;
                field.addHunters(freePlacesForHunters);
            }
        }*/
    }

    public int getHunters() { return this.hunters; }

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
        if ( type != TerrainField.MEADOW ) {
            this.setSun(0);
            this.setRain(0);
            this.setRabbits(0);
        }
    }

    public int getJuiciness() {
        return this.juiciness;
    }

    public void setJuiciness(int i) {
        if (i >= 0) {
            this.juiciness = Math.min(i, TerrainField.MAX_JUCINESS);
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


    public void setSun(int i) { this.sun = i; }

    public void setRain(int i) { this.rain = i; }

    private boolean isNeedUpdateRabbits() {
        return (this.rabbits >= 2) || (this.rabbits > this.juiciness);
    }

    private void rabbitEatTheGrass()
    {
        this.juiciness = ((juiciness - this.rabbits)) >= 0 ? juiciness - this.rabbits : 0;
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
        if (this.juiciness < TerrainField.MAX_JUCINESS) {
            ++this.juiciness;
        }
    }

    private void addRabbit()
    {
        ++this.rabbits;
    }

    private void addHunters(int i) {
        this.hunters += i;
        if (this.hunters > 3) {
            StdOut.println("Exception: more then 3 hunters!");
        }
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
