package ru.surfstudio.standard.ui.screen.tabs.fragments.tab2

import android.os.Bundle
import android.support.v4.app.Fragment
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithParamsRoute
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.RootFragmentRoute


class Tab2FragmentRoute(val id: Int) : FragmentWithParamsRoute(), RootFragmentRoute {

    constructor(bundle: Bundle) : this(bundle.getInt(Route.EXTRA_FIRST))

    override fun prepareBundle(): Bundle =
            Bundle().apply {
                putInt(Route.EXTRA_FIRST, id)
            }

    override fun getFragmentClass(): Class<out Fragment> = Tab2FragmentView::class.java

    override fun getTag(): String {
        return fragmentClass.canonicalName + id
    }
}