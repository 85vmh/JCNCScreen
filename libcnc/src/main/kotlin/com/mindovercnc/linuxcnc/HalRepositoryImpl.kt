package com.mindovercnc.linuxcnc

import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

private const val RefreshRate = 50L
private const val ComponentName = "weiler-e30"
private const val PinJoystickXPlus = "joystick-x-plus"
private const val PinJoystickXMinus = "joystick-x-minus"
private const val PinJoystickZPlus = "joystick-z-plus"
private const val PinJoystickZMinus = "joystick-z-minus"
private const val PinJoystickRapid = "joystick-rapid"
private const val PinIsPowerFeeding = "is-power-feeding"
private const val PinCycleStart = "cycle-start"
private const val PinCycleStop = "cycle-stop"
private const val PinSpindleSwitchRevIn = "spindle-switch-rev-in"
private const val PinSpindleSwitchFwdIn = "spindle-switch-fwd-in"
private const val PinSpindleSwitchRevOut = "spindle-switch-rev-out"
private const val PinSpindleSwitchFwdOut = "spindle-switch-fwd-out"
private const val PinSpindleActualRpm = "spindle-actual-rpm"

class HalRepositoryImpl(
    private val scope: CoroutineScope
) : HalRepository {
    private val halHandler = HalHandler()

    private var halComponent: HalComponent? = null
    private var pinJoystickXPlus: HalPin<Boolean>? = null
    private var pinJoystickXMinus: HalPin<Boolean>? = null
    private var pinJoystickZPlus: HalPin<Boolean>? = null
    private var pinJoystickZMinus: HalPin<Boolean>? = null
    private var pinJoystickRapid: HalPin<Boolean>? = null
    private var pinIsPowerFeeding: HalPin<Boolean>? = null
    private var pinCycleStart: HalPin<Boolean>? = null
    private var pinCycleStop: HalPin<Boolean>? = null
    private var pinSpindleSwitchRevIn: HalPin<Boolean>? = null
    private var pinSpindleSwitchFwdIn: HalPin<Boolean>? = null
    private var pinSpindleSwitchRevOut: HalPin<Boolean>? = null
    private var pinSpindleSwitchFwdOut: HalPin<Boolean>? = null
    private var pinSpindleActualRpm: HalPin<Float>? = null
    private var allowSpindleOperation: Boolean = true

    init {
        halComponent = halHandler.createComponent(ComponentName)
        halComponent?.let {
            pinJoystickXPlus = it.addPin(PinJoystickXPlus, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinJoystickXMinus = it.addPin(PinJoystickXMinus, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinJoystickZPlus = it.addPin(PinJoystickZPlus, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinJoystickZMinus = it.addPin(PinJoystickZMinus, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinJoystickRapid = it.addPin(PinJoystickRapid, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinIsPowerFeeding = it.addPin(PinIsPowerFeeding, HalPin.Type.BIT, HalPin.Dir.OUT) as? HalPin<Boolean>
            pinSpindleSwitchRevIn = it.addPin(PinSpindleSwitchRevIn, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinSpindleSwitchFwdIn = it.addPin(PinSpindleSwitchFwdIn, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinSpindleSwitchRevOut = it.addPin(PinSpindleSwitchRevOut, HalPin.Type.BIT, HalPin.Dir.OUT) as? HalPin<Boolean>
            pinSpindleSwitchFwdOut = it.addPin(PinSpindleSwitchFwdOut, HalPin.Type.BIT, HalPin.Dir.OUT) as? HalPin<Boolean>
            pinCycleStart = it.addPin(PinCycleStart, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinCycleStop = it.addPin(PinCycleStop, HalPin.Type.BIT, HalPin.Dir.IN) as? HalPin<Boolean>
            pinSpindleActualRpm = it.addPin(PinSpindleActualRpm, HalPin.Type.FLOAT, HalPin.Dir.IN) as? HalPin<Float>
            handleForwarding()
            it.setReady(it.componentId)
        }
    }

    override fun getJoystickStatus(): Flow<JoystickStatus> {
        if (pinJoystickXPlus != null && pinJoystickXMinus != null && pinJoystickZPlus != null && pinJoystickZMinus != null && pinJoystickRapid != null) {
            return combine(
                pinJoystickXPlus!!.valueFlow(RefreshRate),
                pinJoystickXMinus!!.valueFlow(RefreshRate),
                pinJoystickZPlus!!.valueFlow(RefreshRate),
                pinJoystickZMinus!!.valueFlow(RefreshRate),
                pinJoystickRapid!!.valueFlow(RefreshRate),
            ) { xPlus, xMinus, zPlus, zMinus, isRapid ->
                when {
                    xPlus -> JoystickStatus(JoystickStatus.Position.XPlus, isRapid)
                    xMinus -> JoystickStatus(JoystickStatus.Position.XMinus, isRapid)
                    zPlus -> JoystickStatus(JoystickStatus.Position.ZPlus, isRapid)
                    zMinus -> JoystickStatus(JoystickStatus.Position.ZMinus, isRapid)
                    else -> JoystickStatus(JoystickStatus.Position.Neutral, false)
                }
            }.distinctUntilChanged()
        } else {
            return flowOf(JoystickStatus(JoystickStatus.Position.Neutral)).distinctUntilChanged()
        }
    }

    override fun getJoystickPosition(): Flow<JoystickPosition> {
        if (pinJoystickXPlus != null && pinJoystickXMinus != null && pinJoystickZPlus != null && pinJoystickZMinus != null) {
            return combine(
                pinJoystickXPlus!!.valueFlow(RefreshRate),
                pinJoystickXMinus!!.valueFlow(RefreshRate),
                pinJoystickZPlus!!.valueFlow(RefreshRate),
                pinJoystickZMinus!!.valueFlow(RefreshRate),
            ) { xPlus, xMinus, zPlus, zMinus ->
                when {
                    xPlus -> JoystickPosition.XPlus
                    xMinus -> JoystickPosition.XMinus
                    zPlus -> JoystickPosition.ZPlus
                    zMinus -> JoystickPosition.ZMinus
                    else -> JoystickPosition.Neutral
                }
            }.distinctUntilChanged()
        } else {
            return flowOf(JoystickPosition.Neutral).distinctUntilChanged()
        }
    }

    override fun getJoystickRapidState(): Flow<Boolean> {
        return pinJoystickRapid?.valueFlow(RefreshRate)?.distinctUntilChanged() ?: flowOf(false)
    }

    override fun setPowerFeedingStatus(isActive: Boolean) {
        pinIsPowerFeeding?.setPinValue(isActive)
    }

    override fun getSpindleSwitchStatus(): Flow<SpindleSwitchStatus> {
        return combine(
            pinSpindleSwitchRevIn!!.valueFlow(RefreshRate),
            pinSpindleSwitchFwdIn!!.valueFlow(RefreshRate)
        ) { isRev, isFwd ->
            when {
                isRev -> SpindleSwitchStatus.REV
                isFwd -> SpindleSwitchStatus.FWD
                else -> SpindleSwitchStatus.NEUTRAL
            }
        }.distinctUntilChanged()
    }

    override fun getCycleStartStatus(): Flow<Boolean> {
        return pinCycleStart?.valueFlow(RefreshRate)?.distinctUntilChanged() ?: flowOf(false)
    }

    override fun getCycleStopStatus(): Flow<Boolean> {
        return pinCycleStop?.valueFlow(RefreshRate)?.distinctUntilChanged() ?: flowOf(false)
    }

    override fun actualSpindleSpeed(): Flow<Float> {
        return pinSpindleActualRpm?.valueFlow(RefreshRate)?.distinctUntilChanged() ?: flowOf(0.0f)
    }

    override fun allowSpindleOperation(allowed: Boolean) {
        this.allowSpindleOperation = allowed
    }

    private fun handleForwarding() {
        pinSpindleSwitchRevIn?.let {
            it.valueFlow(RefreshRate)
                .filter { allowSpindleOperation }
                .onEach { value -> pinSpindleSwitchRevOut?.setPinValue(value) }
                .flowOn(Dispatchers.IO)
                .launchIn(scope)
        }

        pinSpindleSwitchFwdIn?.let {
            it.valueFlow(RefreshRate)
                .filter { allowSpindleOperation }
                .onEach { value -> pinSpindleSwitchFwdOut?.setPinValue(value) }
                .flowOn(Dispatchers.IO)
                .launchIn(scope)
        }
    }
}