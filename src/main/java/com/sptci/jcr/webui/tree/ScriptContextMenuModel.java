package com.sptci.jcr.webui.tree;

import nextapp.echo.app.ImageReference;
import nextapp.echo.extras.app.menu.DefaultOptionModel;

import java.io.File;

/**
 * A context menu model to attach to {@link ScriptNode} instances.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptContextMenuModel.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptContextMenuModel extends DefaultOptionModel
{
  private static final long serialVersionUID = 1L;

  /** The script file associated with the context menu. */
  private final File file;

  public ScriptContextMenuModel( final String id, final String text,
      final ImageReference icon, final File file )
  {
    super( id, text, icon );
    this.file = file;
  }

  public File getFile() { return file; }
}
