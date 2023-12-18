package com.wcorp.w_corpandroidpedido.Util;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

public class DataStore {
    private static RxDataStore<Preferences> instance;
    private DataStore() {}

    public static RxDataStore<Preferences> getInstance(Context context) {
        if (instance == null) {
            instance = new RxPreferenceDataStoreBuilder(context,  "authentication").build();;
        }
        return instance;
    }
}
