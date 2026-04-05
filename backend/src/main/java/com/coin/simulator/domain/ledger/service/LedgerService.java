package com.coin.simulator.domain.ledger.service;

import com.coin.simulator.domain.ledger.dto.LedgerHistoryResponse;
import com.coin.simulator.domain.ledger.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    @Transactional(readOnly = true)
    public Page<LedgerHistoryResponse> getHistory(Long userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ledgerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(LedgerHistoryResponse::from);
    }
}
