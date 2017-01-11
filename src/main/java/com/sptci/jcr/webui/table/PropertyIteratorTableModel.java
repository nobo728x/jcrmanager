package com.sptci.jcr.webui.table;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;

/**
 * A table model for working with a pre-generated property iterator.
 * 
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-18
 * @version $Id: PropertyIteratorTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public class PropertyIteratorTableModel extends AbstractPropertiesTableModel
{
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private final transient PropertyIterator properties;

  public PropertyIteratorTableModel( final PropertyIterator properties )
  {
    this.properties = properties;
  }

  @Override
  protected void populate()
  {
    data.clear();

    while ( properties.hasNext() )
    {
      final Property property = properties.nextProperty();
      data.add( property );
    }
  }
}
