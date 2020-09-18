package org.d3if1071.helloworld.models

import com.squareup.moshi.Json

data class Harian (
    val key : Long,
    @Json(name="jumlah_positif") val jumlahPositif : Value,
    //penambahan model jumlah_sembuh
    @Json(name="jumlah_sembuh") val jumlahSembuh : Value
)