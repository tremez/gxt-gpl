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

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * Abstract base class for message boxes containing an input field.
 */
public abstract class AbstractInputMessageBox extends MessageBox {

  /**
   * Input for the message box prompt
   */
  protected Field<String> field;

  /**
   * Creates a messageHtml box that prompts for input.
   *
   * @param titleHtml the titleHtml of the messageHtml box
   * @param messageHtml the messageHtml that appears in the messageHtml box
   * @param windowAppearance the messageHtml box window appearance
   * @param messageBoxAppearance the messageHtml box content appearance
   */
  protected AbstractInputMessageBox(Field<String> field, SafeHtml titleHtml, SafeHtml messageHtml,
                                    WindowAppearance windowAppearance, MessageBoxAppearance messageBoxAppearance) {
    super(titleHtml, messageHtml, windowAppearance, messageBoxAppearance);

    ComponentHelper.setParent(this, field);

    this.field = field;

    setFocusWidget(field);

    messageBoxAppearance.getContentElement(getElement()).appendChild(field.getElement());
    setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
  }

  /**
   * Returns the input field.
   * 
   * @return the input field
   */
  public Field<String> getField() {
    return field;
  }

  /**
   * Returns the current value of the input field.
   * 
   * @return the value of the input field
   */
  public String getValue() {
    return field.getValue();
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(field);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(field);
  }

  /**
   * Resize the field width to fit the content box.
   */
  @Override
  protected void resizeContents() {
    int width = getAppearance().getContentElem(getElement()).getWidth(true);
    int padding = getMessageBoxAppearance().getContentElement(getElement()).getPadding(Side.LEFT, Side.RIGHT);

    field.setWidth(width - padding);
  }

}
