<#import "macros/select_type_screen_model_macros.ftl" as superClass>
package ${packageName};

import ru.surfstudio.android.core.mvp.model.LdsScreenModel;
import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel;
import ru.surfstudio.android.core.mvp.model.ScreenModel;

@Data
class ${className}ScreenModel extends <@superClass.selectTypeScreenModel /> {
    <#if generateRecyclerView>
            <#if (screenType=='activity' && usePaginationableAdapter) || (screenType=='fragment' && usePaginationableAdapter)>
                private DataList<${nameTypeData}> itemList = DataList.empty();
            <#else>
                private List<${nameTypeData}> itemList = Collections.emptyList();
            </#if>
    </#if>
}
