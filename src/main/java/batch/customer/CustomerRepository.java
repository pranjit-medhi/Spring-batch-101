package batch.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Serializable> {
}
