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

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public abstract class MenuItemBaseAppearance extends ItemBaseAppearance implements MenuItem.MenuItemAppearance {

  public interface MenuItemResources extends ItemResources {
    @Override
    MenuItemStyle style();
  }

  public interface MenuItemStyle extends ItemStyle {

    String menuItem();

    String menuItemArrow();

    String menuItemIcon();

    String menuListItem();

  }

  public interface MenuItemTemplate extends XTemplates {

    @XTemplates.XTemplate(source = "MenuItem.html")
    SafeHtml template(MenuItemStyle style);

  }

  private final MenuItemStyle style;
  private final MenuItemTemplate template;

  public MenuItemBaseAppearance(MenuItemResources resources, MenuItemTemplate template) {
    super(resources);
    style = resources.style();
    this.template = template;
  }

  public XElement getAnchor(XElement parent) {
    return XElement.as(parent.getFirstChild());
  }

  public void onAddSubMenu(XElement parent) {
    parent.getFirstChildElement().addClassName(style.menuItemArrow());
  }

  public void onRemoveSubMenu(XElement parent) {
    parent.getFirstChildElement().removeClassName(style.menuItemArrow());
  }

  public void render(SafeHtmlBuilder result) {
    result.append(template.template(style));
  }

  public void setIcon(XElement parent, ImageResource icon) {
    XElement anchor = getAnchor(parent);
    XElement oldIcon = parent.selectNode("." + style.menuItemIcon());
    if (oldIcon != null) {
      oldIcon.removeFromParent();
    }
    if (icon != null) {
      Element e = IconHelper.getElement(icon);
      e.addClassName(style.menuItemIcon());
      anchor.insertChild(e, 0);
    }
  }

  @Override
  public void setHtml(XElement parent, SafeHtml html) {
    XElement oldIcon = parent.selectNode("." + style.menuItemIcon());

    if (html == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      getAnchor(parent).setInnerSafeHtml(Util.NBSP_SAFE_HTML);
    } else {
      getAnchor(parent).setInnerSafeHtml(html);
    }

    if (oldIcon != null) {
      getAnchor(parent).insertFirst(oldIcon);
    }
  }

  @Override
  public void setWidget(XElement parent, Widget widget) {
    XElement oldIcon = parent.selectNode("." + style.menuItemIcon());

    getAnchor(parent).setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    getAnchor(parent).appendChild(widget.getElement());

    if (oldIcon != null) {
      getAnchor(parent).insertFirst(oldIcon);
    }
  }

}
