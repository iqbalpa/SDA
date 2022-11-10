import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Bismillah {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // TODO: get the input from user

        // banyak mesin pada FunZone
        int N = in.nextInt();
        for (int i=1; i<=N; i++){
            MesinPermainan newMesin = new MesinPermainan(i);

            // banyak skor awal pada mesin ke-i
            int Mi = in.nextInt();
            for (int j=1; j<=Mi; j++){
                int Zj = in.nextInt();
            }
        }

        // banyak query
        int Q = in.nextInt();
        for (int i=1; i<=Q; i++){
            String query = in.next();

            // cek jenis query
            if (query.equals("MAIN")){
                // skor Budi
                int Y = in.nextInt();
                mainmain(Y);
            }
            else if (query.equals("GERAK")){
                // arah gerak Budi
                String arahGerak = in.next();
                gerak(arahGerak);
            }
            else if (query.equals("HAPUS")) {
                // banyak skor yg mau dihapus
                int X = in.nextInt();
                hapus(X);
            }
            else if (query.equals("LIHAT")) {
                // batas bawah skor
                int L = in.nextInt();
                // batas atas skor
                int H = in.nextInt();
                lihat(L, H);
            }
            else if (query.equals("EVALUASI")){
                evaluasi();
            }
        }
        
        out.close();
    }

    // method untuk handle masing-masing query
    // query MAIN
    static void mainmain(int skorBudi){}

    // query GERAK
    static void gerak(String arahGerak){}

    // query HAPUS
    static void hapus(int banyakSkor){}

    // query LIHAT
    static void lihat(int batasBawah, int batasAtas){}

    // query EVALUASI
    static void evaluasi(){}

    
    // referensi: template Lab SDA biasanya
    // untuk input output
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

// class untuk node skor di dalam mesin permainan
class Skor {
    int skor, jumlahSkor;
    int leftSize, rightSize, height;
    Skor left, right;
    Skor (int skor) {
        this.skor = skor;
        this.jumlahSkor = 1;
        this.leftSize = 0;
        this.rightSize = 0;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}

// class untuk mesin permainan
// mesin permainan sebagai AVL tree
class MesinPermainan {
    int id;
    Skor root;
    MesinPermainan next, prev;
    MesinPermainan(int id){
        this.id = id; 
    }

    // method-method untuk operasi pada AVL tree    
    int getHeight(Skor node){
        if (node == null) return 0;
        return node.height;
    }
    int getBalance(Skor node){
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
    Skor rightRotate(Skor node) {
        Skor node1 = node.left;
        Skor node2 = node1.right;
        node1.right = node;
        node.left = node2;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node1.height = Math.max(getHeight(node1.left), getHeight(node1.right)) + 1;
        return node1;
    }
    Skor leftRotate(Skor node) {
        // TODO: implement left rotate
        Skor node1 = node.right;
        Skor node2 = node1.left;
        node1.left = node;
        node.right = node2;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node1.height = Math.max(getHeight(node1.left), getHeight(node1.right)) + 1;
        return node1;
    }
    Skor insertSkor(Skor node, int newSkor){
        if (node == null) return new Skor(newSkor);
        if (newSkor < node.skor) node.left = insertSkor(node.left, newSkor);
        else if (newSkor > node.skor) node.right = insertSkor(node.right, newSkor);
        else node.jumlahSkor++;
        
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (newSkor < node.left.skor) {
                return rightRotate(node);
            }
            else if (newSkor > node.left.skor) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balanceFactor < -1) {
            if (newSkor > node.right.skor) {
                return leftRotate(node);
            }
            else if (newSkor < node.right.skor) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }
    Skor lowerBound(Skor node, int batasBawah){
        if (node == null) return null;
        if (node.skor == batasBawah) return node;
        if (node.skor < batasBawah) return lowerBound(node.right, batasBawah);
        Skor temp = lowerBound(node.left, batasBawah);
        return (temp.skor >= batasBawah) ? temp : node;
    }
    Skor upperBound(Skor node, int batasAtas){
        if (node == null) return null;
        if (node.skor == batasAtas) return node;
        if (node.skor > batasAtas) return upperBound(node.left, batasAtas);
        Skor temp = upperBound(node.right, batasAtas);
        return (temp.skor <= batasAtas) ? temp : node;
    }
}

// FunZone sebagai kumpulan MesinPermainan as LinkedList class
class FunZone {
    MesinPermainan first, last, current;
    FunZone(){
        this.first = null;
        this.last = null;
        this.current = null; // buat posisi Budi
    }
    void addMesinPermainan(MesinPermainan newMesin){
        if (first == null){
            first = newMesin;
            last = first;
        } else {
            newMesin.prev = last;
            last = newMesin;
            last.next = first;
            first.prev = last;
        }
    }
}
