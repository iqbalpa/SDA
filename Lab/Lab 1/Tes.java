import java.io.*;
import java.util.StringTokenizer;

public class Tes {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        boolean[] flag = new boolean[N];
        boolean flagS = false;
        boolean flagO = false;
        boolean flagF = false;
        boolean flagI = false;
        boolean flagT = false;
        boolean flagA = false;

        // S S S S S S
        // dihitung kepake semua jadinya 0

        for (int i=0; i<N; i++){
            if (!flag[i]) {
                if (x[i] == 'S') {
                    flag[i] = true;
                    flagS = true;
                } else if (flagS && x[i] == 'O') {
                    flag[i] = true;
                    flagO = true;
                } else if (flagO && x[i] == 'F') {
                    flag[i] = true;
                    flagF = true;
                } else if (flagF && x[i] == 'I') {
                    flag[i] = true;
                    flagI = true;
                } else if (flagI && x[i] == 'T') {
                    flag[i] = true;
                    flagT = true;
                } else if (flagT && x[i] == 'A') {
                    flag[i] = true;
                    flagA = true;
                } else if (flagA) {
                    flagS = false;
                    flagO = false;
                    flagF = false;
                    flagI = false;
                    flagT = false;
                    flagA = false;
                }
            }
        }

        int counter = 0;
        for (int i=0; i<N; i++){
            if (!flag[i]) {
                counter++;
            }
        }

        return N-((int)counter/6);
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