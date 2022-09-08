import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Test {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        int[] flag = new int[N];
        int[] num = new int[6];
        // S O F I T A
        // 0 1 2 3 4 5

        for (int i=0; i<N; i++) {
            if (flag[i] == 0) {
                if (x[i] == 'S') {
                    flag[i]++;
                    num[0]++;
                }
                else if (x[i] == 'O'){
                    flag[i]++;
                    num[1]++;
                }
                else if (x[i] == 'F') {
                    flag[i]++;
                    num[2]++;
                }
                else if (x[i] == 'I') {
                    flag[i]++;
                    num[3]++;
                }
                else if (x[i] == 'T') {
                    flag[i]++;
                    num[4]++;
                }
                else if (x[i] == 'A'){
                    flag[i]++;
                    num[5]++;
                }
            }
        }

        int minimum = num[0];
        for (int i=1; i<6; i++) {
            minimum = Math.min(minimum, num[i]);
        }

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