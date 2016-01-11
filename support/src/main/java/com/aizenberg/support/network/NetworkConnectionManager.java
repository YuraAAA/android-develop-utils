package com.aizenberg.support.network;

import android.content.Context;
import android.os.AsyncTask;

import com.aizenberg.support.common.SupportExecutor;
import com.aizenberg.support.common.collection.NullExcludeSafetyList;
import com.aizenberg.support.common.error.SupportException;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.utils.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private static INetworkRequestConfigurable configurable = new DefaultRequestConfiguration();

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

    public static void setConfiguration(INetworkRequestConfigurable configuration) {
        if (configuration == null) throw new SupportException("Configuration can't be null");

        if (StringUtils.isEmpty(configuration.getServer()))
            throw new SupportException("Configuration can't be null");

        if (StringUtils.isEmpty(configuration.getRequestMethod()))
            throw new SupportException("Request method can't be null");

        if (configuration.getResponseCode() <= 0)
            throw new SupportException(String.format("Response code %d is not allowed", configuration.getResponseCode()));
        NetworkConnectionManager.configurable = configuration;
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
        SupportExecutor.execute(new AsyncTask<Void, Void, Boolean>() {
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
        });
    }

    public void pingAsync(final String server, IAction eventKey) {
        if (eventKey == null) throw new IllegalArgumentException("Eventkey can't be null");
        pingAsync(server, eventKey.getAction());
    }

    public void pingAsync(final String server, final NetworkConnectionAware connectionAware) {
        checkServer(server);
        SupportExecutor.execute(new AsyncTask<Void, Void, Boolean>() {
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
        });
    }

    private void checkServer(String server) {
        if (StringUtils.isEmpty(server))
            throw new IllegalArgumentException("Server can't be null or empty");
    }

    public boolean doRequest(String server, int responseCode, int timeOut) {
        checkServer(server);
        try {
            URL u = new URL(server);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod(getRequestMethod());
            huc.setConnectTimeout(timeOut);
            huc.connect();
            return responseCode == huc.getResponseCode();
        } catch (IOException e) {
            return false;
        }

    }

    private String getRequestMethod() {
        return configurable.getRequestMethod();
    }

    public boolean doRequest(String server, int timeOut) {
        return doRequest(server, getResponseCode(), timeOut);
    }

    private int getResponseCode() {
        return configurable.getResponseCode();
    }


    public boolean doRequest(String server) {
        return doRequest(server, getResponseCode(), getRequestConnectionTimeOut());
    }

    public boolean doRequest() {
        return doRequest(getServer(), getResponseCode(), getRequestConnectionTimeOut());
    }


    public void doRequestAsync(final String server, final int responseCode, final int timeOut, final NetworkConnectionAware connectionAwareListener) {
        doRequestAsyncInternal(server, responseCode, timeOut, null, connectionAwareListener, false);


    }

    public void doRequestAsync(String server, int timeOut, NetworkConnectionAware connectionAwareListener) {
        doRequestAsync(server, getResponseCode(), timeOut, connectionAwareListener);
    }


    public void doRequestAsync(String server, NetworkConnectionAware connectionAwareListener) {
        doRequestAsync(server, getResponseCode(), getRequestConnectionTimeOut(), connectionAwareListener);
    }

    public void doRequestAsync(NetworkConnectionAware connectionAwareListener) {
        doRequestAsync(getServer(), getResponseCode(), getRequestConnectionTimeOut(), connectionAwareListener);
    }


    public void doRequestAsync(final String server, final int responseCode, final int timeOut, final IAction action) {
        doRequestAsyncInternal(server, responseCode, timeOut, action.getAction(), null, true);

    }


    public void doRequestAsync(String server, int timeOut, IAction action) {
        doRequestAsync(server, getResponseCode(), timeOut, action);
    }


    public void doRequestAsync(String server, IAction action) {
        doRequestAsync(server, getResponseCode(), getRequestConnectionTimeOut(), action);
    }

    public void doRequestAsync(IAction action) {
        doRequestAsync(getServer(), getResponseCode(), getRequestConnectionTimeOut(), action);
    }

    private String getServer() {
        return configurable.getServer();
    }

    private int getRequestConnectionTimeOut() {
        return configurable.getRequestConnectionTimeOut();
    }


    public void doRequestAsync(final String server, final int responseCode, final int timeOut, final String action) {
        doRequestAsyncInternal(server, responseCode, timeOut, action, null, true);
    }


    public void doRequestAsync(String server, int timeOut, String action) {
        doRequestAsync(server, getResponseCode(), timeOut, action);
    }


    public void doRequestAsync(String server, String action) {
        doRequestAsync(server, getResponseCode(), getRequestConnectionTimeOut(), action);
    }

    public void doRequestAsync(String action) {
        doRequestAsync(getServer(), getResponseCode(), getRequestConnectionTimeOut(), action);
    }

    private void doRequestAsyncInternal(final String server, final int responseCode, final int timeOut, final String actionString, final NetworkConnectionAware connectionAware, final boolean isNotifyByAction) {
        checkServer(server);

        if (isNotifyByAction && actionString == null)
            throw new NullPointerException("Action can't be null");

        if (!isNotifyByAction && connectionAware == null) {
            throw new NullPointerException("Listener not set");
        }

        SupportExecutor.execute(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return doRequest(server, responseCode, timeOut);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (isNotifyByAction) {
                    EventBus.getBus().notifyAction(actionString, aBoolean);
                } else {
                    connectionAware.onNetworkStateChange(aBoolean, NetworkType.UNDEFINED);
                }
            }
        });
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

    public static class NetworkState {
        private boolean enabled;
        private NetworkType networkType;

        NetworkState(NetworkState networkState) {
            this.enabled = networkState.enabled;
            this.networkType = networkState.networkType;
        }


        NetworkState(boolean enabled, NetworkType networkType) {
            this.enabled = enabled;
            this.networkType = networkType;
        }

        public boolean isEnabled() {
            return enabled;
        }


        public NetworkType getNetworkType() {
            return networkType;
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
