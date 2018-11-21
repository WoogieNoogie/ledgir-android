package com.ledgir.android.model

data class User(
    var accountId: String? = null,
    var deviceIds: List<String>? = null,
    var emailAddress: String? = null,
    var externalId: String? = null,
    var externalSource: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var guid: String? = null,
    var pushTokens: List<String>? = null
)