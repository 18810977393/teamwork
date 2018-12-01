package com.scwang.refreshlayout;

import java.util.Iterator;

public class Sorting<T extends Comparable<T>>
{
    public static <T extends Comparable<T>> void selectionSort(T[] data)
    {
        String result = "";
        int min;

        for (int index = 0; index < data.length-1; index++)
        {
            min = index;
            for (int scan = index+1; scan < data.length; scan++)
            {
                if (data[scan].compareTo(data[min])<0)
                    min = scan;
            }
            swap(data, min, index);
        }
    }
    private static <T extends Comparable<T>> void swap(T[] data, int index1, int index2)
    {
        T temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    public static <T extends Comparable<T>>void shellSort(T[] data)
    {
        for(int gap=data.length/2;gap>0;gap=gap/2)
        {
            for(int i=gap;i<data.length;i++)
            {
                int j = i;
                while(j-gap>=0 && data[j].compareTo(data[j-gap])<0)
                {
                    swap(data,j,j-gap);
                    j-=gap;
                }
            }
        }
    }

}
