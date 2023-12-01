package com.springframework.asyncexecution.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryService {
    public boolean checkProductInventory(int productId){
        log.info("The product with id - {} is present in the inventory ", productId);
        return true;
    }
}
