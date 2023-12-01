package com.springframework.asyncexecution.controller;

import com.springframework.asyncexecution.dto.Order;
import com.springframework.asyncexecution.service.OrderFulfillmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderFulfillmentController {
    private final OrderFulfillmentService orderFulfillmentService;

    public OrderFulfillmentController(OrderFulfillmentService orderFulfillmentService) {
        this.orderFulfillmentService = orderFulfillmentService;
    }

    @PostMapping
    public ResponseEntity<Order> processOrder(@RequestBody Order order) throws InterruptedException {
        // Initial synchronous processing (up to payment)
        Order processedOrder = orderFulfillmentService.processOrder(order);

        /* Trigger asynchronous processing for the remaining steps, but this will not be in order
        CompletableFuture.runAsync(() -> {
            orderFulfillmentService.notifyUser(processedOrder);
            orderFulfillmentService.assignVendor(processedOrder);
            orderFulfillmentService.packaging(processedOrder);
            orderFulfillmentService.assignDeliveryPartner(processedOrder);
            orderFulfillmentService.assignTrailerAndDispatch(processedOrder);
        });*/

        // if we want things to run in order then we have to use the theRun() method
        CompletableFuture<Void> asyncProcessing = CompletableFuture.completedFuture(null); //manually completed things
        asyncProcessing
                .thenRun(() -> orderFulfillmentService.notifyUser(processedOrder))
                .thenRun(() -> orderFulfillmentService.assignVendor(processedOrder))
                .thenRun(() -> orderFulfillmentService.packaging(processedOrder))
                .thenRun(() -> orderFulfillmentService.assignDeliveryPartner(processedOrder))
                .thenRun(() -> orderFulfillmentService.assignTrailerAndDispatch(processedOrder));
        // Handle exceptions if needed
        asyncProcessing.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        return ResponseEntity.ok(processedOrder);
    }
}
