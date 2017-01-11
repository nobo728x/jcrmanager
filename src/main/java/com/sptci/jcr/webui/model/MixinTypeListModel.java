package com.sptci.jcr.webui.model;

import com.sptci.echo.list.ListModel;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;

import java.util.TreeMap;

/**
 * A list model that returns all the mixin node types registered with the
 * repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-20
 * @version $Id: MixinTypeListModel.java 63 2010-02-16 15:23:00Z spt $
 */
public class MixinTypeListModel extends ListModel<NodeType>
{
  private static final long serialVersionUID = 1L;

  public MixinTypeListModel()
  {
    populate();
  }

  @Override
  protected String getValue( final NodeType model )
  {
    return model.getName();
  }

  private void populate()
  {
    final TreeMap<String,NodeType> map = new TreeMap<String,NodeType>();

    try
    {
      for ( final NodeTypeIterator iter =
          getController().getWorkspace().getNodeTypeManager().getMixinNodeTypes();
          iter.hasNext(); )
      {
        final NodeType type = iter.nextNodeType();
        map.put( type.getName(), type );
      }
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }

    for ( final NodeType type : map.values() )
    {
      add( type );
    }
  }
}