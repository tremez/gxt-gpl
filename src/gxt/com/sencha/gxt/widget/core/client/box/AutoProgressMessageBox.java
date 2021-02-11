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
import com.sencha.gxt.widget.core.client.AutoProgressBar;
import com.sencha.gxt.widget.core.client.ComponentHelper;

/**
 * A <code>MessageBox</code> which displays an {@link AutoProgressBar}.
 */
public class AutoProgressMessageBox extends MessageBox {

  private AutoProgressBar progressBar;
  private String progressText = "";
  private int minProgressWidth = 250;

  /**
   * Creates a progress message box with the specified heading HTML. The
   * progress bar auto-updates using the current duration, increment, and
   * interval (see {@link #getProgressBar()}.
   *
   * @param headingText the text to display for the message box heading.
   */
  public AutoProgressMessageBox(String headingText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.EMPTY_SAFE_HTML);
  }


  /**
   * Creates a progress message box with the specified heading HTML. The
   * progress bar auto-updates using the current duration, increment, and
   * interval (see {@link #getProgressBar()}.
   *
   * @param headingHtml the HTML to display for the message box heading
   */
  public AutoProgressMessageBox(SafeHtml headingHtml) {
    this(headingHtml, SafeHtmlUtils.EMPTY_SAFE_HTML);
  }

  /**
   * Creates a progress message box with the specified heading and message HTML.
   * The progress bar auto-updates using the current duration, increment, and
   * interval (see {@link #getProgressBar()}.
   *
   * @param headingText the text to display for the message box heading
   * @param messageText the text to display in the message box
   */
  public AutoProgressMessageBox(String headingText, String messageText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a progress message box with the specified heading and message HTML.
   * The progress bar auto-updates using the current duration, increment, and
   * interval (see {@link #getProgressBar()}.
   *
   * @param headingHtml the HTML to display for the message box heading
   * @param messageHtml the HTML to display in the message box
   */
  public AutoProgressMessageBox(SafeHtml headingHtml, SafeHtml messageHtml) {
    this(headingHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a progress message box with the specified heading and message HTML.
   * The progress bar auto-updates using the current duration, increment, and
   * interval (see {@link #getProgressBar()}.
   *
   * @param headingHtml the HTML to display for the message box heading
   * @param messageHtml the HTML to display in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public AutoProgressMessageBox(SafeHtml headingHtml, SafeHtml messageHtml, WindowAppearance windowAppearance,
                                MessageBoxAppearance messageBoxAppearance) {
    super(headingHtml, messageHtml, windowAppearance, messageBoxAppearance);

    setPredefinedButtons();

    progressBar = new AutoProgressBar();

    messageBoxAppearance.getContentElement(getElement()).appendChild(progressBar.getElement());

    setFocusWidget(progressBar);

    icon = null;
  }

  /**
   * Initiates an auto-updating progress bar using the current duration,
   * increment, and interval (see {@link #getProgressBar()}.
   */
  public void auto() {
    progressBar.getCell().setProgressText(progressText);
    progressBar.auto();
  }

  /**
   * Returns the minimum progress width.
   * 
   * @return the width
   */
  public int getMinProgressWidth() {
    return minProgressWidth;
  }

  /**
   * Returns the box's progress bar.
   * 
   * @return the progress bar
   */
  public AutoProgressBar getProgressBar() {
    return progressBar;
  }

  /**
   * Returns the progress text.
   * 
   * @return the progress text
   */
  public String getProgressText() {
    return progressText;
  }

  /**
   * The minimum width in pixels of the message box if it is a progress-style
   * dialog. This is useful for setting a different minimum width than text-only
   * dialogs may need (defaults to 250).
   * 
   * @param minProgressWidth the min progress width
   */
  public void setMinProgressWidth(int minProgressWidth) {
    this.minProgressWidth = minProgressWidth;
  }

  /**
   * The text to display inside the progress bar (defaults to "").
   * 
   * @param progressText the progress text
   */
  public void setProgressText(String progressText) {
    this.progressText = progressText;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(progressBar);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(progressBar);
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (getProgressText() != null) {
      progressBar.updateText(getProgressText());
    }
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    if (width != -1) {
      progressBar.setWidth(width - getFrameSize().getWidth());
    }
  }

}
