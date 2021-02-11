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
package com.sencha.gxt.widget.core.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Accessibility;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.Layer;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * A base class for all menu items that require menu-related functionality (like
 * sub-menus) and are not static display items. Item extends the base
 * functionality of {@link Item} by adding menu-specific activation and click
 * handling.
 */
@SuppressWarnings("deprecation")
public class MenuItem extends Item implements HasSafeHtml, HasHTML, HasIcon {

  public interface MenuItemAppearance extends ItemAppearance {

    void onAddSubMenu(XElement parent);

    void onRemoveSubMenu(XElement parent);

    void render(SafeHtmlBuilder result);

    void setIcon(XElement parent, ImageResource icon);

    void setHtml(XElement parent, SafeHtml html);

    void setWidget(XElement parent, Widget widget);

  }

  protected ImageResource icon;
  protected Menu subMenu;
  protected SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  protected Widget widget;

  /**
   * Creates a new item.
   */
  public MenuItem() {
    this(GWT.<MenuItemAppearance> create(MenuItemAppearance.class));
  }

  /**
   * Creates a menu item with the given appearances.
   * 
   * @param menuItemAppearance the menu item appearance
   */
  public MenuItem(MenuItemAppearance menuItemAppearance) {
    super(menuItemAppearance);

    canActivate = true;

    SafeHtmlBuilder markupBuilder = new SafeHtmlBuilder();
    getAppearance().render(markupBuilder);

    setElement((Element) XDOM.create(markupBuilder.toSafeHtml()));
    getElement().addClassName(CommonStyles.get().unselectable());
  }

  /**
   * Creates a new item with the given html.
   *
   * @param html the item's html
   */
  public MenuItem(SafeHtml html) {
    this();
    setHTML(html);
  }

  /**
   * Creates a new item with the given text.
   * 
   * @param text the item's text
   */
  public MenuItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new item with the given html and icon.
   *
   * @param html the item's html
   * @param icon the item's icon
   */
  public MenuItem(SafeHtml html, ImageResource icon) {
    this(html);
    setIcon(icon);
  }

  /**
   * Creates a new item with the given text and icon.
   * 
   * @param text the item's text
   * @param icon the item's icon
   */
  public MenuItem(String text, ImageResource icon) {
    this(text);
    setIcon(icon);
  }

  /**
   * Creates a new item with the given html and selection handler.
   *
   * @param html the item html
   * @param handler the selection handler
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public MenuItem(SafeHtml html, SelectionHandler<MenuItem> handler) {
    this(html);
    addSelectionHandler((SelectionHandler) handler);
  }

  /**
   * Creates a new item with the given text and selection handler.
   * 
   * @param text the item text
   * @param handler the selection handler
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public MenuItem(String text, SelectionHandler<MenuItem> handler) {
    this(text);
    addSelectionHandler((SelectionHandler) handler);
  }

  /**
   * Expands the item's sub menu.
   */
  public void expandMenu() {
    if (isEnabled() && subMenu != null) {
      subMenu.setFocusOnShow(true);
      subMenu.show(getElement(), new AnchorAlignment(Anchor.TOP_LEFT, Anchor.TOP_RIGHT, true));
    }
  }

  public MenuItemAppearance getAppearance() {
    return (MenuItemAppearance) super.getAppearance();
  }

  /**
   * Returns the item's text.
   *
   * If text was set that contained reserved html characters, the return value will be html escaped.
   * If html was set instead, the return value will be html.
   *
   * @return the text or html, depending on what was set
   * @see #getHTML()
   */
  @Override
  public String getText() {
    return getHTML();
  }

  /**
   * Sets the item's text.
   *
   * Text that contains reserved html characters will be escaped.
   * 
   * @param text the text
   */
  @Override
  public void setText(String text) {
    setHTML(SafeHtmlUtils.fromString(text));
  }

  /**
   * Returns the item's html.
   *
   * @return the html
   */
  public SafeHtml getSafeHtml() {
    return html;
  }

  /**
   * Returns the item's html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return html.asString();
  }

  /**
   * Sets the item's html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    this.html = html;
    getAppearance().setHtml(getElement(), html);
  }

  /**
   * Sets the item's html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    setHTML(ExpandedHtmlSanitizer.sanitizeHtml(html));
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  @Override
  public ImageResource getIcon() {
    return icon;
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
    getAppearance().setIcon(getElement(), icon);
  }

  /**
   * Returns the item's sub menu.
   * 
   * @return the sub menu
   */
  public Menu getSubMenu() {
    return subMenu;
  }

  /**
   * Returns true if this has a sub menu.
   *
   * @return true if there is a sub menu
   */
  public boolean hasSubMenu() {
    return subMenu != null;
  }


  /**
   * Sets the item's sub menu.
   * 
   * @param menu the sub menu
   */
  @UiChild(limit = 1, tagname = "submenu")
  public void setSubMenu(Menu menu) {
    this.subMenu = menu;

    if (menu == null) {
      getAppearance().onRemoveSubMenu(getElement());
      Accessibility.setState(getElement(), "aria-haspopup", "false");
    } else {
      menu.parentItem = this;
      getAppearance().onAddSubMenu(getElement());
      Accessibility.setState(getElement(), "aria-haspopup", "true");
    }
  }

  public void setWidget(Widget widget) {
    this.widget = widget;
    getAppearance().setWidget(getElement(), widget);
    if (isAttached()) {
      ComponentHelper.doAttach(widget);
    }
  }

  @Override
  protected void activate(boolean autoExpand) {
    super.activate(autoExpand);
    if (autoExpand && subMenu != null) {
      expandMenu();
    }
  }

  @Override
  protected void deactivate() {
    super.deactivate();
    if (subMenu != null && subMenu.isVisible()) {
      subMenu.hide();
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(widget);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(widget);
  }

  @Override
  protected void expandMenu(boolean autoActivate) {
    if (!disabled && subMenu != null) {
      if (!subMenu.isVisible()) {
        expandMenu();
        subMenu.tryActivate(0, 1);
      }
    }
  }

  @Override
  protected void handleClick(NativeEvent be) {
    // if a submenu is present, the selection event should fire but the parent menu shouldn't close on click
    if (subMenu == null) {
      super.handleClick(be);
    }
  }

  @Override
  protected boolean shouldDeactivate(NativeEvent ce) {
    if (super.shouldDeactivate(ce)) {
      if (subMenu != null && subMenu.isVisible()) {
        Point xy = ce.<XEvent> cast().getXY();
        xy.setX(xy.getX() + XDOM.getBodyScrollLeft());
        xy.setY(xy.getY() + XDOM.getBodyScrollTop());

        Rectangle rec = subMenu.getElement().getBounds();
        if (getLayer(subMenu) != null) {
          Layer l = getLayer(subMenu);
          if (l.isShim() && l.isShadow()) {
            return !rec.contains(xy) && !l.getShadow().getBounds().contains(xy)
                && !l.getShim().getBounds().contains(xy);
          } else if (l.isShadow()) {
            return !rec.contains(xy) && !l.getShadow().getBounds().contains(xy);
          } else if (l.isShim()) {
            return !rec.contains(xy) && !l.getShim().getBounds().contains(xy);
          }
        }

        return !rec.contains(xy);
      }
    }
    return true;
  }

  private native Layer getLayer(Component c) /*-{
		c.@com.sencha.gxt.widget.core.client.Component::layer;
  }-*/;

}
