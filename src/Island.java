import java.util.Stack;
import java.util.Vector;

public class Island implements Cloneable{

    private int N = 4;
    private TerrainField[][] island;
    private int time;

    private static class Direction {
        int dx, dy;

        public Direction(int i, int j )
        {
            this.dx = i;
            this.dy = j;
        }
    }

    private Direction TOP = new Direction(-1, 0);
    private Direction TOP_RIGHT = new Direction(-1, 1);
    private Direction RIGHT = new Direction(0, 1);
    private Direction BOTTOM_RIGHT = new Direction(1, 1);
    private Direction BOTTOM = new Direction(1, 0);
    private Direction BOTTOM_LEFT = new Direction(1, -1);
    private Direction LEFT = new Direction(0, -1);
    private Direction TOP_LEFT = new Direction(-1, -1);

    private Direction[] DIRECTIONS = { TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT };

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

                this.updateHunters(i, j);
                this.updateRabbits(i, j);

                island[i][j].updateTerrain( isNearWater(i, j) );
            }
        }

        for (int i = 0; i < this.N; ++i) {
            for (int j = 0; j < this.N; ++j) {
                System.out.println("ApplyDeltas " + i + " " + j);
                island[i][j].applyDeltas();
                island[i][j].resetDeltas();
            }
        }
    }

    private void updateRabbits(int i, int j)
    {
        int hungryRabbits = island[i][j].rabbitsNeedMoveCount();
        if ( hungryRabbits > 0 )
        {
            for (int it = 1; it <= hungryRabbits; ++it)
            {
                this.moveRabbit(i ,j);
            }
        }
        island[i][j].rabbitsEatsGrass();
        island[i][j].rabbitsReproduction();
    }

    private void moveRabbit(int i, int j)
    {
        //System.out.println("current i j = " + i + " " + j);
        Vector<Direction> availableDirVector = new Vector<Direction>();
        availableDirVector.clear();
        for (int it = 0; it < DIRECTIONS.length; ++it)
        {
            int currRow = i + DIRECTIONS[it].dx;
            int currCol = j + DIRECTIONS[it].dy;
            //System.out.println("potential point " + currRow + " " + currCol);
            if (isCorrectCoordinate(currRow, currCol) && island[ currRow ][ currCol ].isAvailableForRabbit())
            {
                availableDirVector.add( DIRECTIONS[it] );
            }
        }
        island[i][j].dieRabbit(); //Кролик из текущей клетки изымается в любом случае
        if (!availableDirVector.isEmpty())
        {
            Direction randDir = getRandomDirection( availableDirVector );

            island[i + randDir.dx][j + randDir.dy].addRabbit();
        }

    }

    private void updateHunters(int i, int j)
    {
        int unsatisfiedHunters = island[i][j].getUnsatisfiedHunters();
        if ( unsatisfiedHunters > 0 )
        {
            for (int it = 1; it <= unsatisfiedHunters; ++it)
            {
                this.moveHunter(i ,j);
            }
        }

        //island[i][j].rabbitsEatsGrass();
        //island[i][j].rabbitsReproduction();
    }

    private void moveHunter(int i, int j)
    {
        Vector<Direction> availableDirVector = new Vector<Direction>();
        availableDirVector.clear();
        for (int it = 0; it < DIRECTIONS.length; ++it)
        {
            int currRow = i + DIRECTIONS[it].dx;
            int currCol = j + DIRECTIONS[it].dy;
            //System.out.println("potential point " + currRow + " " + currCol);
            if (isCorrectCoordinate(currRow, currCol) && island[ currRow ][ currCol ].isAvailableForHunter())
            {
                availableDirVector.add( DIRECTIONS[it] );
            }
        }
        if (!availableDirVector.isEmpty())
        {
            Direction randDir = getRandomDirection( availableDirVector );
            island[i][j].removeHunter();
            island[i + randDir.dx][j + randDir.dy].addHunter();
        }
    }

    private Direction getRandomDirection(Vector<Direction> vector)
    {
        int dirCount = vector.size();
        int randDirNum = dirCount > 1 ? (int) Math.round( Math.random() * (dirCount - 1) ) : 0 ; // magic, magic!
        return vector.get(randDirNum);
    }

    private boolean isNearWater(int row, int col) {
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = col - 1; j <= col + 1; ++j) {
                if ( !((i == row) && (j == col)) && isCorrectCoordinate(i, j) ) {
                    if (island[i][j].getTerrainType() == TerrainField.WATER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCorrectCoordinate(int i, int j) {
        if ( (i >= 0) && (i < this.N) && (j >= 0) && (j < this.N) ) {
            return true;
        }
        return false;
    }

    public Island clone() throws CloneNotSupportedException {
        return (Island)super.clone();
    }
}
