package org.techtown.seminar2.data.entry.search.lotto

import com.google.gson.annotations.SerializedName

data class ResponseLottoNum(
    @SerializedName("returnValue")
    val returnValue: String,
    @SerializedName("drwNoDate")
    val date: String,
    @SerializedName("drwtNo1")
    val lottoNum_one: String,
    @SerializedName("drwtNo2")
    val lottoNum_two: String,
    @SerializedName("drwtNo3")
    val lottoNum_three: String,
    @SerializedName("drwtNo4")
    val lottoNum_four: String,
    @SerializedName("drwtNo5")
    val lottoNum_five: String,
    @SerializedName("drwtNo6")
    val lottoNum_six: String,
    @SerializedName("bnusNo")
    val bonus_num: String
)
