/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress;


import org.openldap.fortress.cfg.Config;


/**
 * This class contains constants that must be defined globally but are not to be used by external programs.
 * The constants are used internally by Fortress when looking up cfg values, performing maintenance on
 * standard and custom ldap objects and attributes, instantiating manager instances, validating objects and attributes, and more.
 * Some of the values for public constants defined here must be known to end users of system to declare system cfg parameters, see {@link org.openldap.fortress.cfg.Config}.
 * For example the {@link #SUFFIX} constant uses key name {@code suffix} which must have a corresponding value, i.e. {@code dc=example,dc=com},
 * which tells location of Directory Information Tree to the Fortress runtime processor.
 * </p>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public class GlobalIds
{
    public static final String HOME = "HOME";
    public static final String TENANT = "tenant";
    private static final String ENABLE_AUDIT = "enable.audit";
    public static final boolean IS_AUDIT = ( ( Config.getProperty( ENABLE_AUDIT ) != null ) && ( Config
        .getProperty( ENABLE_AUDIT ).equalsIgnoreCase( "true" ) ) );
    private static final String ENABLE_REST = "enable.mgr.impl.rest";
    public static final boolean IS_REST = ( ( Config.getProperty( ENABLE_REST ) != null ) && ( Config
        .getProperty( ENABLE_REST ).equalsIgnoreCase( "true" ) ) );

    /**
     * The following constants are used within the factory classes:
     */

    /**
     * When this optional tag, {@code accessmgr.implementation}, is placed in Fortress properties, its class name will be the default {@link org.openldap.fortress.AccessMgr} instance used.
     */
    public final static String ACCESS_IMPLEMENTATION = "accessmgr.implementation";

    /**
     * When this optional tag, {@code adminImplementation}, is placed in Fortress properties, its class name will be the default {@link org.openldap.fortress.AdminMgr} instance used.
     */
    public final static String ADMIN_IMPLEMENTATION = "adminmgr.implementation";

    /**
     * When this optional tag, {@code daoConnector}, is placed in Fortress properties,
     */
    public final static String DAO_CONNECTOR = "dao.connector";

    /**
     * When this optional tag, {@code reviewImplementation}, is placed in Fortress properties, its class name will be the default {@link org.openldap.fortress.ReviewMgr} instance used.
     */
    public final static String REVIEW_IMPLEMENTATION = "reviewmgr.implementation";

    /**
     * When this optional tag, {@code policyImplementation}, is placed in Fortress properties, its class name will be the default {@link PwPolicyMgr} instance used.
     */
    public final static String PSWD_POLICY_IMPLEMENTATION = "policymgr.implementation";

    /**
     * When this optional tag, {@code auditmgr.implementation}, is placed in Fortress properties, its class name will be the default {@link org.openldap.fortress.AuditMgr} instance used.
     */
    public final static String AUDIT_IMPLEMENTATION = "auditmgr.implementation";

    /**
     * When this optional tag, {@code delegatedAdminImplementation}, is placed in Fortress properties, its class name will be the default {@link DelAdminMgr} instance used.
     */
    public final static String DELEGATED_ADMIN_IMPLEMENTATION = "delegated.adminmgr.implementation";

    /**
     * When this optional tag, {@code delegatedReviewImplementation}, is placed in Fortress properties, its class name will be the default {@link DelReviewMgr} instance used.
     */
    public final static String DELEGATED_REVIEW_IMPLEMENTATION = "delegated.reviewmgr.implementation";

    /**
     * When this optional tag, {@code delegatedAccessImplementation}, is placed in Fortress properties, its class name will be the default {@link DelAccessMgr} instance used.
     */
    public final static String DELEGATED_ACCESS_IMPLEMENTATION = "delegated.accessmgr.implementation";

    /**
     * When this optional tag, {@code configImplementation}, is placed in Fortress properties, its class name will be the default {link ConfigMgr} instance used.
     */
    public final static String CONFIG_IMPLEMENTATION = "configmgr.implementation";

    /**
     * When this optional tag, {@code accelsmgr.implementation}, is placed in Fortress properties, its class name will be the default {@link org.openldap.fortress.AccelMgr} instance used.
     */
    public final static String ACCEL_IMPLEMENTATION = "accelmgr.implementation";

    //	AUTHENTICATION_TYPE
    /**
     * This property is used to specify if authentication is being performed within a security realm.
     */
    public final static String AUTHENTICATION_TYPE = "authn.type";

    /**
     * Specifies realm authentication mode.
     */
    public final static String REALM_TYPE = "REALM";

    /**
     * Used to declare validation modules that are used to process constraint checks during session activation.
     */
    public final static String VALIDATOR_PROPS = "temporal.validator.";

    /**
     * The DSD validator performs Dynamic Separation of Duty checks during role activation.
     */
    public final static String DSD_VALIDATOR_PROP = "temporal.validator.dsd";

    /**
     * This constant is used during authentication to determine if runtime is security realm.  If IS_REALM == true,
     * the authentication module will not throw SecurityException on password resets.  This is to enable the authentication
     * event to succeed allowing the application to prompt user to change their password.
     */
    public final static boolean IS_REALM = GlobalIds.REALM_TYPE.equalsIgnoreCase( org.openldap.fortress.cfg.Config
        .getProperty( GlobalIds.AUTHENTICATION_TYPE ) );

    /**
     * Constant is used to tell ldap server to return no attributes on search.
     */
    public final static String[] NO_ATRS =
        {
            "1.1"
    };

    /**
     * Parameter specifies the distinguished name (dn) of the LDAP suffix.  The is the root or top-most node for a Directory Information Tree (DIT).  The typical
     * Fortress suffix format is {@code dc=example,dc=com}.
     */
    public final static String SUFFIX = "suffix";

    /**
     * Specifies the dn of the container where the Fortress User data set is located within DIT.  This is typically in the format of
     * {@code ou=People, dc=example, dc=com}
     */
    public final static String USER_ROOT = "user.root";

    /**
     * Specifies the dn of the container where the Fortress Permissions are located.  This is typically in the format of
     * {@code ou=Permissions,ou=RBAC,dc=example,dc=com}
     */
    public final static String PERM_ROOT = "perm.root";

    /**
     * Specifies the dn of the container where the Fortress RBAC Roles are located.  This is typically in the format of
     * {@code ou=Roles,ou=RBAC,dc=example,dc=com}
     */
    public final static String ROLE_ROOT = "role.root";

    /**
     * Specifies the dn of the container where the Fortress Password Polices are located.  This is typically in the format of
     * {@code ou=Policies,dc=example,dc=com}
     */
    public final static String PPOLICY_ROOT = "pwpolicy.root";

    /**
     * Specifies the dn of the container where the Fortress SSD and DSD constraints are located.  This is typically in the format of
     * {@code ou=Constraints,ou=RBAC,dc=example,dc=com}
     */
    public final static String SD_ROOT = "sdconstraint.root";

    /**
     * Specifies the dn of the container where the Fortress User OU pools are located.  This is typically in the format of
     * {@code ou=OS-U,ou=ARBAC,dc=example,dc=com}
     */
    public final static String OSU_ROOT = "userou.root";

    /**
     * Specifies the dn of the container where the Fortress Permission OU pools are located.  This is typically in the format of
     * {@code ou=OS-P,ou=ARBAC,dc=example,dc=com}
     */
    public final static String PSU_ROOT = "permou.root";

    /**
     * Specifies the dn of the container where the Fortress Administrative Roles are located.  This is typically in the format of
     * {@code ou=AdminRoles,ou=ARBAC,dc=example,dc=com}
     */
    public final static String ADMIN_ROLE_ROOT = "adminrole.root";

    /**
     * Specifies the dn of the container where the Fortress Administrative Permissions are located.  This is typically in the format of
     * {@code ou=AdminPerms,ou=ARBAC,dc=example,dc=com}
     */
    public final static String ADMIN_PERM_ROOT = "adminperm.root";

    /*
      *  *************************************************************************
      *  **  BEGIN LDAP STANDARD ATTRIBUTE NAMES
      *  ************************************************************************
      */

    /*
     * Constant contains the top object class name that is at the root of all ldap object class structures.
     */
    public final static String TOP = "top";

    /**
     * Constant contains the organization object class name.
     */
    public final static String ORGANIZATION_CLASS = "organization";

    /**
     * Constant contains the suffix's dcObject object class name.
     */
    public final static String SUFFIX_CLASS = "dcObject";

    /**
     * Constant contains the userId attribute name for inetOrgPerson object class.
     */
    public final static String UID = "uid";

    /**
     * Constant contains the common name attribute name used within inetOrgPerson and other ldap object classes.
     */
    public final static String CN = "cn";

    /**
     * Constant contains the ldap distinguished name attribute.
     */
    public final static String DN = "dn";

    /**
     * Used within search operation using the OpenLDAP proxy control.
     */
    public final static String UTF8 = "UTF-8";

    /**
     * Constant contains the description attribute name used within inetOrgPerson and other ldap object classes.
     */
    public final static String DESC = "description";

    /**
     * Constant contains the ou attribute name used within inetOrgPerson and other ldap object classes.
     */
    public final static String OU = "ou";

    /**
     * Password policy object class structure uses cn attribute name.
     */
    public final static String POLICY_NODE_TYPE = CN;

    /**
     * Constant contains the ldap object class attribute name.
     */
    public final static String OBJECT_CLASS = "objectclass";

    /*
    *  *************************************************************************
    *  **  OPENLDAP PW POLICY ATTRIBUTES AND CONSTANTS
    *  ************************************************************************
    */

    public static final String SERVER_TYPE = "ldap.server.type";
    public static final boolean IS_OPENLDAP = ( ( Config.getProperty( SERVER_TYPE ) != null ) && ( Config
        .getProperty( SERVER_TYPE ).equalsIgnoreCase( "openldap" ) ) );

    /**
     * OpenLDAP uses this object class to store policy definitions that are processed by the pw policy overlay.
     */
    //private final static String OPENLDAP_PW_POLICY_IMPL = "password.policy";

    /**
     * When openldap password policy is in effect the openldap pw policy response control is interrogated subsequent to bind operations.
     */
    //private final static String OPENLDAP_SERVER = "openldap";

    /**
     * When set to true the pw policy control is interrogated.
     */
    //public final static boolean OPENLDAP_IS_PW_POLICY_ENABLED = (org.openldap.fortress.cfg.Config.getProperty(GlobalIds.OPENLDAP_PW_POLICY_IMPL) != null && (Config.getProperty(GlobalIds.OPENLDAP_PW_POLICY_IMPL)).equalsIgnoreCase(GlobalIds.OPENLDAP_SERVER));

    /**
     * This is control id openldap uses to communicate password messages to the ldap client subsequent to bind operations:
     */
    public final static String OPENLDAP_PW_RESPONSE_CONTROL = "1.3.6.1.4.1.42.2.27.8.5.1";

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr AUDIT
      *  ************************************************************************
      */
    /**
     * This object class contains Fortress audit contextual information.
     */
    public final static String FT_MODIFIER_AUX_OBJECT_CLASS_NAME = "ftMods";

    /**
     * The ftModifier contains the internalUserId of administrator who performed action.
     */
    public final static String FT_MODIFIER = "ftModifier";

    /**
     * The {@code ftModCode} attribute contains the permission object name and operation of admin function performed.
     */
    public final static String FT_MODIFIER_CODE = "ftModCode";

    /**
     * The {@code ftModId} contains a globally unique id that is bound to the audit event entity.
     */
    public final static String FT_MODIFIER_ID = "ftModId";

    /**
     * The {@code ftId} contains a globally unique id that is bound to the application entity.
     */
    public final static String FT_IID = "ftId";

    /**
     * This string literal contains a common start for most ldap search filters that fortress uses.
     */
    public final static String FILTER_PREFIX = "(&(objectclass=";

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr PROPERTIES are used by USER, PERM, CONFIG DAO'S.
      *  ************************************************************************
      */
    /**
     * The {@code ftProperties} object class contains name-value pairs that are neither validated nor constrained.
     * Properties are application defined parameters and clients may store any reasonable values.
     */
    public final static String PROPS_AUX_OBJECT_CLASS_NAME = "ftProperties";

    /**
     * The {@code ftProps} attribute contains a single name-value pairs that is {@code :} separated.
     */
    public final static String PROPS = "ftProps";

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr ROLE STATICS are used by RBAC and ARBAC DAO
      *  ************************************************************************
      */

    /**
     * The object class is used to store Fortress Role entity data.
     */
    public final static String ROLE_OBJECT_CLASS_NM = "ftRls";

    /**
     * Defines the object class structure used within Fortress Role processing.
     */
    public final static String ROLE_OBJ_CLASS[] =
        {
            GlobalIds.TOP,
            ROLE_OBJECT_CLASS_NM,
            GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr CONSTRAINTS are used by USER, ROLE, ADMINROLE DAO'S.
      *  ************************************************************************
      */

    /**
     * Constraint AUX Object Class Schema definitions:
     */

    /**
     * This single occurring attribute is used to store constraint policies on Fortress User objects.
     */
    public final static String CONSTRAINT = "ftCstr";

    // USER Role Definitions:

    /**
     * Multi-occurring attribute contains RBAC Role assignments for Users.
     */
    public final static String USER_ROLE_ASSIGN = "ftRA";

    /**
     * Multi-occurring attribute contains constraint policies for RBAC Role assignments for Users.
     */
    public final static String USER_ROLE_DATA = "ftRC";

    /**
     * Multi-occurring attribute contains Administrative Role assignments for Users.
     */
    public final static String USER_ADMINROLE_ASSIGN = "ftARA";

    /**
     * Multi-occurring attribute contains constraint policies for Administrative Role assignments for Users.
     */
    public final static String USER_ADMINROLE_DATA = "ftARC";

    /**
     * Attribute name for storing Fortress permission object names.
     */
    public final static String POBJ_NAME = "ftObjNm";

    /**
     * Attribute name for storing parent node names for hierarchical processing.
     */
    public final static String PARENT_NODES = "ftParents";

    /*
    *  *************************************************************************
    *  **  RBAC Entity maximum length constants
    *  ************************************************************************
    */

    /**
     * Fortress userId cannot exceed length of 40.
     */
    public final static int USERID_LEN = 40;

    /**
     * Fortress role names cannot exceed length of 40.
     */
    public final static int ROLE_LEN = 40;

    /**
     * Fortress description text cannot exceed length of 80.
     */
    public final static int DESC_LEN = 180;

    /**
     * Fortress permission names cannot exceed length of 100.
     */
    public final static int PERM_LEN = 100;

    /**
     * Fortress User passwords must have length of 50 or less..
     */
    public final static int PASSWORD_LEN = 50;

    /**
     * Fortress password policy names cannot exceed length of 40.
     */
    public final static int PWPOLICY_NAME_LEN = 40;

    /**
     * Fortress ou's cannot exceed length of 40.
     */
    public final static int OU_LEN = 40;

    /**
     * Fortress User surname cannot exceed length of 80.
     */
    public final static int SN_LEN = 80;

    /**
     * Fortress common name attributes cannot exceed length of 80.
     */
    public final static int CN_LEN = 80;

    /**
     * Fortress properties cannot exceed length of 100.
     */
    public final static int PROP_LEN = 100;

    // Regular Expression Patterns stored in Fortress config file:
    public final static String REG_EX_SAFE_TEXT = "regXSafetext";

    /*
      *  *************************************************************************
      *  **  LDAP FILTER CONSTANTS
      *  ************************************************************************
      */
    /**
     * Used to define characters that must be encoded before being processed by ldap operations.
     */
    public final static String LDAP_FILTER = "ldap.filter.";

    /**
     * Used to define encoded replacements for characters to be filtered.
     */
    public final static String LDAP_SUB = "ldap.sub.";

    /**
     * Defines how many entries are to be stored in the encoding set.
     */
    public final static String LDAP_FILTER_SIZE_PROP = "ldap.filter.size";

    /**
     * Used during ldap filter processing.
     */
    public final static boolean LDAP_FILTER_SIZE_FOUND = ( org.openldap.fortress.cfg.Config
        .getProperty( LDAP_FILTER_SIZE_PROP ) != null );
    public static final String APACHE_LDAP_API = "apache";

    /**
     * maximum number of entries allowed for ldap filter replacements.
     */
    private static int ldapFilterSize = 25;

    /**
     * enable the ldap filter size variable to be used later during filter processing.
     */
    static
    {
        try
        {
            String lenProp = org.openldap.fortress.cfg.Config.getProperty( LDAP_FILTER_SIZE_PROP );
            if ( LDAP_FILTER_SIZE_FOUND )
            {
                Integer len = new Integer( lenProp );
                ldapFilterSize = len;
            }
        }
        catch ( java.lang.NumberFormatException nfe )
        {
            //ignore
        }
    }

    /**
     * Maximum number of entries allowed for ldap filter replacements.
     */
    public static final int LDAP_FILTER_SIZE = ldapFilterSize;

    /**
     * This property contains the location for the remote Fortress properties stored in ldap.  This is typically in the format of
     * {@code cn=DEFAULT,ou=Config,dc=example,dc=com}
     */
    public static final String CONFIG_REALM = "config.realm";

    /**
     * Fortress stores name-value pairs within multi-occurring attributes in ldap.  Usually a separator of ':' is used
     * format: {@code name:value},
     */
    public final static char PROP_SEP = ':';

    /**
     * Fortress stores complex attribute types within a single attribute in ldap.  Usually a delimiter of '$' is used for string tokenization.
     * format: {@code part1$part2$part3....}  Stored in fortress.properties as 'attr.delimiter=$'
     */
    public static final String DELIMITER = Config.getProperty( "attr.delimiter", "$" );

    /**
     * Maximum number of records for ldap client to wait on while processing results sets from ldap server.
     */
    public static final int BATCH_SIZE = 100;

    /**
     * Attribute is used in Fortress time/date constraints as default which will always pass.  i.e. values stored as beginDate=none or beginTime=none will turn the date and time constraints off
     * for a particular entity..
     */
    public static final String NONE = "none";

    /**
     * Attribute is used in Fortress day mask constraints as default which will always pass.  i.e. values stored as dayMask=all will always pass the day of week constraint.
     */
    public static final String ALL = "all";
    public static final String NULL = "null";
    public static final String POP_NAME = "ftOpNm";
}