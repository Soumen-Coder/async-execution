package com.springframework.asyncexecution.service;

import com.springframework.asyncexecution.dto.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderFulfillmentService {
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    public OrderFulfillmentService(InventoryService inventoryService, PaymentService paymentService) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    /* All Required process */
    /*
      1. Inventory service check order availability
      2. Process payment for order
      3. Notify to the user
      3. Assign to vendor
      4. packaging
      5. assign delivery partner
      6. assign trailer
      7. dispatch product
      **/

    /*@Async has two limitations:

    It must be applied to public methods only.
    Self-invocation — calling the async method from within the same class — won’t work.
    The reasons are simple: The method needs to be public so that it can be proxied.
    And self-invocation doesn't work because it bypasses the proxy and calls the underlying method directly.*/

    /* public CompletableFuture<Order> processOrder(Order order) throws InterruptedException {
         order.setTrackingId(UUID.randomUUID().toString());
         return CompletableFuture.supplyAsync(() -> {
             if (inventoryService.checkProductInventory(order.getProductId())) {
                 try {
                     paymentService.processPayment(order);
                 } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                 }
             } else {
                 throw new RuntimeException("Technical issue please retry");
             }
             return order;
         });
    }*/

    public Order processOrder(Order order) throws InterruptedException {
        order.setTrackingId(UUID.randomUUID().toString());
            if (inventoryService.checkProductInventory(order.getProductId())) {
                try {
                    paymentService.processPayment(order);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Technical issue please retry");
            }
            return order;
    }

    //Earlier the below public methods had the @async annotations on them
    public CompletableFuture<Void> notifyUser(Order order) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(4000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("Notified to the user " + Thread.currentThread().getName());
        });
    }
    public CompletableFuture<Void> assignVendor(Order order) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5000L);
                log.info("Assign order to vendor " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException("Vendor assignment interrupted", e);
            }
        });
    }

    public CompletableFuture<Void> packaging(Order order) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000L);
                log.info("Order packaging completed " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException("Packaging interrupted", e);
            }
        });
    }

    public CompletableFuture<Void> assignDeliveryPartner(Order order) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10000L);
                log.info("Delivery partner assigned " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException("Delivery partner assignment interrupted", e);
            }
        });
    }

    public CompletableFuture<Void> assignTrailerAndDispatch(Order order) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000L);
                log.info("Trailer assigned and Order dispatched " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException("Trailer assignment interrupted", e);
            }
        });
    }
}
