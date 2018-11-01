package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_list.view.*
import ru.surfstudio.android.mvpwidget.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class ListItemController(
        val onItemClick: (String) -> Unit
) : BindableItemController<Int, ListItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(int: Int): String = int.toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Int>(parent, R.layout.item_list_list) {

        val constraintWidgetView = itemView.constraint_w

        init {
            //todo find view here
            //itemView.setOnClickListener { onItemClick(itemView.hashCode().toString()) }
        }

        override fun bind(int: Int) {
            constraintWidgetView.id = int
            constraintWidgetView.render(int.toString())
        }
    }
}
