package yjh.devtoon.cookie_wallet.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;

public interface CookieWalletRepository extends JpaRepository<CookieWalletEntity, Long> {
}