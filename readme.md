**Table of Contents**  *generated with [MarkdownTocGenerator](https://github.com/YuraAAA/)
- [ Android development utils](#android-development-utils)
  - [ Install library](#install-library) 
    - [ Gradle](#gradle) 
  - [ Fragment management (Switcher)](#fragment-management-switcher) 
    - [ Create switcher](#create-switcher) 
    - [ Use fragment](#use-fragment) 
  - [ Event Bus](#event-bus) 
    - [ Send/receive actions by id](#sendreceive-actions-by-id) 
    - [ Multiaction](#multiaction) 
  - [ Cache](#cache) 
    - [ Simple cache](#simple-cache) 
    - [ Typed cache](#typed-cache) 
    - [ Expire data in cache](#expire-data-in-cache) 
    - [ Notification cache](#notification-cache) 
    - [ Configure global ](#configure-global) 
    - [ Clear cache](#clear-cache) 
    - [ Change expire](#change-expire) 
  - [ Logging](#logging) 
    - [ Configure log level](#configure-log-level) 
    - [ Obtain logger](#obtain-logger) 
    - [ Usage](#usage) 
  - [ Map iteration](#map-iteration) 
    - [ Map filtering](#map-filtering) 
    - [ Simple KeyValue filter](#simple-keyvalue-filter) 
    - [ Other filters implementation](#other-filters-implementation) 
  - [ Geolocation support](#geolocation-support) 
    - [ Configure geolocation service](#configure-geolocation-service) 
    - [ Handle location changes](#handle-location-changes) 
    - [ Start/stop location retrieving](#startstop-location-retrieving) 
    - [ Obtain last location directly](#obtain-last-location-directly) 
  - [ Utils](#utils) 
    - [ Generic utils](#generic-utils) 
      - [  orElse(T data, T default)](#t-orelset-data-t-default) 
    - [ String utils](#string-utils) 
    - [ I/O Utils](#io-utils) 
    - [ File utils](#file-utils) 
      - [ Work with file async](#work-with-file-async) 
      - [ Work sync](#work-sync) 
    - [ Validation](#validation) 
      - [ Caching your custom validator](#caching-your-custom-validator) 
    - [ Commons](#commons) 
      - [ Typed adapter for lists](#typed-adapter-for-lists) 
      - [ Common async task](#common-async-task) 
    - [ Network connection aware](#network-connection-aware) 
      - [ Use listeners](#use-listeners) 
      - [ Use events](#use-events) 
      - [ Get network state directly](#get-network-state-directly) 
      - [ Direct sync request (PING)](#direct-sync-request-ping) 
      - [ Async request (PING) with listener](#async-request-ping-with-listener) 
      - [ Async request (PING) with events](#async-request-ping-with-events) 
      - [ Sync request to the server](#sync-request-to-the-server) 
      - [ Async request (listener)](#async-request-listener) 
      - [ Async request (event)](#async-request-event) 
      - [ Request config](#request-config) 
    - [ Global async configuration](#global-async-configuration)

# Android development utils


## Install library

### Gradle
Add dependency to your build.gradle file

```sh
 compile 'com.aizenberg:support:0.1.5'
```

## Fragment management (Switcher)

### Create switcher

```java
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create switcher for this activity
        ISwitcher switcher = Switcher.createSwitcher(this, R.id.container);
     }
    }
```

### Use fragment

With fragment classes:

```java
 switcher.switchTo(OneFragment.class);
```

With enums:

```java
public enum Fragments implements ISwitchable {
    ONE(OneFragment.class),
    TWO(TwoFragment.class),
    THREE(ThreeFragment.class);

    private Class<? extends Fragment> clazz;

    Fragments(Class<? extends Fragment> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends Fragment> getFragmentClass() {
        return clazz;
    }
}

//.......//

switcher.switchTo(Fragments.ONE);
```

With alias names:
```java
switcher.addAliases(
                new Alias(OneFragment.class, "one"),
                new Alias(TwoFragment.class, "two"),
                new Alias(ThreeFragment.class, "three")
        );
//....
switcher.switchTo("one");
```
You can obtain switcher from any fragment only by activity class:

```java
//Switch by alias
Switcher.obtainSwitcher(MainActivity.class).switchTo("two");
//or by ISwitchable
Switcher.obtainSwitcher(MainActivity.class).switchTo(Fragments.TWO);
//or by class
Switcher.obtainSwitcher(MainActivity.class).switchTo(TwoFragment.class);
```

Clear back stack

```java
 Switcher
     .obtainSwitcher(MainActivity.class)
     .clearBackStack()
     .switchTo(OneFragment.class);
```
 
 
Pass arguments:
```java
Bundle args = new Bundle();
args.putLong("id", 1);
Switcher.obtainSwitcher(MainActivity.class).switchTo(OneFragment.class, args);
```
 
Skip back stack:
```java
//Without args
Switcher.obtainSwitcher(MainActivity.class).switchTo(OneFragment.class, false);

//With args
Switcher.obtainSwitcher(MainActivity.class).switchTo(OneFragment.class, args, false);
```

Override back pressing in fragment (In this example we show back-dialog in fragment):
 
```java
public class OneFragment extends Fragment implements IFragmentBackPressListener {

     @Override
    public boolean onBackPressed() {
        new AlertDialog.Builder(getActivity())
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Back without fragment notification
                        Switcher.obtainSwitcher(MainActivity.class).getBack();
                    }
                })

             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        //Return true, we override 'back' behaviour'
        return true;

    }
```

In Activity:

```java
public class MainActivity extends Activity implements IActivityBackPressListener {

     @Override
    public void onBackPressed() {
        if (!Switcher.obtainSwitcher(this).overrideBack())
            getBack();
    }

    @Override
    public void getBack() {
        super.onBackPressed();
    }
}
```

## Event Bus

You can use event bus for send events.

Register receiver:

```java
public class ThreeFragment extends Fragment implements IEventReceiver {

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getBus().addListener("computation", this);
        getActivity().startService(new Intent(getActivity(), BackService.class));
    }
    
     @Override
    public void onReceiveAction(String action, Object... args) {
        UUID uuid = (UUID) args[0]; //Get result
    }
```

Send event:
```java
public class BackService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UUID uuid = UUID.randomUUID();
        EventBus.getBus().notifyAction("computation", uuid);
        stopSelf();
    }
}
```

Use enums as action key:

```java
public enum Events implements IAction {

    COMPUTATION("computation");

    private String key;

    Events(String key) {
        this.key = key;
    }


    @Override
    public String getAction() {
        return key;
    }
}
```
Now you can use enum constants as action key:
Register
```java
EventBus.getBus().addListener(Events.COMPUTATION, this);
```
Send:
```java
EventBus.getBus().notifyAction(Events.COMPUTATION, uuid);
```

### Send/receive actions by id

Use IEventIdentificationReceiver listener

```java
EventBus.getBus()
 .addListener("computation", new IEventIdentificationReceiver() {
            @Override
            public Long getIdentifier() {
                //Receive action only with this ID
                return 1L;
            }

            @Override
            public void onReceiveAction(String action, Object... args) {

            }
        });
```

Send by id:
```java
//Send result is true because we have listeners for this action/id pair
boolean sendResult = EventBus.getBus().notifyById("computation", 1L, uuid);


//Send result is false, id 2 is not acceptable in listener
boolean sendResult = EventBus.getBus().notifyById("computation", 2L, uuid);
```

Accept any ids:
```java
EventBus
        .getBus()
        .addListener("computation", new IEventIdentificationReceiver() {
            @Override
            public Long getIdentifier() {
                return IEventIdentificationReceiver.ANY;
            }

            @Override
            public void onReceiveAction(String action, Object... args) {

            }
            });
```

Any ids will accept:

```java
//true
boolean sendResult1 = EventBus.getBus().notifyById("computation", 1L, uuid);
//true
boolean sendResult2 = EventBus.getBus().notifyById("computation", 1L, uuid);
```

### Multiaction

You can register one listener for more than one action:

```java
EventBus.getBus().addListeners(this, "computation", "camera_image", "file_copy");
```

## Cache

### Simple cache
Library supports mem cache with change listener integration:

Default cache is not typed:

```java
//Put
 MemCache.defaultCache().put("myUUID", UUID.randomUUID());

//Get
 UUID uuid = (UUID) MemCache.defaultCache().get("myUUID");
```
 
### Typed cache
But you can create your own typed cache by single line :)
 
```java
MemCache.cache(UUID.class).put("myUUID", UUID.randomUUID());

UUID uuid = MemCache.cache(UUID.class).get("myUUID");
```

### Expire data in cache

You can put data to cache with expire param:

```java
//Put value 1L with key "myvalue" with expire after 10 seconds
MemCache.cache(Long.class).put("myvalue", 1L, System.currentTimeMillis() + 10000);

//Value is available
Long value = MemCache
                .cache(Long.class)
                .get("myValue");
    
//Wait 11 seconds    
TimeUnit.SECONDS.sleep(11000);

//Value not available more
value = MemCache
                .cache(Long.class)
                .get("myValue");
```

### Notification cache

There are 2 ways about notifications cache changes:

With listener:
```java
ChangeConfig<UUID> listenerConfig = ChangeConfig.createListenerConfig(new ICacheChangeListener<UUID>() {
            @Override
            public void onChange(UUID data) {

            }

            @Override
            public void onChange(List<UUID> data) {

            }
        });
        MemCache.cache(UUID.class).setConfig(listenerConfig);
```

or with event bus :

```java
ChangeConfig<UUID> eventConfig = ChangeConfig.createEventConfig("change_object", "change_collection");

MemCache.cache(UUID.class).setConfig(eventConfig);
```

### Configure global 
```java
 MemCache.addConfig(UUID.class, ChangeConfig.<UUID>createEventConfig("uuid_change", "uuid_list"));
 
        MemCache.addConfig(Long.class, ChangeConfig.createListenerConfig(new ICacheChangeListener<Long>() {
            @Override
            public void onChange(Long data) {

            }

            @Override
            public void onChange(List<Long> data) {

            }
        }));
```

### Clear cache

You can clear cache by key:
```java
MemCache.cache(String.class).put("Key", "Value");

MemCache.cache(String.class).evict("Key");
```
Or clear all cache data:
```java
 MemCache.cache(String.class).evictAll();
```
 
### Change expire
```java
MemCache.cache(UUID.class).put("uuid", UUID.randomUUID());
//Change expire to 10 minutes 
MemCache.cache(UUID.class).setExpire("uuid", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
```

## Logging

### Configure log level

You can configure any log level from any application point:
```java
//No logs will be print
LoggerEnvironment.setProd();

//All logs will be print
LoggerEnvironment.setDev();

//Set custom level: ALL (all level is equal #setDev)
LoggerEnvironment.setCustom(LogLevel.ALL);

//Only debug and error log will be print
LoggerEnvironment.setCustom(LogLevel.D, LogLevel.E);
```
Available logs are:
```java
public enum LogLevel {

    ALL, //all
    V, //verbose
    D, //debug
    E, //error
    W, //warning
    I //info
}
```
### Obtain logger

By class:
```java
Logger logger = LoggerFactory.getLogger(MainActivity.class);
```

By string tag:
```java
Logger logger = LoggerFactory.getLogger("MainActivity");
```

### Usage
```java
 Logger logger = LoggerFactory.getLogger(MainActivity.class);
```

```java
//Print I/MainActivity: Hello
 logger.i("Hello")
 
 //Print W/MainActivity: Warning message
 logger.w("Warning message"); 
 
 //Print:
 // E/MainActivity: Oops, something goes wrong
 // E/MainActivity: com.aizenberg.support.common.error.SupportException: Test exception
 // +stacktrace
 logger.e("Oops, something goes wrong", new SupportException("Test exception"));
```

## Map iteration

Library supports map iteration and filtering feature.
In this example we use this map:
```java
Map<Long, String> map = new HashMap<>();
map.put(24L, "John");
map.put(30L, "Helena");
map.put(1L, "Vas");
map.put(90L, "TestUser");
```
You can get traverser by calling Traverser#of static method:
```java
ITraversable<Long, String> traversable = Traverser.of(map);
while (traversable.hasNext()) {
    TraversPair<Long, String> current = traversable.next();
    logger.i(current.toString());
}
```

Output:
```
TraversPair{key=1, value=Vas}
TraversPair{key=24, value=John}
TraversPair{key=90, value=TestUser}
TraversPair{key=30, value=Helena}
```

Get only keys:

```java
while (traversable.hasNext()) {
    logger.i(String.valueOf(traversable.nextKey()));
}
```

Output:
```java
1
24
90
30
```

### Map filtering

 Get elements only with keys > 29
```java
ITraversable<Long, String> traversable = Traverser.of(map);

TraversFilter<Long, String> filter = 
TraversFilter.<Long, String>newFilter().key(new IValidatable<Long>() {
        @Override
        public boolean isValid(Long candidate) {
            return candidate > 29;
        }
        });

Map<Long, String> filteredMap = traversable.filter(filter);
        
//Iterate by new map
ITraversable<Long, String> filteredMapTraverser = Traverser.of(filteredMap);
    while (filteredMapTraverser.hasNext()) {
        logger.i(filteredMapTraverser.next().toString());
    }
```

```java
TraversPair{key=90, value=TestUser}
TraversPair{key=30, value=Helena}
```

Filtering with key OR by value:
Get elements where key > 29 OR value contains "Vas":
```java
TraversFilter<Long, String> filter
                = TraversFilter.<Long, String>newFilter()
                .key(new IValidatable<Long>() {
                    @Override
                    public boolean isValid(Long candidate) {
                        return candidate > 29;
                    }
                })
                .or()
                .value(new IValidatable<String>() {
                    @Override
                    public boolean isValid(String candidate) {
                        return candidate.contains("Vas");
                    }
                });
```

```java
TraversPair{key=1, value=Vas}
TraversPair{key=90, value=TestUser}
TraversPair{key=30, value=Helena}
```

Use "AND" filter:
```java
TraversFilter<Long, String> filter
                = TraversFilter.<Long, String>newFilter()
                .key(new IValidatable<Long>() {
                    @Override
                    public boolean isValid(Long candidate) {
                        return candidate > 29;
                    }
                })
                .and()
                .value(new IValidatable<String>() {
                    @Override
                    public boolean isValid(String candidate) {
                        return candidate.startsWith("Test");
                    }
                });
```

```java
TraversPair{key=90, value=TestUser}
```

### Simple KeyValue filter
```java
Map<Long, String> filteredMap = 
traversable.filter(new TraversKVFilter<Long, String>() {
        @Override
        public boolean accept(Long key, String value) {
            return key > 29 && !value.isEmpty();
        }
});
```

### Other filters implementation
See TraversOneOfKVFilter and implementations: TraversOneOfKVFilterImplOr and TraversOneOfKVFilterImplAnd filters.


## Geolocation support

In this library geolocation is provided via SupportLocationManager class.


### Configure geolocation service

```java
LocationConfiguration.LocationConfigurationBuilder builder = new LocationConfiguration.LocationConfigurationBuilder();
        builder
                //Auto start after #init method
                .setLazyStart(false)
                //Invoke onLocationChanged method immediate after #addListener
                .setNotificationImmediate(true)
                //Choose your provider
                .setLocationProviderType(LocationConfiguration.LocationProviderType.GPS)
                //Minimal distance to change loc
                .setMinDistance(20f)
                //Minimal time to change loc
                .setMinTime(0)
                //Notification type
                .setNotificationByEvent("location");

        SupportLocationManager.init(this, builder.build());
```

### Handle location changes
```java
builder.setNotificationByEvent("location");
//............
EventBus.getBus().addListener("location", new IEventReceiver() {
            @Override
            public void onReceiveAction(String action, Object... args) {
                Location location = (Location) args[0];
                String provider = (String) args[1];
            }
});
```

or handle in listener:
```java
SupportLocationManager.getInstance().addListener(new IGeoListener() {
    @Override
    public void onLocationChanged(Location location, String provider) {

    }
});
```

### Start/stop location retrieving

For battery saving you can start/stop from any application point.
Also you can bind lifecycle to activity/fragment
```java
private LifecycleHook locationManager = SupportLocationManager.getInstance();

    @Override
    public void onResume() {
        super.onResume();
        locationManager.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.onStop();
    }
```

### Obtain last location directly

```java
Location location = SupportLocationManager.getInstance().getLastKnownLocation();
```

## Utils

Library provides few utils for easier development.

### Generic utils


#### <T> orElse(T data, T default)

Returns data if data not null, default otherwise
```java
User currentUser = GenericUtils.orElse(user, defaultUser);
```

### String utils

Checking that string is empty:
```java
if (StringUtils.isEmpty(password)) {
    Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
}
```

Returns empty string if arg is null.
Method like this:
```java
private String checkString(String str) {
    return str != null ? str : "";
}
```
can be simplify to:
```java
return StringUtils.orEmpty(str);
```

### I/O Utils

Library provides helper method for very simple closing any streams. Earlier:
```java
InputStream is = null;
OutputStream os = null;
try {
    //Some work with streams
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {}
    }

    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {}
    }
}
```
Now:
```java
InputStream is = null;
OutputStream os = null;
try {
    //Some work with streams
} catch (IOException e) {
    e.printStackTrace();
} finally {
    IOUtils.closeQuietly(is, os);
}
```

### File utils

#### Work with file async

We have two ways to work with async copy/move: listener or event.

Copying async file with listener:
```java
FileUtils.copyFileAsync(sourceFile, targetFile, CopyConfig.createListenerConfig(new ICopyListener() {
            @Override
            public void onCopyFinish() {

            }

            @Override
            public void onCopyFailure(Throwable cause) {

            }
    }));

```

Copying async file with events:
```java
EventBus.getBus().addListeners(new IEventReceiver() {
            @Override
            public void onReceiveAction(String action, Object... args) {

            }
        }, "copy_success", "copy_fail");

FileUtils.copyFileAsync(
        sourceFile,
        targetFile,
        CopyConfig.createEventConfig("copy_success", "copy_fail"));
```

#### Work sync

Library provides 2 ways of copying sync files: with exception throwing and withou

With exception:
```java
try {
    FileUtils.copyFileSync(sourceFile, targetFile);
} catch (IOException e) {
    //Handle exception
}
```

Without exception:
```java
//Return true if file sopyied, false otherwise
boolean success = FileUtils.copyFileSyncQuietly(sourceFile, targetFile);
```


### Validation

Library supports custom validation rules. 
For example, prepared validator, to use email validation
```java
public class EmailValidator extends PatternValidator {
    @Override
    public String getPattern() {
        return Patterns.EMAIL_ADDRESS.pattern();
    }
}
```
That's all. Now you can use this validator only by class:
```java
boolean failedValidation = Validator.isValidCustomPattern(EmailValidator.class, "not_email");
```

Completely custom validations for any classes:

```java
IValidatable<Long> customValidationRule = new IValidatable<Long>() {
    @Override
    public boolean isValid(Long candidate) {
        return candidate != null && candidate > 5;
    }
};

boolean customValid = Validator.isCustomValid(customValidationRule, 1L);
```

#### Caching your custom validator
```java
 IValidatable<Long> customValidationRule = new IValidatable<Long>() {
    @Override
    public boolean isValid(Long candidate) {
        return candidate != null && candidate > 5;
    }
};

Validator.registerCustomValidator("long", customValidationRule);
```
Now you can use validator by alias name:

```java
boolean isValid = Validator.isCustomValid("long", 1L);
```

### Commons

#### Typed adapter for lists
```java
public class FriendsAdapter extends CommonAdapter<User> {

    public FriendsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater instance with name inflater available
        return inflater.inflate(android.R.layout.activity_list_item, parent, false);
    }
}
```

Using adapter:

```java
FriendsAdapter adapter = new FriendsAdapter(this);
listView.setAdapter(adapter);
adapter.setData(users);
```

There is no need to call notification of changes, just call setData.

Notification when adapter is empty:
```java
adapter.setEmptyAdapterListener(new IEmptyAdapterListener() {
    @Override
    public void onPipelineChange(boolean empty) {

    }
});
```

#### Common async task

Library provides easy access to async operations with callback out of box.

For using this functionality, create your own class and extend CommonAsyncTask<ReturnType>.

```java
public class AsyncUUIDGenerator extends CommonAsyncTask<UUID> {

    public AsyncUUIDGenerator(IAsyncCallback<UUID> callback) {
        super(callback);
    }

    @Override
    public UUID doAction() throws Throwable {
        return UUID.randomUUID();
    }
}
```
Usage:
```java
new AsyncUUIDGenerator(new IAsyncCallback<UUID>() {
    @Override
    public void onBegin() {
        //Operation start
    }

    @Override
    public void onEnd() {
        //Operation end
    }

    @Override
    public void onSuccess(UUID result) {
        //Result delivered success
    }

    @Override
    public void onFailure(Throwable cause) {
        //Exception handle here
    }
}).execute();
```

You can override only necessary methods by using SimpleAsyncCallbackAdapter:
```java
new AsyncUUIDGenerator(new SimpleAsyncCallbackAdapter<UUID>() {

    @Override
    public void onSuccess(UUID result) {
        super.onSuccess(result);

    }
}).execute();
```

Result analyzer do not accept null as result by default. But this behavior can be changed:
```java
public class SaveUserInDBJob extends CommonAsyncTask<Void> {

    public SaveUserInDBJob(IAsyncCallback<Void> callback) {
        super(callback);
    }

    @Override
    public Void doAction() throws Throwable {
        getDatabase().save(user);
        return null;
    }

    @Override
    protected boolean isNullResultAcceptable() {
        return true;
    }
}
```

### Network connection aware

Library supports runtime network notification changes. 
Connection manager turned off by default. But you can turn on by single line code:
```java
NetworkConnectionManager.getInstance().start();
```



#### Use listeners

```java
 NetworkConnectionManager.getInstance().addListener(new NetworkConnectionAware() {
    @Override
    public void onNetworkStateChange(boolean connected, NetworkType networkType) {
                
    }
});
```

#### Use events

```java
NetworkConnectionManager.getInstance().setEventActionKey("network");
        
EventBus.getBus().addListener("network", new IEventReceiver() {
    @Override
    public void onReceiveAction(String action, Object... args) {
        boolean networkEnabled = (boolean) args[0];
        NetworkType networkType = (NetworkType) args[1];
    }
});
```

#### Get network state directly

```java
NetworkState state = NetworkConnectionManager.getInstance().getCurrentNetworkState(context);
```
Library provides few methods:

#### Direct sync request (PING)

```java
boolean success = NetworkConnectionManager.getInstance().ping("8.8.8.8");
```

#### Async request (PING) with listener
```java
NetworkConnectionManager.getInstance().pingAsync("8.8.8.8", new NetworkConnectionAware() {
    @Override
    public void onNetworkStateChange(boolean connected, NetworkType networkType) {

    }
});
```

#### Async request (PING) with events

Register your event listener wherever you want:
```java
EventBus.getBus().addListener("ping_result", new IEventReceiver() {
    @Override
    public void onReceiveAction(String action, Object... args) {
        boolean pingSuccess = (boolean) args[0];
    }
});
```

And run async ping task:
```java
NetworkConnectionManager.getInstance().pingAsync("8.8.8.8", "ping_result");
```

#### Sync request to the server

You can also check network state using direct HTTP Get request. Library use next parameters  by default:
```
Server: http://www.google.com
Method: GET
Connection timeout: 1000ms
Exptected response code: 200 OK
```
You can do request by single line:

```java
boolean successRequest = NetworkConnectionManager.getInstance().doRequest();
```

But you can also use custom parameters:
```java
//Request to "example.com"
boolean successRequest = NetworkConnectionManager.getInstance().doRequest("example.com");

//Or request to example.com, expect status 403, timeout 2s
boolean successRequest = NetworkConnectionManager.getInstance().doRequest("example.com", HttpURLConnection.HTTP_FORBIDDEN, 2000);

//Or request to example.com, expect status 200, timeout 3s
boolean successRequest = NetworkConnectionManager.getInstance().doRequest("example.com", 3000);
```


#### Async request (listener)

```java
NetworkConnectionManager.getInstance().doRequestAsync(new NetworkConnectionAware() {
    @Override
    public void onNetworkStateChange(boolean connected, NetworkType networkType) {
               
           }
       });
```
Library supports customization request params, as well as sync request:
```java
NetworkConnectionManager.getInstance().doRequestAsync("http://www.google.com", new NetworkConnectionAware() {
    @Override
    public void onNetworkStateChange(boolean connected, NetworkType networkType) {
        }
});
```

#### Async request (event)

```java
 EventBus.getBus().addListener("google_connection", new IEventReceiver() {
    @Override
    public void onReceiveAction(String action, Object... args) {
        boolean requestSuccess = (boolean) args[0];
    }
});
```

```
NetworkConnectionManager.getInstance().doRequestAsync("http://www.google.com", "google_connection");
```

#### Request config

You can configure default params for request only once. You must create INetworkRequestConfigurable instance to do this:
```java
public class RestServerConfiguration implements INetworkRequestConfigurable {
    @Override
    public int getResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    public int getRequestConnectionTimeOut() {
        return 1000;
    }

    @Override
    public String getServer() {
        return "http://myserver.com/api/health";
    }

    @Override
    public String getRequestMethod() {
        return "GET";
    }
}
```
Now you can setup configuration.

```java
NetworkConnectionManager.setConfiguration(new RestServerConfiguration());
```

### Global async configuration

Async methods in this library:
* FileUtils#copyFileAsync
* FileUtils#moveFileAsync
* NetworkConnectionManager#doRequestAsync
* NetworkConnectionManager#pingAsync

are performed in default executor. But this behaviour can be changed

Using default executor:
```java
SupportExecutor.setDefaultSingleThreadExecutor();
```

Using fixed thread pool with threads count 4.
```java
SupportExecutor.setCustomThreadExecutor(Executors.newFixedThreadPool(4));
```

