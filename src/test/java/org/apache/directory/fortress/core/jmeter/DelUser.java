/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.jmeter;

import org.apache.directory.fortress.core.*;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;

import static org.junit.Assert.*;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelUser extends AbstractJavaSamplerClient
{
    private AdminMgr adminMgr;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( DelUser.class );
    private static int count = 0;
    private int key = 0;
    private int ctr = 0;
    private String hostname;

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        String userId = "";
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            String message = "FT DelUser TID: " + getThreadId() + " UID:" + userId + " CTR:" + ctr++;
            LOG.info( message );
            //System.out.println( message );
            assertNotNull( adminMgr );

            key = getKey();
            userId = hostname + '-' + key;

            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( userId );
            adminMgr.deleteUser( user );
            sampleResult.sampleEnd();
            sampleResult.setBytes(1);
            sampleResult.setResponseMessage("test completed TID: " + getThreadId() + " UID: " + userId);
            sampleResult.setSuccessful(true);
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            System.out.println( "ThreadId:" + getThreadId() + "Error running test: " + se );
            se.printStackTrace();
            sampleResult.setSuccessful( false );
        }

        return sampleResult;
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void setupTest( JavaSamplerContext samplerContext )
    {
        ctr = 0;
        hostname = samplerContext.getParameter( "hostname" );
        String message = "FT SETUP Del User TID: " + getThreadId() + ", hostname: " + hostname;
        LOG.info( message );
        System.out.println( message );
        try
        {
            adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( SecurityException se )
        {
            System.out.println( "ThreadId:" + getThreadId() + "FT SETUP Error: " + se );
            se.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    synchronized private int getKey( )
    {
        return ++count;
    }
    synchronized private String getKey( long threadId )
    {
        return threadId + "-" + count++;
    }
    private String getThreadId()
    {
        return "" + Thread.currentThread().getId();
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void teardownTest( JavaSamplerContext samplerContext )
    {
        String message = "FT SETUP DelUser TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
    }
}