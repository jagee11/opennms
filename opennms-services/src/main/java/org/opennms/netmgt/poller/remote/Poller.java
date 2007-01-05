//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
package org.opennms.netmgt.poller.remote;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.model.PollStatus;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class Poller implements InitializingBean, PollObserver, ConfigurationChangedListener {
	
	private PollerFrontEnd m_pollerFrontEnd;
	private Scheduler m_scheduler;
	private long m_initialSpreadTime = 300000L;
	
	public void setPollerFrontEnd(PollerFrontEnd pollerFrontEnd) {
		m_pollerFrontEnd = pollerFrontEnd;
	}

	public void setScheduler(Scheduler scheduler) {
		m_scheduler = scheduler;
	}
	
	public void setInitialSpreadTime(long initialSpreadTime) {
		m_initialSpreadTime = initialSpreadTime;
	}
	

	public void afterPropertiesSet() throws Exception {
		assertNotNull(m_scheduler, "scheduler");
		assertNotNull(m_pollerFrontEnd, "pollerFrontEnd");
        
        m_pollerFrontEnd.addConfigurationChangedListener(this);
		
        if (m_pollerFrontEnd.isRegistered()) {
            schedulePolls();
        } else {
            log().debug("Poller not yet registered");
        }

	}
    
    private void unschedulePolls() throws Exception {
        for (String jobName : m_scheduler.getJobNames(PollJobDetail.GROUP)) {
            m_scheduler.deleteJob(jobName, PollJobDetail.GROUP);
        }
    }
	
	private void schedulePolls() throws Exception {
        
        log().debug("Enter schedulePolls");
		
		Collection<PolledService> polledServices = m_pollerFrontEnd.getPolledServices();

		if (polledServices == null || polledServices.size() == 0) {
			log().warn("No polling scheduled.");
            log().debug("Exit schedulePolls");
			return;
		}

		long startTime = System.currentTimeMillis();
		long scheduleSpacing = m_initialSpreadTime / polledServices.size();

        for (PolledService polledService : polledServices) {
			
			Date initialPollTime = new Date(startTime);
			
			m_pollerFrontEnd.setInitialPollTime(polledService.getServiceId(), initialPollTime);
			
			Trigger pollTrigger = new PolledServiceTrigger(polledService);
			pollTrigger.setStartTime(initialPollTime);
			
			PollJobDetail jobDetail = new PollJobDetail(polledService.toString(), PollJob.class);
			jobDetail.setPolledService(polledService);
			jobDetail.setPollerFrontEnd(m_pollerFrontEnd);
			
            log().debug("Scheduling job for "+polledService);
            
			m_scheduler.scheduleJob(jobDetail, pollTrigger);
			
			startTime += scheduleSpacing;
		}
		
        log().debug("Exit schedulePolls");
		
	}

	private Category log() {
		return ThreadCategory.getInstance(getClass());
	}

	private void assertNotNull(Object propertyValue, String propertyName) {
		Assert.state(propertyValue != null, propertyName+" must be set for instances of "+Poller.class);
	}

	public void pollCompleted(String pollId, PollStatus pollStatus) {
		System.err.println(new Date()+": Complete Poll for "+pollId+" status = "+pollStatus);
	}

	public void pollStarted(String pollId) {
		System.err.println(new Date()+": Begin Poll for "+pollId);
		
	}

    public void configurationChanged(PropertyChangeEvent e) {
        try {
            unschedulePolls();
            schedulePolls();
        } catch (Exception ex) {
            log().fatal("Unable to schedule polls!", ex);
            throw new RuntimeException("Unable to schedule polls!");
        }
    }


}
