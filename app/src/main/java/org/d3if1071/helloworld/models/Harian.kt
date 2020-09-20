package org.d3if1071.helloworld.models

import com.squareup.moshi.Json

data class Harian (
    val key : Long,
    @Json(name="jumlah_positif") val jumlahPositif : Value,
    //penambahan model jumlah_sembuh
     val jumlah_sembuh : Value
    //jumlah_sembuh tidak memakai @Json dikarenakan nama val nya sudah sama dengan database
)