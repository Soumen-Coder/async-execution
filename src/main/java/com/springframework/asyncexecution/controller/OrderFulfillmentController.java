package com.springframework.asyncexecution.controller;

import com.springframework.asyncexecution.dto.Order;
import com.springframework.asyncexecution.service.OrderFulfillmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderFulfillmentController {
    private final OrderFulfillmentService orderFulfillmentService;

    public OrderFulfillmentController(OrderFulfillmentService orderFulfillmentService) {
        this.orderFulfillmentService = orderFulfillmentService;
    }

    @PostMapping
    public ResponseEntity<Order> processOrder(@RequestBody Order order) throws InterruptedException {
        orderFulfillmentService.processOrder(order); //synchronous
        // asynchronous
        orderFulfillmentService.notifyUser(order);
        orderFulfillmentService.assignVendor(order);
        orderFulfillmentService.packaging(order);
        orderFulfillmentService.assignDeliveryPartner(order);
        orderFulfillmentService.assignTrailerAndDispatch(order);
        return ResponseEntity.ok(order);
    }
}
