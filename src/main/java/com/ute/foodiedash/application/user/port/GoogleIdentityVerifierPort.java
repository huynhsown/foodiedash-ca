package com.ute.foodiedash.application.user.port;

import com.ute.foodiedash.application.user.query.GoogleIdentityQueryResult;

public interface GoogleIdentityVerifierPort {
    GoogleIdentityQueryResult verifyIdToken(String idToken);
}

