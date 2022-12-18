package com.kr.kwansim.ui.myfish.vo

data class FishAddVO (
    var experienceYN : String = "",
    var fishTitle : String = "",
    var fishWidth : String = "",
    var fishHeight : String = "",
    var waterHeight : String = "",
    var noticeTime : String = "",
    var startDay : String = "",
    var fishSize : String = "",
    var alert : String = ""
)
data class FishVO(
    var hwanSu: HwansuVo?,
    var fishAdd: FishAddVO?
)

data class BolVO (
    var experienceYN : String ="",
    var fishTitle : String = "",
    var fishWidth : String = "",
    var fishHeight : String = "",
    var waterHeight : String = "",
    var noticeTime : String = "",
    var startDay : String = "",
    var reWeak : String = "",
    var reCount : String = "",
    var reDay : String = "",
    var cnt : String = "",
    var alert : String = ""
)

data class HwansuVo (
    var experienceYN : String = "",
    var fishTitle : String = "",
    var fishWidth : String = "",
    var fishHeight : String = "",
    var waterHeight : String = "",
    var noticeTime : String = "",
    var startDay : String = "",
    var reWeak : String = "",
    var reCount : String = "",
    var alert : String = "",
    var reDay : String = ""
 )