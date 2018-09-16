# Complex Java example

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Introduction](#introduction)
- [Main class](#main-class)
- [Modules](#modules)
- [Component](#component)
- [References](#references)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Introduction

Example2 is much more sophisticated than Example1. The idea is to create a service that simply
takes a long URL and uses a URL shortening service to turn this into a short URL. The basic code
is available here (in Kotlin).

```kotlin
#!/usr/bin/env kscript
//DEPS com.squareup.okhttp3:okhttp:3.11.0

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

In this code, all the dependencies are explicitly created by the program. We want to keep the
desired functionality but use Dagger 2 to translate this into something more modular and testable.
And change the language to Java.

# Main class

The idea is very simple. Create a simple interface `UrlShortenService` that allows a long URL to
be converted into a short one, shown below.

```java
interface UrlShortenService { String getShortUrl(String longUrl); }
```

The code to use this service (via Dagger 2) is also straightforward, shown below.

```java
class Main {

    private final UrlShortenService service;

    public static void main(String[] args) {
        new Main().run();
    }

    public Main() {
        service = DaggerUrlShortenServiceComponent.create().urlShortenService();
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
```

When the `Main` class uses the interface, it doesn't know anything about how it's implemented or
what its dependencies are. And that is exactly what we want. In order to hide the dependencies and
construction details, we have to create a few modules and a component. 

# Modules

The **modules** we create (which are simply classes annotated w/ `@Module`) enable the following:

1. `NetworkClientModule` - Creation of the `OkHttpClient` instance, and providing it to Dagger 2
   so that it can inject this where needed (it's needed in `UrlShortenServiceImpl`).

1. `ServiceProviderModule` Creation of the desired service providers that actually shorten the 
   long URLs. We have two: `google.com` and `tinyurl.com`. This module names them both via the 
   `@Named` annotation, so that the specific one can be used where needed (by the 
   `UrlShortenServiceModule`).

1. `UrlShortenServiceModule` - This takes dependency objects provided by the two modules above and
   selects which service to use (`tinyurl.com` or `google.com`) in order to provide an object that 
   `Main` can use (which is an instance of `UrlShortenServiceaImpl` class). It also chooses which 
   class should implement the interface (`UrlShortenService`) in case there is more than one 
   implementation (in this example there's only one).

    1. `@Injects` and `UrlShortenServiceImpl` - An instance of this class is the object that will
    actually provide the implementation of `UrlShortenService`, that is required by `Main` to turn 
    a given long URL into a short one. The `@Inject` tag used in the constructor takes objects 
    available from the modules in the section above, and assembles them into the implementation 
    that will be used by `Main`. This tag also provides the object that's created to be served by
    it's module to Dagger 2. 
    1. Note that this class is not aware of the configuration (via `@Named`) that allows 
    `tinyurl.com` or `google.com` to be selected. However, the module which exposes this class is
    aware of this and makes the choice!

# Component

The `UrlShortenServiceComponent` is very simple. The hard work is done by the modules that it
composites.

```java
@Singleton
@Component(modules = {NetworkClientModule.class,
                      ServiceProviderModule.class,
                      UrlShortenServiceModule.class})
interface UrlShortenServiceComponent {

    UrlShortenService urlShortenService();
}
```

# References
- [OkHttp maven](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/3.9.0)
- [SO - @Named](https://stackoverflow.com/questions/45080227/dagger2-where-inject-named-provides-in-dependent-module)
- [SO - @Inject constructor params](https://stackoverflow.com/questions/32076244/dagger-2-injecting-parameters-of-constructor)