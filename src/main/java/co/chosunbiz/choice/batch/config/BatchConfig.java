package co.chosunbiz.choice.batch.config;

import co.chosunbiz.choice.batch.job.JsonProcessor;
import co.chosunbiz.choice.batch.job.JsonWrite;
import co.chosunbiz.choice.batch.service.BatchService;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.json.GsonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private BatchService batchService;

    @Bean
    public Job youtubeJob() throws Exception {
        return jobBuilderFactory.get("youtubeJob")
                .incrementer(new RunIdIncrementer())
//                .listener(listener)
                .flow(saveStep())
                .end()
                .build();
    }

    @Bean
    public Step saveStep() throws Exception {
//        log.debug(channelId);
        return this.stepBuilderFactory.get("saveStep")
                .<BasicDBObject, BasicDBObject>chunk(10)
                .reader(jsonItemReader())
                .processor(processor())
                .writer(writer())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<BasicDBObject> jsonItemReader() throws Exception {
        log.debug("-------------Into the reader-------");
        Gson gson = new Gson();
        // configure gson as required
        GsonJsonObjectReader<BasicDBObject> jsonObjectReader = new GsonJsonObjectReader<>(BasicDBObject.class);

        String channelId[] = {"UCdp4_yTBhQmB8E339Lafzow", "UCEE10-s88CeOCDzNMLhsblw", "UCWlV3Lz_55UaX4JsMj-z__Q"};
        String jsonArrayItems = batchService.getChannelItem(channelId);
        Resource resource = new ByteArrayResource(jsonArrayItems.getBytes());

        jsonObjectReader.setMapper(gson);

        JsonItemReaderBuilder<BasicDBObject> jsonItemReaderBuilder = new JsonItemReaderBuilder<BasicDBObject>();
        jsonItemReaderBuilder.jsonObjectReader(jsonObjectReader);
        jsonItemReaderBuilder.resource(resource);
        jsonItemReaderBuilder.name("jsonItemReader");

        return jsonItemReaderBuilder.build();
//        return new JsonItemReaderBuilder<BasicDBObject>()
//                .jsonObjectReader(jsonObjectReader)
//                .resource(resource)
//                .name("jsonItemReader")
//                .build();
    }

    @Bean
    @StepScope
    public JsonProcessor processor() {
        log.debug("-------------Into the processor-------");
        return new JsonProcessor();
    }

    @Bean
    @StepScope
    public JsonWrite writer() {
        log.debug("-------------Into the writer-------");
        return new JsonWrite(template, "youtube");
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager) throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
        return factory.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }
}
