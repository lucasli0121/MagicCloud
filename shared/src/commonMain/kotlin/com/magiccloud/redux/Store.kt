package com.magiccloud.redux

import com.magiccloud.redux.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/*
*  一个Redux风格的存储类，接收外部的Action，
* 并存放到channel，在存放channel中调用Action中的stateReducer函数返回对应的状态
* */
interface Store {
    fun send(action: Action)
    val stateFlow: StateFlow<State>
    val state get() = stateFlow.value
}

/*
* 为coroutineScope增加一个扩展函数
* 获取被实例化的Store对象, 这个Store用来存放Ble devices
* 在Store对象中，通过send函数向一个channel存放action
* */
fun CoroutineScope.createBleStore(): Store {
    val mutableStateFlow = MutableStateFlow(State.BleState())
    val channel : Channel<Action> = Channel(Channel.UNLIMITED)

    return object: Store {
        init {
            launch {
                channel.consumeAsFlow().collect {action ->
                    mutableStateFlow.value = stateReducer(mutableStateFlow.value, action) as State.BleState
                }
            }
        }
        override fun send(action: Action) {
            launch {
                channel.send(action)
            }
        }

        override val stateFlow: StateFlow<State>
            get() = mutableStateFlow
    }
}