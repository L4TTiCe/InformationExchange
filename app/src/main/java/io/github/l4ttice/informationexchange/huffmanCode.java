package io.github.l4ttice.informationexchange;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

class NodeHuffman {

    int freq;
    char character;

    NodeHuffman left;
    NodeHuffman right;
}

class MinComparator implements Comparator<NodeHuffman> {
    @Override
    public int compare(NodeHuffman x, NodeHuffman y) {
        return x.freq - y.freq;
    }
}


public class huffmanCode {
    public HashMap<Character, String> cipher = new HashMap<Character, String>();
    public String input;

    public huffmanCode(String inp) {
        input = inp;
    }

    public void printCode(NodeHuffman root, String s) {
        if (root.left == null && root.right == null) {
            cipher.put(root.character, s);
            // System.out.println(root.character + " :\t" + s);
            return;
        }

        printCode(root.left, s + "0");
        printCode(root.right, s + "1");
    }

    public int getEncodedStringLength() {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            Integer val = map.get(currentChar);
            if (val == null)
                map.put(currentChar, 0);
            else
                map.put(currentChar, val + 1);
        }

        Object[] charArray = map.keySet().toArray();
        Object[] charfreq = map.values().toArray();

        PriorityQueue<NodeHuffman> queue = new PriorityQueue<NodeHuffman>(charArray.length, new MinComparator());

        for (int i = 0; i < charArray.length; i++) {
            NodeHuffman tempNode = new NodeHuffman();

            tempNode.character = (char) charArray[i];
            tempNode.freq = (int) charfreq[i];

            tempNode.left = null;
            tempNode.right = null;

            // PUSH the node to queue
            queue.add(tempNode);
        }

        // create a root node
        NodeHuffman root = null;
        while (queue.size() > 1) {

            NodeHuffman elem1 = queue.peek();
            queue.poll();

            NodeHuffman elem2 = queue.peek();
            queue.poll();

            NodeHuffman newRoot = new NodeHuffman();

            newRoot.freq = elem1.freq + elem2.freq;
            newRoot.character = '-';

            newRoot.left = elem1;
            newRoot.right = elem2;
            root = newRoot;

            queue.add(newRoot);
        }
        //System.out.println("____________________________________________________");
        //System.out.println("Generated Cipher    :");
        printCode(root, "");
        //System.out.println("____________________________________________________");
        //System.out.println("Encoded String is :");
        Object[] values = cipher.values().toArray();
        int cipherlength = 0;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            System.out.print(cipher.get(currentChar) + " "); // The space is just for visual clarity
            cipherlength = cipherlength + cipher.get(currentChar).length();
        }
        return cipherlength;
    }

    // main function
    public void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Enter a String that needs to be encoded to Huffman Code     :");
        String input = in.nextLine();
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            Integer val = map.get(currentChar);
            if (val == null)
                map.put(currentChar, 0);
            else
                map.put(currentChar, val + 1);
        }

        Object[] charArray = map.keySet().toArray();
        Object[] charfreq = map.values().toArray();

        PriorityQueue<NodeHuffman> queue = new PriorityQueue<NodeHuffman>(charArray.length, new MinComparator());

        for (int i = 0; i < charArray.length; i++) {
            NodeHuffman tempNode = new NodeHuffman();

            tempNode.character = (char) charArray[i];
            tempNode.freq = (int) charfreq[i];

            tempNode.left = null;
            tempNode.right = null;

            // PUSH the node to queue
            queue.add(tempNode);
        }

        // create a root node
        NodeHuffman root = null;
        while (queue.size() > 1) {

            NodeHuffman elem1 = queue.peek();
            queue.poll();

            NodeHuffman elem2 = queue.peek();
            queue.poll();

            NodeHuffman newRoot = new NodeHuffman();

            newRoot.freq = elem1.freq + elem2.freq;
            newRoot.character = '-';

            newRoot.left = elem1;
            newRoot.right = elem2;
            root = newRoot;

            queue.add(newRoot);
        }
        System.out.println("____________________________________________________");
        System.out.println("Generated Cipher    :");
        printCode(root, "");
        System.out.println("____________________________________________________");
        System.out.println("Encoded String is :");
        Object[] values = cipher.values().toArray();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            System.out.print(cipher.get(currentChar) + " "); // The space is just for visual clarity
        }
    }
}