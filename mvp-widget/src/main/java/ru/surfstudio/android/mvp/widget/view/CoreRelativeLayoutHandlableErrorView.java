package ru.surfstudio.android.mvp.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.mvp.view.HandleableErrorView;

/**
 * базовый класс для кастомной вьюшки с презентером, основанном на FrameLayout
 * <p>
 * !!!ВАЖНО!!!
 * 1) Необходимо вызвать метод init во время onCreate() Activity или onActivityCreated() Fragment
 * 2) кастомная вьюшка с презентером может быть только в статической иерархии вью,
 * то есть должна создаваться при старте экрана, и не может быть использована при
 * динамическом создании вью, в том числе внутри элементов RecyclerView
 */


public abstract class CoreRelativeLayoutHandlableErrorView extends CoreRelativeLayoutView implements HandleableErrorView {

    @Inject
    ErrorHandler errorHandler;

    public CoreRelativeLayoutHandlableErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreRelativeLayoutHandlableErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public CoreRelativeLayoutHandlableErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void handleError(Throwable error) {
        errorHandler.handleError(error);
    }
}
