package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.guild.GuildOrderGroupDto;
import com.example.kptc_smp.dto.guild.GuildOrderDto;
import com.example.kptc_smp.entity.main.GuildOrder;
import com.example.kptc_smp.exception.guild.OrderNotFoundException;
import com.example.kptc_smp.repository.main.GuildOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildOrderService {
    private final GuildOrderRepository guildOrderRepository;

    public GuildOrder createNewOrder(GuildOrderDto guildOrderDto) {
        GuildOrder guildOrder = new GuildOrder();
        guildOrder.setHeader(guildOrderDto.getHeader());
        guildOrder.setMessage(guildOrderDto.getMessage());
        guildOrder.setPseudonym(guildOrderDto.getPseudonym());
        return guildOrderRepository.save(guildOrder);
    }

    public GuildOrder changeOrder(GuildOrderDto guildOrderDto, int id) {
        GuildOrder guildOrder = guildOrderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        guildOrder.setHeader(guildOrderDto.getHeader());
        guildOrder.setMessage(guildOrderDto.getMessage());
        guildOrder.setPseudonym(guildOrderDto.getPseudonym());
        guildOrderRepository.save(guildOrder);
        return guildOrder;
    }

    public ResponseDto deleteOrder(int id) {
        GuildOrder guildOrder = guildOrderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        guildOrderRepository.delete(guildOrder);
        return new ResponseDto("Заказ удален");
    }

    public GuildOrderGroupDto getOrders(int page) {
        GuildOrderGroupDto guildOrderGroupDto = new GuildOrderGroupDto();
        PageRequest pageRequest = PageRequest.of(page-1, 6);
        Page<GuildOrder> ordersPage = guildOrderRepository.findAll(pageRequest);
        int totalPages = ordersPage.getTotalPages();
        guildOrderGroupDto.setGuildOrders(ordersPage.getContent());
        guildOrderGroupDto.setCountPage(totalPages);
        return guildOrderGroupDto;
    }
}
