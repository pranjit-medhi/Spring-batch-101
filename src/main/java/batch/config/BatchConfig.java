package batch.config;

import batch.customer.CustomerEntity;
import batch.customer.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private CustomerRepository customerRepository;

    @Autowired
    public BatchConfig(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Bean
    public Job jobBean(JobRepository jobRepository, Step steps){
        return new JobBuilder("jobA", jobRepository)
                .start(steps)
                .build();

    }

    @Bean
    public Step steps(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<CustomerEntity> reader,
                      ItemProcessor<CustomerEntity, CustomerEntity>  itemProcessor , ItemWriter<CustomerEntity> itemWriter ) {
        return new StepBuilder("step", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(10, transactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
    @Bean
    public FlatFileItemReader<CustomerEntity> reader()
    {
        return new FlatFileItemReaderBuilder<CustomerEntity>()
                .name("itemReader")
                .resource(new ClassPathResource("customers.csv"))
                .delimited()
                .names("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob")
                .targetType(CustomerEntity.class)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity>  itemProcessor()
    {
        return new CustomerProcessor();

    }

    @Bean
    public RepositoryItemWriter<CustomerEntity> writeCsv(){
        RepositoryItemWriter<CustomerEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");
        return writer;
    }

}
