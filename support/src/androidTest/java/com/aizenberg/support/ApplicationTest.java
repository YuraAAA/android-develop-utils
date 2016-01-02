package com.aizenberg.support;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.aizenberg.support.cache.ICache;
import com.aizenberg.support.cache.MemCache;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.event.IEventReceiver;

import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testEventBus() throws Exception {

        IAction action = new IAction() {
            @Override
            public String getAction() {
                return "SimpleAction";
            }
        };

        EventBus bus = EventBus.getBus();
        bus.addListener(action, new IEventReceiver() {
            @Override
            public void onReceiveAction(String action, Object... args) {
                assertEquals(action, "SimpleAction");
            }
        });

        assertTrue(bus.hasListener("SimpleAction"));
        assertTrue(bus.hasListener(action));
        assertTrue(bus.notifyAction("SimpleAction"));
        assertTrue(bus.notifyAction(action));

        bus.removeListeners(action);

        assertTrue(!bus.hasListener("SimpleAction"));
        assertTrue(!bus.hasListener(action));
        assertTrue(!bus.notifyAction("SimpleAction"));
        assertTrue(!bus.notifyAction(action));

        TimeUnit.SECONDS.sleep(1L);

    }

    public void testCache() throws Exception {
        ICache<User> cache = MemCache.cache(User.class);
        assertNotNull(cache);
        User u = cache.get("df");
        assertNull(u);
        u = new User(1L, "U");
        cache.put("df", u);
        User fromCache = cache.get("df");
        assertNotNull(fromCache);
        assertEquals(u, fromCache);

        cache.evict("df");
        fromCache = cache.get("df");
        assertNull(fromCache);

        cache.put("df", u, System.currentTimeMillis() + 1000);
        fromCache = cache.get("df");
        assertNotNull(fromCache);
        assertEquals(u, fromCache);

        TimeUnit.SECONDS.sleep(2);

        fromCache = cache.get("df");
        assertNull(fromCache);



    }

    private static class User {
        private Long id;
        private String usename;

        public User(Long id, String usename) {
            this.id = id;
            this.usename = usename;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;

            User user = (User) o;

            if (id != null ? !id.equals(user.id) : user.id != null) return false;
            return !(usename != null ? !usename.equals(user.usename) : user.usename != null);

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (usename != null ? usename.hashCode() : 0);
            return result;
        }
    }
}