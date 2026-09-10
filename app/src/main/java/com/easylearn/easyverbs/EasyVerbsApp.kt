package com.easylearn.easyverbs

import android.app.Application
import com.easylearn.easyverbs.data.local.AppDatabase

class EasyVerbsApp : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }
}
