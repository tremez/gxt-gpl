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
package com.sencha.gxt.theme.base.client.menu;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.menu.Menu;

public abstract class MenuBaseAppearance implements Menu.MenuAppearance {

  public interface MenuResources {

    MenuStyle style();

  }

  public interface MenuStyle extends CssResource {

    String dateMenu();

    String menu();

    String menuList();

    String menuListItemIndent();

    String menuRadioGroup();

    String menuScroller();

    String menuScrollerActive();

    String menuScrollerBottom();

    String menuScrollerTop();
    
    String noSeparator();
    
    String plain();

  }

  public interface BaseMenuTemplate extends XTemplates {

    @XTemplate(source = "Menu.html")
    SafeHtml template(MenuStyle style, String ignoreClass);

  }

  protected final MenuResources resources;
  protected final MenuStyle style;

  private BaseMenuTemplate template;

  public MenuBaseAppearance(MenuResources resources, BaseMenuTemplate template) {
    this.resources = resources;
    this.style = resources.style();
    this.template = template;

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  public void applyDateMenu(XElement element) {
    element.addClassName(style.dateMenu());
  }

  public XElement createItem(XElement parent, String childId, boolean needsIndent) {
    XElement div = Document.get().createDivElement().cast();
    if (childId != null && childId.length() != 0) {
      div.setId("x-menu-el-" + childId);
    }
    // div.setClassName(style.menuListItem());
    if (needsIndent) div.setClassName(style.menuListItemIndent());
    return div;
  }

  public XElement getBottomScroller(XElement parent) {
    // Check whether scroller already exists
    Node firstChild = parent.getLastChild();
    if (firstChild != null && Element.is(firstChild)) {
      XElement firstChildXElement = XElement.as(firstChild);
      if (firstChildXElement.is("." + style.menuScrollerTop())) {
        // Found scroller
        return firstChildXElement;
      }
    }

    // Scroller does not already exist; create it
    XElement scroller = Document.get().createDivElement().cast();
    scroller.addClassName(style.menuScroller(), style.menuScrollerBottom());
    scroller.setInnerSafeHtml(Util.NBSP_SAFE_HTML);
    parent.appendChild(scroller);
    return scroller;
  }

  public XElement getGroup(XElement parent, String id, String groupName) {
    XElement groupElement = parent.selectNode("#" + id + "-" + groupName);
    return groupElement != null ? groupElement : createGroup(parent, id, groupName);
  }

  public NodeList<Element> getGroups(XElement parent) {
    return parent.select("." + style.menuRadioGroup());
  }

  public XElement getMenuList(XElement element) {
    return element.selectNode("." + style.menuList());
  }

  public NodeList<Element> getScrollers(XElement parent) {
    return parent.select("." + style.menuScroller());
  }

  public XElement getTopScroller(XElement parent) {
    // Check whether scroller already exists
    Element firstChild = parent.getFirstChildElement();
    if (firstChild != null) {
      XElement firstChildXElement = XElement.as(firstChild);
      if (firstChildXElement.is("." + style.menuScrollerTop())) {
        // Found scroller
        return firstChildXElement;
      }
    }

    // Scroller does not already exist; create it
    XElement scroller = Document.get().createDivElement().cast();
    scroller.addClassName(style.menuScroller(), style.menuScrollerTop());
    scroller.setInnerSafeHtml(Util.NBSP_SAFE_HTML);
    parent.insertFirst(scroller);
    return scroller;
  }

  public boolean hasScrollers(XElement parent) {
    return parent.select("." + style.menuScroller()).getLength() > 0;
  }

  public void onScrollerOut(XElement target) {
    target.removeClassName(style.menuScrollerActive());
  }

  public void render(SafeHtmlBuilder result) {
    result.append(template.template(style, CommonStyles.get().ignore()));
  }

  private XElement createGroup(XElement parent, String id, String groupName) {
    XElement groupElement = XElement.createElement("div");
    groupElement.makePositionable(true);
    groupElement.addClassName(HideMode.OFFSETS.value());
    groupElement.addClassName(style.menuRadioGroup());
    groupElement.setId(id + "-" + groupName);
    parent.appendChild(groupElement);
    return groupElement;
  }

  @Override
  public String noSeparatorClass() {
    return style.noSeparator();
  }

  @Override
  public String plainClass() {
    return style.plain();
  }

}
