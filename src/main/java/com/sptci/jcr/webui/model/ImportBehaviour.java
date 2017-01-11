package com.sptci.jcr.webui.model;

/**
 * An enumeration for the constants defined in {@link javax.jcr.ImportUUIDBehavior}
 * for controlling the behaviour of UUID values being imported into the
 * repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-25
 * @version $Id: ImportBehaviour.java 22 2009-08-26 14:42:03Z spt $
 */
public enum ImportBehaviour
{
  /**
   * The value that maps to {@link
   * javax.jcr.ImportUUIDBehavior#IMPORT_UUID_CREATE_NEW}
   */
  Create,

  /**
   * The value that maps to {@link
   * javax.jcr.ImportUUIDBehavior#IMPORT_UUID_COLLISION_REMOVE_EXISTING}
   */
  Remove,

  /**
   * The value that maps to {@link
   * javax.jcr.ImportUUIDBehavior#IMPORT_UUID_COLLISION_REPLACE_EXISTING}
   */
  Replace,

  /**
   * The value that maps to {@link
   * javax.jcr.ImportUUIDBehavior#IMPORT_UUID_COLLISION_THROW}
   */
  Error
}
