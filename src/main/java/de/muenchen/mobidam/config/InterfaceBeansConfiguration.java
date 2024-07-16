package de.muenchen.mobidam.config;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import de.muenchen.mobidam.scheduler.MobilithekJobExecute;
import javax.annotation.PostConstruct;
import java.util.Map;
import org.quartz.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceBeansConfiguration implements BeanFactoryAware {

    private static final String SCHEDULER_GROUP = "MobidamMobilithekEai";
    private static final String BEAN_NAME_TRIGGER = "interface_trigger_";
    private static final String BEAN_NAME_JOBDETAIL = "interface_jobdetail_";

    public InterfaceBeansConfiguration(Interfaces interfaces) {
        this.interfaces = interfaces;
    }

    private Interfaces interfaces;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void onPostConstruct() {

        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        var iterator = interfaces.getInterfaces().entrySet().iterator();
        while (iterator.hasNext()) {
            var next = iterator.next();
             if (CronExpression.isValidExpression(next.getValue().getCronExpression())) {
                Trigger trigger = createTrigger(next);
                configurableBeanFactory.registerSingleton(BEAN_NAME_TRIGGER + next.getKey(), trigger);
                JobDetail jobDetail = createJobDetail(next);
                configurableBeanFactory.registerSingleton(BEAN_NAME_JOBDETAIL + next.getKey(), jobDetail);
             }
        };
    }

    private JobDetail createJobDetail(Map.Entry<String, InterfaceDTO> next) {

        return JobBuilder.newJob(MobilithekJobExecute.class)
                .withIdentity(next.getKey() ,SCHEDULER_GROUP)
                .usingJobData(Constants.INTERFACE_TYPE, next.getKey())
                .storeDurably()
                .build();
    }

    private Trigger createTrigger(final Map.Entry<String, InterfaceDTO> next) {

        return TriggerBuilder.newTrigger()
                .forJob(next.getKey(), SCHEDULER_GROUP)
                .withIdentity(next.getKey(), SCHEDULER_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(next.getValue().getCronExpression()))
                .build();
    }

}
