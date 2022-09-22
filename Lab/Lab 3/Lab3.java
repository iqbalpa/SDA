import java.io.*;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)
        int sumLeft = 0;
        int sumRight = 0;
        int maxLeft = 0;
        int maxRight = 0;
        int center = (start + end) / 2;
        if (start == end) {
            return A[start] == 'R' ? 1 : 0;
        }

        sumLeft = getMaxRedVotes(start, center);
        sumRight = getMaxRedVotes(center + 1, end);
        
        for (int i=center; i>=0; i--){
            sumLeft = countRedVotes(A, i, center);
            if (sumLeft > maxLeft){
                maxLeft = sumLeft;
            }
        }
        for (int i=center+1; i<=end; i++){
            sumRight = countRedVotes(A, center+1, i);
            if (sumRight > maxRight){
                maxRight = sumRight;
            }
        }

        return max3(maxLeft, maxRight, maxLeft + maxRight);
    }

    // helper method for counting votes
    public static int countRedVotes(char[] array, int start, int end) {
        int R = 0;
        int B = 0;
        int[] result = new int[2]; // result[0] = R, result[1] = B
        for (int i = start; i <= end; i++) {
            if (array[i] == 'R') {
                R++;
            } else {
                B++;
            }
        }
        if (R > B) {
            R += B;
            B = 0;
        } else {
            B += R;
            R = 0;
        }
        result[0] = R;
        result[1] = B;
        return R;
    }
    public static int max3(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}