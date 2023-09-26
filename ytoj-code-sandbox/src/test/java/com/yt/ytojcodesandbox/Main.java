package com.yt.ytojcodesandbox;

import javafx.util.Pair;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别

        int n = in.nextInt();
        int q = in.nextInt();
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (int i = 0; i < n-1; i++) {
            Pair<Integer, Integer> pair = new Pair<>(in.nextInt(), in.nextInt());
            list.add(pair);
        }
        for (int i = 0; i < q; i++) {
            int m = in.nextInt();
            List<Integer> list1 = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                list1.add(in.nextInt());
            }
            for (int j = 0; j < list1.size(); j++) {
                Set<Integer> set = new HashSet<>(list1);
                for (Integer integer : set) {
                    set.remove(integer);

                }
            }
        }
    }
}
