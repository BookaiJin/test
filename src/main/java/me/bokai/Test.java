package me.bokai;

public class Test {


    public static int numTrees(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int res[] = new int[n + 1];
        res[0] = 1;
        res[1] = 1;
        for (int i = 2; i < n + 1; i++) {
            for (int j = 0; j < i; j++) {
                res[i] = res[j] * res[i - j - 1] + res[i];
            }
        }
        return res[n];
    }

    public static void main(String... args){
        System.out.println(numTrees(5));
    }
}

