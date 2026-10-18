package dto;

import java.math.BigDecimal;

public record WalletBalanceUpdateDto(
        BigDecimal totalBalance,
        BigDecimal prizePool,
        String currency
) {
}
