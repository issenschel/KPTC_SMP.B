package com.example.kptc_smp.service.minecraft;

import com.example.kptc_smp.entity.mySQL.ExternalWhitelist;
import com.example.kptc_smp.repository.mySQL.WhitelistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhitelistService {
    private final WhitelistRepository whitelistRepository;

    public void createWhitelist(String username) {
        ExternalWhitelist whitelist = new ExternalWhitelist();
        whitelist.setUser(username);
        whitelistRepository.save(whitelist);
    }
}
