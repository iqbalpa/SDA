import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

// Kolaborator
// 1. Aushaaf Fadhillah

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer

        // create new array
        int[] num = new int[6];

        // create array of characters to compare
        char[] chars = {'S', 'O', 'F', 'I', 'T', 'A'};

        // looping over x
        for (int i=0; i<N; i++){

            // if the character is 'S'
            // then increment the value of num[0]
            if (x[i] == 'S'){
                num[0]++;
            }

            // loop for comparing if the previous character is counted already
            // then count the next character
            for (int j=1; j<6; j++) {
                if (num[j-1] > num[j] && x[i] == chars[j]) {
                    num[j]++;
                }
            }
        }

        // initialize the minimum value (as the number of SOFITA appearances)
        int minimum = num[0];

        // loop to find the minimum value
        for (int i=1; i<6; i++) {
            minimum = Math.min(minimum, num[i]);
        }

        // return the total of characters from user minus the multiply of minimum value and 6
        return N-(minimum*6);
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}