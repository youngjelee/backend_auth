package com.backend.ljy.payment.entity;

import com.backend.ljy.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String status = "PENDING";
}
