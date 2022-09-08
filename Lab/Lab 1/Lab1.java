import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        int counter = 0;
        ArrayList<Integer> indeks = new ArrayList<>();
        for (int i=0; i<N; i++){
            for (int j=i; j<N; j++) {
                if (indeks.isEmpty()) {
                    if (x[i] == 'S') {
                        indeks.add(i);
                    } else {
                        counter++;
                    }
                } else if (indeks.size() == 1) {
                    if (x[j] == 'O') {
                        indeks.add(j);
                    } else {
                        counter++;
                    }
                } else if (indeks.size() == 2) {
                    if (x[j] == 'F') {
                        indeks.add(j);
                    } else {
                        counter++;
                    }
                } else if (indeks.size() == 3) {
                    if (x[j] == 'I') {
                        indeks.add(j);
                    } else {
                        counter++;
                    }
                } else if (indeks.size() == 4) {
                    if (x[j] == 'T') {
                        indeks.add(j);
                    } else {
                        counter++;
                    }
                } else if (indeks.size() == 5) {
                    if (x[j] == 'A') {
                        indeks.add(j);
                    } else {
                        counter++;
                    }
                }
            }
            if (indeks.size() == 6) {
                indeks = new ArrayList<>();
            } else {
                return counter;
            }
        }
        
        return counter;
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