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

import java.text.MessageFormat;

/*
This example converts the following code snippet (written in Kotlin) to use Dagger 2. This snippet
simply takes a longUrl and uses tinyurl.com to convert it into a shortUrl (by using OkHttpClient
for the HTTP IO).

```kotlin
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

val arg = when {
    args.size > 0 -> args[0]
    else -> "https://en.wikipedia.org/wiki/Cache_replacement_policies#Last_in_first_out_(LIFO)"
}

val encodedData = URLEncoder.encode(arg, "UTF-8")
val url = "https://tinyurl.com/api-create.php?url=$encodedData"

print(doGet(url))

fun doGet(url: String): String {
    val client = OkHttpClient()
    val request = Request.Builder()
            .url(url)
            .build()
    val response = client.newCall(request).execute()
    return response.body()!!.string()
}
```
 */
class Main {

    private final UrlShortenService service;

    public static void main(String[] args) {
        new Main().run();
    }

    public Main() {
        /*
        Instead of using create(), if you use builder() you can pass arguments to any of the
        module constructors (if they take any params). This is a way to pass information that
        is not available in the dependency graph that Dagger 2 generates. In this case, each
        module has a default constructor, so there's really no need to do it this way, and
        create() would suffice.
         */
        UrlShortenServiceComponent component = DaggerUrlShortenServiceComponent.builder()
                .networkClientModule(new NetworkClientModule())
                .urlShortenServiceModule(new UrlShortenServiceModule())
                .serviceProviderModule(new ServiceProviderModule())
                .build();
        service = component.urlShortenService();
    }

    public void run() {
        String longUrl =
                "https://en.wikipedia.org/wiki/Cache_replacement_policies#Last_in_first_out_(LIFO)";
        String shortUrl = service.getShortUrl(longUrl);
        System.out.println(MessageFormat.format("longUrl={0}\nshortUrl={1}",
                                                longUrl,
                                                shortUrl));
    }

}