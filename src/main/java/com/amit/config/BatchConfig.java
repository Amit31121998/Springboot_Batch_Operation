package com.amit.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.amit.entity.Customer;
import com.amit.repo.CustomerRepo;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	// item read bean

	@Bean
	public FlatFileItemReader<Customer> customerReader() {

		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));

		itemReader.setName("customer-item-reader");

		itemReader.setLinesToSkip(1);

		itemReader.setLineMapper(LineMapper());

		return itemReader;

	}

	private LineMapper<Customer> LineMapper() {

		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);

		lineTokenizer.setNames("id", "firstname", "lastname", "email", "gender", "contactNum", "country", "dob");

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<Customer>();
		fieldSetMapper.setTargetType(Customer.class);

		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(lineTokenizer);

		return lineMapper;
	}

	// item processcer bean

	@Bean
	public CustomerPorcessor customerProcessor() {

		return new CustomerPorcessor();

	}

	// item writer bean

	@Bean
	public RepositoryItemWriter<Customer> customerWriter() {

		RepositoryItemWriter<Customer> itemWriter = new RepositoryItemWriter<Customer>();
		itemWriter.setRepository(customerRepo);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

	// step bean
	@Bean
	public Step step() {

		return stepBuilderFactory.get("step-1").<Customer, Customer>chunk(10).reader(customerReader())
				.processor(customerProcessor()).writer(customerWriter()).build();

	}

	// job item

	@Bean
	public Job job() {
		return jobBuilderFactory.get("customer-import").flow(step()).end().build();
	}

}
