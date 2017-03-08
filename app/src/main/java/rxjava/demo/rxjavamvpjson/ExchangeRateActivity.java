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

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxjava.demo.rxjavamvpjson.mvp.model.CurrencyRepository;
import rxjava.demo.rxjavamvpjson.mvp.model.Currency;
import rxjava.demo.rxjavamvpjson.mvp.model.Repository;
import rxjava.demo.rxjavamvpjson.mvp.presenter.CurrencyPresenter;
import rxjava.demo.rxjavamvpjson.mvp.view.CurrencyView;

/**
 * A simple activity to demonstrate MVP with RxJava2 for async operations.
 *
 * It uses a repository pattern to download currency exchange rates
 * off the main thread using OkHttp, Gson, and RxJava.
 *
 * Thanks to the Oday Maleh and Rakesh Patel for the lessons.
 * @see <a href="http://y2u.be/JwBGnN06Kso">http://y2u.be/JwBGnN06Kso</a>
 */

public class ExchangeRateActivity extends AppCompatActivity
        implements CurrencyView {

    private CurrencyPresenter presenter;
    private Repository repository;

    private TextView mTextViewLog;
    private View mButtonReload;
    private ScrollView mScrollView;
    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        initializeViews();
        initializeListeners();
        initializeRepository();
        initializePresenter();
    }

    public void initializeViews() {
        mButtonReload =
                findViewById(R.id.buttonReload);
        mTextViewLog =
                (TextView) findViewById(R.id.text_output);
        mScrollView =
                (ScrollView) findViewById(R.id.scrollview_text_output);
        mSpinner =
                (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initializeRepository() {
        repository = new CurrencyRepository();
    }

    private void initializePresenter() {
        if(presenter == null)
            presenter = new CurrencyPresenter(this, repository,
                    AndroidSchedulers.mainThread());

        presenter.provideRate();
    }

    private void initializeListeners() {
        mButtonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadRates();
            }
        });
    }

    private void resetScrollView() {
        mTextViewLog.setText("");
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    private void reloadRates() {
        initializePresenter();
    }

    public void println(final String input) {
        new Handler()
            .post(new Runnable() {
                      @Override
                      public void run() {
                      mTextViewLog.append(input + "\n");
                      mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                           }
                  }
        );
    }

    @Override
    public void displayNoRate() {
        println("No data returned");
    }

    @Override
    public void displayError() {
        println("An error occurred loading data");
    }

    @Override
    public void showProgress() {
        mSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mSpinner.setVisibility(View.GONE);
    }

    @Override
    public void displayRate(Currency item) {
        resetScrollView();
        println(item.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exchange, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_log:
                resetScrollView();
                return true;
            case R.id.reload_log:
                reloadRates();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
