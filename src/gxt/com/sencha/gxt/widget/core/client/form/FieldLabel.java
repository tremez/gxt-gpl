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
package com.sencha.gxt.widget.core.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.ExpandedHtmlSanitizer;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

/**
 * Form field wrapper to add a label and validation error text.
 */
public class FieldLabel extends SimpleContainer implements HasText, HasHTML, HasSafeHtml {

  /**
   * Describes the appearance for a {@link FieldLabel} object. Different from
   * most appearance objects, as it must provide direct access to the wrapper
   * element instead of just access to set and get the properties of the UI.
   */
  public interface FieldLabelAppearance {

    void clearLabelFor(XElement parent);

    XElement getChildElementWrapper(XElement parent);

    XElement getLabelElement(XElement parent);

    void onUpdateOptions(XElement parent, FieldLabelOptions options);

    void render(SafeHtmlBuilder sb, String id, FieldLabelOptions label);

    void setLabelFor(XElement parent, String id);
  }

  /**
   * A set of configuration parameters for a FieldLabel.
   */
  public class FieldLabelOptions implements HasHTML, HasSafeHtml, HasWordWrap {

    /** The justification of a field label inside its available space */
    private LabelAlign labelAlign = LabelAlign.LEFT;

    /** The width of area available for label text */
    private int labelWidth = 100;

    /**
     * The width of the padding between the label and the control to which the
     * label applies
     */
    private int labelPad = 5;

    /**
     * The string to use as a suffix on the label to separate it from the field
     * it labels
     */
    private String labelSeparator = ":";

    /**
     * The content of the label.
     */
    private SafeHtml content = SafeHtmlUtils.EMPTY_SAFE_HTML;

    /**
     * Whether the {@link #content} should word wrap as needed.
     */
    private boolean wordWrap = true;

    /**
     * Returns the content of the label.
     * 
     * @return the content of the label
     */
    public SafeHtml getContent() {
      return content;
    }

    /**
     * Sets the content of the label as html.
     *
     * @param html the label content html
     */
    public void setContent(SafeHtml html) {
      content = html;
    }

    /**
     * Sets the content of the label as text.
     *
     * Text that contains reserved html characters will be escaped.
     *
     * @param text the label content text
     */
    public void setContent(String text) {
      content = SafeHtmlUtils.fromString(text);
    }

    /**
     * Returns the content of the label as text.
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
     * Sets the content of the label as text.
     *
     * Text that contains reserved html characters will be escaped.
     *
     * @param text the text
     */
    @Override
    public void setText(String text) {
      setContent(text);
    }

    /**
     * Returns the content of the label as html.
     *
     * @return the html
     */
    @Override
    public String getHTML() {
      return content.asString();
    }

    /**
     * Sets the content of the label as html.
     *
     * @param html the html
     */
    @Override
    public void setHTML(SafeHtml html) {
      setContent(html);
    }

    /**
     * Sets the content of the label as html.
     *
     * Untrusted html will be sanitized before use to protect against XSS.
     *
     * @param html the html
       */
    @Override
    public void setHTML(String html) {
      setContent(ExpandedHtmlSanitizer.sanitizeHtml(html));
    }

    /**
     * Returns the justification of a field label inside its available space.
     * 
     * @return the justification of a field label inside its available space
     */
    public LabelAlign getLabelAlign() {
      return labelAlign;
    }

    /**
     * Returns the width of the padding between the label and the control to
     * which the label applies.
     * 
     * @return the width of the padding between the label and the control to
     *         which the label applies
     */
    public int getLabelPad() {
      return labelPad;
    }

    /**
     * Returns the string to use as a suffix on the label to separate it from
     * the field it labels.
     * 
     * @return the string to use as a suffix on the label to separate it from
     *         the field it labels
     */
    public String getLabelSeparator() {
      return labelSeparator;
    }

    /**
     * Returns the width of area available for label text.
     * 
     * @return the width of area available for label text
     */
    public int getLabelWidth() {
      return labelWidth;
    }

    /**
     * Returns true if the {@link #content} should word wrap.
     * 
     * @return true if content should word wrap.
     */
    public boolean getWordWrap() {
      return wordWrap;
    }

    /**
     * Sets the justification of a field label inside its available space.
     * 
     * @param labelAlign the justification of a field label inside its available
     *          space
     */
    public void setLabelAlign(LabelAlign labelAlign) {
      this.labelAlign = labelAlign;
    }

    /**
     * Sets the width of the padding between the label and the control to which
     * the label applies.
     * 
     * @param labelPad the width of the padding between the label and the
     *          control to which the label applies
     */
    public void setLabelPad(int labelPad) {
      this.labelPad = labelPad;
    }

    /**
     * Sets the string to use as a suffix on the label to separate it from the
     * field it labels.
     * 
     * @param labelSeparator the string to use as a suffix on the label to
     *          separate it from the field it labels
     */
    public void setLabelSeparator(String labelSeparator) {
      this.labelSeparator = labelSeparator;
    }

    /**
     * Sets the width of area available for label text.
     * 
     * @param labelWidth the width of area available for label text
     */
    public void setLabelWidth(int labelWidth) {
      this.labelWidth = labelWidth;
    }

    /**
     * Sets whether {@link #content} should word wrap.
     * 
     * @param wordWrap
     */
    public void setWordWrap(boolean wordWrap) {
      this.wordWrap = wordWrap;
    }
  }

  private final FieldLabelAppearance appearance;

  private FieldLabelOptions options = new FieldLabelOptions();

  /**
   * Creates a field label with the default appearance. To be useful, use
   * {@link #setWidget(Widget)} to set the widget and {@link #setText(String)}
   * to set the label.
   */
  public FieldLabel() {
    this(null);
  }

  /**
   * Creates a field label with the default appearance for the specified widget.
   * 
   * @param widget the widget to label
   */
  public FieldLabel(IsWidget widget) {
    this(widget, GWT.<FieldLabelAppearance> create(FieldLabelAppearance.class));
  }

  /**
   * Creates a field label with the specified the specified widget and
   * appearance.
   * 
   * @param widget the widget to label
   * @param appearance the appearance of the field label
   */
  public FieldLabel(IsWidget widget, FieldLabelAppearance appearance) {
    this(asWidgetOrNull(widget), appearance);
  }

  /**
   * Creates a field label with the specified the specified widget and
   * appearance.
   * 
   * @param widget the widget to label
   * @param appearance the appearance of the field label
   */
  public FieldLabel(Widget widget, FieldLabelAppearance appearance) {
    super(true);
    this.appearance = appearance;
    String id;
    if (widget == null) {
      id = null;
    } else {
      id = ComponentHelper.getWidgetId(widget);

      if (widget instanceof ValueBaseField<?>) {
        id += "-input";
      }
    }

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb, id, options);
    setElement((Element) XDOM.create(sb.toSafeHtml()));

    if (widget != null) {
      setWidget(widget);
    }
  }

  /**
   * Creates a field label with the default appearance and the specified widget
   * and label html.
   *
   * @param widget the widget to label
   * @param html the html to use for the label
   */
  public FieldLabel(IsWidget widget, SafeHtml html) {
    this(widget);
    setContent(html);
  }

  /**
   * Creates a field label with the default appearance and the specified widget
   * and label text.
   * 
   * @param widget the widget to label
   * @param text the text to use for the label
   */
  public FieldLabel(IsWidget widget, String text) {
    this(widget);
    setContent(text);
  }

  /**
   * Creates a field label with the specified widget, label html and appearance.
   *
   * @param widget the widget to label
   * @param html the html to use for the label
   * @param appearance the appearance of the field label
   */
  public FieldLabel(IsWidget widget, SafeHtml html, FieldLabelAppearance appearance) {
    this(widget, appearance);
    setContent(html);
  }

  /**
   * Creates a field label with the specified widget, label text and appearance.
   * 
   * @param widget the widget to label
   * @param text the text to use for the label
   * @param appearance the appearance of the field label
   */
  public FieldLabel(IsWidget widget, String text, FieldLabelAppearance appearance) {
    this(widget, appearance);
    setContent(text);
  }

  public FieldLabelAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the content of the label.
   *
   * @return the content of the label
   */
  public SafeHtml getContent() {
    return options.getContent();
  }

  /**
   * Sets the content of the label as html.
   *
   * @param html the label content html
   */
  public void setContent(SafeHtml html) {
    options.setContent(html);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Sets the content of the label as text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the label content text
   */
  public void setContent(String text) {
    options.setContent(text);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Returns the content of the label as text.
   *
   * If text was set that contained reserved html characters, the return value will be html escaped.
   * If html was set instead, the return value will be html.
   *
   * @return the text or html, depending on what was set
   * @see #getHTML()
   */
  @Override
  public String getText() {
    return options.getText();
  }

  /**
   * Sets the content of the label as text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  @Override
  public void setText(String text) {
    options.setText(text);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Returns the content of the label as html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return options.getHTML();
  }

  /**
   * Sets the content of the label as html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    options.setHTML(html);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Sets the content of the label as html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    options.setHTML(html);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Returns the justification of a field label inside its available space.
   * 
   * @return the justification of a field label inside its available space
   */
  public LabelAlign getLabelAlign() {
    return options.getLabelAlign();
  }

  /**
   * Returns the width of the padding between the label and the control to which
   * the label applies.
   * 
   * @return the width of the padding between the label and the control to which
   *         the label applies
   */
  public int getLabelPad() {
    return options.getLabelPad();
  }

  /**
   * Returns the label separator.
   * 
   * @return the label separator
   */
  public String getLabelSeparator() {
    return options.getLabelSeparator();
  }

  /**
   * Returns the label width.
   * 
   * @return the label width
   */
  public int getLabelWidth() {
    return options.getLabelWidth();
  }

  /**
   * Returns true if the label should be allowed to word wrap.
   * 
   * @return true if label should be allowed to word wrap.
   */
  public boolean isLabelWordWrap() {
    return options.getWordWrap();
  }

  /**
   * Sets the justification of a field label inside its available space.
   * 
   * @param labelAlign the justification of a field label inside its available
   *          space
   */
  public void setLabelAlign(LabelAlign labelAlign) {
    options.setLabelAlign(labelAlign);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Sets the width of the padding between the label and the control to which
   * the label applies.
   * 
   * @param labelPad the width of the padding between the label and the control
   *          to which the label applies
   */
  public void setLabelPad(int labelPad) {
    options.setLabelPad(labelPad);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * The standard separator to display after the text of each form label
   * (defaults to colon ':').
   * 
   * @param labelSeparator the label separator or "" for none
   */
  public void setLabelSeparator(String labelSeparator) {
    options.setLabelSeparator(labelSeparator);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Sets the label width (defaults to 100).
   * 
   * @param labelWidth the label width
   */
  public void setLabelWidth(int labelWidth) {
    options.setLabelWidth(labelWidth);
    appearance.onUpdateOptions(getElement(), options);
  }

  /**
   * Sets whether the label should be allowed to word wrap.
   * 
   * @param wordWrap whether label should be allowed to word wrap.
   */
  public void setLabelWordWrap(boolean wordWrap) {
    options.setWordWrap(wordWrap);
    appearance.onUpdateOptions(getElement(), options);
  }

  @Override
  public void setWidget(Widget w) {
    super.setWidget(w);
    String id;
    if (w == null) {
      appearance.clearLabelFor(getElement());
      id = getId();
    } else {
      id = ComponentHelper.getWidgetId(widget);

      if (widget instanceof ValueBaseField<?>) {
        id += "-input";
      }

      appearance.setLabelFor(getElement(), id);
    }
  }

  @Override
  protected void doLayout() {
    if (widget != null) {
      Size size = getElement().getStyleSize();
      int width = -1;
      if (!isAutoWidth()) {

        if (options.getLabelAlign() == LabelAlign.TOP) {
          width = size.getWidth() - getLeftRightMargins(widget);
        } else {
          XElement wrapper = appearance.getChildElementWrapper(getElement());
          width = (wrapper != null ? wrapper.getWidth(true) : size.getWidth()) - getLeftRightMargins(widget);
        }
      }
      int height = -1;

      if (!isAutoHeight()) {
        if (options.getLabelAlign() == LabelAlign.TOP) {
          height = size.getHeight() - getTopBottomMargins(widget)
                  - getElement().getFrameWidth(Side.TOP, Side.BOTTOM)
                  - appearance.getLabelElement(getElement()).getOffsetHeight()
                  - appearance.getChildElementWrapper(getElement()).getFrameWidth(Side.TOP, Side.BOTTOM);
        } else {
          height = size.getHeight() - getTopBottomMargins(widget);
        }
      }

      if (widget.getLayoutData() instanceof MarginData) {
        widget.getElement().getStyle().clearMargin();
        MarginData data = (MarginData) widget.getLayoutData();
        ((XElement) widget.getElement()).makePositionable();
        ((XElement) widget.getElement()).setLeftTop(data.getMargins().getLeft(), data.getMargins().getTop());
      }
      applyLayout(widget, width, height);
    }
  }

  @Override
  protected XElement getContainerTarget() {
    return appearance.getChildElementWrapper(getElement());
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    String id;
    if (widget == null) {
      appearance.clearLabelFor(getElement());
    } else {
      id = ComponentHelper.getWidgetId(widget);
      appearance.setLabelFor(getElement(), id);
    }
  }

}
