/*
 * The MIT License
 * Copyright © 2024 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.mobidam.config;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import de.muenchen.mobidam.scheduler.MobilithekJobExecute;
import java.util.Map;
import javax.annotation.PostConstruct;
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
        }
    }

    private JobDetail createJobDetail(Map.Entry<String, InterfaceDTO> next) {

        return JobBuilder.newJob(MobilithekJobExecute.class)
                .withIdentity(next.getKey(), SCHEDULER_GROUP)
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
