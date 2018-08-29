<#import "function/lds_view_function.ftl" as ldsFunction>

package ${packageName};

import ru.surfstudio.android.core.mvp.presenter.BasePresenter;
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency;
import ru.surfstudio.android.dagger.scope.PerScreen;
import javax.inject.Inject;

/**
 * Презентер экрана todo
 */
@PerScreen
class ${className}Presenter extends BasePresenter<${className}${screenTypeCapitalized}View> {

    public ${className}Presenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency);
    }

    @Override
    public void onLoad(boolean viewRecreated) {
        super.onLoad(viewRecreated);
    }
    <#if generateRecyclerView && hasListener>
        <#if typeController=='1'>

            void on${nameTypeData}ItemClick(${nameTypeData} ${nameTypeData?uncap_first}) {
             //todo действия при нажатии на элемент
             }
        <#elseif typeController=='2'>

            void on${nameTypeData}ItemClick() {
             //todo действия при нажатии на элемент
             }
        </#if>
    </#if>
    <#if ldsFunction.isLdsView()>

        void reloadData() {
            // todo перезагрузить данные здесь
        }
    </#if>
    <#if generateRecyclerView && usePaginationableAdapter>

        void loadMore() {
            // todo постраничная загрузка след данных
        }
    </#if>
}
