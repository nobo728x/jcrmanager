package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.list.ListBox;
import com.sptci.jcr.webui.model.MixinTypeListModel;

import nextapp.echo.app.Button;
import nextapp.echo.app.SplitPane;

import static com.sptci.echo.Application.getApplication;
import static nextapp.echo.app.Color.WHITE;
import static nextapp.echo.app.list.ListSelectionModel.MULTIPLE_SELECTION;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The dialog used to edit the {@code mixin} types associated with a specified
 * node.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-21
 * @version $Id: NodeMixinTypesDialog.java 48 2010-01-21 20:51:31Z spt $
 */
public class NodeMixinTypesDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private transient final Node node;
  private ListBox<MixinTypeListModel> mixins;

  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeMixinTypesListener" )
  private Button save;

  public NodeMixinTypesDialog( final Node node )
  {
    this.node = node;
    setMaximizeEnabled( false );
    setBackground( WHITE );
    new ViewInitialiser<NodeMixinTypesDialog>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final SplitPane pane = new SplitPane( SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP );
    pane.setAutoPositioned( true );

    mixins.setModel( new MixinTypeListModel() );
    mixins.setSelectionMode( MULTIPLE_SELECTION );
    setSelectedMixins();

    pane.add( save );
    pane.add( mixins );
    add( pane );
  }

  private void setSelectedMixins()
  {
    try
    {
      if ( node.getMixinNodeTypes().length == 0 ) return;

      final Set<NodeType> set = getMixinTypes();

      int[] indices = new int[ node.getMixinNodeTypes().length ];
      int count = 0;
      int index = 0;

      for ( final NodeType type : mixins.getModel().getData() )
      {
        if ( set.contains( type ) ) indices[index++] = count;
        ++count;
      }

      mixins.setSelectedIndices( indices );
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }
  }

  public Set<NodeType> getMixinTypes() throws RepositoryException
  {
    final HashSet<NodeType> set = new HashSet<NodeType>();
    set.addAll( Arrays.asList( node.getMixinNodeTypes() ) );
    return set;
  }

  public Collection<String> getSelectedMixinTypes()
  {
    final ArrayList<String> collection = new ArrayList<String>();

    for ( final int index : mixins.getSelectedIndices() )
    {
      collection.add( mixins.getModel().getObject( index ).getName() );
    }

    return collection;
  }

  public Node getNode() { return node; }
}
