public class Main {
    static int[][] gameBoard = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 2, 2, 0, 5, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 5, 0, 0},
            {0, 0, 0, 3, 3, 3, 0, 5, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 5, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 5, 0, 0},
            {0, 3, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 3, 0, 4, 4, 4, 4, 0, 0, 0},
            {0, 3, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    static int[][] probabilityBoard = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    static char[][] hitBoard = {
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
    };

    static int turnCount = 0;

    public static void main(String[] args) {
        printBoard(probabilityBoard);
    setProbabilityBoard(probabilityBoard);
        printBoard(probabilityBoard);
        while (!isGameOver()) {
            playBattleship();
            printBoard(hitBoard);
            printBoard(probabilityBoard);
        }
        System.out.println("Finished in " + turnCount + " turns");
    }

    public static void printBoard(int[][] probabilityBoard) {
        for (int i = 0; i < probabilityBoard.length; i++) {
            for (int j = 0; j < probabilityBoard[i].length; j++) {
                System.out.print(probabilityBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printBoard(char[][] probabilityBoard) {
        for (int i = 0; i < probabilityBoard.length; i++) {
            for (int j = 0; j < probabilityBoard[i].length; j++) {
                System.out.print(probabilityBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void setProbabilityBoard(int[][] probabilityBoard) {
        for (int i = 0; i < probabilityBoard.length; i++) {
            for (int j = 0; j < probabilityBoard[i].length; j++) {
                if (hitBoard[i][j] == 'X') {
                    int[] bestNextHit = checkNearbyHitCoords(i, j);
                    if (bestNextHit[0] != -1) {
                        probabilityBoard[bestNextHit[0]][bestNextHit[1]] = 50;
                    }
                }
                if (!isValidHit(i, j)) {
                    probabilityBoard[i][j] = 0;
                    continue;
                }
                //50 arbitrarily for nearby ships if ship has been hit
                if (probabilityBoard[i][j] != 50) {
                    probabilityBoard[i][j] = getClosestObstacle(i, j);
                }
            }
        }
    }


    public static int[] checkNearbyHitCoords(int x, int y) {
        int[] bestHit = {-1, -1};
            if (x < 9) {
                if (x != 0) {
                    if (hitBoard[x - 1][y] == 'X' && isValidHit(x + 1, y)) {
                        bestHit[0] = x + 1;
                        bestHit[1] = y;
                    }
                }
            }
            if (x > 0) {
                if (x != 9) {
                    if (hitBoard[x + 1][y] == 'X' && isValidHit(x - 1, y)) {
                        bestHit[0] = x - 1;
                        bestHit[1] = y;
                    }
                }
            }
            if (y < 9) {
                if (y != 0) {
                    if (hitBoard[x][y - 1] == 'X' && isValidHit(x, y + 1)) {
                        bestHit[0] = x;
                        bestHit[1] = y + 1;
                    }
                }
            }
            if (y > 0) {
                if (y != 9) {
                    if (hitBoard[x][y + 1] == 'X' && isValidHit(x, y - 1)) {
                        bestHit[0] = x;
                        bestHit[1] = y - 1;
                    }
                }
            }
        return bestHit;
    }

    public static int getClosestObstacle(int x, int y) {
        int shortestNode = getShortestNodeDistance(x, y);
        if (shortestNode == 0) {
            return getShortestWallDistance(x, y);
        }
        return Math.min(getShortestWallDistance(x, y), getShortestNodeDistance(x, y));
    }

    public static int getShortestWallDistance(int x, int y) {
        int distanceFromLeft = x + 1;
        int distanceFromRight =  10 - x;
        int distanceFromTop = y + 1;
        int distanceFromBottom = 10 - y;
        int shortestDistance = Math.min(distanceFromLeft, distanceFromRight);
        shortestDistance = Math.min(shortestDistance, distanceFromTop);
        shortestDistance = Math.min(shortestDistance, distanceFromBottom);

        return shortestDistance;
    }

    public static int getShortestNodeDistance(int row, int col) {
        int distLeft = 100;
        int distRight = 100;
        int distTop = 100;
        int distBottom = 100;
        for (int i = col; i >= 0; i--) {
            if (hitBoard[row][i] == 'O') {
                distLeft = col - i;
                break;
            }
        }
        for (int i = col; i <= 9; i++) {
            if (hitBoard[row][i] == 'O') {
                distRight = i - col;
                break;
            }
        }
        for (int i = row; i >= 0; i--) {
            if (hitBoard[i][col] == 'O') {
                distTop = row - i;
                break;
            }
        }
        for (int i = row; i <= 9; i++) {
            if (hitBoard[i][col] == 'O') {
                distBottom = i - row;
                break;
            }
        }


        int shortestDistance = Math.min(distLeft, distRight);
        shortestDistance = Math.min(shortestDistance, distTop);
        shortestDistance = Math.min(shortestDistance, distBottom);
        return shortestDistance;
    }

    public static boolean isHit(int x, int y) {
        return gameBoard[x][y] > 0;
    }

    public static boolean isGameOver() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] > 0 && hitBoard[i][j] != 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void playBattleship() {
        setProbabilityBoard(probabilityBoard);
        int[] hitCoords = getNextHitCoords();
        hit(hitCoords[0], hitCoords[1]);
        turnCount++;
    }

    public static int[] getNextHitCoords() {
        int maxScore = probabilityBoard[0][0];
        int[] coords = new int[2];
        for (int i = 0; i < probabilityBoard.length; i++) {
            for (int j = 0; j < probabilityBoard[i].length; j++) {
                if (probabilityBoard[i][j] > maxScore) {
                    maxScore = probabilityBoard[i][j];
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }
        return coords;
    }

    public static boolean isValidHit(int x, int y) {
        if (hitBoard[x][y] == '.') {
            return true;
        }
        return false;
    }

    public static void hit(int x, int y) {
        if (hitBoard[x][y] == '.') {
            if (isHit(x, y)) {
                hitBoard[x][y] = 'X';
                System.out.printf("Hit at %d, %d\n", x, y);
            } else {
                hitBoard[x][y] = 'O';
            }
        }
    }
}