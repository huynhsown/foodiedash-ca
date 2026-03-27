package com.ute.foodiedash.interfaces.rest.payment.vnpay;

import com.ute.foodiedash.application.payment.vnpay.command.HandleVnpayReturnCommand;
import com.ute.foodiedash.application.payment.vnpay.query.VnpayReturnResult;
import com.ute.foodiedash.application.payment.vnpay.usecase.HandleVnpayReturnUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/vnpay")
@RequiredArgsConstructor
public class VnpayReturnController {
    private final HandleVnpayReturnUseCase handleVnpayReturnUseCase;

    @GetMapping("/return")
    public ResponseEntity<?> returnUrl(@RequestParam Map<String, String> params) {
        VnpayReturnResult result = handleVnpayReturnUseCase.execute(new HandleVnpayReturnCommand(params));
        if (result.success()) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:4200/checkout/success"))
                    .build();
        }
        return ResponseEntity.ok("vnpay failed");
    }
}

