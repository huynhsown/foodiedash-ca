package com.ute.foodiedash.application.user.query;

import java.util.List;

public record CustomerAddressesQueryResult(
        List<CustomerAddressQueryResult> addresses
) {
}

