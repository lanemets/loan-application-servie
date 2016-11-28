package my.homework;

import my.homework.settings.ThreadPoolSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class LoanManagerApplication extends AsyncConfigurerSupport {

    private final ThreadPoolSettings threadPoolSettings;

    public LoanManagerApplication(ThreadPoolSettings threadPoolSettings) {
        this.threadPoolSettings = threadPoolSettings;
    }

    public static void main(String[] args) {
        SpringApplication.run(LoanManagerApplication.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSettings.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolSettings.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolSettings.getQueueCapacity());
        executor.setThreadNamePrefix(threadPoolSettings.getThreadNamePrefix());
        executor.initialize();

        return executor;
    }

}
