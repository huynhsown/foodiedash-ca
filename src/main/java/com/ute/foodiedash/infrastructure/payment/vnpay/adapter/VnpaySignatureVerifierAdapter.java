package com.ute.foodiedash.infrastructure.payment.vnpay.adapter;

import com.ute.foodiedash.application.payment.vnpay.port.VnpaySignatureVerifierPort;
import com.ute.foodiedash.infrastructure.payment.vnpay.config.VnpayProperties;
import com.ute.foodiedash.infrastructure.payment.vnpay.utils.VnpayUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VnpaySignatureVerifierAdapter implements VnpaySignatureVerifierPort {
    private final VnpayProperties props;

    @Override
    public boolean verifySecureHash(Map<String, String> params) {
        if (params == null) {
            return false;
        }

        String secureHash = params.get("vnp_SecureHash");
        if (secureHash == null || secureHash.isBlank()) {
            return false;
        }

        Map<String, String> hashParams = new HashMap<>(params);
        hashParams.remove("vnp_SecureHash");

        String hashData = VnpayUtils.buildHashData(hashParams);
        String expectedHash = VnpayUtils.hmacSHA512(props.getHashSecret(), hashData);

        return expectedHash != null && expectedHash.equalsIgnoreCase(secureHash);
    }
}

