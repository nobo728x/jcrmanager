package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;

/**
 * A dialog used to capture the name of the new workspace to create.
 *
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-12
 * @version $Id: WorkspaceCreateDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public class WorkspaceCreateDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private final boolean clone;

  private Label nameLabel;
  @ActionListener( value = "com.sptci.jcr.webui.listener.WorkspaceCreateListener" )
  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField name;

  public WorkspaceCreateDialog( final boolean clone )
  {
    this.clone = clone;
    new ViewInitialiser<WorkspaceCreateDialog>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final Row row = new Row();
    row.add( nameLabel );
    row.add( name );
    add( row );

    getApplicationInstance().setFocusedComponent( name );
  }

  public String getName() { return name.getText(); }

  public boolean isClone() { return clone; }
}