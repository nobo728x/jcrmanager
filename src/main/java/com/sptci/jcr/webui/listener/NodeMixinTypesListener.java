package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.NodeMixinTypesDialog;

import nextapp.echo.app.event.ActionEvent;

import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import java.util.HashSet;
import java.util.Set;

/**
 * The event listener for saving the changes made using the {@link
 * com.sptci.jcr.webui.NodeMixinTypesDialog} to the wrapped node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-21
 * @version $Id: NodeMixinTypesListener.java 48 2010-01-21 20:51:31Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeMixinTypesListener extends Listener<NodeMixinTypesDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeMixinTypesDialog dialog = getView( event );

    try
    {
      boolean save = false;
      save = saveMixins( dialog, save );
      save = removeMixins( dialog, save );

      if ( save ) dialog.getNode().getSession().save();
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }

    dialog.userClose();
  }

  private boolean saveMixins( final NodeMixinTypesDialog dialog,
      boolean save ) throws RepositoryException
  {
    for ( final String type : dialog.getSelectedMixinTypes() )
    {
      dialog.getNode().addMixin( type );
      save = true;
    }
    
    return save;
  }

  private boolean removeMixins( final NodeMixinTypesDialog dialog,
      boolean save ) throws RepositoryException
  {
    final Set<NodeType> current = dialog.getMixinTypes();
    final HashSet<String> modified = new HashSet<String>();
    
    for ( final String type : dialog.getSelectedMixinTypes() )
    {
      modified.add( type );
    }

    for ( final NodeType type : current )
    {
      if ( ! modified.contains( type.getName() ) )
      {
        dialog.getNode().removeMixin( type.getName() );
        save = true;
      }
    }

    return save;
  }
}
