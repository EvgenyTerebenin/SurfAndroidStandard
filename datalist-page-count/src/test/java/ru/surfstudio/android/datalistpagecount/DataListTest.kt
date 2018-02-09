package ru.surfstudio.android.datalistpagecount

import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList


class DataListTest {

    @Test
    fun checkNormalMerge() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 0, 5)
        val list2 = DataList(arrayListOf(6, 7, 8, 8, 10), 1, 5)

        val list3 = DataList(arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 8, 10), 0, 2, 5)

        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }

    @Test
    fun mergePagesWithSpace() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 0, 5)
        val list2 = DataList(arrayListOf(6, 7, 8, 8, 10), 2, 5)

        val list3 = DataList(arrayListOf(1, 2, 3, 4, 5), 0, 1, 5)

        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }

    @Test
    fun mergeNotFromFirstPage() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 4, 5)
        val list2 = DataList(arrayListOf(6, 7, 8, 8, 10), 5, 5)

        val list3 = DataList(arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 8, 10), 4, 2, 5)

        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }

    @Test
    fun mergeWithEmptyList() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 0, 5)
        val list2 = DataList(arrayListOf<Int>(), 2, 5)

        val list3 = DataList(arrayListOf(1, 2, 3, 4, 5), 0, 1, 5)

        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }

    @Test
    fun mergeEmptyListWithAnotherList() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 1, 5)
        val list2 = DataList(arrayListOf<Int>(), 0, 5)

        val list3 = DataList(arrayListOf<Int>(), 0, 5)

        list2.merge(list1)
        Assert.assertEquals(list3, list2)
    }

    @Test
    fun mergeWithCollision1() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 2, 5)
        val list2 = DataList(arrayListOf(6, 7, 8, 8, 10), 1, 5)

        val list3 = DataList(arrayListOf(6, 7, 8, 8, 10, 1, 2, 3, 4, 5), 1, 2, 5)
        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }

    @Test
    fun mergeWithCollision2() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 0, 5,1)
        val list2 = DataList(arrayListOf(6, 7, 8, 9, 10), 1, 5,1)

        val list3 = DataList(arrayListOf(1, 6, 7, 8, 9, 10), 0, 6, 1)

        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }


    @Test(expected = IllegalArgumentException::class)
    fun mergeWithError() {
        val list1 = DataList(arrayListOf(1, 2, 3, 4, 5), 2, 5)
        val list2 = DataList(arrayListOf(6, 7, 8, 8, 10), 1, 1)

        list1.merge(list2)
    }

}