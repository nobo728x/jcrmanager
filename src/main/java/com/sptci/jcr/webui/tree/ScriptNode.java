package com.sptci.jcr.webui.tree;

import com.sptci.echo.tree.TreeNode;

import static com.sptci.jcr.Environment.environment;

import java.io.File;

/**
 * A tree node that represents a script directory or a script file.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptNode.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptNode extends TreeNode<File>
{
  private static final long serialVersionUID = 1L;

  private ScriptTreeModel model;

  public ScriptNode()
  {
    this( new File( environment.getScriptDirectory() ) );
  }

  public ScriptNode( final File file )
  {
    super( file );
  }

  public void add( final ScriptNode node )
  {
    super.add( node );
    model.addNode( node );
  }

  /** Over-ridden to decide based on whether file is directory or not. */
  @Override
  public boolean isLeaf()
  {
    return ! getUserObject().isDirectory();
  }

  @Override
  public String toString()
  {
    return getUserObject().getName();
  }

  /** Over-ridden to lazily load children and return count. */
  @Override
  public int getChildCount()
  {
    if ( ! initialised ) createChildren();
    return super.getChildCount();
  }

  private void createChildren()
  {
    initialised = true;
    if ( ! getUserObject().isDirectory() ) return;
    
    for ( final File file : getUserObject().listFiles() )
    {
      if ( ! file.isHidden() )
      {
        final ScriptNode child = new ScriptNode( file );
        child.setModel( model );
        add( child );
      }
    }
  }

  public ScriptTreeModel getModel() { return model; }

  public void setModel( final ScriptTreeModel model ) { this.model = model; }
}
