package com.rajankali.plasma.views.fragments

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.rajankali.core.extensions.matchParent
import com.rajankali.core.extensions.toast
import com.rajankali.plasma.composable.*
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    @Composable
    override fun setContent(){
        Box(Modifier.matchParent()) {
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center).padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Title(mutableStateOf("Plasma"))
                    columnSpacer(value = 20)
                    H6(text = "Login to Plasma")
                    columnSpacer(value = 20)
                    CenteredCaption(text = "By Logging in to Plasma, you will be able to access your events.", Modifier.padding(20.dp, 0.dp))
                    columnSpacer(value = 30)
                }
                val userNameErrorState = loginViewModel.userNameErrorLiveData.observeAsState()
                val passwordErrorState = loginViewModel.passwordErrorLiveData.observeAsState()
                val username = remember { mutableStateOf(TextFieldValue("rajan")) }

                OutlinedTextField(value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    isErrorValue = userNameErrorState.value != null,
                    errorColor = MaterialTheme.colors.error)
                Text(
                    text = userNameErrorState.value?:"",
                    textAlign = TextAlign.Start,
                    fontSize = TextUnit(12),
                    modifier = Modifier.height(userNameErrorState.value?.let { 20.dp }?:0.dp),
                    color = MaterialTheme.colors.error)
                columnSpacer(value = 16)

                val password = remember { mutableStateOf(TextFieldValue("chandana")) }
                OutlinedTextField(value = password.value,
                    onValueChange = { password.value = it},
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isErrorValue = passwordErrorState.value != null,
                    errorColor = MaterialTheme.colors.error
                )
                Text(
                    text = passwordErrorState.value?:"",
                    textAlign = TextAlign.Start,
                    fontSize = TextUnit(12),
                    modifier = Modifier.height(passwordErrorState.value?.let { 20.dp }?:0.dp),
                    color = MaterialTheme.colors.error)
                columnSpacer(value = 30)

                CardButton(text = "Login", onClick = {
                    loginViewModel.login(username = username.value.text, password = password.value.text)
                })
            }
        }
        loginViewModel.loginResultLiveData.observe(viewLifecycleOwner){
            if(it != -1L) {
                updateUser(userId = it)
                navController.navigateSafely(LoginFragmentDirections.actionLoginFragmentToHomeFragment().setLoggedInUserId(it))
            }else{
                toast("Invalid Credentials!")
            }
        }
    }
}