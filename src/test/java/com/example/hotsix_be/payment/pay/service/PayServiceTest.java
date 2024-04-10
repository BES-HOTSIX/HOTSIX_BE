package com.example.hotsix_be.payment.pay.service;

import com.example.hotsix_be.coupon.service.CouponService;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.repository.PayRepository;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {
    @InjectMocks
    private PayService payService;

    @Mock
    private PayRepository payRepository;

    @Mock
    private CashLogService cashLogService;

    @Mock
    private RechargeService rechargeService;

    @Mock
    private TossService tossService;

    @Mock
    private CouponService couponService;


}
