import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.*;

public class Lab6 {
    private static InputReader in;
    static PrintWriter out;
    static Saham[] daftarSaham = new Saham[400012];
    static MaxHeap maxheap;
    static MinHeap minheap;
    static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
 
        // banyak saham
        N = in.nextInt();
        for (int i=1; i<=N; i++) {
            int Ci = in.nextInt();
            Saham newSaham = new Saham(i, Ci);
            daftarSaham[i] = newSaham;
        }

        Arrays.sort(daftarSaham, 1, N+1);
        int batasAtasMaxHeap = -999;
        int batasBawahMinHeap = -999;
        if (N%2 == 0){
            batasAtasMaxHeap = N/2; // a
            batasBawahMinHeap = N/2 + 1; // b
        } else {
            batasAtasMaxHeap = N/2;
            batasBawahMinHeap = N/2 + 1;
        }
        maxheap = new MaxHeap();
        for (int i=1; i<=batasAtasMaxHeap; i++) {
            maxheap.insert(daftarSaham[i]);
        }
        minheap = new MinHeap();
        for (int i=batasBawahMinHeap; i<=N; i++) {
            minheap.insert(daftarSaham[i]);
        }

        // banyak query
        int Q = in.nextInt();
        for (int i=0; i<Q; i++){
            String query = in.next();
            if (query.equals("TAMBAH")){
                int C = in.nextInt();
                TAMBAH(C);
            }
            else if (query.equals("UBAH")){
                int X = in.nextInt();
                int C = in.nextInt();
                UBAH(X, C);
            }
        }
        out.close();
    }
    static void TAMBAH(int C){
        N++;
        Saham newSaham = new Saham(N, C);
        Saham minSaham = minheap.getMin();
        if (newSaham.compareTo(minSaham) < 0){
            // System.out.println(">>>>> tail: " + maxheap.tail);
            maxheap.insert(newSaham);
            // System.out.println(">>>>> new tail: " + maxheap.tail);
        } else {
            // System.out.println(">>>>> tail: " + minheap.tail);
            minheap.insert(newSaham);
            // System.out.println(">>>>> new tail: " + minheap.tail);
        }
        if (getDiffSize() > 1){
            Saham theSaham = minheap.removeMin();
            maxheap.insert(theSaham);
        } else if (getDiffSize() < -1){
            Saham theSaham = maxheap.removeMax();
            minheap.insert(theSaham);
        }
        minSaham = minheap.getMin();
        out.println(minSaham.seri);
    }
    static void UBAH(int X, int C){}


    static int getDiffSize(){
        // jumlah 1/2 termahal - jumlah 1/2 termurah
        // System.out.println(">>>>> minheap tail: " + minheap.tail);
        // System.out.println(">>>>> maxheap tail: " + maxheap.tail);
        // System.out.println(">>>>> different size: " + (minheap.tail - maxheap.tail));
        return minheap.tail - maxheap.tail;
    }

    // taken from https://codeforces.com/submissions/Petr
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


class Saham implements Comparable<Saham> {
    int seri, harga;
    Saham (int seri, int harga){
        this.seri = seri;
        this.harga = harga;
    }
    public int compareTo(Saham o){
        if (this.harga < o.harga) return -1;
        else if (this.harga > o.harga) return 1;
        else return this.seri - o.seri;
    }
}

// * 1/2 TERMAHAL
class MinHeap {
    Saham[] heap;
    int tail;
    MinHeap(){
        this.tail = 0;
        heap = new Saham[200012];
    }
    int parent(int i){
        return (i-1)/2;
    }
    int leftChild(int i){
        return 2*i+1;
    }
    int rightChild(int i){
        return 2*i+2;
    }
    // get saham paling murah di 1/2 termahal
    Saham getMin(){
        return heap[0];
    }
    void swap(int i, int j){
        Saham temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    void percolateUp(int i){
        if (i==0) return;
        if (heap[parent(i)].compareTo(heap[i]) > 0){
            swap(i, parent(i));
            percolateUp(parent(i));
        }
    }
    void percolateDown(int i){
        int min = i;
        if (leftChild(i) < tail && heap[leftChild(i)].compareTo(heap[min]) < 0) min = leftChild(i);
        if (rightChild(i) < tail && heap[rightChild(i)].compareTo(heap[min]) < 0) min = rightChild(i);
        if (min != i){
            swap(i, min);
            percolateDown(min);
        }
    }
    void insert(Saham saham){
        heap[tail] = saham;
        tail++;
        percolateUp(tail-1); //percolate up newest element
    }
    Saham removeMin(){
        Saham min = heap[0];
        heap[0] = heap[tail-1];
        tail--;
        percolateDown(0);
        return min;
    }
}
// * 1/2 TERMURAH
class MaxHeap {
    Saham[] heap;
    int tail;
    MaxHeap(){
        this.tail = 0;
        heap = new Saham[200012];
    }
    int parent(int i){
        return (i-1)/2;
    }
    int leftChild(int i){
        return 2*i+1;
    }
    int rightChild(int i){
        return 2*i+2;
    }
    // get saham paling mahal di 1/2 termurah
    Saham getMax(){
        return heap[0];
    }
    void swap(int i, int j){
        Saham temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    void percolateUp(int i){
        if (i==0) return;
        if (heap[parent(i)].compareTo(heap[i]) < 0){
            swap(i, parent(i));
            percolateUp(parent(i));
        }
    }
    void percolateDown(int i){
        int max = i;
        if (leftChild(i) < tail && heap[leftChild(i)].compareTo(heap[max]) > 0) max = leftChild(i);
        if (rightChild(i) < tail && heap[rightChild(i)].compareTo(heap[max]) > 0) max = rightChild(i);
        if (max != i){
            swap(i, max);
            percolateDown(max);
        }
    }
    void insert(Saham saham){
        heap[tail] = saham;
        tail++;
        percolateUp(tail-1); // percolate up newest element
    }
    Saham removeMax(){
        Saham max = heap[0];
        heap[0] = heap[tail-1];
        tail--;
        percolateDown(0);
        return max;
    }
}