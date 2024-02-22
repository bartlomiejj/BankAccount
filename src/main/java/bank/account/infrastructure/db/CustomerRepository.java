package bank.account.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByPesel(String pesel);

    Optional<CustomerEntity> findByAccountsAccountNumber(Long accountNumber);
}
