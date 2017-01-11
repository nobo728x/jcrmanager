package com.sptci.jcr.webui.tree;

import echopoint.tree.DefaultTreeModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A tree model that maintains additional file path mappings to enable efficient
 * reverse look up of nodes based on a file path.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptTreeModel.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptTreeModel extends DefaultTreeModel
{
  private static final long serialVersionUID = 1L;

  /** The map used to retrieve nodes based on file path. */
  private Map<String,ScriptNode> map = new HashMap<String,ScriptNode>(); 

  public ScriptTreeModel()
  {
    super( new ScriptNode() );
    getRoot().setModel( this );
    addToMap( getRoot() );
  }

  public void addNode( final ScriptNode node )
  {
    addToMap( node );
  }

  public void addChild( final ScriptNode parent, final ScriptNode child )
  {
    super.addChild( parent, child );
    addToMap( child );
  }

  public void removeChild( final ScriptNode parent,
      final ScriptNode child, final String childPath )
  {
    super.removeChild( parent, child );
    map.remove( childPath );
  }

  public ScriptNode getParent( final File file )
  {
    ScriptNode parent = null;

    final String path = file.getAbsolutePath();

    if ( map.containsKey( path ) )
    {
      parent = (ScriptNode) map.get( path ).getParent();
    }

    return parent;
  }

  private void addToMap( final ScriptNode node )
  {
    map.put( node.getUserObject().getAbsolutePath(), node );
  }

  public ScriptNode getNode( final File file )
  {
    return map.get( file.getAbsolutePath() );
  }

  @Override
  public ScriptNode getRoot()
  {
    return (ScriptNode) super.getRoot();
  }
}
