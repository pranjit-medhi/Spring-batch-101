package batch.config;

import batch.customer.CustomerEntity;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<CustomerEntity,CustomerEntity> {
    @Override
    public CustomerEntity process(CustomerEntity item) throws Exception {
        return item;
    }
}
