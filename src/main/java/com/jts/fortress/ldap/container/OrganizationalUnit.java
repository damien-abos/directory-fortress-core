/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap.container;


/**
 * This class contains the container node for the OpenLDAP Directory Information Tree.  A container node is used to
 * group other related nodes, i.e. 'ou=People' or 'ou'Roles'.
 * <br />The organizational unit object class is 'organizationalUnit' <br />
 * <p/>
 * organizational unit structural object class is used to organize groups of nodes within the DIT.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>#Standard object class from RFC2256</code>
 * <li> <code>objectclass ( 2.5.6.5 NAME 'organizationalUnit'</code>
 * <li> <code>DESC 'RFC2256: an organizational unit'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST ou</code>
 * <li> <code>MAY ( userPassword $ searchGuide $ seeAlso $ businessCategory $</code>
 * <li> <code>x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>

 *
 * @author Shawn McKinney
 * @created January 21, 2010
 */
public class OrganizationalUnit
{
    private String name;
    private String parent;
    private String description;

    /**
     * Generate instance of organizational unit object to be loaded as container node.
     *
     * @param name        required attribute must be unique for rDn level and maps to 'ou' attribute in 'organizationalUnit' object class.
     * @param description maps optional attribute maps to name in 'organizationalUnit' object class.
     */
    public OrganizationalUnit(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor generates instance of organizational unit object to be loaded as container node.
     * The object cannot be used until 'name' value is set.
     */
    public OrganizationalUnit()
    {
    }

    /**
     * Get the required name attribute from the entity.  This attribute must be unique for the level of tree it is
     * set.
     *
     * @return required attribute must be unique for rDn level and maps to 'ou' attribute in 'organizationalUnit' object class.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the required name attribute in the entity.  This attribute must be unique for the level of tree it is
     * set.
     *
     * @param name is required attribute and must be unique for rDn level and maps to 'ou' attribute in 'organizationalUnit' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the description for the organizational unit object.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to same name attribute on 'organizationalUnit'.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description for the organizational unit object.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description field maps to same name attribute on 'organizationalUnit'.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get the optional parent attribute allows nesting of container nodes two levels below suffix.  For example, if parent
     * node is created it may be used to subdivide collections of related nodes, dn=ou=Roles, ou=RBAC, dc=companyName, dc=com.
     *
     * @return attribute that contains name of parent node that is used to construct the dn.
     */
    public String getParent()
    {
        return parent;
    }

    /**
     * Set the optional parent attribute allows nesting of container nodes two levels below suffix.  For example, if parent
     * node is created it may be used to subdivide collections of related nodes, dn=ou=Roles, ou=RBAC, dc=companyName, dc=com.
     *
     * @param parent attribute that contains name of parent node that is used to construct the dn.  This maps to 'ou'
     *               attribute in parent node's 'organizationalUnit' object class.
     */
    public void setParent(String parent)
    {
        this.parent = parent;
    }
}
