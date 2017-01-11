package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Button;
import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Column;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.SelectField;
import nextapp.echo.app.list.DefaultListModel;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.echo.Configuration.getString;
import static nextapp.echo.app.Color.WHITE;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * The dialog used to get user input on the options for exporting a user
 * selected node.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-25
 * @version $Id: NodeExportDialog.java 54 2010-01-28 03:30:13Z spt $
 */
public class NodeExportDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private final transient Node node;

  private Label recursiveLabel;
  private CheckBox recursive = new CheckBox();

  private Label binaryLabel;
  private CheckBox binary = new CheckBox();

  private Label formatLabel;
  private CheckBox format = new CheckBox();

  private Label viewModeLabel;
  private SelectField viewMode;

  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeExportListener" )
  private Button export;

  public NodeExportDialog( final Node node )
  {
    this.node = node;
    new ViewInitialiser<NodeExportDialog>( this ).init();
    setBackground( WHITE );
  }

  public void init()
  {
    removeAll();
    super.init();

    final Column column = new Column();
    try
    {
      column.add( new Label( node.getPath() ) );
    }
    catch ( RepositoryException e )
    {
      getApplication().processFatalException( e );
    }

    recursive.setSelected( true );
    binary.setSelected( true );

    final Grid grid = new Grid();

    grid.add( recursiveLabel );
    grid.add( recursive );

    grid.add( binaryLabel );
    grid.add( binary );

    grid.add( formatLabel );
    grid.add( format );

    viewMode.setModel( new DefaultListModel(
        new String[] { getString( this, "systemView.text" ),
            getString( this, "documentView.text" ) } ) );

    grid.add( viewModeLabel );
    grid.add( viewMode );

    column.add( grid );
    column.add( export );
    add( column );
  }

  public boolean isRecursive() { return recursive.isSelected(); }

  public boolean isBinary() { return binary.isSelected(); }

  public boolean isFormat() { return format.isSelected(); }

  public boolean isSystemView()
  {
    return getString( this, "systemView.text" ).equals( viewMode.getSelectedItem() );
  }

  public Node getNode() { return node; }
}
