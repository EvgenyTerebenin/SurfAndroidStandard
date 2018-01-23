package ru.surfstudio.android.core.util.rx;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;
import io.reactivex.internal.functions.Functions;


/**
 * содержит недостающие методы для Observable
 */
public class ObservableUtil {

    private ObservableUtil() {
        //do nothing
    }

    /**
     * работает также как {@see Observable#combineLatestDelayError}, но еще распараллеливает запросы с помощью {@param scheduler}
     */
    public static <T, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                               List<Observable<? extends T>> sources,
                                                               Function<? super Object[], ? extends R> combineFunction) {
        sources = toParallel(sources, scheduler);
        return Observable.combineLatestDelayError(sources, combineFunction);
    }

    public static <T1, T2, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                    Observable<? extends T1> o1,
                                                                    Observable<? extends T2> o2,
                                                                    BiFunction<? super T1, ? super T2, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2), Functions.toFunction(combineFunction));
    }

    public static <T1, T2, T3, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                        Observable<? extends T1> o1,
                                                                        Observable<? extends T2> o2,
                                                                        Observable<? extends T3> o3,
                                                                        Function3<? super T1, ? super T2, ? super T3, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3), Functions.toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                            Observable<? extends T1> o1,
                                                                            Observable<? extends T2> o2,
                                                                            Observable<? extends T3> o3,
                                                                            Observable<? extends T4> o4,
                                                                            Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler,
                Arrays.asList(o1, o2, o3, o4),
                Functions.toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                Observable<? extends T1> o1,
                                                                                Observable<? extends T2> o2,
                                                                                Observable<? extends T3> o3,
                                                                                Observable<? extends T4> o4,
                                                                                Observable<? extends T5> o5,
                                                                                Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5), Functions.toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                    Observable<? extends T1> o1,
                                                                                    Observable<? extends T2> o2,
                                                                                    Observable<? extends T3> o3,
                                                                                    Observable<? extends T4> o4,
                                                                                    Observable<? extends T5> o5,
                                                                                    Observable<? extends T6> o6,
                                                                                    Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6), Functions.toFunction(combineFunction));
    }


    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                        Observable<? extends T1> o1,
                                                                                        Observable<? extends T2> o2,
                                                                                        Observable<? extends T3> o3,
                                                                                        Observable<? extends T4> o4,
                                                                                        Observable<? extends T5> o5,
                                                                                        Observable<? extends T6> o6,
                                                                                        Observable<? extends T7> o7,
                                                                                        Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7), Functions.toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                            Observable<? extends T1> o1,
                                                                                            Observable<? extends T2> o2,
                                                                                            Observable<? extends T3> o3,
                                                                                            Observable<? extends T4> o4,
                                                                                            Observable<? extends T5> o5,
                                                                                            Observable<? extends T6> o6,
                                                                                            Observable<? extends T7> o7,
                                                                                            Observable<? extends T8> o8,
                                                                                            Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), Functions.toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                                Observable<? extends T1> o1,
                                                                                                Observable<? extends T2> o2,
                                                                                                Observable<? extends T3> o3,
                                                                                                Observable<? extends T4> o4,
                                                                                                Observable<? extends T5> o5,
                                                                                                Observable<? extends T6> o6,
                                                                                                Observable<? extends T7> o7,
                                                                                                Observable<? extends T8> o8,
                                                                                                Observable<? extends T9> o9,
                                                                                                Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), Functions.toFunction(combineFunction));
    }

    private static <T> List<Observable<? extends T>> toParallel(List<Observable<? extends T>> sources, Scheduler scheduler) {
        return Stream.of(sources)
                .map(source -> source.subscribeOn(scheduler))
                .collect(Collectors.toList());
    }
}
