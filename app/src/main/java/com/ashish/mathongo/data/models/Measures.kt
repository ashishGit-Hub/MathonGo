package com.ashish.mathongo.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Measures(
    val metric: Metric,
    val us: Us
): Parcelable