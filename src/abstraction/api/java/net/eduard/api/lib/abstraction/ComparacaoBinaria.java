package net.eduard.api.lib.abstraction;

public class ComparacaoBinaria {
    public static void main(String[] args) {
        int n1 = 100;
        int n2 = 105;
        System.out.println(""+ Integer.toBinaryString(~(100 >> 1)));
        System.out.println(""+ Integer.toBinaryString(~(-100 >> 1)));

        System.out.println("n1: "+Integer.toBinaryString(n1));
        System.out.println("n2: "+Integer.toBinaryString(n2));
        System.out.println(""+ Integer.toBinaryString(n1 & n2));
        System.out.println(""+ Integer.toBinaryString(n1 | n2));
        System.out.println(""+ Integer.toBinaryString(n1 ^ n2));

    }
}
