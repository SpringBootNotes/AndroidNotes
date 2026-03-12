package com.android.login.view

import com.android.data.domain.SetIsRememberMeEnabledUseCase
import com.android.login.domain.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginUseCase: LoginUseCase = mockk()
    private val setIsRememberMeEnabledUseCase: SetIsRememberMeEnabledUseCase = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: LoginViewModel

    private val email = "test@example.com"
    private val password = "password123"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            setIsRememberMeEnabledUseCase = setIsRememberMeEnabledUseCase,
            dispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onEmailChanged called THEN email state is updated`() = runTest {
        assertNotEquals(email, viewModel.state.value.email)
        viewModel.onEmailChanged(email)
        assertEquals(email, viewModel.state.value.email)
    }

    @Test
    fun `WHEN onPasswordChanged called THEN state is updated`() = runTest {
        assertNotEquals(email, viewModel.state.value.password)
        viewModel.onPasswordChanged(password)
        assertEquals(password, viewModel.state.value.password)
    }

    @Test
    fun `WHEN onRememberMeClicked called THEN state is updated`() = runTest {
        assertFalse(viewModel.state.value.rememberMeIsChecked)
        viewModel.onRememberMeClicked(true)
        assertTrue(viewModel.state.value.rememberMeIsChecked)
    }

    @Test
    fun `WHEN onSignUpClicked called THEN NavigateToSignUp event sent`() = runTest {
        viewModel.onSignUpClicked()

        val event = viewModel.events.first()
        assertEquals(LoginEvents.NavigateToSignUp, event)
    }

    @Test
    fun `GIVEN LoginUseCase returns success WHEN onLoginButtonClicked called THEN setIsRememberMeEnabledUseCase called and NavigateToNotes event sent`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.Success
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.onLoginButtonClicked()

        coVerify { setIsRememberMeEnabledUseCase(isEnabled = false) }
        val event = viewModel.events.first()
        assertEquals(LoginEvents.NavigateToNotes, event)
    }

    @Test
    fun `GIVEN LoginUseCase returns InvalidCredentialsError WHEN onLoginButtonClicked called THEN showInvalidCredentialsErrorDialog set to true`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.InvalidCredentialsError
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        assertFalse(viewModel.state.value.showInvalidCredentialsErrorDialog)

        viewModel.onLoginButtonClicked()

        assertTrue(viewModel.state.value.showInvalidCredentialsErrorDialog)
    }

    @Test
    fun `GIVEN LoginUseCase returns NetworkError WHEN onLoginButtonClicked called THEN showNetworkErrorDialog set to true`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.NetworkError
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        assertFalse(viewModel.state.value.showNetworkErrorDialog)

        viewModel.onLoginButtonClicked()

        assertTrue(viewModel.state.value.showNetworkErrorDialog)
    }

    @Test
    fun `GIVEN LoginUseCase returns GenericError WHEN onLoginButtonClicked called THEN showNetworkErrorDialog set to true`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.GenericError
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        assertFalse(viewModel.state.value.showGenericErrorDialog)

        viewModel.onLoginButtonClicked()

        assertTrue(viewModel.state.value.showGenericErrorDialog)
    }

    @Test
    fun `WHEN dismissInvalidCredentialsErrorDialog called THEN showInvalidCredentialsErrorDialog set to false`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.InvalidCredentialsError
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.onLoginButtonClicked()
        assertTrue(viewModel.state.value.showInvalidCredentialsErrorDialog)

        viewModel.dismissInvalidCredentialsErrorDialog()
        assertFalse(viewModel.state.value.showInvalidCredentialsErrorDialog)
    }

    @Test
    fun `WHEN dismissNetworkErrorDialog called THEN showNetworkErrorDialog set to false`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.NetworkError
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.onLoginButtonClicked()
        assertTrue(viewModel.state.value.showNetworkErrorDialog)

        viewModel.dismissNetworkErrorDialog()
        assertFalse(viewModel.state.value.showNetworkErrorDialog)
    }

    @Test
    fun `WHEN dismissGenericErrorDialog called THEN showGenericErrorDialog set to false`() = runTest {
        coEvery { loginUseCase(email, password) } returns LoginUseCase.LoginResult.GenericError
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)


        viewModel.onLoginButtonClicked()
        assertTrue(viewModel.state.value.showGenericErrorDialog)

        viewModel.dismissGenericErrorDialog()
        assertFalse(viewModel.state.value.showGenericErrorDialog)
    }
}
