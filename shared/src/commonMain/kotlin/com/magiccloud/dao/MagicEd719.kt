package com.magiccloud.dao

class MagicEd719 : MagicDao(){
    var frameHeader: PackageHeader = PackageHeader()
    companion object {
        val packHeaderMark: Int = 0x7c7c5a77
        val packEndMark: Int = 0x7c7c5aee
        val magicHeaderMark: Int = 0x7c7c5a0f
        val magicEndMark: Int = 0x7c7c5af0
        val trackHeaderMark: Int = 0x7c7c5a0e
        val trackEndMark: Int = 0x7c7c5ae0
    }
    inner class PackageHeader {
        var sn: Int = 0
        var mac: String = ""
        var dyHighSignalNums: Int = 0
        var dylowSignalNums: Int = 0
        var trackNums: Int = 0
        var longHighSignalNums: Int = 0
        var longLowSignalNums: Int = 0
        var shortHighSignalNums: Int = 0
        var shortLowSignalNums: Int = 0
        var frameInterval: Int = 0
    }
    inner class CloudPackage {
        var dyHighSignalXArray: IntArray = IntArray(0)
        var dyHighSignalYArray: IntArray = IntArray(0)
        var dyHighSignalZArray: IntArray = IntArray(0)
        var dyLowSignalXArray: IntArray = IntArray(0)
        var dyLowSignalYArray: IntArray = IntArray(0)
        var dyLowSignalZArray: IntArray = IntArray(0)
    }

    // 实现解析函数
    override fun decode(buf: ByteBuf) {

    }
}
