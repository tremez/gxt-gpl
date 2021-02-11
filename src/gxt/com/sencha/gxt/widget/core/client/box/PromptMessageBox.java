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
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * A message box that prompts for input with a single line text field and OK and
 * CANCEL buttons.
 * <p />
 * Code snippet:
 *
 * <pre>
    final PromptMessageBox mb = new PromptMessageBox("Description", "Please enter a brief description");
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
public class PromptMessageBox extends AbstractInputMessageBox {

  /**
   * Creates a messageText box that prompts for input with a single line text field
   * and OK and CANCEL buttons.
   *
   * @param titleText the titleText of the messageText box
   * @param messageText the messageText that appears in the messageText box
   */
  public PromptMessageBox(String titleText, String messageText) {
    this(SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a messageHtml box that prompts for input with a single line text field
   * and OK and CANCEL buttons.
   *
   * @param titleHtml the titleHtml of the messageHtml box
   * @param messageHtml the messageHtml that appears in the messageHtml box
   */
  public PromptMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, (WindowAppearance) GWT.create(WindowAppearance.class),
        (MessageBoxAppearance) GWT.create(MessageBoxAppearance.class));
  }

  /**
   * Creates a messageHtml box that prompts for input with a single line text field
   * and OK and CANCEL buttons.
   *
   * @param titleHtml the titleHtml of the messageHtml box
   * @param messageHtml the messageHtml that appears in the messageHtml box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public PromptMessageBox(SafeHtml titleHtml, SafeHtml messageHtml,
                          WindowAppearance windowAppearance, MessageBoxAppearance messageBoxAppearance) {
    super(new TextField(), titleHtml, messageHtml, windowAppearance, messageBoxAppearance);
  }

  /**
   * Returns the single line text field.
   *
   * @return the single line text field
   */
  public TextField getTextField() {
    return (TextField) field;
  }

}
