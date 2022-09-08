import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        int[] num = new int[6];
        char[] chars = {'S', 'O', 'F', 'I', 'T', 'A'};
        for (int i=0; i<N; i++){
            if (x[i] == 'S'){
                num[0]++;
            }
            for (int j=1; j<6; j++) {
                if (num[j-1] > num[j] && x[i] == chars[j]) {
                    num[j]++;
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