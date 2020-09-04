package com.festive.iranweatherapp.repository.main.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

@Deprecated
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava3CallAdapterFactory mOriginalCallAdapterFactory;

    private RxErrorHandlingCallAdapterFactory() {
        mOriginalCallAdapterFactory = RxJava3CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(final Type returnType, final Annotation[] annotations, final Retrofit retrofit) {
        return new RxCallAdapterWrapper<>(retrofit, mOriginalCallAdapterFactory.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Observable<R>> {
        private final Retrofit mRetrofit;
        private final CallAdapter<R, ?> mWrappedCallAdapter;

        public RxCallAdapterWrapper(final Retrofit retrofit, final CallAdapter<R, ?> wrapped) {
            mRetrofit = retrofit;
            mWrappedCallAdapter = wrapped;
        }

        @Override
        public Type responseType() {
            return mWrappedCallAdapter.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Observable<R> adapt(final Call<R> call) {
            return ((Observable) mWrappedCallAdapter.adapt(call)).onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                @Override
                public Observable apply(final Throwable throwable) {
                    return Observable.error(asRetrofitException(throwable));
                }
            });
        }

        private RetrofitException asRetrofitException(final Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                final HttpException httpException = (HttpException) throwable;
                final Response response = httpException.response();

                return RetrofitException.httpError(response.raw().request().url().toString(), response, mRetrofit);
            }
            // A network error happened
            if (throwable instanceof NoNetworkConnectivityException) {
                return RetrofitException.networkError((NoNetworkConnectivityException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error

            return RetrofitException.unexpectedError(throwable);
        }
    }
}