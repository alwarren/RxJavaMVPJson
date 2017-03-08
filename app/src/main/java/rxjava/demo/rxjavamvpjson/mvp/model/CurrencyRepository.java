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

package rxjava.demo.rxjavamvpjson.mvp.model;

import java.util.concurrent.Callable;
import io.reactivex.Single;
import rxjava.demo.rxjavamvpjson.mvp.model.source.NetworkProvider;

/**
 * Implementation of the Model repository component of MVP.
 * Simulates network latency and fetches a Currency from the network provider.
 */
public class CurrencyRepository implements Repository<Currency> {

    @Override
    public Single<Currency> getItem() {
        return Single.fromCallable(new Callable<Currency>() {
            @Override
            public Currency call() throws Exception {

                // Simulate latency
                Thread.sleep(3000);

                // Fetch the item through the network provider
                return new NetworkProvider().getItem();
            }
        });
    }

}
