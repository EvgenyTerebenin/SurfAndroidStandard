package ru.surfstudio.android.core.util.rx;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * {@link BiFunction} без обязательной
 * проверки на {@link Exception}
 *
 * @see BiFunction
 */
public interface SafeBiFunction<T1, T2, R> extends BiFunction<T1, T2, R> {
    @Override
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2);
}
