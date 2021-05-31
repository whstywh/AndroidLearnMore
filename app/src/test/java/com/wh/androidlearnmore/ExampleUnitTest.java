package com.wh.androidlearnmore;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        int i = numSquares(12);
        System.out.println(i);
    }

    public int numSquares(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(0);
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            step++;
            for (int i = 0; i < size; i++) {
                int poll = queue.poll();
                for (int j = 1; j * j <= n; j++) {
                    int p = poll + j * j;
                    if (p > n) {
                        continue;
                    }
                    if (p == n) {
                        return step;
                    }
                    if (!visited.contains(p)) {
                        queue.add(p);
                        visited.add(p);
                    }
                }
            }
        }
        return step;
    }
}