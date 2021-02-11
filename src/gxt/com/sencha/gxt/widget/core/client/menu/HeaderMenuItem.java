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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHTML;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;

/**
 * A menu item for headings. Typically, places as the first item in a Menu.
 */
public class HeaderMenuItem extends Item implements HasHTML, HasSafeHtml {

  public interface HeaderMenuItemAppearance extends ItemAppearance {

    public void applyItemStyle(Element element);

  }

  private SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;

  /**
   * Creates a new header menu item.
   */
  public HeaderMenuItem(HeaderMenuItemAppearance appearance) {
    super(appearance);
    setHideOnClick(false);

    Element span = DOM.createSpan();
    appearance.applyItemStyle(XElement.as(span));
    setElement(span);
  }

  public HeaderMenuItem() {
    this(GWT.<HeaderMenuItemAppearance> create(HeaderMenuItemAppearance.class));
  }

  /**
   * Creates a new header menu item.
   *
   * @param text the text
   */
  public HeaderMenuItem(String text, HeaderMenuItemAppearance appearance) {
    this(appearance);
    setText(text);
  }

  public HeaderMenuItem(SafeHtml html, HeaderMenuItemAppearance appearance) {
    this(appearance);
    setHTML(html);
  }

  public HeaderMenuItem(String text) {
    this(GWT.<HeaderMenuItemAppearance> create(HeaderMenuItemAppearance.class));
    setText(text);
  }

  public HeaderMenuItem(SafeHtml html) {
    this(GWT.<HeaderMenuItemAppearance> create(HeaderMenuItemAppearance.class));
    setHTML(html);
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
    getElement().setInnerSafeHtml(html);
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
