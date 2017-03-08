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

package rxjava.demo.rxjavamvpjson.mvp.view;

import rxjava.demo.rxjavamvpjson.mvp.model.Currency;

/**
 * The interface for the View component of MVP.
 * Implemented by the activity.
 * Because it is an interface, we can mock the view in unit tests.
 */
public interface CurrencyView {

    void displayRate(Currency item);

    void displayNoRate();

    void displayError();

    void showProgress();

    void hideProgress();
}
