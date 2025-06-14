package com.magiccloud.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.ui.screen.BleSearchScreen
import com.magiccloud.ui.screen.BleSettingWifiScreen
import com.magiccloud.ui.theme.MagicTheme
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import magiccloud.shared.generated.resources.Res
import magiccloud.shared.generated.resources.app_name
import magiccloud.shared.generated.resources.back
import magiccloud.shared.generated.resources.ble_search
import magiccloud.shared.generated.resources.device_set_wifi
import magiccloud.shared.generated.resources.radar_cloud
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 文件名：MagicApp.kt
 * 描述： 界面入口，通过MagicApp函数实现页面导航，可以通过NavController实现页面跳转
 * 作者：liguoqiang
 * 日期：2025/3/22
 */

enum class MagicNavScreen(val title: StringResource) {
    // 定义首页导航
    Main(title = Res.string.app_name),
    // 蓝牙搜索导航
    BleSearch(title = Res.string.ble_search),
    // 蓝牙配置wifi导航
    BleSetWifi(title = Res.string.device_set_wifi)
}


@Composable
fun MagicApp(
    navController: NavHostController = rememberNavController()
) {
    val selectAdvertisement = remember { MutableStateFlow<List<PlatformAdvertisement>>(emptyList()) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MagicNavScreen.valueOf(
        backStackEntry?.destination?.route ?: MagicNavScreen.Main.name
    )
    MagicTheme {
        Scaffold(
            topBar = {
                AppBar(currentScreen, navController)
            },
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = MagicNavScreen.Main.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ){
                    // 跳转到首页
                    composable(route = MagicNavScreen.Main.name) {

                    }
                    //
                    composable(route = MagicNavScreen.BleSearch.name) {
                        BleSearchScreen(
                            onNextClicked = { advertisement ->
                                selectAdvertisement.value = listOf(advertisement)
                                navController.navigate (MagicNavScreen.BleSetWifi.name )
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    composable ( route = MagicNavScreen.BleSetWifi.name) {
                        Napier.i("enter MagicNavScreen.BleSetWifi.name")
                        if (selectAdvertisement.value.isNotEmpty()) {
                            Napier.i(selectAdvertisement.value[0].name.toString())
                            BleSettingWifiScreen(
                                selectAdvertisement.value[0],
                                modifier = Modifier.fillMaxSize()
                            ) {

                            }
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    currentScreen: MagicNavScreen,
    naviController: NavHostController,
    modifier: Modifier = Modifier
) {
    val canNaviBack = naviController.previousBackStackEntry != null
    val currRoute = naviController.currentDestination?.route
    val isMainScreen = currRoute == null || currRoute == MagicNavScreen.Main.name
    TopAppBar(
        title = {
            Text(
                stringResource(currentScreen.title),
                color = MagicTheme.colors.colorScheme.onPrimaryContainer)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = MagicTheme.colors.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNaviBack) {
                IconButton(onClick = {naviController.navigateUp()}) {
                    Icon (
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back)
                    )
                }
            }
        },
        actions = {
            if (isMainScreen) {
                IconButton(
                    onClick = {
                        naviController.navigate(MagicNavScreen.BleSearch.name)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bluetooth,
                        contentDescription = stringResource(Res.string.radar_cloud)
                    )
                }
            }
        }
    )
}

/*
* function: cancelAndNavigateToMain
* desc: 返回到主界面
* */
private fun cancelAndNavigateToMain(navController: NavHostController) {
    navController.popBackStack(MagicNavScreen.Main.name, inclusive = false)
}

