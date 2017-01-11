package com.sptci.jcr.webui;


import static com.sptci.echo.Application.getApplication;
import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextField;

import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;


/**
 * A dialogue used to select the repository configuration file and repository
 * location for opening a session.
 * 
 * <p>
 * &copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans Pareil Technologies, Inc.</a>
 * </p>
 * 
 * @author Rakesh Vidyadharan 2009-06-25
 * @version $Id: LoginJNDIDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public class LoginJNDIDialog extends LoginDialog {

	private static final long serialVersionUID = 1L;

	private Label jndiLabel;
	@Constraints(value = Constraints.Value.NOT_NULL)
	private TextField jndi;

	@ActionListener(value = "com.sptci.jcr.webui.listener.RepositoryDialogSaveListener")
	private Button save;

	@Override
	public void init() {
		super.init();
		getApplication().setFocusedComponent(jndi);
	}

	@Override
	protected Component createButtons() {
		final Component buttons = super.createButtons();
		buttons.add(save);

		return buttons;
	}

	@Override
	protected void createGrid(final SplitPane pane) {
		final Grid grid = new Grid(2);
		grid.add(jndiLabel);
		grid.add(jndi);
		grid.add(userLabel);
		grid.add(user);
		grid.add(passwordLabel);
		grid.add(password);
		pane.add(grid);
	}

	/** @return The value in the repository jndi field */
	public String getJndi() { return jndi.getText(); }

	/**
	 * @param jndi
	 *            The value to set for the jndi text field.
	 */
	public void setJndi(final String jndi) { this.jndi.setText(jndi); }

	public TextField getJndiTextField() { return jndi; }
}
