package com.aizenberg.support.network;

import android.content.Context;
import android.os.AsyncTask;

import com.aizenberg.support.common.collection.NullExcludeSafetyList;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.utils.StringUtils;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public class NetworkConnectionManager {

    private static final Object MUTEX = new Object();

    private final List<NetworkConnectionAware> connectionAwares = new NullExcludeSafetyList<>();
    private String connectionEventKey;
    private boolean enabled;
    private static NetworkConnectionManager instance;
    private NetworkState currentNetworkState;

    public static NetworkConnectionManager getInstance() {
        if (instance == null) {
            synchronized (MUTEX) {
                if (instance == null) {
                    instance = new NetworkConnectionManager();
                }
            }
        }
        return instance;
    }

    public void setEventActionKey(String key) {
        this.connectionEventKey = key;
    }

    public void setEventActionKey(IAction actionKey) {
        this.connectionEventKey = actionKey.getAction();
    }

    public void addListener(NetworkConnectionAware connectionAware) {
        connectionAwares.add(connectionAware);
    }

    public void removeListener(NetworkConnectionAware connectionAware) {
        connectionAwares.remove(connectionAware);
    }

    public NetworkState getCurrentNetworkState(Context context) {
        if (currentNetworkState != null) return new NetworkState(currentNetworkState);
        return NetworkAwareReceiver.getState(context);
    }

    public void start() {
        enabled = true;
    }

    public void stop() {
        enabled = false;
    }

    public boolean ping(String server) {
        checkServer(server);
        try {
            Process process = Runtime.getRuntime().exec(String.format("ping -c 1 %s", server));
            return process.waitFor() == 0;
        } catch (Exception ignored) {
        }
        return false;
    }

    public void pingAsync(final String server, final String eventKey) {
        checkServer(server);
        if (eventKey == null) throw new IllegalArgumentException("Eventkey can't be null");
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return ping(server);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                NetworkType type = currentNetworkState != null ? NetworkConnectionManager.this.currentNetworkState.networkType : NetworkType.UNDEFINED;
                EventBus.getBus().notifyAction(eventKey, aBoolean, type);

            }
        }.execute();
    }

    public void pingAsync(final String server, IAction eventKey) {
        if (eventKey == null) throw new IllegalArgumentException("Eventkey can't be null");
        pingAsync(server, eventKey.getAction());
    }

    public void pingAsync(final String server, final NetworkConnectionAware connectionAware) {
        checkServer(server);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return ping(server);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (connectionAware == null) return;
                connectionAware.onNetworkStateChange(aBoolean, currentNetworkState != null ? currentNetworkState.networkType : NetworkType.UNDEFINED);
            }
        }.execute();
    }

    private void checkServer(String server) {
        if (StringUtils.isEmpty(server))
            throw new IllegalArgumentException("Server can't be null or empty");
    }


    void notifyConnectivityChange(NetworkState networkState) {
        if (!enabled || networkState == null) return;
        if (currentNetworkState == null) {
            currentNetworkState = networkState;
        } else if (currentNetworkState.equals(networkState)) {
            return;
        } else {
            currentNetworkState = networkState;
        }

        for (NetworkConnectionAware connectionAware : connectionAwares) {
            if (connectionAware != null) {
                connectionAware.onNetworkStateChange(networkState.enabled, networkState.networkType);
            }
        }

        if (connectionEventKey != null) {
            EventBus.getBus().notifyAction(connectionEventKey, networkState.enabled, networkState.networkType);
        }

    }

    static class NetworkState {
        private boolean enabled;
        private NetworkType networkType;

        public NetworkState(NetworkState networkState) {
            this.enabled = networkState.enabled;
            this.networkType = networkState.networkType;
        }


        public NetworkState(boolean enabled, NetworkType networkType) {
            this.enabled = enabled;
            this.networkType = networkType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NetworkState that = (NetworkState) o;

            if (enabled != that.enabled) return false;
            return networkType == that.networkType;

        }

        @Override
        public int hashCode() {
            int result = (enabled ? 1 : 0);
            result = 31 * result + (networkType != null ? networkType.hashCode() : 0);
            return result;
        }
    }

}
