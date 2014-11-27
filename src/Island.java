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

                this.moveWolves(i, j);                  // волки, которые не под прицелом перемещаются
                this.moveHunters(i, j);                 // охотники, которых не съедят волки
                island[i][j].huntersKillsWolf();        // люди убивают 1 волка
                island[i][j].wolvesKillsHunter();       // волки съедают 1 охотника
                island[i][j].wolvesKillsRabbits();      // если нет охотнико, волки едят кроликов
                island[i][j].rabbitsReproduction();     // в этой клетке появится 1 новый кролик, независимо будут там его родители или нет
                island[i][j].huntersKillsRabbits();     // если нет волков, охотники убивают кролей
                this.moveRabbits(i, j);
                island[i][j].rabbitsEatsGrass();

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

    private void moveWolves(int i, int j)
    {
        int freeWolves = island[i][j].getFreeWolves();
        if ( freeWolves > 0 )
        {
            for (int it = 1; it <= freeWolves; ++it)
            {
                this.moveWolf(i ,j);
            }
        }

    }

    private void moveWolf(int i, int j)
    {
        Vector<Direction> availableDirVector = new Vector<Direction>();
        availableDirVector.clear();
        for (int it = 0; it < DIRECTIONS.length; ++it)
        {
            int currRow = i + DIRECTIONS[it].dx;
            int currCol = j + DIRECTIONS[it].dy;
            if (isCorrectCoordinate(currRow, currCol) && island[ currRow ][ currCol ].isAvailableForWolves())
            {
                availableDirVector.add( DIRECTIONS[it] );
            }
        }
        if (!availableDirVector.isEmpty())
        {
            Direction randDir = getRandomDirection( availableDirVector );
            island[i][j].removeWolf();
            island[i + randDir.dx][j + randDir.dy].addWolf();
        }
    }

    private void moveRabbits(int i, int j)
    {
        int freeRabbits = island[i][j].rabbitsNeedMoveCount();
        if ( freeRabbits > 0 )
        {
            for (int it = 1; it <= freeRabbits; ++it)
            {
                this.moveRabbit(i ,j);
            }
        }

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

    private void moveHunters(int i, int j)
    {
        int freeHunters = island[i][j].getFreeHunters();
        if ( freeHunters > 0 )
        {
            for (int it = 1; it <= freeHunters; ++it)
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
