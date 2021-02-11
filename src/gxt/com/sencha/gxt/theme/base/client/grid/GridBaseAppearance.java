/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.grid.GridView.GridAppearance;
import com.sencha.gxt.widget.core.client.grid.GridView.GridStateStyles;
import com.sencha.gxt.widget.core.client.grid.GridView.GridStyles;

public abstract class GridBaseAppearance implements GridAppearance {

  public interface GridResources extends ClientBundle {

    @Source("Grid.gss")
    @Import(GridStateStyles.class)
    GridStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource specialColumn();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource specialColumnSelected();

  }

  public interface GridStyle extends GridStyles {
    String scroller();

    String body();
  }

  public interface GridTemplates extends XTemplates {
    @XTemplate(source = "Grid.html")
    SafeHtml render(GridStyle style);
  }

  protected final GridResources resources;
  protected final GridStyle style;
  private GridTemplates templates = GWT.create(GridTemplates.class);

  public GridBaseAppearance(GridResources resources) {
    this.resources = resources;
    this.style = this.resources.css();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(templates.render(style));
  }

  @Override
  public GridStyles styles() {
    return style;
  }

  @Override
  public Element findRow(Element elem) {
    if (Element.is(elem)) {
      return elem.<XElement> cast().findParentElement("." + style.row(), -1);
    }
    return null;
  }

  @Override
  public NodeList<Element> getRows(XElement parent) {
    return TableElement.as(parent.getFirstChildElement()).getTBodies().getItem(1).getRows().cast();
  }

  @Override
  public Element findCell(Element elem) {
    if (Element.is(elem)) {
      return elem.<XElement> cast().findParentElement("." + style.cell(), -1);
    }
    return null;
  }

  @Override
  public void onRowOver(Element row, boolean over) {
    row.<XElement> cast().setClassName(style.rowOver(), over);
  }

  @Override
  public void onRowHighlight(Element row, boolean highlight) {
    row.<XElement> cast().setClassName(style.rowHighlight(), highlight);
  }

  @Override
  public void onRowSelect(Element row, boolean select) {
  }

  @Override
  public void onCellSelect(Element cell, boolean select) {
  }

  @Override
  public Element getRowBody(Element row) {
    return TableElement.as(row.getFirstChildElement().getFirstChildElement().getFirstChildElement()).getTBodies().getItem(
        1).getRows().getItem(1).getCells().getItem(0).getFirstChildElement();
  }

  @Override
  public SafeHtml renderEmptyContent(String emptyText) {
    return Util.isEmptyString(emptyText) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(emptyText);
  }

}
