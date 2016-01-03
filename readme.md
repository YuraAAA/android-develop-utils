# Android development utils

## Install library

### Gradle
Add dependency to your build.gradle file

```sh
 compile 'com.aizenberg:support:0.1.2'
```

## Fragment management (Switcher)

### Simple fragment management in activity

```
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

```
 switcher.switchTo(OneFragment.class);
```

With enums:

```
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
```
switcher.addAliases(
                new Alias(OneFragment.class, "one"),
                new Alias(TwoFragment.class, "two"),
                new Alias(ThreeFragment.class, "three")
        );
//....
switcher.switchTo("one");
```
You can obtain switcher from any fragment only by activity class:

```
//Switch by alias
Switcher.obtainSwitcher(MainActivity.class).switchTo("two");
//or by ISwitchable
Switcher.obtainSwitcher(MainActivity.class).switchTo(Fragments.TWO);
//or by class
Switcher.obtainSwitcher(MainActivity.class).switchTo(TwoFragment.class);
```

Clear back stack

```
 Switcher
     .obtainSwitcher(MainActivity.class)
     .clearBackStack()
     .switchTo(OneFragment.class);
```
 
 
Pass arguments:
```
Bundle args = new Bundle();
args.putLong("id", 1);
Switcher.obtainSwitcher(MainActivity.class).switchTo(OneFragment.class, args);
```
 
Skip back stack:
```
//Without args
Switcher.obtainSwitcher(MainActivity.class).switchTo(OneFragment.class, false);

//With args
Switcher.obtainSwitcher(MainActivity.class).switchTo(OneFragment.class, args, false);
```
 Override back pressing in fragment (In this example we show back dialog in fragment):
 
 ```
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

```
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

### You can use event bus for send events.

Register receiver:

```
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
```
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

```
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
```
EventBus.getBus().addListener(Events.COMPUTATION, this);
```
Send:
```
EventBus.getBus().notifyAction(Events.COMPUTATION, uuid);
```

### Send/receive actions by id

Use IEventIdentificationReceiver listener

```
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
```
//Send result is true because we have listeners for this action/id pair
boolean sendResult = EventBus.getBus().notifyById("computation", 1L, uuid);


//Send result is false, id 2 is not acceptable in listener
boolean sendResult = EventBus.getBus().notifyById("computation", 2L, uuid);
```

Accept any ids:
```
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

```
//true
boolean sendResult1 = EventBus.getBus().notifyById("computation", 1L, uuid);
//true
boolean sendResult2 = EventBus.getBus().notifyById("computation", 1L, uuid);
```

### Multiaction

You can register one listener for more than one action:

```
EventBus.getBus().addListeners(this, "computation", "camera_image", "file_copy");
```

## Cache

### Simple cache
Library support mem cache with change listener integration:

Default cache is not typed:

```
//Put
 MemCache.defaultCache().put("myUUID", UUID.randomUUID());

//Get
 UUID uuid = (UUID) MemCache.defaultCache().get("myUUID");
```
 
### Typed cache
But you can create your own typed cache by single line :)
 
```
MemCache.cache(UUID.class).put("myUUID", UUID.randomUUID());

UUID uuid = MemCache.cache(UUID.class).get("myUUID");
```

### Expire data in cache

You can put data to cache with expire param:

```
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

### Notify about data changed in cache

We have two ways about notifications cache changes:

With listener:
```
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

```
ChangeConfig<UUID> eventConfig = ChangeConfig.createEventConfig("change_object", "change_collection");

MemCache.cache(UUID.class).setConfig(eventConfig);
```

### Configure global 
```
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
```
MemCache.cache(String.class).put("Key", "Value");

MemCache.cache(String.class).evict("Key");
```
Or clear all cache data:
```
 MemCache.cache(String.class).evictAll();
```
 
### Change expire
```
MemCache.cache(UUID.class).put("uuid", UUID.randomUUID());
//Change expire to 10 minutes 
MemCache.cache(UUID.class).setExpire("uuid", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
```

## Logging

### Configure log level

You can configure any log level from any application point:
```
//No logs will be print
LoggerEnvironment.setProd();

//All logs will be print
LoggerEnvironment.setDev();

//Set custom level: ALL (all level is equal #setDev)
LoggerEnvironment.setCustom(LogLevel.ALL);

//Only debug and error log will be print
LoggerEnvironment.setCustom(LogLevel.D, LogLevel.E);
```
Available logs is:
```
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
```
Logger logger = LoggerFactory.getLogger(MainActivity.class);
```

By string tag:
```
Logger logger = LoggerFactory.getLogger("MainActivity");
```

### Usage
```
 Logger logger = LoggerFactory.getLogger(MainActivity.class);
```

```
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

Library support map iteration and filtering feature.
In this example we use this map:
```
Map<Long, String> map = new HashMap<>();
map.put(24L, "John");
map.put(30L, "Helena");
map.put(1L, "Vas");
map.put(90L, "TestUser");
```
You can get traverser by calling Traverser#of static method:
```
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

```
while (traversable.hasNext()) {
    logger.i(String.valueOf(traversable.nextKey()));
}
```

Output:
```
1
24
90
30
```

### Map filtering

Get only elements where keys > 29
```
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

```
TraversPair{key=90, value=TestUser}
TraversPair{key=30, value=Helena}
```

Filter by key OR by value:
Get elements where key > 29 OR value contains "Vas":
```
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

```
TraversPair{key=1, value=Vas}
TraversPair{key=90, value=TestUser}
TraversPair{key=30, value=Helena}
```

Use AND filter:
```
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

```
TraversPair{key=90, value=TestUser}
```

### Simple KeyValue filter
```
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

In this library geolocation provided via SupportLocationManager class.


### Configure geolocation service

```
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
