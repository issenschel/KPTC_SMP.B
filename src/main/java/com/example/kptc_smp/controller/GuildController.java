package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.guild.GuildOrderRequestDto;
import com.example.kptc_smp.dto.guild.GuildOrderGroupResponseDto;
import com.example.kptc_smp.model.main.GuildOrder;
import com.example.kptc_smp.service.main.guild.GuildOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@RequestMapping("/guild")
@Validated
@Tag(name = "Guild")
public class GuildController {
    private final GuildOrderService guildOrderService;

    @PostMapping("/order")
    @Operation(summary = "Создание заказов")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ добавлен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GuildOrder.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")})
    })
    public GuildOrder createNewOrder(@Valid @RequestBody GuildOrderRequestDto guildOrderRequestDto) {
        return guildOrderService.createNewOrder(guildOrderRequestDto);
    }

    @PutMapping("/order/{id}")
    @Operation(summary = "Изменений заказа")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ изменен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GuildOrder.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Заказ не найден", content = {@Content(mediaType = "application/json")})
    })
    public GuildOrder changeOrder(@Valid @RequestBody GuildOrderRequestDto guildOrderRequestDto,
                                  @PathVariable @Min(value = 1, message = "Номер заказа должен быть не меньше 1")
                                  @Max(value = 1000000, message = "Номер заказа должен быть не больше 1000000") int id ) {
        return guildOrderService.changeOrder(guildOrderRequestDto, id);
    }

    @DeleteMapping("/order/{id}")
    @Operation(summary = "Удаление заказа")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ удален", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Заказ не найден", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto deleteOrder(@PathVariable @Min(value = 1, message = "Номер заказа должен быть не меньше 1")
                                       @Max(value = 1000000, message = "Номер заказа должен быть не больше 1000000") int id) {
        return guildOrderService.deleteOrder(id);
    }

    @GetMapping("/orders")
    @Operation(summary = "Получение заказов")
    @ApiResponse(responseCode = "200", description = "Список заказов получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = GuildOrderGroupResponseDto.class))})
    public GuildOrderGroupResponseDto getOrders(@RequestParam(name = "page", defaultValue = "1")
                                                    @Min(value = 1, message = "Номер страницы заказов должен быть не меньше 1")
                                                    @Max(value = 1000000, message = "Номер страницы заказов должен быть не больше 1000000") int page) {
        return guildOrderService.getOrders(page);
    }
}
