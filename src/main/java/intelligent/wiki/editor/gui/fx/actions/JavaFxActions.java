/*
 * Copyright (C) 2016 Myroslav Rudnytskyi
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package intelligent.wiki.editor.gui.fx.actions;

import intelligent.wiki.editor.gui.actions.Action;
import intelligent.wiki.editor.gui.actions.ActionId;
import intelligent.wiki.editor.gui.actions.Actions;
import intelligent.wiki.editor.gui.fx.ObservableCodeArea;
import intelligent.wiki.editor.gui.fx.dialogs.DialogsFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import org.reactfx.EventStreams;

import java.time.Duration;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;

import static intelligent.wiki.editor.gui.actions.ActionId.*;
import static intelligent.wiki.editor.gui.fx.actions.JavaFxActionBuilder.action;
import static java.lang.String.join;
import static java.lang.System.lineSeparator;

/**
 * Action holder implementation for JavaFx.
 *
 * @author Myroslav Rudnytskyi
 * @version 13.04.2016
 */
public class JavaFxActions implements Actions {

	private final EnumMap<ActionId, JavaFxAction> actions = new EnumMap<>(ActionId.class);
	private final ReadOnlyBooleanProperty alwaysFalse = new SimpleBooleanProperty(false);
	private final BooleanProperty noSelection = new SimpleBooleanProperty();
	private final BooleanProperty noStringInClipboard = new SimpleBooleanProperty();
	private final DialogsFactory dialogs;

	public JavaFxActions(ObservableCodeArea area, TreeView tree, DialogsFactory dialogs) {
		this.dialogs = Objects.requireNonNull(dialogs, "Null dialogs!");
		Objects.requireNonNull(area, "Null code area!");
		Objects.requireNonNull(tree, "Null tree view!");
		initProperties(area);
		initActions(area, tree);
	}

	private void initActions(ObservableCodeArea area, TreeView tree) {
		Control areaControl = (Control) area;
		init(CUT, action().withText("menu.cut").withAccelerator("Shortcut+X").withIcon("images/cut_big.png")
				.withSmallIcon("images/cut_small.png").withTooltip("toolbar.cut")
				.withDisabled(noSelection).withHandler(event -> area.cut()).build());
		init(COPY, action().withText("menu.copy").withAccelerator("Shortcut+C").withIcon("images/copy_big.png")
				.withSmallIcon("images/copy_small.png").withTooltip("toolbar.copy")
				.withDisabled(noSelection).withHandler(event -> area.copy()).build());
		init(PASTE, action().withText("menu.paste").withAccelerator("Shortcut+V").withIcon("images/paste_big.png")
				.withSmallIcon("images/paste_small.png").withTooltip("toolbar.paste")
				.withDisabled(noStringInClipboard).withHandler(event -> area.paste()).build());
		init(REQUEST_FOCUS_TREE, action().withText("menu.request-focus-tree").withAccelerator("alt+f1")
				.withIcon("images/tree_big.png").withSmallIcon("images/tree_small.png")
				.withTooltip("toolbar.request-focus-tree").withDisabled(alwaysFalse)
				.withHandler(event -> requestFocusTree(tree)).build());
		init(REQUEST_FOCUS_TEXT, action().withText("menu.request-focus-text").withAccelerator("alt+f2")
				.withIcon("images/text_big.png").withSmallIcon("images/text_small.png")
				.withTooltip("toolbar.request-focus-text").withDisabled(alwaysFalse)
				.withHandler(event -> areaControl.requestFocus()).build());
		init(SELECT_ALL, action().withText("menu.select-all").withAccelerator("Shortcut+A")
				.withTooltip("toolbar.select-all").withDisabled(alwaysFalse)
				.withHandler(event -> area.selectAll()).build());
		init(INSERT_WIKI_LINK, action().withText("menu.insert-wiki-link").withAccelerator("Shortcut+f1")
				.withIcon("images/wikilink_big.png").withSmallIcon("images/wikilink_small.png")
				.withTooltip("toolbar.insert-wiki-link").withDisabled(alwaysFalse)
				.withHandler(event -> insertWikiLink(area)).build());
		init(INSERT_H2, action().withText("menu.insert-heading-2").withAccelerator("Shortcut+f2")
				.withIcon("images/heading_2_big.png").withSmallIcon("images/heading_2_small.png")
				.withTooltip("toolbar.insert-heading-2").withDisabled(alwaysFalse)
				.withHandler(event -> insertHeading(area, 2)).build());
		init(INSERT_H3, action().withText("menu.insert-heading-3").withAccelerator("Shortcut+f3")
				.withIcon("images/heading_3_big.png").withSmallIcon("images/heading_3_small.png")
				.withTooltip("toolbar.insert-heading-3").withDisabled(alwaysFalse)
				.withHandler(event -> insertHeading(area, 3)).build());
		init(INSERT_H4, action().withText("menu.insert-heading-4").withAccelerator("Shortcut+f4")
				.withIcon("images/heading_4_big.png").withSmallIcon("images/heading_4_small.png")
				.withTooltip("toolbar.insert-heading-4").withDisabled(alwaysFalse)
				.withHandler(event -> insertHeading(area, 4)).build());
		init(INSERT_H5, action().withText("menu.insert-heading-5").withAccelerator("Shortcut+f5")
				.withIcon("images/heading_5_big.png").withSmallIcon("images/heading_5_small.png")
				.withTooltip("toolbar.insert-heading-5").withDisabled(alwaysFalse)
				.withHandler(event -> insertHeading(area, 5)).build());
		init(INSERT_H6, action().withText("menu.insert-heading-6").withAccelerator("Shortcut+f6")
				.withIcon("images/heading_6_big.png").withSmallIcon("images/heading_6_small.png")
				.withTooltip("toolbar.insert-heading-6").withDisabled(alwaysFalse)
				.withHandler(event -> insertHeading(area, 6)).build());
		init(INSERT_EXTERNAL_LINK, action().withText("menu.insert-external-link").withAccelerator("Shortcut+f7")
				.withIcon("images/link_big.png").withSmallIcon("images/link_small.png")
				.withTooltip("toolbar.insert-external-link").withDisabled(alwaysFalse)
				.withHandler(event -> insertExternalLink(area)).build());
		init(INSERT_TEMPLATE, action().withText("menu.insert-template").withAccelerator("Shortcut+f8")
				.withIcon("images/template_big.png").withSmallIcon("images/template_small.png")
				.withTooltip("toolbar.insert-template").withDisabled(alwaysFalse)
				.withHandler(event -> insertTemplate(area)).build());
		init(ADD_CATEGORIES, action().withText("menu.add-categories").withAccelerator("Shortcut+f9")
				.withIcon("images/category_big.png").withSmallIcon("images/category_small.png")
				.withTooltip("toolbar.add-categories").withDisabled(alwaysFalse)
				.withHandler(event -> addCategories()).build());
	}

	private void initProperties(ObservableCodeArea area) {
		EventStreams.ticks(Duration.ofMillis(200)).subscribe(tick -> {
			noStringInClipboard.setValue(!Clipboard.getSystemClipboard().hasString());
			noSelection.setValue(area.getSelectedCode().isEmpty());
		});
	}

	private void init(ActionId id, JavaFxAction action) {
		actions.put(id, action);
	}

	private void requestFocusTree(TreeView tree) {
		if (tree.getRoot() != null) {
			tree.requestFocus();
			tree.getRoot().setExpanded(true);
			tree.getFocusModel().focus(0);
		}
	}

	private void insertWikiLink(ObservableCodeArea area) {
		String selection = area.getSelectedCode();
		Optional<String> result = dialogs.makeInsertWikiLinkDialog(selection, selection).showAndWait();
		if (result.isPresent()) {
			area.replaceSelection(result.get());
		}
	}

	private void insertExternalLink(ObservableCodeArea area) {
		String selection = area.getSelectedCode();
		Optional<String> result = dialogs.makeInsertExternalLinkDialog(selection, selection).showAndWait();
		if (result.isPresent()) {
			area.replaceSelection(result.get());
		}
	}

	private void insertHeading(ObservableCodeArea area, int headingType) {
		String headingText = (area.getSelectedCode() == null) ? "" : area.getSelectedCode().trim();
		String header = createHeader(headingType);
		area.replaceSelection(join("", lineSeparator(), join(" ", header, headingText, header), lineSeparator()));
	}

	private String createHeader(int headingType) {
		char[] headerChars = new char[headingType];
		Arrays.fill(headerChars, '=');
		return String.valueOf(headerChars);
	}

	private void insertTemplate(ObservableCodeArea area) {
		String selection = area.getSelectedCode();
		Optional<String> result = dialogs.makeInsertTemplateDialog(selection).showAndWait();
		if (result.isPresent()) {
			area.replaceSelection(result.get());
		}
	}

	public void addCategories() {
		dialogs.makeNotImplementedErrorDialog().show();
	}

	@Override
	public Action get(ActionId id) {
		Objects.requireNonNull(id, "Null action id!");
		Action cachedAction = actions.get(id);
		if (cachedAction == null) {
			throw new AssertionError("Do not forget add action in holder!");
		}
		return cachedAction;
	}
}
