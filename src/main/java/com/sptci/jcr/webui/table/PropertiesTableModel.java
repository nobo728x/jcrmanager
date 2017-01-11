package com.sptci.jcr.webui.table;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-16
 * @version $Id: PropertiesTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public class PropertiesTableModel extends NodePropertiesTableModel
{
  private static final long serialVersionUID = 1L;

  public PropertiesTableModel( final Node node ) { super( node ); }

  @Override
  protected void populate()
  {
    data.clear();
    
    try
    {
      final PropertyIterator iter = node.getProperties();

      while ( iter.hasNext() )
      {
        final Property property = iter.nextProperty();
        if ( property.getName().startsWith( "jcr:" ) ) continue;
        
        data.add( property );
      }
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }
}
