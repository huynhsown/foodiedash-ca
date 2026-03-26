package com.ute.foodiedash.application.paymentmethod.factory;

import com.ute.foodiedash.application.paymentmethod.port.PaymentPort;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentGatewayFactory {
    private final Map<PaymentMethodCode, PaymentPort> ports;

    public PaymentGatewayFactory(List<PaymentPort> portList) {
        this.ports = portList.stream()
                .collect(Collectors.toMap(PaymentPort::getCode, g -> g));
    }

    public PaymentPort get(PaymentMethodCode code) {
        PaymentPort port = ports.get(code);
        if (port == null) {
            throw new IllegalArgumentException("Unsupported payment method");
        }
        return port;
    }
}
