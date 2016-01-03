package com.aizenberg.support.utils;

import android.os.AsyncTask;

import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.utils.config.CopyConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Yuriy Aizenberg
 */
public class FileUtils {


    public static void copyFileAsync(File src, File dest, CopyConfig copyConfig) {
        copyFileAsync(src, dest, copyConfig, false);
    }

    public static void moveFileAsync(File src, File dest, CopyConfig copyConfig) {
        copyFileAsync(src, dest, copyConfig, true);
    }

    public static boolean copyFileSyncQuitely(File src, File dest) {
        try {
            copyFileSyncInternal(src, dest, false);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void copyFileSync(File src, File dest) throws IOException {
        copyFileSyncInternal(src, dest, false);
    }

    public static boolean moveFileSyncQuitely(File src, File dest) {
        try {
            copyFileSyncInternal(src, dest, true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void moveFileSync(File src, File dest) throws IOException {
        copyFileSyncInternal(src, dest, false);
    }

    private static void copyFileSyncInternal(File src, File dest, boolean delete) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(src).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            if (delete) //noinspection ResultOfMethodCallIgnored
                dest.delete();
        } finally {
            IOUtils.closeQuietly(inputChannel, outputChannel);
        }

    }

    private static void copyFileAsync(final File src, final File dest, final CopyConfig copyConfig, final boolean deleteAfterCopy) {
        new AsyncTask<Void, Void, Void>() {

            private Throwable t;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    copyFileSyncInternal(src, dest, deleteAfterCopy);
                } catch (IOException e) {
                    t = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (t != null) {
                    notificationFail();
                } else {
                    notificationOk();
                }
            }

            private void notificationByActionInternal(IAction action, String actionString) {
                Long id = copyConfig.getId();

                if (id != null) {
                    if (copyConfig.isAsAction()) {
                        EventBus.getBus().notifyById(action, id);
                    } else {
                        EventBus.getBus().notifyById(actionString, id);
                    }
                } else {
                    if (copyConfig.isAsAction()) {
                        EventBus.getBus().notifyAction(action);
                    } else {
                        EventBus.getBus().notifyAction(actionString);
                    }
                }
            }


            private void notificationFail() {
                switch (copyConfig.getNotificationType()) {
                    case LISTENER:
                        ICopyListener copyListener = copyConfig.getCopyListener();
                        if (copyListener != null) {
                            copyListener.onCopyFailure(t);
                        }
                        break;
                    case EVENT:
                        notificationByActionInternal(copyConfig.getActionFail(), copyConfig.getActionFailString());
                        break;
                }
            }

            private void notificationOk() {
                switch (copyConfig.getNotificationType()) {
                    case LISTENER:
                        ICopyListener copyListener = copyConfig.getCopyListener();
                        if (copyListener != null) {
                            copyListener.onCopyFinish();
                        }
                        break;
                    case EVENT:
                        notificationByActionInternal(copyConfig.getActionOk(), copyConfig.getActionOkString());
                        break;
                }
            }


        }.execute();
    }

}
