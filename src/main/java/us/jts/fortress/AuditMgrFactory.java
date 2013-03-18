/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.AuditMgrImpl;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rest.AuditMgrRestImpl;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AuditMgr object.
 * <p/>
 * The default implementation class is specified as {@link AuditMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#AUDIT_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class AuditMgrFactory
{
    private static String auditClassName = Config.getProperty(GlobalIds.AUDIT_IMPLEMENTATION);
    private static final String CLS_NM = AuditMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link us.jts.fortress.AuditMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link us.jts.fortress.AuditMgr}.
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AuditMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(auditClassName))
        {
            if(GlobalIds.IS_REST)
            {
                auditClassName = AuditMgrRestImpl.class.getName();
            }
            else
            {
                auditClassName = AuditMgrImpl.class.getName();
            }
        }

        AuditMgr auditMgr = (AuditMgr) ClassUtil.createInstance(auditClassName);
        auditMgr.setContextId(contextId);
        return auditMgr;
    }

    /**
     * Create and return a reference to {@link us.jts.fortress.AuditMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link us.jts.fortress.AuditMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AuditMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        AuditMgr auditMgr = createInstance(contextId);
        auditMgr.setAdmin(adminSess);
        return auditMgr;
    }
}