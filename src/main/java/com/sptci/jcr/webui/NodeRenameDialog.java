package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.TextField;

import static com.sptci.echo.Application.getApplication;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * A dialog used to capture user input for the new name to assign to the
 * specified node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-25
 * @version $Id: NodeRenameDialog.java 47 2010-01-21 20:38:45Z spt $
 */
public class NodeRenameDialog<V extends TableView> extends WindowPane
{
  private static final long serialVersionUID = 1;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private transient final Node node;

  private Label oldnameLabel;
  private TextField oldname;

  private Label nameLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeRenameListener" )
  private TextField name;

  /**
   * An optional view for when the dialog is launched from a
   * query/search results view.
   */
  @SuppressWarnings( { "NonSerializableFieldInSerializableClass" } )
  private V view;

  public NodeRenameDialog( final Node node )
  {
    new ViewInitialiser<NodeRenameDialog>( this ).init();
    this.node = node;
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    oldname.setEnabled( false );

    try
    {
      oldname.setText( node.getName() );
    }
    catch ( RepositoryException e )
    {
      getApplication().processFatalException( e );
    }

    final Grid row = new Grid();
    row.add( oldnameLabel );
    row.add( oldname );
    row.add( nameLabel );
    row.add( name );
    add( row );
  }

  public Node getNode() { return node; }
  public String getName() { return name.getText(); }

  public V getView()
  {
    return view;
  }

  public void setView( final V view )
  {
    this.view = view;
  }
}
