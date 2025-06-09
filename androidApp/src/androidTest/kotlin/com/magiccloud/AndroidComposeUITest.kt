package com.magiccloud

import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.magiccloud.android.MainActivity
import com.magiccloud.android.TestActivity
import com.magiccloud.ui.theme.MagicTheme
import com.magiccloud.utils.ComposeToast
import com.magiccloud.utils.getToastUtil
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 文件名：ComposeTest
 * 描述： 测试compose在Android下的UI界面
 * 作者：liguoqiang
 * 日期：2025/3/28
 */

@RunWith(AndroidJUnit4::class)
class ComposeToastTest {
    @get:Rule
    val composeTestRule = androidx.compose.ui.test.junit4.createAndroidComposeRule<TestActivity>()

    @Test
    fun TestComposeToast() {
        composeTestRule.setContent {
            MagicTheme {
                ComposeToast("这是一个toast测试用例", show = true) {
                    assert(true)
                }
            }
        }
        composeTestRule.onNodeWithText("这是一个toast测试用例").assertExists()
    }

    @Test
    fun TestAndroidToast() {
        composeTestRule.setContent {
            MagicTheme {
                getToastUtil().showToast("这是一个toast测试用例")
            }
        }
        composeTestRule.onNodeWithText("测试通过").assertExists()
    }
}

