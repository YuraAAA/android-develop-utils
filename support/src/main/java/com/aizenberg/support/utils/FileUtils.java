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

    private static void copyFileAsync(final File src, final File dest, final CopyConfig copyConfig, final boolean deleteAfterCopy) {
        new AsyncTask<Void, Void, Void>() {

            private Throwable t;

            @Override
            protected Void doInBackground(Void... params) {
                FileChannel inputChannel = null;
                FileChannel outputChannel = null;
                try {
                    inputChannel = new FileInputStream(src).getChannel();
                    outputChannel = new FileOutputStream(dest).getChannel();
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    if (deleteAfterCopy) //noinspection ResultOfMethodCallIgnored
                        dest.delete();
                } catch (IOException e) {
                    t = e;
                } finally {
                    IOUtils.closeQuietly(inputChannel, outputChannel);
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
