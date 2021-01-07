import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;

public class DiskScheduler {

    public static int count = 1;
    public static int totalArmMotions = 0;

    /**
     * Implements the Elevator Algorithm using a TreeSet and two recursive helper methods. A TreeSet is a Tree Data
     * structure that will stay sorted as you add and remove items to it. A TreeSet also contains useful methods for
     * splitting into sub-TreeSets based on a value.
     */
    public static void elevatorAlgorithm(TreeSet<Integer> diskRequests, int currPos, boolean dirUp)
    {
        int newPos;

        // If starting direction is up, process diskRequests greater than the current position of the disk arm first.
        if (dirUp)
        {
           newPos = elevatorUp((TreeSet<Integer>) diskRequests.tailSet(currPos), currPos);
           elevatorDown((TreeSet<Integer>) diskRequests.headSet(currPos), newPos);
        }
        // If starting direction is down, process diskRequests less than the current position of the disk arm first.
        else
        {
            newPos = elevatorDown((TreeSet<Integer>) diskRequests.headSet(currPos), currPos);
            elevatorUp((TreeSet<Integer>) diskRequests.tailSet(currPos), newPos);
        }

        System.out.println("Total number of arm motions = " + totalArmMotions);
    }

    /**
     * Will move up the tree to each greater element, removing it after printing. Once it reaches the highest element,
     * the recursion hits the base case and stops.
     */
    public static int elevatorUp(TreeSet<Integer> diskRequests, int currPos)
    {
        // Base case: We have reached the largest element, return the current position.
        if (diskRequests.higher(currPos) == null)
        {
            return currPos;
        }

        int diskReq = diskRequests.higher(currPos);
        int armMotion = diskReq - currPos;

        diskRequests.remove(diskReq);
        totalArmMotions += armMotion;
        System.out.println(count++ + " " + diskReq + " UP " + armMotion);

        return elevatorUp(diskRequests, diskReq);
    }

    /**
     * Will move up the tree to each lesser element, removing it after printing. Once it reaches the lowest element,
     * the recursion hits the base case and stops.
     */
    public static int elevatorDown(TreeSet<Integer> diskRequests, int currPos)
    {
        // Base case: We have reached the smallest element, return the current position.
        if (diskRequests.lower(currPos) == null)
        {
            return currPos;
        }
        int diskReq = diskRequests.lower(currPos);
        int armMotion = currPos - diskReq;

        diskRequests.remove(diskReq);
        totalArmMotions += armMotion;
        System.out.println(count++ + " " + diskReq + " DOWN " + armMotion);

        return elevatorDown(diskRequests, diskReq);
    }

    public static void validateInputs(int numCylinders, int currPos, int numDirection, int numDiskRequest)
    {
        if (numCylinders < 0)
        {
            throw new IllegalArgumentException("Number of cylinders must be greater than 0.");
        }

        if (currPos <= 0)
        {
            throw new IllegalArgumentException("Current position must be greater than 0.");
        }

        if (numDirection != 0 && numDirection != 1)
        {
            throw new IllegalArgumentException("Direction must be either 1 or 0.");
        }

        if (numDiskRequest < 0)
        {
            throw new IllegalArgumentException("Number of Disk Requests must be 0 or greater.");
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        if (args.length == 1 && args[0] != null)
        {
            Scanner scan = new Scanner(new File(args[0]));

            try
            {
                int numCylinders = scan.nextInt();
                int currPos = scan.nextInt();
                int numDirection = scan.nextInt();
                int numDiskRequest = scan.nextInt();

                validateInputs(numCylinders, currPos, numDirection, numDiskRequest);

                boolean dirUp = numDirection == 1;
                TreeSet<Integer> diskRequests = new TreeSet<>();

                for (int i = 0; i < numDiskRequest; i++)
                {
                    if (!scan.hasNext())
                    {
                        throw new IllegalArgumentException("Number of Disk Requests specified does not " +
                                "match number of disk requests given.");
                    }

                    int cylinderNum = scan.nextInt();

                    if (cylinderNum <= 0 || cylinderNum > numCylinders)
                    {
                        throw new IllegalArgumentException("Disk request cylinder number must be between 1 and " +
                                numCylinders + ".");
                    }

                    // Special case if the currPos is already on a Cylinder
                    if (cylinderNum == currPos)
                    {
                        String direction = dirUp ? " UP " : " DOWN ";
                        System.out.println(count++ + " " + cylinderNum + direction + "0");
                    }
                    else
                    {
                        diskRequests.add(cylinderNum);
                    }
                }

                elevatorAlgorithm(diskRequests, currPos, dirUp);
            }
            catch (InputMismatchException e)
            {
                throw new IllegalArgumentException("All numbers in file must be integers.");
            }

            scan.close();
        }
        else
        {
            throw new IllegalArgumentException("Usage: java Lab_12_A01022399 filename.txt");
        }
    }
}
