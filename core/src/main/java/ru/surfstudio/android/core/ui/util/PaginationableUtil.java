package ru.surfstudio.android.core.ui.util;

import java.util.ArrayList;
import java.util.List;

import ru.surfstudio.android.core.domain.datalist.DataList;
import ru.surfstudio.android.core.util.rx.ObservableUtil;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PaginationableUtil {

    /**
     * Создает запрос составленный из нескольких запросов, каждый из которых загружает блок данных
     * размером blockSize.
     * Такое разбиение необходимо чтобы при обновлении данных списка они кешировались блоками с размером,
     * который используется при подгрузке новых данных.
     *
     * @param paginationRequestCreator функции создающая один из подзапросов, имеет 2 параметра limit и offset
     * @param numPages                 Клоличество страниц которые необходимо загрузить
     * @return Observable, который эмитит необходимый блок данных, может эмитить несколько раз из-за
     * combineLatestDelayError
     */
    private static <T, L extends DataList<T>> Observable<L> getPaginationRequestPortions(
            Func1<Integer, Observable<L>> paginationRequestCreator,
            L emptyValue,
            int numPages) {
        List<Observable<? extends L>> portionRequests = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            portionRequests.add(paginationRequestCreator.call(i));
        }
        if (portionRequests.isEmpty()) {
            portionRequests.add(Observable.just(emptyValue));
        }
        return ObservableUtil.combineLatestDelayError(Schedulers.immediate(), portionRequests,
                portions -> {
                    L result = null;
                    for (Object rawPortion : portions) {
                        L portion = (L) rawPortion;
                        if (result == null) {
                            result = portion;
                        } else {
                            result.merge(portion);
                        }
                    }
                    return result;
                });
    }

    public static <T> Observable<DataList<T>> getPaginationRequestPortions(
            Func1<Integer, Observable<DataList<T>>> paginationRequestCreator,
            int numPages) {
        return getPaginationRequestPortions(
                paginationRequestCreator,
                DataList.empty(),
                numPages);
    }

}