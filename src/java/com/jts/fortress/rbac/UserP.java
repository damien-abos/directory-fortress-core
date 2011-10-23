/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.PasswordException;
import com.jts.fortress.configuration.Config;
import com.jts.fortress.SecurityException;
import com.jts.fortress.ValidationException;
import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.AdminRoleP;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.arbac.OrgUnitP;
import com.jts.fortress.arbac.UserAdminRole;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.pwpolicy.openldap.PolicyP;
import com.jts.fortress.util.time.CUtil;
import com.jts.fortress.util.AlphabeticalOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.jts.fortress.util.attr.AttrHelper;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

/**
 * Process module for the User entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress manager classes ({@link com.jts.fortress.rbac.AdminMgrImpl}, {@link com.jts.fortress.rbac.AccessMgrImpl},
 * {@link com.jts.fortress.rbac.ReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link User}, validate its contents and forward on to it's corresponding DAO class {@link com.jts.fortress.rbac.UserDAO}.
 * <p>
 * Class will throw {@link com.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link com.jts.fortress.constants.GlobalErrIds}.
 * <p>
 * This object is thread safe.
 * </p>

 *
 * @author smckinn
 * @created August 30, 2009
 */
public final class UserP
{
    /**
     * Takes a User entity that contains full or partial userId OR a full internal userId for search.
     *
     * @param user contains all or partial userId or full internal userId.
     * @return List of type User containing fully populated matching User entities.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<User> search(User user)
        throws SecurityException
    {
        return uDao.findUsers(user);
    }


    public final List<User> search(OrgUnit ou, boolean limitSize)
        throws SecurityException
    {
        return uDao.findUsers(ou, limitSize);
    }


    /**
     * Search according to full or partial search string that maps to Fortress userid.
     * This search is used by RealmMgr for Websphere.
     *
     * @param searchVal contains full or partial userId.
     * @param limit     specify the max number of records to return in result set.
     * @return List of type String containing userId of all matching User entities. If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<String> search(String searchVal, int limit)
        throws SecurityException
    {
        return uDao.findUsers(searchVal, limit);
    }


    /**
     * Return a list of Users that are authorized the given Role.
     *
     * @param role contains the role name targeted for search.
     * @return List of type User containing fully populated matching User entities. If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<User> getAuthorizedUsers(Role role)
        throws SecurityException
    {
        return uDao.getAuthorizedUsers(role);
    }


    /**
     * Return a list of Users that are authorized the given Role.
     *
     * @param roles contains the set of role names targeted for search.
     * @return Set of type String containing the userId's for matching User entities. If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final Set<String> getAssignedUsers(Set<String> roles)
        throws SecurityException
    {
        return uDao.getAssignedUsers(roles);
    }


    /**
     * Return a list of Users that are authorized the given Role.
     * In RBAC the word "authorized" implies the hierarchical role relations graph is considered in result set.
     * This search is used by RealmMgr for Websphere.
     *
     * @param role
     * @param limit specify the max number of records to return in result set.
     * @return list of type String of userIds. If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<String> getAuthorizedUsers(Role role, int limit)
        throws SecurityException
    {
        return uDao.getAuthorizedUsers(role, limit);
    }


    /**
     * Return a list of Users assigned the given RBAC role.
     * "Assigned" implies the hierarchical role relation graph will NOT be considered in result set.
     *
     * @param role contains name of RBAC role used for search.
     * @return List of fully populated User entities matching target search. If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<User> getAssignedUsers(Role role)
        throws SecurityException
    {
        return uDao.getAssignedUsers(role);
    }


    /**
     * Return a list of Users assigned the given Administrative role.
     * "Assigned" implies the hierarchical role relation graph will NOT be considered in result set.
     *
     * @param role contains name of Admin role used for search.
     * @return List of fully populated User entities matching target search.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<User> getAssignedUsers(AdminRole role)
        throws SecurityException
    {
        return uDao.getAssignedUsers(role);
    }


    /**
     * Return the list of User's RBAC roles.
     *
     * @param userId contains full userId for target operation.
     * @return List of type String containing RBAC role names.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<String> getAssignedRoles(String userId)
        throws SecurityException
    {
        return uDao.getRoles(userId);
    }


    /**
     * Return a fully populated User entity for a given userId.  If the User entry is not found a SecurityException
     * will be thrown.
     *
     * @param userId  contains full userId value.
     * @param isRoles return user's assigned roles if "true".
     * @return User entity containing all attributes associated with User in directory.
     * @throws com.jts.fortress.SecurityException in the event of User not found or DAO search error.
     */
    public User read(String userId, boolean isRoles)
        throws SecurityException
    {
        return uDao.getUser(userId, isRoles);
    }


    /**
     * Adds a new User entity to directory.  The User entity input object will be validated to ensure that:
     * userId is present, orgUnitId is valid, roles (optiona) are valid, reasonability checks on all of the
     * other populated values.
     *
     * @param entity User entity contains data targeted for insertion.
     * @return User entity copy of input + additional attributes (internalId) that were added by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public User add(User entity)
        throws SecurityException
    {
        return add(entity, true);
    }


    /**
     * Adds a new User entity to directory.
     * The User entity input object will be validated to ensure that: userId is present, orgUnitId is valid,
     * roles (optiona) are valid, reasonability checks on all of the other populated values.
     *
     * @param entity   User entity contains data targeted for insertion.
     * @param validate if false will skip the validations described above.
     * @return User entity copy of input + additional attributes (internalId)
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public User add(User entity, boolean validate)
        throws SecurityException
    {
        if (validate)
        {
            // Ensure the input data is valid.
            validate(entity, false);
        }
        entity = uDao.create(entity);
        return entity;
    }


    /**
     * Update existing user's attributes with the input entity.  Null or empty attributes will be ignored.
     * This method will ignore userId as input as change userId is not allowed.  If password is changed
     * OpenLDAP password policy will not be evaluated on behalf of the user.
     * Other User entity input data can be changed and will also be validated beforehand to ensure that:
     * orgUnitId is valid, roles (optional) are valid, reasonability checks will be performed on all of the populated fields.
     *
     * @param entity User entity contains data targeted for insertion.
     * @return User entity copy of input
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public User update(User entity)
        throws SecurityException
    {
        return update(entity, true);
    }


    /**
     * Update existing user's attributes with the input entity.  Null or empty attributes will be ignored.
     * This method will ignore userId or password as input.  The former is not allowed and latter is performed by other
     * methods in this class.
     * Other User entity input data can be changed and will also be validated beforehand to ensure that:
     * orgUnitId is valid, roles (optional) are valid, reasonability checks will be performed on all of the populated fields.
     *
     * @param entity   User entity contains data targeted for insertion.
     * @param validate if false will skip the validations described above.
     * @return User entity copy of input
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    /**
     * Update existing user's attributes with the input entity.  Null or empty attributes will be ignored.
     * This method will ignore userId or password as input.  The former is not allowed and latter is performed by other
     * methods in this class.
     * Other User entity input data can be changed and will also be validated beforehand to ensure that:
     * orgUnitId is valid, roles (optional) are valid, reasonability checks will be performed on all of the populated fields.
     *
     * @param entity   User entity contains data targeted for insertion.
     * @param validate if false will skip the validations described above.
     * @return User entity copy of input
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public User update(User entity, boolean validate)
        throws SecurityException
    {
        if (validate)
        {
            // Ensure the input data is valid.
            validate(entity, true);
        }
        entity = uDao.update(entity);
        return entity;
    }

    /**
     * Update name value pairs stored on User entity as attributes.  These attributes are not constrained
     * by Fortress policy and are useful for; 1. Audit information on user, ie. host name, IP, etc. 2. Custom attributes stored on User entity on behalf of the client.
     *
     * @param entity contains UserId and the props targeted for insertion.
     * @param replace if set will replace existing vals
     * @return User entity copy of input
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    private User updateProps(User entity, Session session, boolean replace)
        throws SecurityException
    {
        int i = 1;
        if(VUtil.isNotNullOrEmpty(session.getUser().getRoles()))
        {
            for(UserRole uRole : session.getUser().getRoles())
            {
                entity.addProperty("R" + i++, uRole.getName());
            }
        }
        i = 1;
        if(VUtil.isNotNullOrEmpty(session.getUser().getAdminRoles()))
        {
            for(UserAdminRole uAdminRole : session.getUser().getAdminRoles())
            {
                entity.addProperty("A" + i++, uAdminRole.getName());
            }
        }

        // only call the dao if properties exists:
        if(VUtil.isNotNullOrEmpty(entity.getProperties()))
            entity = uDao.updateProps(entity, replace);
        return entity;
    }


    /**
     * Method performs a "soft" delete.  It disables User entity and flags as "deleted".  User must exist in directory
     * prior to making this call.
     *
     * @param user Contains the userId of the user targeted for deletion.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public String softDelete(User user)
        throws SecurityException
    {
        // Ensure this user isn't listed in Fortress config as a system user that can't be removed via API.
        // Is there a match between this userId and a Fortress system user?
        if (sysUserSet.contains(user.getUserId()))
        {
            String warning = OCLS_NM + ".softDelete userId <" + user.getUserId() + "> can't be removed due to policy violation, OAMCD=" + GlobalErrIds.USER_PW_PLCY_VIOLATION;
            throw new SecurityException(GlobalErrIds.USER_PW_PLCY_VIOLATION, warning);
        }
        user.setDescription("DELETED");
        User outUser = uDao.update(user);
        return outUser.getDn();
    }

    /**
     * This method performs a "hard" delete.  It completely removes all data associated with this user from the directory.
     * User entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param user Contains the userid of the user targeted for deletion.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public String delete(User user)
        throws SecurityException
    {
        // Ensure this user isn't listed in Fortress config as a system user that can't be removed via API.
        // Is there a match between this userId and a Fortress system user?
        if (sysUserSet.contains(user.getUserId()))
        {
            String warning = OCLS_NM + ".delete userId <" + user.getUserId() + "> can't be removed due to policy violation, OAMCD=" + GlobalErrIds.USER_PW_PLCY_VIOLATION;
            throw new SecurityException(GlobalErrIds.USER_PW_PLCY_VIOLATION, warning);
        }
        return uDao.remove(user);
    }


    /**
     * Removes the user's association from OpenLDAP password policy.  Once this association is removed, the User
     * password policy will default to that which is default for ldap server.
     *
     * @param user contains the userId for target user.
     * @throws com.jts.fortress.SecurityException in the event of DAO error.
     */
    public void deletePwPolicy(User user)
        throws com.jts.fortress.SecurityException
    {
        uDao.deletePwPolicy(user);
    }

    /**
     * This method performs authentication only.  It does not activate RBAC roles in session.  It will evaluate
     * password policies.
     *
     * @param userId   Contains the userid of the user signing on.
     * @param password Contains the user's password.
     * @return Session object will be returned if authentication successful.  This will not contain user's roles.
     * @throws com.jts.fortress.SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    public final Session authenticate(String userId, char[] password)
        throws com.jts.fortress.SecurityException
    {
        Session session;
        session = uDao.checkPassword(userId, password);
        if (session == null)
        {   // This should not happen, ever:
            String error = "UserP.authenticate failed - null session detected for userId <" + userId + ">";
            throw new SecurityException(GlobalErrIds.USER_SESS_CREATE_FAILED, error);
        }
        else if (!session.isAuthenticated())
        {
            String info = "UserP.authenticate failed  for userId <" + userId + "> reason code <" + session.getErrorId() + "> msg <" + session.getMsg() + ">";
            throw new PasswordException(session.getErrorId(), info);
        }
        CUtil.validateConstraints(session, CUtil.ConstraintType.USER, false);
        return session;
    }


    /**
     * CreateSession
     * <p>
     * This method is called by AccessMgr and is not intended for use outside Fortress core.  The successful
     * result is Session object that contains target user's RBAC and Admin role activations.  In addition to checking
     * user password validity it will apply configured password policy checks.  Method may also store parms passed in for
     * audit trail..
     * <ul>
     * <li> authenticate user password
     * <li> password policy evaluation with OpenLDAP PwPolicy
     * <li> evaluate temporal constraints on User and UserRole entities.
     * <li> allow selective role activations into User RBAC Session.
     * <li> require valid password if trusted == false.
     * <li> will disallow any user who is locked out due to OpenLDAP pw policy, regardless of trusted flag being set as parm on API.
     * <li> return User's RBAC Session containing User and UserRole attributes.
     * <li> throw a SecurityException for authentication failures, other policy violations, data validation errors or system failure.
     * </ul>
     * </p>
     * <p>
     * The function is valid if and only if:
     * <ul>
     * <li> the user is a member of the USERS data set
     * <li> the password is supplied (unless trusted).
     * <li> the (optional) active role set is a subset of the roles authorized for that user.
     * </ul>
     * </p>
     * <p>
     * The User parm contains the following (* indicates required)
     * <ul>
     * <li> String userId*
     * <li> char[] password
     * <li> List<UserRole> userRoles contains a list of RBAC role names authorized for user and targeted for activation within this session.
     * <li> List<UserAdminRole> userAdminRoles contains a list of Admin role names authorized for user and targeted for activation.
     * <li> Properties logonProps collection of auditable name/value pairs to store.  For example hostname:myservername or ip:192.168.1.99
     * </ul>
     * </p>
     * <p>
     * Notes:
     * <ul>
     * <li> roles that violate Dynamic Separation of Duty Relationships will not be activated into session.
     * <li> role activations will proceed in same order as supplied to User entity setter.
     * </ul>
     * </p>
     *
     * @param user    Contains userId, password (optional if "trusted"), optional User RBAC Roles: List<UserRole> rolesToBeActivated., optional User Admin Roles: List<UserAdminRole> adminRolesToBeActivated.
     * @param trusted if true password is not required.
     * @return Session object will contain authentication result code, RBAC and Admin role activations, OpenLDAP pw policy output and more.
     * @throws com.jts.fortress.SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    public final Session createSession(User user, boolean trusted)
        throws SecurityException
    {
        Session session;
        if (trusted)
        {
            session = createSession(user.getUserId());
        }
        else
        {
            VUtil.assertNotNullOrEmpty(user.getPassword(), GlobalErrIds.USER_PW_NULL, OCLS_NM + ".createSession");
            session = createSession(user.getUserId(), user.getPassword());
        }

        // todo - convert this to Set notation.
        if (VUtil.isNotNullOrEmpty(user.getRoles()))
        {
            // Process selective activation of user's RBAC roles into session:
            List<UserRole> rlsActual = session.getRoles();
            List<UserRole> rlsFinal = new ArrayList<UserRole>();
            session.setRoles(rlsFinal);
            for (UserRole role : user.getRoles())
            {
                if (rlsActual.contains(role))
                {
                    rlsFinal.add(role);
                }
            }
        }
        // add props if enabled:
        if (IS_SESSION_PROPS_ENABLED)
            updateProps(user, session, true);
        return session;
    }


    /**
     * Called internal to this class only.  Will do all of the session activations of the public method
     * in addition to the password validation.
     *
     * @param userId   Contains userId that represents rDn of node in ldap directory.
     * @param password User's password will be evaluated for correctness and password policies.
     * @return Session object will contain authentication result code, RBAC and Admin role activations, OpenLDAP pw policy output and more.
     * @throws com.jts.fortress.SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    private final Session createSession(String userId, char[] password)
        throws SecurityException
    {
        // read user entity:
        User user = read(userId, true);

        // authenticate password, check pw policies and validate user temporal constraints:
        Session session = authenticate(userId, password);

        // Set the user entity into the session object:
        session.setUser(user);

        // Check role temporal constraints + activate roles:
        CUtil.validateConstraints(session, CUtil.ConstraintType.ROLE, true);
        return session;
    }


    /**
     * Trusted session creation method called internal to this class only.  Will do all of the session activations of the public method
     *
     * @param userId Contains userId that represents rDn of node in ldap directory.
     * @return Session object will contain authentication result code, RBAC and Admin role activations, OpenLDAP pw policy output and more.
     * @throws com.jts.fortress.SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    private final Session createSession(String userId)
        throws SecurityException
    {
        User user = read(userId, true);
        if (user.isLocked())
        {
            String warning = OCLS_NM + ".createSession failed for userId <" + userId + "> reason user is locked";
            log.warn(warning);
            throw new SecurityException(GlobalErrIds.USER_LOCKED_BY_CONST, warning);
        }
        Session session = new Session();
        session.setUserId(userId);
        // Set this flag to false because user's password was not authenticated.
        session.setAuthenticated(false);
        session.setUser(user);
        CUtil.validateConstraints(session, CUtil.ConstraintType.USER, false);
        CUtil.validateConstraints(session, CUtil.ConstraintType.ROLE, true);
        return session;
    }


    /**
     * Method will set the OpenLDAP pwlocked attribute which will lock user from being able to signon to the system.
     *
     * @param user Contains userId that represents rDn of node in ldap directory.
     * @throws com.jts.fortress.SecurityException in the event of DAO error.
     */
    public final void lock(User user)
        throws SecurityException
    {
        uDao.lock(user);
    }

    /**
     * Method will reset the OpenLDAP pwlocked attribute which will unlock user and allow to signon to the system.
     *
     * @param user Contains userId that represents rDn of node in ldap directory.
     * @throws com.jts.fortress.SecurityException in the event of DAO  error.
     */
    public final void unlock(User user)
        throws SecurityException
    {
        uDao.unlock(user);
    }

    /**
     * Method will change the user's password and validate user's pw policy in OpenLDAP.
     *
     * @param entity      contains userId and old password.
     * @param newPassword contains the new password which must pass the password policy constraints.
     * @throws com.jts.fortress.SecurityException in the event of data validation failure, password policy violation or DAO error.
     */
    public final void changePassword(User entity, char[] newPassword)
        throws SecurityException
    {
        String userId = entity.getUserId();
        boolean result = uDao.changePassword(entity, newPassword);
        if (!result)
        {
            log.warn(OCLS_NM + ".changePassword failed for user <" + userId + ">");
        }
    }

    /**
     * Peform password reset on user entity.  This will change the User password and set the reset flag
     * in OpenLDAP will will force the user to change their password at next logon time.
     *
     * @param user contains the userId and the new password.
     * @throws com.jts.fortress.SecurityException in the event of DAO error.
     */
    public final void resetPassword(User user)
        throws SecurityException
    {
        uDao.resetUserPassword(user);
    }


    /**
     * This command assigns a user to a role.
     * <p>
     * <ul>
     * <li> The command is valid if and only if:
     * <li> The user is a member of the USERS data set
     * <li> The role is a member of the ROLES data set
     * <li> The user is not already assigned to the role
     * <li> The SSD constraints are satisfied after assignment.
     * </ul>
     * </p>
     * <p>
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> Role entity (resides in role container) has userId added as role occupant.
     * <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     * <ul>
     * <li> timeout - number in seconds of session inactivity time allowed.
     * <li> beginDate - YYYYMMDD - determines date when role may be activated.
     * <li> endDate - YYMMDD - indicates latest date role may be activated.
     * <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status
     * <li> endLockDate - YYMMDD - determines end of enforced inactive status.
     * <li> beginTime - HHMM - determines begin hour role may be activated in user's session.
     * <li> endTime - HHMM - determines end hour role may be activated in user's session.*
     * <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.
     * </ul>
     * </ul>
     *
     * @param uRole entity contains userId and role name for targeted assignment.
     * @return String containing the user's DN.  This value is used to update the "roleOccupant" attribute on associated role entity.
     * @throws com.jts.fortress.SecurityException in the event data error in user or role objects or system error.
     */
    public final String assign(UserRole uRole)
        throws SecurityException
    {
        // "assign" custom Fortress role data, i.e. temporal constraints, onto the user node:
        return uDao.assign(uRole);
    }

    /**
     * This command deletes the assignment of the User from the Role entities. The command is
     * valid if and only if the user is a member of the USERS data set, the role is a member of
     * the ROLES data set, and the user is assigned to the role.
     * Any sessions that currently have this role activated will not be effected.
     * Successful completion includes:
     * User entity in USER data set has role assignment removed.
     * Role entity in ROLE data set has userId removed as role occupant.
     * (optional) Temporal constraints will be removed from user aux object if set prior to call.
     *
     * @param uRole entity contains userId and RBAC Role name for targeted assignment.
     * @return String containing the user's DN.  This value is used to remove the "roleOccupant" attribute on associated RBAC Role entity.
     * @throws com.jts.fortress.SecurityException - in the event data error in user or role objects or system error.
     */
    public final String deassign(UserRole uRole)
        throws SecurityException
    {
        // "deassign" custom Fortress role data from the user's node:
        return uDao.deassign(uRole);
    }

    /**
     * This command assigns a user to an admin role.
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> AdminRole entity (resides in admin role container) has userId added as role occupant.
     * <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     * <ul>
     * <li> timeout - number in seconds of session inactivity time allowed.
     * <li> beginDate - YYYYMMDD - determines date when role may be activated.
     * <li> endDate - YYMMDD - indicates latest date role may be activated.
     * <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status
     * <li> endLockDate - YYMMDD - determines end of enforced inactive status.
     * <li> beginTime - HHMM - determines begin hour role may be activated in user's session.
     * <li> endTime - HHMM - determines end hour role may be activated in user's session.*
     * <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.
     * </ul>
     * </ul>
     *
     * @param uRole entity contains userId and Admin Role name for targeted assignment.
     * @return String containing the user's DN.  This value is used to update the "roleOccupant" attribute on associated Admin Role entity.
     * @throws com.jts.fortress.SecurityException in the event data error in user or role objects or system error.
     */
    public final String assign(UserAdminRole uRole)
        throws SecurityException
    {
        // Assign custom Fortress role data, i.e. temporal constraints, onto the user node:
        return uDao.assign(uRole);
    }


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     * del Role to User assignment in User data set
     * AND
     * User to Role assignment in Admin Role data set.
     *
     * @param uRole entity contains userId and Admin Role name for targeted assignment.
     * @return String containing the user's DN.  This value is used to remove the "roleOccupant" attribute on associated Admin Role entity.
     * @throws com.jts.fortress.SecurityException - in the event data error in user or role objects or system error.
     */
    public final String deassign(UserAdminRole uRole)
        throws SecurityException
    {
        // Deassign custom Fortress role data from the user's node:
        return uDao.deassign(uRole);
    }


    /**
     * Method will perform various validations to ensure the integrity of the User entity targeted for insertion
     * or updating in directory.  For example the ou attribute will be "read" from the OrgUnit dataset to ensure
     * that it is valid.  Data reasonability checks will be performed on all non-null attributes.
     * This method will also copy the source constraints to target entity iff the target input entity does not have set
     * prior to calling.
     *
     * @param entity   User entity contains data targeted for insertion or update.  The input role constraints will be accepted.
     * @param isUpdate if true update operation is being performed which specifies a different set of targeted attributes.
     * @throws com.jts.fortress.SecurityException in the event of data validation error or DAO error on Org validation.
     */
    private void validate(User entity, boolean isUpdate)
        throws SecurityException
    {
        if (!isUpdate)
        {
            // the UserId attribute is required on User:
            VUtil.userId(entity.getUserId());

            // the cn attribute is optional as input.  entity will default to userId if cn not set by caller on add:
            if (VUtil.isNotNullOrEmpty(entity.getCn()))
            {
                VUtil.safeText(entity.getCn(), GlobalIds.CN_LEN);
            }
            // the sn attribute is optional as input.  entity will default to userId if sn not set by caller on add:
            if (VUtil.isNotNullOrEmpty(entity.getSn()))
            {
                VUtil.safeText(entity.getSn(), GlobalIds.SN_LEN);
            }
            // password is not required on user object but user cannot execute AccessMgr or DelegatedAccessMgr methods w/out pw.
            if (VUtil.isNotNullOrEmpty(entity.getPassword()))
            {
                VUtil.password(entity.getPassword());
            }

            // the OU attribute is required:
            VUtil.orgUnit(entity.getOu());
            // ensure ou exists in the OS-U pool:
            OrgUnit ou = new OrgUnit(entity.getOu(), OrgUnit.Type.USER);
            if (!op.isValid(ou))
            {
                String error = OCLS_NM + ".validate detected invalid orgUnit name <" + entity.getOu() + "> for userId <" + entity.getUserId() + ">";
                throw new ValidationException(GlobalErrIds.USER_OU_INVALID, error);
            }

            // description attribute is optional:
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                VUtil.description(entity.getDescription());
            }
        }
        else
        {
            // on User update, all attributes are optional:
            if (VUtil.isNotNullOrEmpty(entity.getCn()))
            {
                VUtil.safeText(entity.getCn(), GlobalIds.CN_LEN);
            }
            if (VUtil.isNotNullOrEmpty(entity.getSn()))
            {
                VUtil.safeText(entity.getSn(), GlobalIds.SN_LEN);
            }
            if (VUtil.isNotNullOrEmpty(entity.getPassword()))
            {
                VUtil.password(entity.getPassword());
            }
            if (VUtil.isNotNullOrEmpty(entity.getOu()))
            {
                VUtil.orgUnit(entity.getOu());
                // ensure ou exists in the OS-U pool:
                OrgUnit ou = new OrgUnit(entity.getOu(), OrgUnit.Type.USER);
                if (!op.isValid(ou))
                {
                    String error = OCLS_NM + ".validate detected invalid orgUnit name <" + entity.getOu() + "> for userId <" + entity.getUserId() + ">";
                    //log.warn(error);
                    throw new ValidationException(GlobalErrIds.USER_OU_INVALID, error);
                }
            }
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                VUtil.description(entity.getDescription());
            }
        }

        // 1 OpenLDAP password policy name must be valid if set:
        if (VUtil.isNotNullOrEmpty(entity.getPwPolicy()))
        {
            PolicyP pP = new PolicyP();
            if (!pP.isValid(entity.getPwPolicy()))
            {
                String error = OCLS_NM + ".validate detected invalid OpenLDAP policy name <" + entity.getPwPolicy() + "> for userId <" + entity.getUserId() + ">. Assignment is optional for User but must be valid if specified.";
                throw new ValidationException(GlobalErrIds.USER_PW_PLCY_INVALID, error);
            }
        }

        // 2 Validate constraints on User object:
        CUtil.validate(entity);

        // 3 Validate or copy constraints on RBAC roles:
        if (VUtil.isNotNullOrEmpty(entity.getRoles()))
        {
            RoleP rp = new RoleP();
            List<UserRole> roles = entity.getRoles();
            for (UserRole ure : roles)
            {
                Role role = rp.read(ure.getName());
                CUtil.validateOrCopy(role, ure);
            }
        }

        // 4 Validate and copy constraints on Administrative roles:
        if (VUtil.isNotNullOrEmpty(entity.getAdminRoles()))
        {
            AdminRoleP arp = new AdminRoleP();
            List<UserAdminRole> uRoles = entity.getAdminRoles();
            for (UserAdminRole uare : uRoles)
            {
                AdminRole aRole = arp.read(uare.getName());
                CUtil.validateOrCopy(aRole, uare);

                // copy the ARBAC AdminRole attributes to UserAdminRole:
                AttrHelper.copyAdminAttrs(aRole, uare);
            }
        }
    }


    /**
     *
     * @return
     */
   private static Set<String> getSysUserSet()
    {
        Set<String> localSet =  new TreeSet<String>(new AlphabeticalOrder());
        for (int i = 1; ; i++)
        {
            String prop = SYSTEM_USER_PREFIX + i;
            String value = Config.getProperty(prop);
            if (value == null)
            {
                break;
            }
            localSet.add(value);
        }
        return localSet;
    }

    private static final boolean IS_SESSION_PROPS_ENABLED = Config.getBoolean("userSessionProps", false);
    private static final String OCLS_NM = UserP.class.getName();
    private static final UserDAO uDao = new UserDAO();
    private static final Logger log = Logger.getLogger(OCLS_NM);
    private static final OrgUnitP op = new OrgUnitP();
    private static final String SYSTEM_USER_PREFIX = "sys.user.";
    // This Set contains list of system users as specified in Fortress config.  These users will not be allowed to be deleted using API.
    private Set<String> sysUserSet = getSysUserSet();
}