import edu.princeton.cs.algs4.StdIn;

public class HashAttack {
    public static void main(String[] args) {
        System.out.println("2^N Strings for length N to perform hash attack.");
        System.out.print("Enter N: ");
        int n = StdIn.readInt();
        // delta2 = -delta1 * 31^delPos [independent of actual position]
        // 256 chars. 31^2 . 256. So, change has to be in consecutive positions
        // delta2 = -delta1 * 31
        // 256 / 31 ~ 8.226..
        // so, max 8 char change by more significant char can be reimbursed by lesser significant char change, on the extreme case that the lesser significant char is unicode 255
        // then one pair of of change can be permuted in the whole string for different classes of changes on that position pair

        // the span between the first alphabet and last is 61 unicode. 2 * 31 = 62 > 61. So, max 1 char shift can occur on more significant alphabet.
        // Significant char increased: which allows lesser significant bit to be 97 to 97 + (26 - 1) = 122. ie. all smaller capped alphabets.
        // Significant char decreased: 66(B) + 31 = 97 (a). So, lesser significant digit can be B to Z
    }
}
