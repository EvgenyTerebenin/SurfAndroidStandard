<#import "macros/select_import_type_controller_macros.ftl" as importControllerMacros>
<#import "macros/select_import_type_holder_macros.ftl" as importHolderMacros>
<#import "macros/select_type_controller_macros.ftl" as controllerMacros>
<#import "macros/select_type_holder_macros.ftl" as holderMacros>

package ${packageName};

import android.view.ViewGroup;
<#if applicationPackage??>import ${applicationPackage}.R;</#if>
import ru.surfstudio.android.easyadapter.controller.<@importControllerMacros.selectImportTypeController />;
import ru.surfstudio.android.easyadapter.holder.<@importHolderMacros.selectImportTypeHolder />;

public class ${nameController}${defPostfixController} extends <@controllerMacros.selectTypeController /> {

    <#if hasListener>
    private Listener listener;

    public interface Listener {
        void onItemClick(<#if typeController='1'>${nameTypeData} ${nameParam}</#if>);
    }

    public ${nameController}${defPostfixController}(Listener listener) {
        this.listener = listener;
    }
    
    </#if>
    @Override
    public Holder createViewHolder(ViewGroup parent) {
        return new Holder(parent);
    }

    <#if typeController='1'>
    @Override
    public String getItemId(${nameTypeData} ${nameParam}) {
        return String.valueOf(${nameParam}.getId());
    }
    
    </#if>
    class Holder extends <@holderMacros.selectTypeHolder /> {

        <#if typeController='1'>
        private ${nameTypeData} ${nameParam};
        
        </#if>
        Holder(ViewGroup parent) {
           super(parent, R.layout.${nameRes});
           <#if hasListener>
           itemView.setOnClickListener(v -> listener.onItemClick(<#if typeController='1'>${nameParam}</#if>));
           </#if>
           //todo find view here
        }
        <#if typeController='1'>
        @Override
        public void bind(${nameTypeData} ${nameParam}) {
           this.${nameParam} = ${nameParam};
           //todo render data here
        }
        </#if>
    }
}
