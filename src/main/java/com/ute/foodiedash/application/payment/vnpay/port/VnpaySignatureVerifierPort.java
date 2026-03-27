package com.ute.foodiedash.application.payment.vnpay.port;

import java.util.Map;

public interface VnpaySignatureVerifierPort {
    boolean verifySecureHash(Map<String, String> params);
}

