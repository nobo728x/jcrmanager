package com.sptci.jcr.webui.table;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple list backed property iterator implementation.
 *
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-19
 * @version $Id: PropertyIteratorImpl.java 66 2010-02-22 15:24:18Z spt $
 */
public class PropertyIteratorImpl implements PropertyIterator
{
  private final ArrayList<Property> properties = new ArrayList<Property>();

  private int index;

  public PropertyIteratorImpl( final Collection<Property> properties )
  {
    this.properties.addAll( properties );
  }

  public Property nextProperty()
  {
    return properties.get( index++ );
  }

  public void skip( final long position )
  {
    index = (int) position;
  }

  public long getSize()
  {
    return properties.size();
  }

  public long getPosition()
  {
    return index;
  }

  public boolean hasNext()
  {
    return properties.size() > index;
  }

  public Object next()
  {
    return nextProperty();
  }

  public void remove()
  {
    properties.remove( index );
  }
}
