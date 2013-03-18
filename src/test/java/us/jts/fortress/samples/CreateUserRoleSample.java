/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.samples;

import us.jts.fortress.AdminMgr;
import us.jts.fortress.AdminMgrFactory;
import us.jts.fortress.ReviewMgr;
import us.jts.fortress.ReviewMgrFactory;
import us.jts.fortress.SecurityException;
import us.jts.fortress.rbac.TestUtils;
import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserRole;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * CreateUserRoleSample JUnit Test. This test suite demonstrates RBAC Role assignment and deassignment to Users.
 *
 * @author Shawn McKinney
 */
public class CreateUserRoleSample extends TestCase
{
    private static final String CLS_NM = CreateUserRoleSample.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);

    /**
     *
     * @param name
     */
    public CreateUserRoleSample(String name)
    {
        super(name);
    }

    /**
     * Run the RBAC role test cases.
     *
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreateUserRoleSample("testDeassignRoles"));
        }
        suite.addTest(new CreateUserRoleSample("testAssignComplexRole"));
        return suite;
    }

    /**
     *
     */
    public static void testDeassignRoles()
    {
        String szLocation = CLS_NM + ".testDeassignRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        // The key for User entity is the userId attribute.
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

            // This should return null because all Roles assigned to User were removed above:
            List<UserRole> assignedRoles = reviewMgr.assignedRoles(inUser);

            if(assignedRoles != null)
            {
                // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
                AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
                for(UserRole uRole : assignedRoles)
                {
                    // Call the API to deassign the Role from the User entity.  This will remove 'oamRA' and 'oamRC' attributes from the 'oamUserAttrs' object class.
                    adminMgr.deassignUser(uRole);
                }
            }

            // This should return null because all Roles assigned to User were removed above:
            assignedRoles = reviewMgr.assignedRoles(inUser);
            assertTrue(szLocation + " failed deassign test", assignedRoles.size() == 0);
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     */
    public static void testAssignComplexRole()
    {
        String szLocation = CLS_NM + ".testAssignComplexRole";

        // The key for User entity is the userId attribute.
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // Create roles, sampleRole1 - sampleRole10
            for(int i = 1; i < 11; i++)
            {
                // OpenAccessManagers UserRole entity may override Role's temporal constraints.
                // The key for User-Role addition is userId and role name.
                UserRole inUserRole = new UserRole(inUser.getUserId(), CreateRoleSample.TEST_ROLE_PREFIX + i);
                // Set some random constraints, whatever doesn't get set here will be provided by Constraints in corresponding Role defined in {@code ou=Roles}.
                // Don't set Role start date (accept default):
                // Override default on Role end date:
                inUserRole.setEndDate("21410101");
                // Override Role beginTime:
                inUserRole.setBeginTime("0000");
                // Don't set the Role endTime.
                // Override Role dayMask to Mon, Tue, Wed, Thur, Fri, Sat & Sun.
                inUserRole.setDayMask("1234567");
                // Override the Role beginLockDate to Jan 15, 2112
                inUserRole.setBeginLockDate("21120115");
                // Override the Role endLockDate to Feb 15, 2112.
                inUserRole.setEndLockDate("21120215");

                // Call the API to assign the Role to the User entity.  This will add 'oamRA' and 'oamRC' attributes to the 'oamUserAttrs' object class.
                adminMgr.assignUser(inUserRole);
            }

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

            // Return the list of Roles assigned to User.  The User - Role assignments are loaded into the UserRole entity:
            List<UserRole> assignedRoles = reviewMgr.assignedRoles(inUser);

            // Iterate over list of Roles assigned to User.
            for(UserRole userRole : assignedRoles)
            {
                log.info(szLocation + " userId [" + userRole.getUserId() + " roleNm [" + userRole.getName() + "]");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}