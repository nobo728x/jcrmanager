package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.list.ListBox;
import com.sptci.echo.list.SelectField;
import com.sptci.jcr.webui.model.MixinTypeListModel;
import com.sptci.jcr.webui.model.NodeTypeListModel;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.echo.Utilities.createLabel;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;
import static nextapp.echo.app.list.ListSelectionModel.MULTIPLE_SELECTION;
import static org.apache.jackrabbit.JcrConstants.NT_FILE;

import javax.jcr.Node;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeCreateDialog.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "ClassWithTooManyFields", "UnusedDeclaration" } )
public class NodeCreateDialog<V extends TableView> extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private Label nameLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField name;

  private Label parentPathLabel;
  private NodeSelectionComponent parentPath;

  private Label nodeTypeLabel;
  private SelectField nodeType;
  private FileUploadComponent upload;

  private Label mixinTypesLabel;
  private ListBox<MixinTypeListModel> mixinTypes;

  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeCreateListener" )
  private Button create;

  @SuppressWarnings( { "NonSerializableFieldInSerializableClass" } )
  private V view;
  
  public NodeCreateDialog( final Node node )
  {
    new ViewInitialiser<NodeCreateDialog>( this ).init();
    parentPath = new NodeSelectionComponent( node );
  }

  /** Layout the view when the pane is added to the hierarchy. */
  @Override
  public void init()
  {
    removeAll();
    super.init();

    final SplitPane pane = new SplitPane( ORIENTATION_VERTICAL_BOTTOM_TOP );
    pane.setAutoPositioned( true );

    createButtons( pane );
    createGrid( pane );

    getApplication().setFocusedComponent( name );
    add( pane );
  }

  private void createGrid( final SplitPane pane )
  {
    createNodeType();
    createMixinTypes();
    
    final Grid grid = new Grid();

    grid.add( nameLabel );
    grid.add( name );
    grid.add( parentPathLabel );
    grid.add( parentPath );
    grid.add( nodeTypeLabel );
    grid.add( nodeType );
    grid.add( mixinTypesLabel );
    grid.add( mixinTypes );

    pane.add( grid );
  }

  private void createNodeType()
  {
    final NodeTypeListModel model = new NodeTypeListModel();
    nodeType.setModel( model );
    nodeType.setSelectedIndex( model.getIndex() );
    nodeType.addActionListener( new TypeListener() );
  }

  private void createMixinTypes()
  {
    mixinTypes.setModel( new MixinTypeListModel() );
    mixinTypes.setSelectionMode( MULTIPLE_SELECTION );
  }

  private void createButtons( final SplitPane pane )
  {
    final Row row = new Row();
    row.add( create );
    pane.add( row );
  }

  public String getName() { return name.getText(); }

  public void setName( final String name ) { this.name.setText( name ); }

  public String getPath() { return parentPath.getPath(); }

  public String getNodeType() { return (String) nodeType.getSelectedItem(); }

  public FileUploadComponent getUpload() { return upload; }
  
  public String[] getMixinTypes()
  {
    final Object[] values = mixinTypes.getSelectedValues();
    String[] types = new String[0];

    if ( values != null )
    {
      types = new String[ values.length ];

      for ( int i = 0; i < values.length; ++i )
      {
        types[i] = (String) values[i];
      }
    }

    return types;
  }

  public V getView()
  {
    return view;
  }

  public void setView( final V view )
  {
    this.view = view;
  }

  protected class TypeListener implements nextapp.echo.app.event.ActionListener
  {
    private static final long serialVersionUID = 1L;

    public void actionPerformed( final ActionEvent event )
    {
      if ( NT_FILE.equals( getNodeType() ) )
      {
        final Component parent = nodeType.getParent();
        parent.add( createLabel( NodeCreateDialog.class.getName(), "upload" ) );
        upload = new FileUploadComponent( name );
        parent.add( upload );
      }
    }
  }
}
