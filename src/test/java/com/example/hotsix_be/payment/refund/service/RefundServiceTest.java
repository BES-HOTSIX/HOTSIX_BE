package com.example.hotsix_be.payment.refund.service;

import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.refund.repository.RefundRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RefundServiceTest {
    @InjectMocks
    private RefundService refundService;

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private CashLogService cashLogService;


}
