package com.hbvhuwe.explorergithub.model

import java.io.Serializable

data class Branch(
        val name: String,
        val commit: Commit
) : Serializable