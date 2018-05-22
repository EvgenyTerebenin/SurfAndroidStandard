package ru.surfstudio.android.utilktx.data.wrapper.expandable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Если объект может быть выделяемым
 */
interface ExpandableDataInterface {

    var isExpanded: Boolean

    fun expand() {
        isExpanded = true
    }

    fun collapse() {
        isExpanded = false
    }
}

/**
 * Поддерживает множество выделений
 */
class ExpandableData<T>(override var data: T)
    : DataWrapperInterface<T>, ExpandableDataInterface {

    override var isExpanded: Boolean = false
}