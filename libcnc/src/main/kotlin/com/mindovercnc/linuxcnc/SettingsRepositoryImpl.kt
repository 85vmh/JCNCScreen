package com.mindovercnc.linuxcnc

import com.mindovercnc.base.SettingsRepository
import com.mindovercnc.base.data.BooleanKey
import com.mindovercnc.base.data.DoubleKey
import com.mindovercnc.base.data.IntegerKey
import java.util.prefs.Preferences

class SettingsRepositoryImpl(private val prefs: Preferences) : SettingsRepository {

    override fun put(intKey: IntegerKey, value: Int) {
        prefs.putInt(intKey.name, value)
    }

    override fun put(doubleKey: DoubleKey, value: Double) {
        prefs.putDouble(doubleKey.name, value)
    }

    override fun put(booleanKey: BooleanKey, value: Boolean) {
        prefs.putBoolean(booleanKey.name, value)
    }

    override fun get(intKey: IntegerKey, defaultValue: Int): Int {
        return prefs.getInt(intKey.name, defaultValue)
    }

    override fun get(doubleKey: DoubleKey, defaultValue: Double): Double {
        return prefs.getDouble(doubleKey.name, defaultValue)
    }

    override fun get(booleanKey: BooleanKey, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(booleanKey.name, defaultValue)
    }
}


