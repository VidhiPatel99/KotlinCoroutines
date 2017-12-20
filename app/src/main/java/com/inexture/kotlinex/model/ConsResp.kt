package com.inexture.kotlinex.model

import java.io.Serializable

/**
 * Created by Inexture on 11/27/2017.
 */
class ConsResp {
    data class ConstituencyResp(
            val status: Int, //1
            val message: String, //Success
            val data: List<Data>
    )

    data class Data(
            val consId: String, //1
            val consName: String, //Abdasa
            val consNumber: String, //1
            val consDist: String, //Kutch
            val consCategory: String,
            val consMaleVoters: String, //116028
            val consFemaleVoters: String, //107061
            val consOtherVoters: String, //0
            val consTotalVoters: String, //223089
            val consLatitude: String, //23.2590056
            val consLongitude: String //68.8321023
    )
}