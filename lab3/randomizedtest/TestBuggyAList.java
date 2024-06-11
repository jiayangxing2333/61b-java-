package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        correct.addLast(5);
        correct.addLast(10);
        correct.addLast(15);

        broken.addLast(5);
        broken.addLast(10);
        broken.addLast(15);

        assertEquals(correct.size(), broken.size());

        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
    }


    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            int correctSize = correct.size();
            int brokenSize = broken.size();
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                System.out.println("correctSize: " + correctSize + " and brokenSize: "+ brokenSize);
            }else if (operationNumber == 2) {
                if ( correctSize != 0 && brokenSize != 0){
                    int a = correct.getLast();
                    int b = broken.getLast();
                    System.out.println("correctGetLast(" + a + ")" + " and brokenGetLast(" + b + ")");
                }
            }else if (operationNumber == 3){
                if ( correctSize != 0 && brokenSize != 0){
                    int c = correct.removeLast();
                    int d = broken.removeLast();
                    System.out.println("correctRemoveLast(" + c + ")" + " and brokenRemoveLast(" + d + ")");
                }
            }
            }
        }
}
