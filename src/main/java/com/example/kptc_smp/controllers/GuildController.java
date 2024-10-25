package com.example.kptc_smp.controllers;

import com.example.kptc_smp.dto.OrderDto;
import com.example.kptc_smp.service.GuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class GuildController {
    private final GuildService guildService;


    @PostMapping("/createNewOrder")
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return guildService.createNewOrder(orderDto);
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(@RequestParam(name = "page") int page) {
        return guildService.getOrders(page);
    }
}
