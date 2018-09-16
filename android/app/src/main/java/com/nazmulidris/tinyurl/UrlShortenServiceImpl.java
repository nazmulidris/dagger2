/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
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

package com.nazmulidris.tinyurl;
import com.nazmulidris.tinyurl.ServiceProviderModule.ServiceUrlKeys;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class UrlShortenServiceImpl implements UrlShortenService {

    private final OkHttpClient okHttpClient;
    private final HashMap<ServiceUrlKeys, String> serviceConfig;

    @Inject
    public UrlShortenServiceImpl(OkHttpClient okHttpClient,
                                 HashMap<ServiceUrlKeys, String> serviceConfig) {
        this.okHttpClient = okHttpClient;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public String getShortUrl(String longUrl) {
        try {
            String encodedData = URLEncoder.encode(longUrl, "UTF-8");
            final String url = MessageFormat.format(
                    "{0}?{1}={2}",
                    serviceConfig.get(ServiceUrlKeys.urlKey),
                    serviceConfig.get(ServiceUrlKeys.paramKey),
                    encodedData);
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            System.out.println("NetworkClientImpl error: " + e);
            return "";
        }
    }
}
