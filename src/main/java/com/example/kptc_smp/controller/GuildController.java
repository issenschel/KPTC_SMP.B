package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.entity.Order;
import com.example.kptc_smp.service.GuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GuildController {
    private final GuildService guildService;


    @PostMapping("/createNewOrder")
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(guildService.createNewOrder(orderDto));
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(@RequestParam(name = "page") int page) {
        return ResponseEntity.ok(guildService.getOrders(page));
    }

    @PostMapping("/changeOrder")
    public ResponseEntity<?> changeOrder(@RequestParam(name = "id") int id, @Valid @RequestBody OrderDto orderDto) {
        Order order = guildService.changeOrder(orderDto, id);
        if (order == null) {
            return ResponseEntity.badRequest().body(orderDto);
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<?> deleteOrder(@RequestParam(name = "id") int id) {
        boolean bool = guildService.deleteOrder(id);
        if (!bool) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Заказ не найден " + id);
        }
        return ResponseEntity.ok().build();
    }
}
