package com.example.kptc_smp.service.minecraft;

import com.example.kptc_smp.entity.minecraft.Whitelist;
import com.example.kptc_smp.repository.minecraft.WhitelistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WhitelistService {
    private final WhitelistRepository whitelistRepository;

    @Transactional(transactionManager = "mySQLTransactionManager")
    public void createWhitelist(String username) {
        Whitelist whitelist = new Whitelist();
        whitelist.setUser(username);
        whitelistRepository.save(whitelist);
    }
}
