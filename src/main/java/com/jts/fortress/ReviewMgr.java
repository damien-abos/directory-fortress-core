/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.PermObj;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.SDSet;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserRole;

import java.util.List;
import java.util.Set;

/**
 * This interface prescribes the administrative review functions on already provisioned Fortress RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p/>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p/>
 * <img src="../../../images/RbacCore.png">
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p/>
 * <img src="../../../images/RbacHier.png">
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p/>
 * <img src="../../../images/RbacSSD.png">
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p/>
 * <img src="../../../images/RbacDSD.png">
 * <p/>
 *
 * @author Shawn McKinney
 * @created August 23, 2009
 */
public interface ReviewMgr extends com.jts.fortress.Authorizable
{

    /**
     * This method returns a matching permission entity to caller.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param permission must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Permission entity that is loaded with data.
     * @throws com.jts.fortress.SecurityException
     *          if permission not found or system error occurs.
     */
    public Permission readPermission(Permission permission)
        throws SecurityException;

    /**
     * Method reads permission object from perm container in directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param permObj entity contains the {@link PermObj#objectName} of target record.
     * @return PermObj loaded with perm object data.
     * @throws SecurityException is thrown if object not found or system error.
     */
    public PermObj readPermObj(PermObj permObj)
        throws SecurityException;

    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains one or more characters of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains one or more characters of existing permission operation</li>
     * </ul>
     *
     * @param permission contains object and operation name search strings.  Each contains 1 or more leading chars that correspond to object or op name.
     * @return List of type Permission.  Fortress permissions are object->operation mappings.  The permissions may contain
     *         assigned user, role or group entities as well.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of system error.
     */
    public List<Permission> findPermissions(Permission permission)
        throws SecurityException;


    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains one or more characters of existing object being targeted</li>
     * </ul>
     *
     * @param permObj contains object name search string.  The search val contains 1 or more leading chars that correspond to object name.
     * @return List of type PermObj.  Fortress permissions are object->operation mappings.
     * @throws SecurityException thrown in the event of system error.
     */
    public List<PermObj> findPermObjs(PermObj permObj)
        throws SecurityException;


    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains one or more characters of org unit associated with existing object being targeted</li>
     * </ul>
     *
     * @param ou contains org unit name {@link com.jts.fortress.arbac.OrgUnit#name}.  The search val contains the full name of matching ou in OS-P data set.
     * @return List of type PermObj.  Fortress permissions are object->operation mappings.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of system error.
     */
    public List<PermObj> findPermObjs(OrgUnit ou)
        throws SecurityException;


    /**
     * Method reads Role entity from the role container in directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role to read.</li>
     * </ul>
     *
     * @param role contains role name, {@link Role#name}, to be read.
     * @return Role entity that corresponds with role name.
     * @throws com.jts.fortress.SecurityException
     *          will be thrown if role not found or system error occurs.
     */
    public Role readRole(Role role)
        throws SecurityException;


    /**
     * Method will return a list of type Role matching all or part of Role name, {@link Role#name}.
     *
     * @param searchVal contains the all or some of the chars corresponding to role entities stored in directory.
     * @return List of type Role containing role entities that match the search criteria.
     * @throws SecurityException in the event of system error.
     */
    public List<Role> findRoles(String searchVal)
        throws SecurityException;


    /**
     * Method returns a list of roles of type String.  This method can be limited by integer value that indicates max
     * number of records that may be contained in the result set.  This number can further limit global default but can
     * not increase the max.  This method is called by the Websphere Realm impl.
     *
     * @param searchVal contains all or some leading chars that correspond to roles stored in the role container in the directory.
     * @param limit     integer value specifies the max records that may be returned in the result set.
     * @return
     * @throws com.jts.fortress.SecurityException
     *          in the event of system error.
     */
    public List<String> findRoles(String searchVal, int limit)
        throws SecurityException;


    /**
     * Method returns matching User entity that is contained within the people container in the directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for read.</li>
     * </ul>
     *
     * @param user entity contains a value {@link User#userId} that matches record in the directory.  userId is globally unique in
     *             people container.
     * @return entity containing matching user data.
     * @throws SecurityException if record not found or system error occurs.
     */
    public User readUser(User user)
        throws SecurityException;


    /**
     * Return a list of type User of all users in the people container that match all or part of the {@link User#userId} field passed in User entity.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains all or some leading chars that match userId(s) stored in the directory.</li>
     * </ul>
     *
     * @param user contains all or some leading chars that match userIds stored in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    public List<User> findUsers(User user)
        throws SecurityException;


    /**
     * Return a list of type User of all users in the people container that match the name field passed in OrgUnit entity.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains one or more characters of org unit associated with existing object(s) being targeted</li>
     * </ul>
     *
     * @param ou contains name of User OU, {@link OrgUnit#name} that match ou attribute associated with User entity in the directory.
     * @return List of type User.
     * @throws com.jts.fortress.SecurityException
     *          In the event of system error.
     */
    public List<User> findUsers(OrgUnit ou)
        throws SecurityException;


    /**
     * Return a list of type String of all users in the people container that match the userId field passed in User entity.
     * This method is used by the Websphere realm component.  The max number of returned users may be set by the integer limit arg.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for read.</li>
     * <li>limit - max number of objects to return.</li>
     * </ul>
     *
     * @param user  contains all or some leading chars that correspond to users stored in the directory.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing matching userIds.
     * @throws SecurityException in the event of system error.
     */
    public List<String> findUsers(User user, int limit)
        throws SecurityException;


    /**
     * This function returns the set of users assigned to a given role. The function is valid if and
     * only if the role is a member of the ROLES data set.
     * The max number of users returned is constrained by limit argument.
     * This method is used by the Websphere realm component.  This method does NOT use hierarchical rbac.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * <li>limit - max number of objects to return.</li>
     * </ul>
     *
     * @param role  Contains {@link Role#name} of Role entity assigned to user.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing userIds assigned to a particular role.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public List<String> assignedUsers(Role role, int limit)
        throws SecurityException;


    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains {@link User#userId} matching User entity targeted in the directory.
     * @return List of type UserRole containing the Roles assigned to User.
     * @throws com.jts.fortress.SecurityException
     *          If user not found or system error occurs.
     */
    public List<UserRole> assignedRoles(User user)
        throws SecurityException;

    /**
     * This method returns the data set of all users who are assigned the given role.  This searches the User data set for
     * Role relationship.  This method does NOT search for hierarchical RBAC Roles relationships.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains the role name, {@link Role#name} used to search the User data set.
     * @return List of type User containing the users assigned data.
     * @throws com.jts.fortress.SecurityException
     *          If system error occurs.
     */
    public List<User> assignedUsers(Role role)
        throws SecurityException;


    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     *
     * @param userId matches userId stored in the directory.
     * @return List of type String containing the role names of all roles assigned to user.
     * @throws com.jts.fortress.SecurityException
     *          If user not found or system error occurs.
     */
    public List<String> assignedRoles(String userId)
        throws SecurityException;


    /**
     * This function returns the set of users authorized to a given role, i.e., the users that are assigned to a role that
     * inherits the given role. The function is valid if and only if the given role is a member of the ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Contains role name, {@link Role#name} of Role entity assigned to User.
     * @return List of type User containing all user's that having matching role assignment.
     * @throws com.jts.fortress.SecurityException
     *          In the event the role is not present in directory or system error occurs.
     */
    public List<User> authorizedUsers(Role role)
        throws SecurityException;


    /**
     * This function returns the set of roles authorized for a given user. The function is valid if
     * and only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains the {@link User#userId} matching User entity stored in the directory.
     * @return Set of type String containing the roles assigned and roles inherited.
     * @throws SecurityException If user not found or system error occurs.
     */
    public Set<String> authorizedRoles(User user)
        throws SecurityException;


    /**
     * This function returns the set of all permissions (op, obj), granted to or inherited by a
     * given role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains role name, {@link Role#name} of Role entity Permission is granted to.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws com.jts.fortress.SecurityException
     *          In the event system error occurs.
     */
    public List<Permission> rolePermissions(Role role)
        throws SecurityException;


    /**
     * This function returns the set of permissions a given user gets through his/her authorized
     * roles. The function is valid if and only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains the {@link User#userId} of User targeted for search.
     * @return List of type Permission containing matching permission entities.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public List<Permission> userPermissions(User user)
        throws SecurityException;


    /**
     * Return a list of type String of all roles that have granted a particular permission.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type string containing the Role names that have the matching perm granted.
     * @throws SecurityException in the event permission not found or system error occurs.
     */
    public List<String> permissionRoles(Permission perm)
        throws SecurityException;


    /**
     * Return all role names that have been authorized for a given permission.  This will process role hierarchies to determine set of all Roles who have access to a given permission.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all roles names that have been granted a particular permission.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public Set<String> authorizedPermissionRoles(Permission perm)
        throws SecurityException;


    /**
     * Return all userIds that have been granted (directly) a particular permission.  This will not consider assigned or authorized Roles.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type String containing all userIds that have been granted a particular permission.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public List<String> permissionUsers(Permission perm)
        throws SecurityException;


    /**
     * Return all userIds that have been authorized for a given permission.  This will process role hierarchies to determine set of all Users who have access to a given permission.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all userIds that have been granted a particular permission.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public Set<String> authorizedPermissionUsers(Permission perm)
        throws SecurityException;


    /**
     * This function returns the list of all SSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all SSD role sets.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Will contain the role name, {@link Role#name}, for targeted SSD set or null to return all
     * @return List containing all matching SSD's.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public List<SDSet> ssdRoleSets(Role role)
        throws SecurityException;

    /**
     * This function returns the SSD data set that matches a particular set name.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param set Will contain the name for existing SSD data set, {@link SDSet#name}.
     * @return SDSet containing all attributes from matching SSD name.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public SDSet ssdRoleSet(SDSet set)
        throws SecurityException;


    /**
     * This function returns the set of roles of a SSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name for the SSD set targeted, {@link SDSet#name}.
     * @return Set containing all Roles that are members of SSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    public Set<String> ssdRoleSetRoles(SDSet ssd)
        throws SecurityException;

    /**
     * This function returns the cardinality associated with a SSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name of the SSD set targeted, {@link SDSet#name}.
     * @return int value containing cardinality of SSD set.
     * @throws SecurityException in the event of data or system error.
     */
    public int ssdRoleSetCardinality(SDSet ssd)
        throws SecurityException;


    /**
     * This function returns the list of all dSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all dSD role sets.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Will contain the role name, {@link Role#name}, for targeted dSD set or null to return all
     * @return List containing all matching dSD's.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public List<SDSet> dsdRoleSets(Role role)
        throws SecurityException;

    /**
     * This function returns the DSD data set that matches a particular set name.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param set Will contain the name for existing DSD data set, {@link SDSet#name}.
     * @return SDSet containing all attributes from matching DSD name.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public SDSet dsdRoleSet(SDSet set)
        throws SecurityException;


    /**
     * This function returns the set of roles of a DSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name for the DSD set targeted, {@link SDSet#name}.
     * @return Set containing all Roles that are members of DSD data set.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public Set<String> dsdRoleSetRoles(SDSet dsd)
        throws SecurityException;

    /**
     * This function returns the cardinality associated with a DSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name of the DSD set targeted, {@link SDSet#name}.
     * @return int value containing cardinality of DSD set.
     * @throws SecurityException in the event of data or system error.
     */
    public int dsdRoleSetCardinality(SDSet dsd)
        throws SecurityException;
}
