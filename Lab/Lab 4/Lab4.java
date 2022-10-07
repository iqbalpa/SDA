import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab4 {
    private static InputReader in;
    private static PrintWriter out;
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
            listGedung.addGedung(newGedung);
        }


        // DENJI
        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        // loop over the list of gedung and find the initialize gedung for denji
        Gedung temp = listGedung.first;
        while (temp.next != null){
            if (temp.nama.equals(gedungDenji)){
                denji.diGedung = temp;
                break;
            }
            temp = temp.next;
        }
        // set the initialize lantai for denji
        denji.diLantai = lantaiDenji;
        // set the initialize walk direction for denji
        denji.isTurun = false;


        // IBLIS
        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        // loop over the list of gedung and find the initialize gedung for iblis
        temp = listGedung.first;
        while(temp.next != null){
            if(temp.nama.equals(gedungIblis)){
                iblis.diGedung = temp;
                break;
            }
            temp = temp.next;
        }
        // set the initialize lantai for iblis
        iblis.diLantai = lantaiIblis;
        // set the initialize walk direction for iblis
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
        // gerak denji
        gerakDenji();
        // if denji and iblis meet, increment jumlahBertemu
        if (denji.diGedung == iblis.diGedung && denji.diLantai == iblis.diLantai) {
            jumlahBertemu++;
        }
        // gerak iblis
        gerakIblis();
        gerakIblis();
        // if denji and iblis meet, increment jumlahBertemu
        if (denji.diGedung == iblis.diGedung && denji.diLantai == iblis.diLantai) {
            jumlahBertemu++;
        }

        out.print(denji.diGedung.nama + " ");
        out.print(denji.diLantai + " ");
        out.print(iblis.diGedung.nama + " ");
        out.print(iblis.diLantai + " ");
        out.println(jumlahBertemu);
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        // if denji is not in the first floor
        if (denji.diLantai != 1){
            // if denji and iblis arent in the same gedung
            if (denji.diGedung != iblis.diGedung){
                denji.diGedung.jumlahLantai--;
                denji.diLantai--;
                out.println(denji.diGedung.nama + " " + denji.diLantai);
            } 
            // if denji and iblis are in the same gedung
            else {
                // if iblis is not in the one floor below denji
                if (denji.diLantai-1 != iblis.diLantai){
                    // if iblis above the denji's position
                    if (iblis.diLantai >= denji.diLantai){
                        iblis.diLantai--;
                    }
                    denji.diGedung.jumlahLantai--;
                    denji.diLantai--;
                    out.println(denji.diGedung.nama + " " + denji.diLantai);
                } else {
                    out.println(denji.diGedung.nama + " " + (-1));
                }
            }
        } else {
            out.println(denji.diGedung.nama + " " + (-1));
        }
    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {
        // if denji and iblis are in the same gedung
        if (denji.diGedung == iblis.diGedung) { 
            // if denji is above the iblis's position
            if (denji.diLantai >= iblis.diLantai){
                denji.diLantai++;
            }
        }
        iblis.diGedung.jumlahLantai++;
        iblis.diLantai++;
        out.println(iblis.diGedung.nama + " " + (iblis.diLantai-1));
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        denji.diGedung = denji.diGedung.next;
        // if denji is Turun
        if (denji.isTurun){
            denji.diLantai = denji.diGedung.jumlahLantai;
        } 
        // if denji is Naik
        else {
            denji.diLantai = 1;
        }
        // if denji meet iblis, increment jumlahBertemu
        if (denji.diGedung == iblis.diGedung && denji.diLantai == iblis.diLantai) {
            jumlahBertemu++;
        }
        out.println(denji.diGedung.nama + " " + denji.diLantai);
    }

    // method for gerak denji
    static void gerakDenji(){
        // kalo denji sedang turun
        if (denji.isTurun) {
            // if denji is in the first floor
            if (denji.diLantai == 1) {
                denji.diGedung = denji.diGedung.next;
                denji.diLantai = 1;
                denji.isTurun = false;
            } else {
                denji.diLantai--;
            }
        } 
        // sedang naik
        else {
            // if denji is in the top floor of the gedung
            if (denji.diLantai == denji.diGedung.jumlahLantai) {
                denji.diGedung = denji.diGedung.next;
                denji.diLantai = denji.diGedung.jumlahLantai;
                denji.isTurun = true;
            } else {
                denji.diLantai++;
            }
        }
    }
    // method for gerak iblis
    static void gerakIblis(){
        // kalo iblis sedang turun
        if (iblis.isTurun){
            // if iblis is in the first floor
            if (iblis.diLantai == 1){
                iblis.diGedung = iblis.diGedung.next;
                iblis.diLantai = 1;
                iblis.isTurun = false;
            } else {
                iblis.diLantai--;
            }
        }
        // sedang naik
        else {
            // if iblis is in the top of the gedung
            if (iblis.diLantai == iblis.diGedung.jumlahLantai){
                iblis.diGedung = iblis.diGedung.next;
                iblis.diLantai = iblis.diGedung.jumlahLantai;
                iblis.isTurun = true;
            } else {
                iblis.diLantai++;
            }
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
    public void addGedung(Gedung newGedung){
        if (first == null){
            first = newGedung;
        } else {
            last.next = newGedung;
        }
        last = newGedung;
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
