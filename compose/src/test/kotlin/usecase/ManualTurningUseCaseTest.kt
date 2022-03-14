package usecase

import com.mindovercnc.base.*
import com.mindovercnc.base.data.CncStatus
import com.mindovercnc.base.data.JoystickStatus
import com.mindovercnc.base.data.SpindleSwitchStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ManualTurningUseCaseTest {
    private val joystickStatusFlow = MutableStateFlow(JoystickStatus(JoystickStatus.Position.Neutral))
    private val spindleSwitchStatusFlow = MutableStateFlow(SpindleSwitchStatus.NEUTRAL)
    private val cycleStopStatusFlow = MutableStateFlow(false)
    private val actualSpindleSpeed = MutableStateFlow(1f)
    private val cncStatusFlow = MutableSharedFlow<CncStatus>()

    private val statusRepository = mockk<CncStatusRepository>() {
        every { cncStatusFlow() } returns cncStatusFlow
    }
    private val commandRepository = mockk<CncCommandRepository>()
    private val messagesRepository = mockk<MessagesRepository>()
    private val halRepository = mockk<HalRepository>() {
        every { getJoystickStatus() } returns joystickStatusFlow
        every { getSpindleSwitchStatus() } returns spindleSwitchStatusFlow
        every { getCycleStopStatus() } returns cycleStopStatusFlow
        every { actualSpindleSpeed() } returns actualSpindleSpeed
    }
    private val settingsRepository = mockk<SettingsRepository>()
    private val iniFileRepository = mockk<IniFileRepository>()

    private lateinit var testSubject: ManualTurningUseCase

    //cncStatusFlow.tryEmit(cncStatus)


    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val coroutineScope = CoroutineScope(mainThreadSurrogate)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        testSubject =
            ManualTurningUseCase(coroutineScope, statusRepository, commandRepository, messagesRepository, halRepository, settingsRepository, iniFileRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `Test Something`() {

        assert(1 + 1 == 2)

    }
}