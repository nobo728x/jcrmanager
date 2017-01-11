package com.sptci.jcr.webui.model;

import com.sptci.echo.list.ListModel;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.jcr.webui.MainController.getController;
import static org.apache.jackrabbit.JcrConstants.NT_UNSTRUCTURED;
import org.apache.jackrabbit.core.nodetype.NodeTypeImpl;

import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;

import java.util.TreeMap;

/**
 * A list model that returns all the primary node types registered with the
 * repository.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-20
 * @version $Id: NodeTypeListModel.java 63 2010-02-16 15:23:00Z spt $
 */
public class NodeTypeListModel extends ListModel<NodeType>
{
  private static final long serialVersionUID = 1L;

  /** The index for {@code nt:unstructured} to make it default selection. */
  private int index;

  public NodeTypeListModel()
  {
    populate();
  }

  @Override
  protected String getValue( final NodeType model )
  {
    return model.getName();
  }

  /**
   * TODO Filter out abstract types when moving to JCR 2.0
   */
  private void populate()
  {
    final TreeMap<String,NodeType> map = new TreeMap<String,NodeType>();

    try
    {
      for ( final NodeTypeIterator iter =
          getController().getWorkspace().getNodeTypeManager().getPrimaryNodeTypes();
          iter.hasNext(); )
      {
        final NodeTypeImpl type = (NodeTypeImpl) iter.nextNodeType();
        if ( ! type.isAbstract() ) map.put( type.getName(), type );
      }
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }

    int count = 0;
    for ( final NodeType type : map.values() )
    {
      add( type );
      if ( NT_UNSTRUCTURED.equals( type.getName() ) ) index = count;
      ++count;
    }
  }

  public int getIndex()
  {
    return index;
  }
}
