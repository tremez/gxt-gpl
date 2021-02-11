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

/**
 * A message box that prompts for confirmation with YES and NO buttons.
 * <p />
 * Code snippet:
 * 
 * <pre>
    final ConfirmMessageBox mb = new ConfirmMessageBox("Confirmation Required", "Are you ready?");
    mb.addDialogHideHandler(new DialogHideHandler() {
      {@literal @}Override
      public void onDialogHide(DialogHideEvent event) {
        switch (event.getHideButton()) {
          case YES:
            //Perform YES action
            break;
          case NO:
            //perform NO action
            break;
          default:
            //error, button added with no specific action ready
        }
      }
    });
    mb.setWidth(300);
    mb.show();
 * </pre>
 */
public class ConfirmMessageBox extends MessageBox {

  /**
   * Creates a message box that prompts for confirmation with YES and NO
   * buttons.
   *
   * @param titleText the title of the message box
   * @param messageText the message that appears in the message box
   */
  public ConfirmMessageBox(String titleText, String messageText) {
    this(SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a message box that prompts for confirmation with YES and NO
   * buttons.
   *
   * @param titleHtml the title of the message box
   * @param messageHtml the message that appears in the message box
   */
  public ConfirmMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box that prompts for confirmation with YES and NO
   * buttons.
   *
   * @param titleHtml the title of the message box
   * @param messageHtml the message that appears in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public ConfirmMessageBox(SafeHtml titleHtml, SafeHtml messageHtml, WindowAppearance windowAppearance,
                           MessageBoxAppearance messageBoxAppearance) {
    super(titleHtml, messageHtml, windowAppearance, messageBoxAppearance);

    setIcon(ICONS.question());
    setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
  }

}
