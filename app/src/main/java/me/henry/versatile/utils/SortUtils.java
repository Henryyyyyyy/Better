package me.henry.versatile.utils;

/**
 * Created by henry on 2018/1/12.
 */

public class SortUtils {
    /**
     * 从小到大
     * @param array
     */
    public static void getSortX_D(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}