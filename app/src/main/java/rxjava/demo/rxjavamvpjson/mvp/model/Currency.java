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

import java.util.HashMap;
import java.util.Map;

/**
 * Currency class for demonstrating currency exchange rates.
 */
public class Currency {
    private String base;
    private String date;
    private Map<String, Double> rates;

    public Currency() {}

    /**
     * Public constructor.
     *
     * @param base String
     * @param date String
     * @param rates String
     */
    public Currency(String base, String date, Map<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    /**
     * Validate the Currency object.
     *
     * @return true if valid otherwise false
     */
    public boolean isValid() {
        return !(base == null || date == null || rates == null) &&
                !(base.isEmpty() || date.isEmpty() || rates.isEmpty());
    }

    /**
     * Convert the Currency object to a string representation.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Exchange rates against " +
                base +
                " on " + date + "\n" +
                rateString();
    }

    /**
     * Convert the rates map to a string representation.
     *
     * @return String
     */
    private String rateString() {
        String result = "";

        if(rates == null || rates.isEmpty())
            return null;

        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            result = result + entry.getKey() + ":" + entry.getValue() + "\n";
        }

        return result;
    }

    /**
     * Create a mocked Currency object.
     *
     * @return Currency
     */
    public static Currency mock() {
        Map<String, Double> mock = new HashMap<>();
        mock.put("1", 1.0);
        mock.put("2", 2.0);

        return new Currency("MOCK", "mock date", mock);
    }

}

