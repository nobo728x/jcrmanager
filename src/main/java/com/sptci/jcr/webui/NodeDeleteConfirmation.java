package com.sptci.jcr.webui;

import com.sptci.echo.Confirmation;
import com.sptci.echo.Executor;

import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Component;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.Utilities.createButton;

/**
 * A confirmation dialogue used to display a message that the node to be
 * deleted is being referenced by other properties.  The user is also given
 * the choice to delete any weak references to the node.
 *
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-09
 * @version $Id: NodeDeleteConfirmation.java 59 2010-02-09 18:07:17Z spt $
 */
public class NodeDeleteConfirmation extends Confirmation
{
  private static final long serialVersionUID = 1L;

  private final CheckBox deleteWeakReferences;
  private final boolean weakReferences;

  public NodeDeleteConfirmation( final String title, final String message,
      final Executor executor, final boolean weakReferences )
  {
    super( title, message, executor );
    this.weakReferences = weakReferences;
    deleteWeakReferences = new CheckBox( getString( this, "deleteWeakReferences.label" ) );
  }

  @Override
  public void init()
  {
    super.init();

    if ( weakReferences )
    {
      deleteWeakReferences.setSelected( true );
      getComponent( 0 ).add( deleteWeakReferences, 1 );
    }
  }

  @Override
  protected Component createButtons()
  {
    final Row row = new Row();

    final ConfirmationListener listener = new Listener();
    row.add( createButton( getClass().getName(),
        "yes", "General.Button", listener ) );
    row.add( createButton( getClass().getName(),
        "no", "General.Button", listener ) );

    return row;
  }

  class Listener extends ConfirmationListener
  {
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed( final ActionEvent event )
    {
      executor.addParameter( boolean.class, deleteWeakReferences.isSelected() );
      super.actionPerformed( event );
    }
  }
}
