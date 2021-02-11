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
package com.sencha.gxt.widget.core.client.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Configuration settings for {@link Info} which supports a title and text.
 */
public class DefaultInfoConfig extends InfoConfig {

  public interface DefaultInfoConfigAppearance {

    void render(SafeHtmlBuilder sb, SafeHtml title, SafeHtml message);
  }

  private DefaultInfoConfigAppearance appearance;
  private SafeHtml title = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private SafeHtml message = SafeHtmlUtils.EMPTY_SAFE_HTML;

  /**
   * Creates a new config for an Info to display.
   * 
   * @param titleText the title text
   * @param messageText the message text
   */
  public DefaultInfoConfig(String titleText, String messageText) {
    this((DefaultInfoConfigAppearance) GWT.create(DefaultInfoConfigAppearance.class), titleText, messageText);
  }

  /**
   * Creates a new config for an Info to display.
   * 
   * @param titleText the title as HTML
   * @param messageText the message as HTML
   */
  public DefaultInfoConfig(SafeHtml titleText, SafeHtml messageText) {
    this((DefaultInfoConfigAppearance) GWT.create(DefaultInfoConfigAppearance.class), titleText, messageText);
  }

  /**
   * Creates a new config for an Info to display.
   * 
   * @param appearance the appearance to use
   * @param titleText the title text
   * @param messageText the message text
   */
  public DefaultInfoConfig(DefaultInfoConfigAppearance appearance, String titleText, String messageText) {
    this(appearance, SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a new config for an Info to display.
   * 
   * @param appearance the appearance to use
   * @param titleHtml the title as HTML
   * @param messageHtml the message as HTML
   */
  public DefaultInfoConfig(DefaultInfoConfigAppearance appearance, SafeHtml titleHtml, SafeHtml messageHtml) {
    this.appearance = appearance;
    this.title = titleHtml;
    this.message = messageHtml;
  }

  public DefaultInfoConfigAppearance getAppearance() {
    return appearance;
  }

  public SafeHtml getMessage() {
    return message;
  }

  public SafeHtml getTitle() {
    return title;
  }

  public void setMessage(String message) {
    setMessage(SafeHtmlUtils.fromString(message));
  }

  public void setTitle(String title) {
    setTitle(SafeHtmlUtils.fromString(title));
  }

  public void setTitle(SafeHtml title) {
    this.title = title;
  }

  public void setMessage(SafeHtml message) {
    this.message = message;
  }

  @Override
  protected SafeHtml render(Info info) {
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder, title, message);
    return builder.toSafeHtml();
  }

}
