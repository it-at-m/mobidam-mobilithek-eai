package de.muenchen.mobidam.scheduler;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.config.Interfaces;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class SchedulerContext {

    @Bean
    public JobDetailFactoryBean parkAndRideDetail() {
        var jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ParkAndRideJob.class);
        jobDetailFactory.setDescription("Mobilithek Info Park And Ride Job");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean cronParkAndRide(JobDetail job, Interfaces properties) {
        var cron = new CronTriggerFactoryBean();
        cron.setJobDetail(job);
        cron.setGroup("Park And Ride Job");
        cron.setCronExpression(properties.getInterfaces().get(Constants.PARK_RIDE_DATA).getCronExpression());
        return cron;
    }

}
