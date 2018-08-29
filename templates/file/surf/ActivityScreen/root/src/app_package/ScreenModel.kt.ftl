<#import "macros/select_type_screen_model_macros.ftl" as superClass>
package ${packageName}

import ru.surfstudio.android.core.mvp.model.LdsScreenModel
import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel
import ru.surfstudio.android.core.mvp.model.LdsSwrPgnScreenModel
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import java.util.*

/**
 * Модель экрана todo
 */
class ${className}ScreenModel : <@superClass.selectTypeScreenModel /> {
    <#if generateRecyclerView>
        <#if nameTypeData==''>
            <#assign nameTypeData='Unit' />
        </#if>
        <#if usePaginationableAdapter>
            val itemList: DataList<${nameTypeData}> = DataList.empty()
        <#else>
            val itemList: List<${nameTypeData}> = Collections.emptyList()
        </#if>
    </#if>
}
