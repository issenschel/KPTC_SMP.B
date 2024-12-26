package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.guild.GuildOrderDto;
import com.example.kptc_smp.dto.guild.GuildOrdersDto;
import com.example.kptc_smp.entity.postgreSQL.GuildOrder;
import com.example.kptc_smp.service.GuildOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Guild")
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@RequestMapping("/guild")
public class GuildController {
    private final GuildOrderService guildOrderService;


    @PostMapping("/order")
    @Operation(summary = "Создание заказов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ добавлен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GuildOrder.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")})
    })
    public GuildOrder createNewOrder(@Valid @RequestBody GuildOrderDto guildOrderDto) {
        return guildOrderService.createNewOrder(guildOrderDto);
    }

    @PutMapping("/order")
    @Operation(summary = "Изменений заказа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ изменен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GuildOrder.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Заказ не найден", content = {@Content(mediaType = "application/json")})
    })
    public GuildOrder changeOrder(@RequestParam(name = "id") int id, @Valid @RequestBody GuildOrderDto guildOrderDto) {
        return guildOrderService.changeOrder(guildOrderDto, id);
    }

    @DeleteMapping("/order")
    @Operation(summary = "Удаление заказа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ удален", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Заказ не найден", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto deleteOrder(@RequestParam(name = "id") int id) {
        return guildOrderService.deleteOrder(id);
    }

    @GetMapping("/orders")
    @Operation(summary = "Получение заказов")
    @ApiResponse(responseCode = "200", description = "Список заказов получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = GuildOrdersDto.class))})
    public GuildOrdersDto getOrders(@RequestParam(name = "page") int page) {
        return guildOrderService.getOrders(page);
    }
}
