package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.service.GuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Guild")
public class GuildController {
    private final GuildService guildService;


    @PostMapping("/createNewOrder")
    @Operation(summary = "Создание заказов")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказ добавлен",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Вы не авторизованы",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    )
            })
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(guildService.createNewOrder(orderDto));
    }

    @GetMapping("/getOrders")
    @Operation(summary = "Получение заказов")
    @ApiResponses(
            {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список заказов получен",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> getOrders(@RequestParam(name = "page") int page) {
        return ResponseEntity.ok(guildService.getOrders(page));
    }

    @PutMapping("/changeOrder")
    @Operation(summary = "Изменений заказа")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказ изменен",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Вы не авторизованы",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Заказ не найден",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
            })
    public ResponseEntity<?> changeOrder(@RequestParam(name = "id") int id, @Valid @RequestBody OrderDto orderDto) {
        guildService.changeOrder(orderDto, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteOrder")
    @Operation(summary = "Удаление заказа")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заказ удален",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Заказ не найден",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> deleteOrder(@RequestParam(name = "id") int id) {
        guildService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
