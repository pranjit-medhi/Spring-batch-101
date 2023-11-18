package batch.config;

import batch.customer.CustomerEntity;
import batch.customer.CustomerRepository;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

//Old class prior to spring 5 obselete

//@Configuration
//@EnableBatchProcessing
public class CsvConfig {

    private CustomerRepository customerRepository;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    private ItemReader itemReader;


    public CsvConfig(CustomerRepository customerRepository, JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader itemReader) {
        this.customerRepository = customerRepository;

        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.itemReader = itemReader;
    }

    @Bean
    public FlatFileItemReader<CustomerEntity> readCsv(){
        FlatFileItemReader<CustomerEntity> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resource/customers.csv"));
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<CustomerEntity> lineMapper() {
        DefaultLineMapper<CustomerEntity> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
        BeanWrapperFieldSetMapper<CustomerEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CustomerEntity.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor customerProcessor(){
        return new CustomerProcessor();
    }
    @Bean
    public RepositoryItemWriter<CustomerEntity>  writeCsv(){
        RepositoryItemWriter<CustomerEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");
        return writer;
    }

//    @Bean
//    public StepBuilder step(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader itemReader){
//        return new StepBuilder("step", jobRepository, itemReader)
//                .<String, CustomerEntity>chunk(10, transactionManager)
//                .writer(writeCsv())
////                .reader(readCsv())
//                .processor(customerProcessor())
//                .writer(writeCsv())
//                .build();
    }




