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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * A message box that prompts for input with a multiple line text area and OK
 * and CANCEL buttons.
 * <p />
 * Code snippet:
 * 
 * <pre>
     MultiLinePromptMessageBox mb = new MultiLinePromptMessageBox("Description", "Please enter a brief description");
     mb.addDialogHideHandler(new DialogHideHandler() {
      {@literal @}Override
      public void onDialogHide(DialogHideEvent event) {
        if (event.getHideButton() == PredefinedButton.OK) {
          // perform OK action
        } else if (event.getHideButton() == PredefinedButton.CANCEL) {
          // perform CANCEL action
        }
      }
    });

    mb.setWidth(300);
    mb.show();
 * </pre>
 */
public class MultiLinePromptMessageBox extends AbstractInputMessageBox {

  /**
   * Creates a messageText box that prompts for input with a multiple line text area
   * and OK and CANCEL buttons.
   *
   * @param titleText the titleText of the messageText box
   * @param messageText the messageText that appears in the messageText box
   */
  public MultiLinePromptMessageBox(String titleText, String messageText) {
    this(SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a message box that prompts for input with a multiple line text area
   * and OK and CANCEL buttons.
   *
   * @param titleHtml the title of the message box
   * @param messageHtml the message that appears in the message box
   */
  public MultiLinePromptMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box that prompts for input with a multiple line text area
   * and OK and CANCEL buttons.
   *
   * @param title the title of the message box
   * @param message the message that appears in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public MultiLinePromptMessageBox(SafeHtml title, SafeHtml message,
                                   WindowAppearance windowAppearance, MessageBoxAppearance messageBoxAppearance) {
    super(new TextArea(), title, message, windowAppearance, messageBoxAppearance);

    getField().setHeight(75);
  }

  /**
   * Returns the multiple line text area.
   * 
   * @return the multiple line text area
   */
  public TextArea getTextArea() {
    return (TextArea) field;
  }

}
