import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi Class Lantai
class Lantai {
    public Lantai() {}
}

// TODO: Lengkapi Class Gedung
class Gedung {
    String nama;
    Gedung next;
    int jumlahLantai;
    public Gedung(String nama, int jumlahLantai) {
        this.nama = nama;
        this.next = null;
        this.jumlahLantai = jumlahLantai;
    }
}

class ListGedung {
    Gedung first;
    Gedung last;
    public ListGedung() {
        this.first = null;
        this.last = null;
    }
    public void add(Gedung newGedung){
        if (first == null){
            first = newGedung;
            last = newGedung;
        } else {
            last.next = newGedung;
            last = newGedung;
        }
        last.next = first;
    }
}

// TODO: Lengkapi Class Karakter
class Karakter {
    int diLantai;
    boolean isTurun;
    Gedung diGedung;
    public Karakter(){}
}

public class Test {

    private static InputReader in;
    static PrintWriter out;
    static ListGedung listGedung = new ListGedung();
    static Karakter denji = new Karakter();
    static Karakter iblis = new Karakter();
    static int jumlahBertemu = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // TODO: Inisiasi gedung pada kondisi awal
            Gedung newGedung = new Gedung(namaGedung, jumlahLantai);
            listGedung.add(newGedung);
        }


        // DENJI
        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        Gedung temp = listGedung.first;
        while (temp.next != null){
            if (temp.nama.equals(gedungDenji)){
                denji.diGedung = temp;
                break;
            }
            temp = temp.next;
        }
        denji.diLantai = lantaiDenji;
        denji.isTurun = false;


        // IBLIS
        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        temp = listGedung.first;
        while(temp.next != null){
            if(temp.nama == gedungIblis){
                iblis.diGedung = temp;
                break;
            }
            temp = temp.next;
        }
        iblis.diLantai = lantaiIblis;
        iblis.isTurun = true;


        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            String command = in.next();
            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }
        out.close();
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {
        if (denji.isTurun) {
            if (denji.diLantai == 1) {
                denji.diGedung = denji.diGedung.next;
                denji.diLantai = 1;
                denji.isTurun = false;
            } else {
                denji.diLantai--;
            }
        } else {
            if (denji.diLantai == denji.diGedung.jumlahLantai) {
                denji.diGedung = denji.diGedung.next;
                denji.diLantai = denji.diGedung.jumlahLantai;
                denji.isTurun = true;
            } else {
                denji.diLantai++;
            }
        }
        if (iblis.isTurun) {
            if (iblis.diLantai <= 2) {
                iblis.diGedung = iblis.diGedung.next;
                iblis.diLantai = 1;
                iblis.isTurun = false;
            } else {
                iblis.diLantai -= 2;
            }
        } else {
            if (iblis.diLantai >= iblis.diGedung.jumlahLantai - 1) {
                iblis.diGedung = iblis.diGedung.next;
                iblis.diLantai = iblis.diGedung.jumlahLantai;
                iblis.isTurun = true;
            } else {
                iblis.diLantai += 2;
            }
        }
        if (denji.diGedung == iblis.diGedung && denji.diLantai == iblis.diLantai) {
            jumlahBertemu++;
        }
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        if (denji.diLantai != 1){
            if (denji.diGedung != iblis.diGedung){
                denji.diGedung.jumlahLantai--;
                denji.diLantai--;
            } else {
                if (denji.diLantai-1 != iblis.diLantai){
                    denji.diGedung.jumlahLantai--;
                    denji.diLantai--;
                }
            }
        }
    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {
        iblis.diGedung.jumlahLantai++;
        iblis.diLantai++;
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        denji.diGedung = denji.diGedung.next;
        if (denji.isTurun){
            denji.diLantai = denji.diGedung.jumlahLantai;
        } else {
            denji.diLantai = 1;
        }
    }

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