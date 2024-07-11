package de.muenchen.mobidam.config;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import de.muenchen.mobidam.scheduler.MobilithekJobExecute;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class InterfaceBeansConfiguration implements BeanFactoryAware {

    private static final String BEAN_NAME_TRIGGER = "interface_trigger_";
    private static final String BEAN_NAME_JOBDETAIL = "interface_jobdetail_";

    @Autowired
    Interfaces interfaces;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void onPostConstruct() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        interfaces.getInterfaces().forEach((key,value) -> {
            Trigger trigger = createTrigger(value);
            configurableBeanFactory.registerSingleton(BEAN_NAME_TRIGGER + key, trigger);
            JobDetail jobDetail = createJobDetail(value.getName()); // TODO: JobDetail wird zweimal initialisiert -> prüfen, ob nur einmal mögl.
            configurableBeanFactory.registerSingleton(BEAN_NAME_JOBDETAIL + key, jobDetail);
        });
    }
    private JobDetail createJobDetail(final String identity) {

        return JobBuilder.newJob(MobilithekJobExecute.class)
                .withIdentity(identity)
                .usingJobData(Constants.INTERFACE_TYPE, identity)
                .storeDurably()
                .build();
    }

    private Trigger createTrigger(final InterfaceDTO item) {

        return TriggerBuilder.newTrigger()
                .forJob(createJobDetail(item.getName()))
                .withIdentity(item.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(item.getCronExpression()))
                .build();
    }

}
