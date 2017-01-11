package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.PropertyEditDialog;

import nextapp.echo.app.event.ActionEvent;

import java.util.ArrayList;

/**
 * The event listener for navigating between the selected properties in
 * {@link com.sptci.jcr.webui.PropertiesView}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-18
 * @version $Id: PropertyEditDialogNavigationListener.java 26 2009-09-07 22:30:10Z spt $
 */
public class PropertyEditDialogNavigationListener
    extends AbstractPropertyEditListener
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    final PropertyEditDialog dialog = getView( event );

    if ( "next".equals( event.getActionCommand() ) )
    {
      next( dialog );
    }
    else if ( "previous".equals( event.getActionCommand() ) )
    {
      previous( dialog );
    }

    new PropertyEditDialogListener().bind( dialog );
    dialog.init();
  }

  private void previous( final PropertyEditDialog dialog )
  {
    final ArrayList<Integer> indices = getIndices( dialog );
    int index = indices.indexOf( dialog.getIndex() );
    dialog.setIndex( indices.get( --index ) );

    if ( index == 0 ) dialog.getPrevious().setEnabled( false );
    dialog.getNext().setEnabled( true );
    dialog.enable();
  }

  private void next( final PropertyEditDialog dialog )
  {
    final ArrayList<Integer> indices = getIndices( dialog );
    int index = indices.indexOf( dialog.getIndex() );
    dialog.setIndex( indices.get( ++index ) );

    if ( index == indices.size() - 1 ) dialog.getNext().setEnabled( false );
    dialog.getPrevious().setEnabled( true );
    dialog.enable();
  }

}
