package com.mygdx.game.utils;

public class QuickSortGeneric<E extends Comparable<? super E>> {
    //  Performance:            Best case: O( n Log(N) )  ----    Average case: O( n Log(N) )  ----    Worst Case: O(n^2)
    public void quickSort(E[] array){
        quickSort(array, 0, array.length-1);
    }

    public void quickSort(E[] array, int first, int last){
        if(first < last){
            int pivotIndex = partition(array, first, last);
            quickSort(array, first, pivotIndex);
            quickSort(array,  pivotIndex+1, last);
        }
    }
    public int partition(E[] array, int first, int last){
        E pivotValue = array[(first+last)/2];
        first--;
        last++;
        while(true) {
            // start at the FIRST index of the sub-array and increment
            // FORWARD until we find a value that is > pivotValue
            do first++; while (array[first].compareTo(pivotValue) < 0);

            // start at the LAST index of the sub-array and increment
            // BACKWARD until we find a value that is < pivotValue
            do last--; while (array[last].compareTo(pivotValue) > 0);

            if( first >= last) return last;

            // swap values at the startIndex and endIndex
            E temp = array[first];
            array[first] = array[last];
            array[last] = temp;
        }
    }
}
