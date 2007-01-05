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
package org.opennms.report.availability;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.C3P0ConnectionFactory;
import org.opennms.netmgt.config.DataSourceFactory;
import org.opennms.netmgt.mock.MockDatabase;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

public class AvailabilityReportSchedulerServiceTest extends AbstractTransactionalDataSourceSpringContextTests{
        private AvailabilityReportLocatorService m_locatorService;
        private AvailabilityReportSchedulerService m_schedulerService;
        private MockDatabase m_db;
        
        public AvailabilityReportSchedulerServiceTest() throws MarshalException, ValidationException, IOException, PropertyVetoException, SQLException {
            System.setProperty("opennms.home", "src/test/opennms-home");
            m_db = new MockDatabase();
            DataSourceFactory.setInstance(m_db);
        }
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] {
				"META-INF/opennms/applicationContext-dao.xml",
				"org/opennms/report/svclayer/applicationContext-svclayer.xml" };
	}
        
        @Override
        protected void onTearDownAfterTransaction() throws Exception {
            super.onTearDownAfterTransaction();
            m_db.drop();
        }
        
	public void testBogus() {
	    // this is just here so that JUnit doesn't complain about not having any tests
        }
	
	@Transactional(readOnly=false)
	public void FIXMEtestScheduleReport() {
		Date date = new Date();
		m_schedulerService.Schedule("all", "html", "classic", date);
		assertNotNull(m_locatorService.locateReports());
		
		
	}


	public AvailabilityReportLocatorService getLocatorService() {
		return m_locatorService;
	}


	public void setLocatorService(AvailabilityReportLocatorService locatorService) {
		m_locatorService = locatorService;
	}


	public AvailabilityReportSchedulerService getSchedulerService() {
		return m_schedulerService;
	}


	public void setSchedulerService(
			AvailabilityReportSchedulerService schedulerService) {
		m_schedulerService = schedulerService;
	}
}
