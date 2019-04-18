package co.chosunbiz.choice.batch.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class AppScheduler {

    private static final Logger log = LoggerFactory.getLogger(AppScheduler.class);

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(cron = "*/60 * * * * *")
    public void work() throws Exception {
        log.info("Job Started at : {}", new Date());
        JobParameters param = new JobParametersBuilder().addString("now",System.currentTimeMillis() + "").toJobParameters();
        JobExecution execution = jobLauncher.run(job, param);
        log.info("Job finished with status : {}", execution.getStatus());
    }
}
