package com.aizenberg.support.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Yuriy Aizenberg
 */
public class IOUtils {

    public static void closeQuietly(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            closeQuietlyInternal(closeable);
        }
    }

    private static void closeQuietlyInternal(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

}
