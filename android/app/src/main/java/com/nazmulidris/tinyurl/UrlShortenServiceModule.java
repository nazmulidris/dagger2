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

import java.util.HashMap;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static com.nazmulidris.tinyurl.ServiceProviderModule.TINYURL_SERVICE;

@Module
class UrlShortenServiceModule {

    @Provides
    public UrlShortenService providesUrlShortenService(
            OkHttpClient okHttpClient,
            @Named(TINYURL_SERVICE) HashMap<ServiceUrlKeys, String> serviceConfig) {
        return new UrlShortenServiceImpl(okHttpClient, serviceConfig);
    }
}
