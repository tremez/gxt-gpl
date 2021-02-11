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
package com.sencha.gxt.widget.core.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;

/**
 * Custom <code>Dialog</code> for displaying information to the user.
 * <p/>
 * <p>
 * Note that the <code>MessageBox</code> is asynchronous. Unlike a regular
 * JavaScript <code>alert</code> (which will halt browser execution), showing a
 * MessageBox will not cause the code to stop.
 * </p>
 */
public class MessageBox extends Dialog implements HasIcon {

  @SuppressWarnings("javadoc")
  public interface MessageBoxIcons extends ClientBundle {
    ImageResource error();

    ImageResource info();

    ImageResource question();

    ImageResource warning();
  }

  @SuppressWarnings("javadoc")
  public interface MessageBoxAppearance {
    XElement getContentElement(XElement parent);

    XElement getIconElement(XElement parent);

    XElement getMessageElement(XElement parent);

    void render(SafeHtmlBuilder sb);
  }

  /**
   * The basic icons used to decorate the message box.
   */
  public static MessageBoxIcons ICONS = GWT.create(MessageBoxIcons.class);

  protected ImageResource icon;

  private final MessageBoxAppearance messageBoxAppearance;

  /**
   * Creates a message box with the specified heading text.
   *
   * @param headingText the text to display for the message box heading.
   */
  public MessageBox(String headingText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.EMPTY_SAFE_HTML);
  }

  /**
   * Creates a message box with the specified heading text.
   *
   * @param headingHtml the html to display for the message box heading
   */
  public MessageBox(SafeHtml headingHtml) {
    this(headingHtml, SafeHtmlUtils.EMPTY_SAFE_HTML);
  }

  /**
   * Creates a message box with the specified heading and message text.
   *
   * @param headingText the text to display for the message box heading
   * @param messageText the text to display in the message box
   */
  public MessageBox(String headingText, String messageText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.fromString(messageText),
        GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box with the default message box appearance and the
   * specified heading and message HTML.
   *
   * @param headingHtml the HTML to display for the message box heading
   * @param messageHtml the HTML to display in the message box
   */
  public MessageBox(SafeHtml headingHtml, SafeHtml messageHtml) {
    this(headingHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box with the specified heading HTML, message HTML and
   * windowAppearance. It is the caller's responsibility to ensure the HTML is CSS
   * safe.
   *
   * @param headingHtml the HTML to display for the message box heading
   * @param messageHtml the HTML to display in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public MessageBox(SafeHtml headingHtml, SafeHtml messageHtml, WindowAppearance windowAppearance,
                    MessageBoxAppearance messageBoxAppearance) {
    super(windowAppearance);

    setMinWidth(300);

    this.messageBoxAppearance = messageBoxAppearance;

    setHeading(headingHtml);

    setBlinkModal(true);

    init();

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    messageBoxAppearance.render(sb);

    windowAppearance.getContentElem(getElement()).setInnerSafeHtml(sb.toSafeHtml());

    messageBoxAppearance.getMessageElement(getElement()).setId(getId() + "-content");

    setMessage(messageHtml);
  }

  @Override
  public ImageResource getIcon() {
    return icon;
  }

  /**
   * Returns the message box appearance.
   *
   * @return the message box appearance
   */
  public MessageBoxAppearance getMessageBoxAppearance() {
    return messageBoxAppearance;
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
    messageBoxAppearance.getIconElement(getElement()).setVisible(true);
    messageBoxAppearance.getIconElement(getElement()).removeChildren();
    messageBoxAppearance.getIconElement(getElement()).appendChild(IconHelper.getElement(icon));
  }

  /**
   * Sets the message. HTML in the message is escaped.
   * Use {@link #setMessage(SafeHtml)} to display HTML.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    if (message == null) {
      setMessage(SafeHtmlUtils.EMPTY_SAFE_HTML);
    } else {
      setMessage(SafeHtmlUtils.fromString(message));
    }
  }

  /**
   * Sets the message.
   *
   * @param message the message
   */
  public void setMessage(SafeHtml message) {
    if (message == null) {
      messageBoxAppearance.getMessageElement(getElement()).setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    } else {
      messageBoxAppearance.getMessageElement(getElement()).setInnerSafeHtml(message);
    }
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);

    resizeContents();
  }

  @Override
  public void show() {
    super.show();

    // Set the field default width to 100%
    resizeContents();
  }

  /**
   * Resize contents on {@link #show()} and {@link #onResize(int, int)}
   */
  protected void resizeContents() {
  }

  private void init() {
    setData("messageBox", true);
    setResizable(false);
    setConstrain(true);
    setMinimizable(false);
    setMaximizable(false);
    setClosable(false);
    setModal(true);
    setButtonAlign(BoxLayoutPack.CENTER);
    setMinHeight(80);
    setPredefinedButtons(PredefinedButton.OK);
    setHideOnButtonClick(true);
  }

}
