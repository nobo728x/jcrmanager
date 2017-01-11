package com.sptci.jcr.webui;

import com.sptci.jcr.webui.listener.menu.MenuListener;
import com.sptci.jcr.webui.model.NamedQueries;
import com.sptci.jcr.webui.model.RepositoryConnectionData;

import nextapp.echo.extras.app.MenuBarPane;
import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.extras.app.menu.DefaultOptionModel;
import nextapp.echo.extras.app.menu.SeparatorModel;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.style.button.Yes.ICON;
import static com.sptci.jcr.Environment.environment;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.String.format;

import javax.jcr.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * The menu bar in use by the application.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-09-06
 * @version $Id: MenuBar.java 73 2011-11-04 17:23:50Z orair $
 */
public class MenuBar extends MenuBarPane
{
  private static final long serialVersionUID = 1L;

  public MenuBar()
  {
    setStyleName( "Default" );
    addActionListener( new MenuListener() );
  }

  @Override
  public void init()
  {
    final DefaultMenuModel model = new DefaultMenuModel();
    model.addItem( createSessionMenu() );
    model.addItem( createRepositoryMenu() );
    model.addItem( createNodeMenu() );
    setModel( model );
  }

  private DefaultMenuModel createSessionMenu()
  {
    final DefaultMenuModel menu =
        new DefaultMenuModel( "session", getString( this, "session.title" ) );

    createResources( menu );
    createSavedSession( menu );
    createLogin( menu );

    menu.addItem( new SeparatorModel() );
    menu.addItem( new DefaultOptionModel(
        "logout", getString( this, "session.logout.title" ), null ) );
    return menu;
  }

  private void createResources( final DefaultMenuModel menu )
  {
    final Collection<String> resources = getController().getResources();
    if ( ! resources.isEmpty() )
    {
      final DefaultMenuModel resource =  new DefaultMenuModel(
          "resource", getString( this, "session.resource.title" ) );

      for ( final String res : resources )
      {
        resource.addItem( new DefaultOptionModel(
            format( "resource:%s", res ), res, null ) );
      }

      menu.addItem( resource );
    }
  }

  private void createSavedSession( final DefaultMenuModel menu )
  {
    final RepositoryConnectionData data = getController().getConnectionData();
    if ( data != null )
    {
      final Collection<String> names = data.getNames();

      if ( ! names.isEmpty() )
      {
        final DefaultMenuModel saved =  new DefaultMenuModel(
            "saved", getString( this, "session.saved.title" ) );

        for ( final String name : names )
        {
          saved.addItem( new DefaultOptionModel(
              format( "saved:%s", name ), name, null ) );
        }

        menu.addItem( saved );
      }
    }
  }

  private void createLogin( final DefaultMenuModel menu )
  {
    final DefaultMenuModel login = new DefaultMenuModel( "login",
        getString( this, "session.login.title" ) );

    login.addItem( new DefaultOptionModel( "loginLocal",
        getString( this, "session.login.local.title" ), null ) );
    login.addItem( new DefaultOptionModel( "loginRMI",
        getString( this, "session.login.rmi.title" ), null ) );
    login.addItem( new DefaultOptionModel( "loginResource",
            getString( this, "session.login.jndi.title" ), null ) );

    menu.addItem( login );
  }

  private DefaultMenuModel createRepositoryMenu()
  {
    final DefaultMenuModel menu = new DefaultMenuModel( "repository",
        getString( this, "repository.title" ) );
    menu.addItem( new DefaultOptionModel( "repositoryDetails",
        getString( this, "repository.details.title" ), null ) );
    createWorkspaces( menu );

    menu.addItem( new DefaultOptionModel(
        "query", getString( this, "repository.query.title" ), null ) );
    createSavedQueries( menu );
    
    createScripts( menu );
    menu.addItem( new DefaultOptionModel(
        "search", getString( this, "repository.search.title" ), null ) );
    menu.addItem( new SeparatorModel() );
    
    menu.addItem( new DefaultOptionModel(
        "registerNode", getString( this, "repository.register.node.title" ), null ) );
    menu.addItem( new DefaultOptionModel(
        "registerNamespace", getString( this, "repository.register.uri.title" ), null ) );
    menu.addItem( new SeparatorModel() );
    menu.addItem( new DefaultOptionModel(
        "refresh", getString( this, "repository.refresh.title" ), null ) );
    return menu;
  }

  private void createWorkspaces( final DefaultMenuModel menu )
  {
    final DefaultMenuModel workspaces = new DefaultMenuModel(
        "workspaces", getString( this, "repository.workspaces.title" ) );
    workspaces.addItem( new DefaultOptionModel( "workspaces:jcr:create",
        getString( this, "repository.workspaces.create.title" ), null ) );
    workspaces.addItem( new DefaultOptionModel( "workspaces:jcr:clone",
        getString( this, "repository.workspaces.clone.title" ), null ) );

    if ( getController().getSession() != null )
    {
      try
      {
        for ( final String name :
            getController().getWorkspace().getAccessibleWorkspaceNames() )
        {
          workspaces.addItem( new DefaultOptionModel(
              format( "workspaces:%s", name ), name,
          ( name.equals( getController().getWorkspace().getName() ) ) ?
              ICON : null ) );
        }
      }
      catch ( RepositoryException e )
      {
        getApplication().processFatalException( e );
      }
    }

    menu.addItem( workspaces );

  }

  private void createSavedQueries( final DefaultMenuModel menu )
  {
    final NamedQueries queries = getController().getQueries();
    final Collection<String> names = queries.getNames();

    if ( ! names.isEmpty() )
    {
      final DefaultMenuModel saved =  new DefaultMenuModel(
          "names", getString( this, "repository.names.title" ) );

      for ( final String name : names )
      {
        saved.addItem( new DefaultOptionModel(
            format( "names:%s", name ), name, null ) );
      }

      menu.addItem( saved );
    }
  }

  private void createScripts( final DefaultMenuModel menu )
  {
    final DefaultMenuModel scripts =  new DefaultMenuModel(
        "scripts", getString( this, "repository.scripts.title" ) );
    
    createScripts( scripts, environment.getScriptDirectory() );

    scripts.addItem( new SeparatorModel() );
    scripts.addItem( new DefaultOptionModel( "scripts:sample",
        getString( this, "repository.scripts.sample" ), null ) );
    scripts.addItem( new DefaultOptionModel( "scripts:manage",
        getString( this, "repository.scripts.manage" ), null ) );
    menu.addItem( scripts );
  }

  @SuppressWarnings( { "ResultOfMethodCallIgnored" } )
  private void createScripts( final DefaultMenuModel menu, final String directory )
  {
    try
    {
      final File base = new File( directory );
      if (base.exists() == false) {
        boolean directoryCreated = base.mkdirs();
        if (directoryCreated == false) {
          throw new IOException("Failed to create the config directory <"+base.getAbsolutePath()+">. The config directory may be changed specifying sptjcrmanager.data.dir environment variable.");
        }
      }
      if (base.isDirectory() == false) {
        throw new IOException("The config directory path <" + base.getAbsolutePath() + "> should be a directory. The config directory may be changed specifying sptjcrmanager.data.dir environment variable.");
      }

      for ( final File file : base.listFiles() )
      {
        if ( file.isDirectory() )
        {
          final DefaultMenuModel child =
              new DefaultMenuModel( file.getName(), file.getName() );
          
          createScripts( child, file.getAbsolutePath() );

          menu.addItem( child );
        }
        else if ( ! file.isHidden() )
        {
          menu.addItem( new DefaultOptionModel(
              format( "scripts:%s", file.getAbsolutePath() ), file.getName(), null ) );
        }
      }
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }
  }

  private DefaultMenuModel createNodeMenu()
  {
    final DefaultMenuModel menu =
        new DefaultMenuModel( "node", getString( this, "node.title" ) );
    menu.addItem( new DefaultOptionModel(
        "nodeCreate", getString( this, "node.create.title" ), null ) );
    menu.addItem( new DefaultOptionModel(
        "nodeExport", getString( this, "node.export.title" ), null ) );
    menu.addItem( new DefaultOptionModel(
        "nodeImport", getString( this, "node.import.title" ), null ) );
    menu.addItem( new DefaultOptionModel(
        "nodeDelete", getString( this, "node.delete.title" ), null ) );
    return menu;
  }
}
