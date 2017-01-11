package com.sptci.jcr.webui.model;

/**
 * An enumeration of the standard value types mandated by JCR specifications.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: ValueTypes.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "ClassWithTooManyFields" } )
public enum ValueTypes
{
  String,
  Boolean,
  Long,
  Double,
  Decimal,
  Date,
  Path,
  WeakReference,
  Reference,
  Binary,
  URI
}
