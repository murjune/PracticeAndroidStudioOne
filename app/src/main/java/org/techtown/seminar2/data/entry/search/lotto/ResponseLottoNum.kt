package org.techtown.seminar2.data.entry.search.lotto

import com.google.gson.annotations.SerializedName

data class ResponseLottoNum(
    @SerializedName("returnValue")
    val returnValue: String,
    @SerializedName("drwNo1")
    val lottoNum_one: Int,
    @SerializedName("drwtNo2")
    val lottoNum_two: Int,
    @SerializedName("drwtNo3")
    val lottoNum_three: Int,
    @SerializedName("drwtNo4")
    val lottoNum_four: Int,
    @SerializedName("drwtNo5")
    val lottoNum_five: Int,
    @SerializedName("drwtNo6")
    val lottoNum_six: Int,
)
