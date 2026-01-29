import java.util.Scanner;

public class TransportationMODImethod {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println();
        System.out.println();
        System.out.println("╔══════════════════════╗");
        System.out.println("║       WELCOME        ║");
        System.out.println("╚══════════════════════╝");
        System.out.println();
        System.out.println("\nPlease enter the following details for the Transportation Problem:\n");
        System.out.println();

        // m=number of sources (supply)
        int m = readNonNegativeInt(input, "Enter number of sources: ");

        // n=number of destinations (demand)
        int n = readNonNegativeInt(input, "Enter number of destinations: ");

        int[][] cost = new int[m][n];
        int[] supply = new int[m];
        int[] demand = new int[n];

        // Read cost matrix
        System.out.println("\n--- Enter the cost matrix --- ");
        for (int i = 0; i < m; i++) {  // i = row (supply)
            for (int j = 0; j < n; j++) { // j = column(demand)
                cost[i][j] = readNonNegativeInt(input, "Cost for Source " + (i + 1) + " -> Destination " + (j + 1) + ": ");
            }
        }

        // Read supply
        int sumSupply = 0;
        System.out.println("Enter the supply values: ");
        for (int i = 0; i < m; i++) {
            supply[i] = readNonNegativeInt(input, "Enter supply for source " + (i + 1) + ": ");
            sumSupply += supply[i];
            System.out.println("Total Supply: " + sumSupply);
        }

        // Read demand
        int sumDemand = 0;
        System.out.println("Enter the demand values: ");
        for (int j = 0; j < n; j++) {
            demand[j] = readNonNegativeInt(input, "Enter demand for destination " + (j + 1) + ": ");
            sumDemand += demand[j];
            System.out.println("Total Demand: " + sumDemand);
        }

        // is supply equal to demand?
        int gapSupply = 0;
        int gapDemand = 0;
        if (sumSupply == sumDemand) {
            System.out.println("Supply = Demand. No need for dummy.");
        } else if (sumSupply > sumDemand) {
            gapSupply = sumSupply - sumDemand;
            System.out.println("Supply > Demand. Adding dummy destination =  " + gapSupply);
        } else {
            gapDemand = sumDemand - sumSupply;
            System.out.println("Demand > Supply. Adding dummy source = " + gapDemand);
        }

        System.out.println("Transportation Matrix is: ");

        if (gapSupply > 0) {
            // Print header 
            System.out.print("\t");
            for (int j = 0; j < n; j++) {
                System.out.print("D" + (j + 1) + "\t");
            }
            System.out.print("D " + "Dummy\t");
            System.out.print("\tsupply");

            System.out.println();

            // Print cost matrix with supply
            for (int i = 0; i < m; i++) {
                System.out.print("S" + (i + 1) + "\t");
                for (int j = 0; j < n; j++) {
                    System.out.print(cost[i][j] + "\t");
                }
                System.out.print("\t\t");
                System.out.println(supply[i]);
            }

            // Print demand row
            System.out.print("Demand\t");
            for (int j = 0; j < n; j++) {
                System.out.print(demand[j] + "\t");
            }
            System.out.print(gapSupply + "\t");
            System.out.println();
        } // end if gapSupply
        else if (gapDemand > 0) {
            // Print header
            System.out.print("\t");
            for (int j = 0; j < n; j++) {
                System.out.print("D" + (j + 1) + "\t");
            }
            System.out.print("supply");

            System.out.println();

            // Print cost matrix with supply
            for (int i = 0; i < m; i++) {
                System.out.print("S" + (i + 1) + "\t");
                for (int j = 0; j < n; j++) {
                    System.out.print(cost[i][j] + "\t");
                }
                System.out.println(supply[i]);
            }
            System.out.print("S Dummy\t\t\t\t" + gapDemand);
            System.out.println();

            // Print demand row
            System.out.print("Demand\t");
            for (int j = 0; j < n; j++) {
                System.out.print(demand[j] + "\t");
            }
            System.out.println();
        } // end else if gapDemand
        else {
            // Print header 
            System.out.print("\t");
            for (int j = 0; j < n; j++) {
                System.out.print("D" + (j + 1) + "\t");
            }
            System.out.print("supply");

            System.out.println();

            // Print cost matrix with supply
            for (int i = 0; i < m; i++) {
                System.out.print("S" + (i + 1) + "\t");
                for (int j = 0; j < n; j++) {
                    System.out.print(cost[i][j] + "\t");
                }
                System.out.println(supply[i]);
            }

            // Print demand row
            System.out.print("Demand\t");
            for (int j = 0; j < n; j++) {
                System.out.print(demand[j] + "\t");
            }
            System.out.println();
        } // end else

        // determine new matrix size
        int M = m;
        int N = n;

        if (gapDemand > 0) {
            M = m + 1;
        } else if (gapSupply > 0) {
            N = n + 1;
        }

        int[][] newCost = new int[M][N];
        int[] newSupply = new int[M];
        int[] newDemand = new int[N];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                newCost[i][j] = cost[i][j];
            }
        }

        if (gapSupply > 0) {
            for (int i = 0; i < m; i++) {
                newCost[i][n] = 0;
            }
            newDemand[n] = gapSupply;
        }

        if (gapDemand > 0) {
            for (int j = 0; j < n; j++) {
                newCost[m][j] = 0;
            }
            newSupply[m] = gapDemand;
        }

        for (int i = 0; i < m; i++) {
            newSupply[i] = supply[i];
        }

        for (int j = 0; j < n; j++) {
            newDemand[j] = demand[j];
        }

        int[][] X = new int[M][N];
        long totalCost = 0;

        System.out.println("\nApplying North West Corner Method...\n");

        int currentRow = 0;
        int currentCol = 0;

        while (currentRow < M && currentCol < N) {

            int allocation = Math.min(newSupply[currentRow], newDemand[currentCol]);
            X[currentRow][currentCol] = allocation;

            newSupply[currentRow] -= allocation;
            newDemand[currentCol] -= allocation;

            totalCost += (long) allocation * newCost[currentRow][currentCol];

            if (newSupply[currentRow] == 0 && newDemand[currentCol] == 0) {
                currentRow++;
                currentCol++;
            } else if (newSupply[currentRow] == 0) {
                currentRow++;
            } else if (newDemand[currentCol] == 0) {
                currentCol++;
            }

        }

        System.out.println("Allocation Matrix:");
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(X[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("\nTotal Cost = " + totalCost);

        System.out.println("\noptimality check (MODI method)");
        // optimality check (MODI method)
        boolean improves = true;
        int MIN_VALUE = Integer.MIN_VALUE;
        int[] u = new int[M];
        int[] v = new int[N];

        while (improves) {
            // initialize u, v as unknown
            for (int i = 0; i < M; i++) u[i] = MIN_VALUE;
            for (int j = 0; j < N; j++) v[j] = MIN_VALUE;
            // assume u0 = 0
            u[0] = 0;
            // compute u and v using occupied cells
            boolean changed = true;

            while (changed) {
                changed = false;
                for (int i = 0; i < M; i++) {
                    for (int j = 0; j < N; j++) {
                        if (X[i][j] > 0) { // occupied cell
                            if (u[i] != MIN_VALUE && v[j] == MIN_VALUE) {
                                v[j] = newCost[i][j] - u[i];
                                changed = true;
                            } else if (v[j] != MIN_VALUE && u[i] == MIN_VALUE) {
                                u[i] = newCost[i][j] - v[j];
                                changed = true;
                            }
                        }
                    }
                }
            }

            // calculate opportunity cost for unoccupied cells
            improves = false;
            int minDelta = Integer.MAX_VALUE;
            int enterI = -1, enterJ = -1;
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    if (X[i][j] == 0) { // unoccupied cell
                        int delta = newCost[i][j] - (u[i] + v[j]);
                        if (delta < minDelta) {
                            minDelta = delta;
                            enterI = i;
                            enterJ = j;
                        }
                    }
                }
            }

            if (minDelta >= 0) {
                System.out.println("Solution is OPTIMAL");
                System.out.println("\nFinal Total Cost = " + totalCost);
                improves = false;
            } else {
                System.out.println("Not optimal, improving allocation...");

                // Find all possible 4-cell loops involving (enterI, enterJ)
                int theta = Integer.MAX_VALUE;
                boolean loopFound = false;
                int loopRowJ = -1;
                int loopColI = -1;

                for (int c = 0; c < N; c++) {
                    if (c == enterJ) continue;
                    if (X[enterI][c] > 0) { // occupied in same row
                        for (int r = 0; r < M; r++) {
                            if (r == enterI) continue;
                            if (X[r][enterJ] > 0 && X[r][c] > 0) { // intersect cell occupied
                                int currTheta = Math.min(X[enterI][c], X[r][enterJ]);
                                if (currTheta < theta) {
                                    theta = currTheta;
                                    loopRowJ = c;
                                    loopColI = r;
                                    loopFound = true;
                                }
                            }
                        }
                    }
                }

                if (!loopFound) {
                    System.out.println("Cannot find suitable 4-cell closed loop for stepping stone method. Ending optimization.");
                    improves = false;
                    break;
                }

                // Adjust allocations along the loop (+, -, +, -)
                X[enterI][enterJ] += theta;
                X[enterI][loopRowJ] -= theta;
                X[loopColI][loopRowJ] += theta;
                X[loopColI][enterJ] -= theta;

                // Recalculate total cost with updated allocation
                totalCost = computeCost(X, newCost);
                System.out.println("Allocation updated using Stepping Stone method.");

            }
        }
        System.out.println("\nFinal Optimal Allocation Matrix:");
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(X[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("\nFinal Total Cost = " + totalCost);

    }

    // compute total transportation cost helper method (must be outside main)
    static long computeCost(int[][] allocation, int[][] cost) {
        long total = 0;
        int m = allocation.length;
        int n = allocation[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                total += (long) allocation[i][j] * cost[i][j];
            }
        }
        return total;
    }

    // Helper method to read validated non-negative integers
    static int readNonNegativeInt(Scanner input, String prompt) {
        int value = -1;
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid non-negative integer.");
                input.next(); // consume invalid token
            } else {
                value = input.nextInt();
                if (value < 0) {
                    System.out.println("Please enter a non-negative number.");
                } else {
                    break; // valid input
                }
            }
        }
        return value;
    }
}
