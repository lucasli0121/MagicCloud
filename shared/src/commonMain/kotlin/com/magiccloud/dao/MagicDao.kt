package com.magiccloud.dao

expect class ByteBuf

enum class MagicType {
    MagicEd719Type,  MagicH03Type
}
abstract class MagicDao {
    // 解析函数，需要继承覆盖
    open fun decode(buf: ByteBuf) {}
}