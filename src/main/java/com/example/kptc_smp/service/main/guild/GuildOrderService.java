package com.example.kptc_smp.service.main.guild;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.guild.GuildOrderGroupResponseDto;
import com.example.kptc_smp.dto.guild.GuildOrderRequestDto;
import com.example.kptc_smp.model.main.GuildOrder;
import com.example.kptc_smp.exception.guild.OrderNotFoundException;
import com.example.kptc_smp.repository.main.GuildOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildOrderService {
    private final GuildOrderRepository guildOrderRepository;

    @Value("${message.order.deleted}")
    private String orderDeletedMessage;

    @Value("${page.guild.order.size}")
    private int pageSize;

    public GuildOrder createNewOrder(GuildOrderRequestDto guildOrderRequestDto) {
        GuildOrder guildOrder = new GuildOrder();
        guildOrder.setHeader(guildOrderRequestDto.getHeader());
        guildOrder.setMessage(guildOrderRequestDto.getMessage());
        guildOrder.setPseudonym(guildOrderRequestDto.getPseudonym());
        return guildOrderRepository.save(guildOrder);
    }

    public GuildOrder changeOrder(GuildOrderRequestDto guildOrderRequestDto, int id) {
        GuildOrder guildOrder = guildOrderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        guildOrder.setHeader(guildOrderRequestDto.getHeader());
        guildOrder.setMessage(guildOrderRequestDto.getMessage());
        guildOrder.setPseudonym(guildOrderRequestDto.getPseudonym());
        guildOrderRepository.save(guildOrder);
        return guildOrder;
    }

    public ResponseDto deleteOrder(int id) {
        GuildOrder guildOrder = guildOrderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        guildOrderRepository.delete(guildOrder);
        return new ResponseDto(orderDeletedMessage);
    }

    public GuildOrderGroupResponseDto getOrders(int page) {
        GuildOrderGroupResponseDto guildOrderGroupResponseDto = new GuildOrderGroupResponseDto();
        PageRequest pageRequest = PageRequest.of(page-1, pageSize);
        Page<GuildOrder> ordersPage = guildOrderRepository.findAll(pageRequest);
        int totalPages = ordersPage.getTotalPages();
        guildOrderGroupResponseDto.setGuildOrders(ordersPage.getContent());
        guildOrderGroupResponseDto.setCountPage(totalPages);
        return guildOrderGroupResponseDto;
    }
}
