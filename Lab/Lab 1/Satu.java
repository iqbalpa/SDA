import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Satu {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        int[] flag = new int[N];
        int[] num = new int[6];
        boolean flagS = false;
        boolean flagO = false;
        boolean flagF = false;
        boolean flagI = false;
        boolean flagT = false;
        boolean flagA = false;

        for (int i=0; i<N; i++) {
            if (x[i] == 'S') {
                flag[i]++;
                num[0]++;
                flagS = true;
            } else if (flagS && x[i] == 'O') {
                flag[i]++;
                num[1]++;
                flagO = true;
            } else if (flagO && x[i] == 'F') {
                flag[i]++;
                num[2]++;
                flagF = true;
            } else if (flagF && x[i] == 'I') {
                flag[i]++;
                num[3]++;
                flagI = true;
            } else if (flagI && x[i] == 'T') {
                flag[i]++;
                num[4]++;
                flagT = true;
            } else if (flagT && x[i] == 'A') {
                flag[i]++;
                num[5]++;
                flagA = true;
            } if (flagA) {
                flagS = false;
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