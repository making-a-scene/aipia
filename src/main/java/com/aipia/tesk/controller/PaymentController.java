package com.aipia.tesk.controller;

import com.aipia.tesk.model.Payment;
import com.aipia.tesk.dto.PaymentResponseDto;
import com.aipia.tesk.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestParam Long orderId) {
        Payment payment = paymentService.createPayment(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentResponseDto.from(payment));
    }

    @PostMapping("/{paymentId}/perform")
    public ResponseEntity<Void> performPayment(@PathVariable Long paymentId) {
        paymentService.performPayment(paymentId);
        return ResponseEntity.ok().build();
    }
}