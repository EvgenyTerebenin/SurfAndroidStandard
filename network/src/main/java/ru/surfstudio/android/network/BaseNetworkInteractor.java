package ru.surfstudio.android.network;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.surfstudio.android.core.app.interactor.common.DataStrategy;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.util.rx.SafeFunction;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.NotModifiedException;

import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_FORCE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_ONLY_IF_CHANGED;


/**
 * Базовый класс репозитория
 */
public class BaseNetworkInteractor {

    private ConnectionQualityProvider connectionQualityProvider;

    public BaseNetworkInteractor(ConnectionQualityProvider connectionQualityProvider) {
        this.connectionQualityProvider = connectionQualityProvider;
    }

    /**
     * Действия при смене пользователя
     */
    public void onSessionChanged() {
        //default empty
    }

    /**
     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
     *
     * @param priority              указывает из какого источника данные должны эмитится в результирующий
     *                              Observable в первую очередь
     * @param cacheRequest          запрос к кешу
     * @param networkRequestCreator функция, которая должна вернуть запрос к серверу,
     *                              Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
     * @param <T>                   тип возвращаемого значения
     */
    protected <T> Observable<T> hybridQuery(DataStrategy priority,
                                            Single<T> cacheRequest,
                                            SafeFunction<Integer, Single<T>> networkRequestCreator) {
        return cacheRequest
                .flatMapObservable(cache -> {
                    Single<T> cacheObservable = Single.just(cache);
                    Single<T> networkObservable = networkRequestCreator.apply(QUERY_MODE_ONLY_IF_CHANGED);

                    return getDataObservable(priority, cacheObservable, networkObservable);
                })
                .onErrorResumeNext((Throwable e) -> {
                    Logger.e(e.getCause(), "Error when getting data from cache");

                    Single<T> cacheObservable = Single.error(e);
                    Single<T> networkObservable = networkRequestCreator.apply(QUERY_MODE_FORCE);

                    return getDataObservable(priority, cacheObservable, networkObservable);
                });
    }

    @SuppressWarnings("squid:S1612")
    private <T> Observable<T> getDataObservable(DataStrategy strategy, Single<T> cache, Single<T> network) {
        DataStrategy actualStrategy = resolveStrategy(strategy);
        Observable<T> first;
        Observable<T> second;

        switch (actualStrategy) {
            case CACHE:
                first = cache.toObservable();
                second = network.toObservable()
                        .onErrorResumeNext((Throwable e) -> processNetworkException(e));
                break;
            case SERVER:
                first = network.toObservable()
                        .onErrorResumeNext((Throwable e) -> processNetworkException(e));
                second = Observable.empty();
                break;
            case ONLY_ACTUAL:
                first = network.onErrorResumeNext(e -> e instanceof NotModifiedException ?
                        cache :
                        Single.error(e))
                        .toObservable();
                second = Observable.empty();
                break;
            default:
                return Observable.error(new IllegalStateException("недопустимая стратегия: " + strategy));
        }
        return Observable.concat(first, second);
    }

    private DataStrategy resolveStrategy(DataStrategy strategy) {
        if (strategy == DataStrategy.AUTO) {
            if (connectionQualityProvider.isConnectedFast())
                return DataStrategy.SERVER;
            else {
                return DataStrategy.CACHE;
            }
        }
        return strategy;
    }

    private <T> Observable<T> processNetworkException(Throwable e) {
        return e instanceof NotModifiedException
                ? Observable.empty()
                : Observable.error(e);
    }

    protected <T> Observable<T> hybridQuery(Single<T> cacheRequest,
                                            SafeFunction<Integer, Single<T>> networkRequestCreator) {
        return hybridQuery(DataStrategy.AUTO, cacheRequest, networkRequestCreator);
    }

    /**
     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
     *
     * @param requestCreator функция, которая должна вернуть запрос к серверу, поддерживающий механизм простого кеширования
     *                       Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
     * @param <T>            тип ответа сервера
     */
    protected <T> Observable<T> hybridQueryWithSimpleCache(DataStrategy priority,
                                                           SafeFunction<Integer, Single<T>> requestCreator) {
        return hybridQuery(priority, requestCreator.apply(QUERY_MODE_FROM_SIMPLE_CACHE), requestCreator);
    }

    protected <T> Observable<T> hybridQueryWithSimpleCache(SafeFunction<Integer, Single<T>> requestCreator) {
        return hybridQueryWithSimpleCache(DataStrategy.AUTO, requestCreator);
    }
}
