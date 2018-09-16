package io.github.l4ttice.informationexchange;

import java.util.Scanner;


public class TEST {
    public static void main(String Args[]) {
        String inp = " This is a test case for Scanner class.";
        Scanner in = new Scanner(inp);
        while (in.hasNext()) {
            System.out.println(in.next());
        }
    }
}