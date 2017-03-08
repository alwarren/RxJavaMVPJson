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

package rxjava.demo.rxjavamvpjson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import rxjava.demo.rxjavamvpjson.mvp.model.CurrencyRepository;
import rxjava.demo.rxjavamvpjson.mvp.model.Currency;
import rxjava.demo.rxjavamvpjson.mvp.presenter.CurrencyPresenter;
import rxjava.demo.rxjavamvpjson.mvp.view.CurrencyView;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class CurrencyPresenterTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    CurrencyRepository repository;
    @Mock
    CurrencyView view;

    private CurrencyPresenter presenter;
    private Currency ITEM = Currency.mock();
    private Currency EMPTY_ITEM = new Currency();

    @Before
    public void setUp() {

        // Pass the trampoline scheduler to the observer for testing
        presenter = new CurrencyPresenter(view, repository, Schedulers.trampoline());

        // Change subscriber scheduler to match observer for testing
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @After
    public void cleanUp() {
        RxJavaPlugins.reset();
    }

    @Test
    public void shouldPassItemToView() throws Exception {
        when(repository.getItem())
                .thenReturn(Single.just(ITEM));

        presenter.provideRate();

        verify(view).displayRate(ITEM);
    }

    @Test
    public void shouldPassNoItemToView() throws Exception {
        when(repository.getItem())
                .thenReturn(Single.just(EMPTY_ITEM));

        presenter.provideRate();

        verify(view).displayNoRate();
    }

    @Test public void shouldHandleError() throws Exception {
        when(repository.getItem())
                .thenReturn(Single.<Currency>error(new Throwable("fail")));

        presenter.provideRate();

        verify(view).displayError();
    }
}