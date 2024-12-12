package com.example.kptc_smp.service.minecraft;

import com.example.kptc_smp.entity.mySQL.Whitelist;
import com.example.kptc_smp.repository.mySQL.WhitelistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhitelistService {
    private final WhitelistRepository whitelistRepository;

    public void createWhitelist(String username) {
        Whitelist whitelist = new Whitelist();
        whitelist.setUser(username);
        whitelistRepository.save(whitelist);
    }
}
