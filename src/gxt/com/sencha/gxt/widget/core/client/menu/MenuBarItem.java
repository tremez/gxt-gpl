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
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHTML;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;
import com.sencha.gxt.widget.core.client.Component;

public class MenuBarItem extends Component implements HasSafeHtml, HasHTML {

  public static interface MenuBarItemAppearance {

    XElement getHtmlElement(XElement parent);

    void onOver(XElement parent, boolean over);

    void onActive(XElement parent, boolean active);

    void render(SafeHtmlBuilder builder);
  }

  private final MenuBarItemAppearance appearance;
  protected SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  protected Menu menu;
  protected boolean expanded;

  @UiConstructor
  public MenuBarItem(String text) {
    this(text, null);
  }

  public MenuBarItem(SafeHtml html) {
    this(html, null);
  }

  public MenuBarItem(String text, Menu menu) {
    this(text, menu, GWT.<MenuBarItemAppearance> create(MenuBarItemAppearance.class));
  }

  public MenuBarItem(SafeHtml html, Menu menu) {
    this(html, menu, GWT.<MenuBarItemAppearance> create(MenuBarItemAppearance.class));
  }

  public MenuBarItem(String text, Menu menu, MenuBarItemAppearance appearance) {
    this(SafeHtmlUtils.fromString(text), menu, appearance);
  }

  public MenuBarItem(SafeHtml html, Menu menu, MenuBarItemAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));
    sinkEvents(Event.MOUSEEVENTS);

    setHTML(html);
    setMenu(menu);
  }

  public MenuBarItemAppearance getAppearance() {
    return appearance;
  }

  public Menu getMenu() {
    return menu;
  }

  @UiChild(limit = 1, tagname = "menu")
  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    switch (event.getTypeInt()) {
      case Event.ONMOUSEOVER:
        appearance.onOver(getElement(), true);
        break;

      case Event.ONMOUSEOUT:
        appearance.onOver(getElement(), false);
        break;
    }
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
    getAppearance().getHtmlElement(getElement()).setInnerSafeHtml(html);
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

}
