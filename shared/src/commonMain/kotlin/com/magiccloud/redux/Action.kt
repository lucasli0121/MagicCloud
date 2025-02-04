package com.magiccloud.redux

import com.juul.kable.PlatformAdvertisement

/*
    定义redux风格的action
    即，根据不同action输出不同状态
 */
sealed interface Action {
    data class BleAdvertisment(val advertisement: PlatformAdvertisement) : Action
}

interface State {
    data class BleState(
        val advertisements: List<PlatformAdvertisement> = emptyList()
    ) : State
}

fun stateReducer(state: State, action: Action): State =
    when(action) {
        is Action.BleAdvertisment -> {
            (state as State.BleState).copy (
                advertisements = state.advertisements + action.advertisement
            )
        }
    }