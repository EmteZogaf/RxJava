package rx.operators;

import rx.Subscriber;

import rx.Observable.OnSubscribe;

import java.util.Collections;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.util.functions.Func1;

public final class OperationSeparate {

    private static class SeparateObserver<T> extends Subscriber<T> {

        private Subscriber<? super Observable<T>> subscriber;

        public SeparateObserver(Subscriber<? super Observable<T>> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onCompleted() {
            subscriber.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            subscriber.onError(e);
        }

        @Override
        public void onNext(T item) {
            subscriber.onNext(Observable.just(item));
        }
    }

    private static class SeparateObservable<T> implements OnSubscribe<Observable<T>> {
        private Observable<T> sequence;

        public SeparateObservable(Observable<T> sequence) {
            this.sequence = sequence;

        }

        @Override
        public void call(Subscriber<? super Observable<T>> subscriber) {
            sequence.subscribe(new SeparateObserver<T>(subscriber));
            
        }
    }

    /**
     * Returns a sequence of {@link Observable}s each wrapping an item of the source {@code Observable}.
     * 
     * @param observable sequence which items to separate into one element {@code Observable}s
     * @return
     */
    public static <R> Observable<Observable<R>> separate(Observable<R> observable) {
        return Observable.create(new SeparateObservable<R>(observable));
    }
}
