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

package rxjava.demo.rxjavamvpjson.mvp.model.source;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rxjava.demo.rxjavamvpjson.mvp.model.Currency;


/**
 * Implementation of the Provider interface.
 * Uses OkHttp and Gson to parse Json from the Internet into a Currency object.
 */

@SuppressWarnings("unchecked")
public class NetworkProvider implements Provider<Currency> {
    private static final String ENDPOINT_USD = "http://api.fixer.io/latest?base=USD";
    private static final String ENDPOINT_GBP = "http://api.fixer.io/latest?base=GBP";

    private static String mEndpoint = ENDPOINT_GBP;

    /**
     * Fetch Json from the internet and parse it into a Rate object.
     *
     * @return Currency
     */
    public Currency getItem() {
        String json = jsonFromNetwork();
        if(json == null || json.isEmpty())
            return null;

        Type customType = new TypeToken<Map<Object, Object>>(){}.getType();

        Gson gson = new Gson();
        Map<Object, Object> map = gson.fromJson(json, customType);

        if (!validObject(map))
            return null;

        String base = (String) map.get("base");
        String date = (String) map.get("date");
        Map<String, Double> rates = (Map<String, Double>) map.get("rates");

        return new Currency(base, date, rates);
    }

    /**
     * Retrieve Json text from the internet.
     *
     * @return String
     */
    private static String jsonFromNetwork() {
        String json = null;

        // Cheating to cycle between two endpoints for demonstration purposes
        if(mEndpoint.equals(ENDPOINT_GBP))
            mEndpoint = ENDPOINT_USD;
        else
            mEndpoint = ENDPOINT_GBP;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(mEndpoint)
                    .build();

            Response response = client.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Check that the map returned from the json parser is valid.
     *
     * @param map object to test
     * @return true if valid otherwise false
     */
    private static boolean validObject(Map<Object, Object> map) {
        return map.containsKey("base") &&
                map.containsKey("date") &&
                map.containsKey("rates");

    }
}
