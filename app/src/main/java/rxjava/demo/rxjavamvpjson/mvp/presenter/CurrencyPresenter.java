/*
 * Copyright (c) 2017. Al Warren.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rxjava.demo.rxjavamvpjson.mvp.presenter;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import rxjava.demo.rxjavamvpjson.mvp.model.Currency;
import rxjava.demo.rxjavamvpjson.mvp.model.Repository;
import rxjava.demo.rxjavamvpjson.mvp.view.CurrencyView;

/**
 * Implementation of the Presenter component for MVP.
 * Interacts with the View component that is implemented in the activity.
 * Interacts with the Model component through a repository.
 */
public class CurrencyPresenter {
    private CurrencyView mView;
    private Repository mRepository;
    private Scheduler mViewScheduler;

/**
 * A simple presenter implementation that doesn't rely on any
 * Android dependencies.
 *
 * Uses RxJava2 for asynchronous operations.
 *
 * @param view interface
 * @param repository interface
 * @param viewScheduler RxJava2 abstract class
 */
    public CurrencyPresenter(CurrencyView view, Repository repository,
                             Scheduler viewScheduler){
        mView = view;
        mRepository = repository;
        mViewScheduler = viewScheduler;
    }

    /**
     * Loads data from the repository.
     * Calls back to the activity using the view interface.
     */
    public void provideRate() {

        // Show the progress spinner
        mView.showProgress();

        //mCompositeDisposable.add(

            // Get the item from the repository
            mRepository.getItem()

                    // Run async on the io scheduler
                    .subscribeOn(Schedulers.io())

                    // Use a constructor parameter for the observer scheduler
                    // so we can change it in unit tests
                    .observeOn(mViewScheduler)

                    // Use a disposable observer that expects a
                    // single response object
                    .subscribeWith(new DisposableSingleObserver<Currency>() {

                        // Call back to activity via the View interface
                        @Override
                        public void onSuccess(Currency item) {
                            if (item != null && item.isValid()) {
                                mView.hideProgress();
                                mView.displayRate(item);
                            } else {
                                mView.hideProgress();
                                mView.displayNoRate();
                            }
                        }

                        // On error call back to the activity via the view interface
                        @Override
                        public void onError(Throwable e) {
                            mView.hideProgress();
                            mView.displayError();
                        }
                    });
    }

}
