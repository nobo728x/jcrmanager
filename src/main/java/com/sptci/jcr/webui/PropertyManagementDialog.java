package com.sptci.jcr.webui;

import static com.sptci.echo.Application.getApplication;
import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.list.BooleanSelectField;
import com.sptci.echo.list.BooleanSelectField.BooleanValues;
import static com.sptci.jcr.webui.MainController.getController;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;

import echopoint.Strut;
import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextField;

import javax.jcr.RepositoryException;

/**
 * A base dialogue for creating/editing node properties.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyManagementDialog.java 24 2009-08-31 14:32:29Z spt $
 */
public abstract class PropertyManagementDialog extends WindowPane
{
  protected final PropertiesTable<PropertiesTableModel> table;

  protected Label nameLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  protected TextField name;

  protected Label typeLabel;

  protected Label versionableLabel;
  protected Component versionable;
  
  protected Label valueLabel;
  protected ValueContainer value;
  
  @ActionListener( value = "com.sptci.echo.WindowPaneCloseListener" )
  protected Button cancel;
  protected SplitPane pane;

  protected PropertyManagementDialog(
      final PropertiesTable<PropertiesTableModel> table )
  {
    this.table = table;
  }

  protected Component createControls( final SplitPane pane )
  {
    final Row row = new Row();
    row.add( new Strut() );
    row.add( cancel );
    pane.add( row );

    return row;
  }

  protected boolean versionable()
  {
    boolean result = false;

    try
    {
      result = getController().isVersionable( table.getModel().getNode() );
    }
    catch ( RepositoryException e )
    {
      getApplication().processFatalException( e );
    }

    return result;
  }

  protected void createVersionable()
  {
    final BooleanSelectField field = new BooleanSelectField();
    field.setSelectedItem(
        versionable() ? BooleanValues.Yes : BooleanValues.No );
    versionable = field;
  }

  public boolean isVersionable()
  {
    return ( (BooleanSelectField) versionable ).isTrue();
  }

  public PropertiesTable<PropertiesTableModel> getTable() { return table; }

  public String getName() { return name.getText(); }

  public ValueContainer getValue() { return value; }
}
