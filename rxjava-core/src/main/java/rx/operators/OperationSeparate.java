package rx.operators;

import java.util.Collections;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.util.functions.Func1;

public final class OperationSeparate {

    private static class SeparateObserver<T> implements Observer<T> {

        private Observer<Observable<T>> observer;

        public SeparateObserver(Observer<Observable<T>> observer) {
            this.observer = observer;
        }

        @Override
        public void onCompleted() {
            observer.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);

        }

        @Override
        public void onNext(T item) {
            observer.onNext(Observable.from(Collections.singleton(item)));
        }

    }

    private static class SeparateObservable<T> implements Func1<Observer<Observable<T>>, Subscription> {
        private Observable<T> sequence;

        public SeparateObservable(Observable<T> sequence) {
            this.sequence = sequence;

        }

        @Override
        public Subscription call(Observer<Observable<T>> observer) {
            return sequence.subscribe(new SeparateObserver<T>(observer));
        }

    }

    /**
     * Returns a sequence of {@link Observable}s each wrapping an item of the source {@code Observable}.
     * 
     * @param observable sequence which items to separate into one element {@code Observable}s
     * @return
     */
    public static <R> Func1<Observer<Observable<R>>, Subscription> separate(Observable<R> observable) {
        return new SeparateObservable<R>(observable);
    }


}
