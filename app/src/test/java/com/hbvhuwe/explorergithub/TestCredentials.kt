package com.hbvhuwe.explorergithub

import com.hbvhuwe.explorergithub.net.Credentials
import org.junit.Assert
import org.junit.Test

class TestCredentials {
    @Test
    fun emptyCredentials() {
        val credentials = Credentials("", "")
        Assert.assertTrue(credentials.isEmpty())
        Assert.assertTrue(Credentials.empty().isEmpty())
    }
    @Test
    fun nonEmptyCredentials() {
        val credentials = Credentials("abcdefg", "Bearer")
        Assert.assertFalse(credentials.isEmpty())
    }
}
