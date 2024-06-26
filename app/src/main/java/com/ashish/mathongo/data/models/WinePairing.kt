package com.ashish.mathongo.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
): Parcelable