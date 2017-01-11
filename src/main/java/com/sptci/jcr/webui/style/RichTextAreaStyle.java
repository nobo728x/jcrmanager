package com.sptci.jcr.webui.style;

import static com.sptci.echo.Dimensions.getExtent;
import com.sptci.echo.style.General;
import com.sptci.jcr.webui.StringComponent;

import static jfix.echo.RichTextArea.PROPERTY_HEIGHT;
import static jfix.echo.RichTextArea.PROPERTY_WIDTH;

/**
 * The default style to apply to the TinyMCE rich text area component.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-11
 * @version $Id: RichTextAreaStyle.java 38 2009-10-12 14:35:15Z spt $
 */
public class RichTextAreaStyle extends General
{
  private static final long serialVersionUID = 1l;

  @Override
  protected void init()
  {
    set( PROPERTY_WIDTH, getExtent( StringComponent.class, "rta.width" ) );
    set( PROPERTY_HEIGHT, getExtent( StringComponent.class, "rta.height" ) );
  }
}
