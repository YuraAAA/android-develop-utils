package com.aizenberg.support.event;


/**
 * Created by Yuriy Aizenberg
 */
public class EventBus {

    private static EventBus instance;
    private static final Object LOCK = new Object();

    private static final SynchronizedSafetyEventMultiMap<IEventReceiver> MAP = new SynchronizedSafetyEventMultiMap<>();


    private EventBus() {
    }

    public static EventBus getBus() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void addListener(IAction action, IEventReceiver receiver) {
        addListener(action.getAction(), receiver);
    }

    public void addListener(String action, IEventReceiver receiver) {
        MAP.addListener(action, receiver);
    }

    public void addListeners(IEventReceiver eventReceiver, IAction... actions) {
        for (IAction action : actions) {
            addListener(action, eventReceiver);
        }
    }

    public void addListeners(IEventReceiver eventReceiver, String... actions) {
        for (String action : actions) {
            addListener(action, eventReceiver);
        }
    }

    public void removeListeners(IEventReceiver eventReceiver, IAction... actions) {
        for (IAction action : actions) {
            removeListener(action, eventReceiver);
        }
    }

    public void removeListeners(IEventReceiver eventReceiver, String... actions) {
        for (String action : actions) {
            removeListener(action, eventReceiver);
        }
    }


    public void removeListener(IAction action, IEventReceiver receiver) {
        MAP.removeListener(action.getAction(), receiver);
    }

    public void removeListener(String action, IEventReceiver receiver) {
        MAP.removeListener(action, receiver);
    }

    public void removeListeners(IAction action) {
        removeListeners(action.getAction());
    }

    public void removeListeners(String action) {
        MAP.removeListeners(action);
    }


    public boolean notifyAction(IAction action) {
        return notifyAction(action.getAction());
    }

    public boolean notifyAction(String action) {
        return MAP.notifyAboutEvent(action);
    }

    public boolean notifyById(IAction action, Long id) {
        return notifyById(action.getAction(), id);
    }

    public boolean notifyById(String action, Long id) {
        return MAP.notifyById(action, id);
    }

    public boolean notifyById(IAction action, Long id, Object... args) {
        return notifyById(action.getAction(), id, args);
    }

    public boolean notifyById(String action, Long id, Object... args) {
        return MAP.notifyById(action, id, args);
    }



    public boolean notifyAction(IAction action, Object... args) {
        return notifyAction(action.getAction(), args);
    }

    public boolean notifyAction(String action, Object... args) {
        return MAP.notifyAboutEvent(action, args);
    }

    public boolean hasListener(IAction action) {
        return hasListener(action.getAction());
    }

    public boolean hasListener(String action) {
        return MAP.hasListenersByKey(action);
    }


}
