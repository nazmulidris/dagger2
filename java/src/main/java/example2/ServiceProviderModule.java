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

package example2;

import java.util.HashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class ServiceProviderModule {

    @Provides
    @Singleton
    @Named(TINYURL_SERVICE)
    HashMap<ServiceUrlKeys, String> provideServiceConfigMapForTinyurl() {
        HashMap<ServiceUrlKeys, String> config = new HashMap<>();
        config.put(ServiceUrlKeys.paramKey, "url");
        config.put(ServiceUrlKeys.urlKey, "https://tinyurl.com/api-create.php");
        return config;
    }

    @Provides
    @Singleton
    @Named(GOOGLE_SERVICE)
    HashMap<ServiceUrlKeys, String> provideServiceConfigMapForGoogle() {
        HashMap<ServiceUrlKeys, String> config = new HashMap<>();
        config.put(ServiceUrlKeys.paramKey, "longUrl");
        config.put(ServiceUrlKeys.urlKey, "https://www.googleapis.com/urlshortener/v1/url");
        return config;
    }

    public static final String TINYURL_SERVICE = "tinyurl";
    public static final String GOOGLE_SERVICE = "google";

    enum ServiceUrlKeys {paramKey, urlKey}

}
