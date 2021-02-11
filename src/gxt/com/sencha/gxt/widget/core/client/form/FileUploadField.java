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

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasName;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;

/**
 * A file upload field. When using this field, the containing form panel's encoding must be set to MULTIPART using
 * {@link FormPanel#setEncoding(Encoding)}. In addition, the method should be set to POST using
 * {@link FormPanel#setMethod(Method)}
 * 
 * <p />
 * You must set a name for uploads to work with Firefox.
 */
public class FileUploadField extends Component implements IsField<String>, HasChangeHandlers, HasName {

  public interface FileUploadFieldAppearance {

    String buttonClass();

    String fileInputClass();

    void render(SafeHtmlBuilder sb);

    String textFieldClass();

    String wrapClass();
  }

  public interface FileUploadFieldMessages {
    String browserText();
  }

  protected class DefaultFileUploadFieldMessages implements FileUploadFieldMessages {

    @Override
    public String browserText() {
      return DefaultMessages.getMessages().uploadField_browseText();
    }

  }

  private FileUploadFieldMessages messages;
  private final FileUploadFieldAppearance appearance;
  private int buttonOffset = 3;
  private TextButton button;
  private XElement file;
  private String name;
  private TextField input;

  /**
   * Creates a new file upload field.
   */
  public FileUploadField() {
    this(GWT.<FileUploadFieldAppearance> create(FileUploadFieldAppearance.class));
  }

  /**
   * Creates a new file upload field.
   * 
   * @param appearance the appearance
   */
  public FileUploadField(FileUploadFieldAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));

    input = new TextField();
    input.setReadOnly(true);
    input.setTabIndex(-1);
    getElement().appendChild(input.getElement());

    sinkEvents(Event.ONCHANGE | Event.ONCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);

    createFileInput();

    button = new TextButton(getMessages().browserText());
    DivElement wrapper = Document.get().createDivElement();
    wrapper.addClassName(appearance.buttonClass());
    XElement buttonElement = button.getElement();
    if (GXT.isIE8()) {
      buttonElement.removeClassName(CommonStyles.get().inlineBlock());
    }
    wrapper.appendChild(buttonElement);
    getElement().appendChild(wrapper);

    ensureVisibilityOnSizing = true;
    setWidth(150);

    addGestureRecognizer(new TapGestureRecognizer() {
      @Override
      protected void onTap(TouchData touchData) {
        super.onTap(touchData);
        FileUploadField.this.onTap(touchData);
      }
    });
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
    return addHandler(valueChangeHandler, ValueChangeEvent.getType());
  }

  @Override
  public void clear() {
    input.reset();
    createFileInput();
    resizeFile();
  }

  @Override
  public void clearInvalid() {
    // do nothing
  }

  @Override
  public void finishEditing() {
  }

  public FileUploadFieldAppearance getAppearance() {
    return appearance;
  }

  @Override
  public List<EditorError> getErrors() {
    return Collections.emptyList();
  }

  /**
   * Returns the file upload field messages.
   * 
   * @return the messages
   */
  public FileUploadFieldMessages getMessages() {
    if (messages == null) {
      messages = new DefaultFileUploadFieldMessages();
    }
    return messages;
  }

  @Override
  public String getName() {
    return file.getAttribute("name");
  }

  @Override
  public String getValue() {
    return getFileInput().getValue();
  }

  /**
   * Returns the field's allow blank state.
   * 
   * @return true if blank values are allowed
   */
  public boolean isAllowBlank() {
    return input.isAllowBlank();
  }

  /**
   * Returns the read only state.
   * 
   * @return true if read only, otherwise false
   */
  public boolean isReadOnly() {
    return input.isReadOnly();
  }

  /**
   * Returns whether or not the field value is currently valid.
   * 
   * @return true if the value is valid, otherwise false
   */
  public boolean isValid() {
    return input.isValid();
  }

  @Override
  public boolean isValid(boolean preventMark) {
    return input.isValid(preventMark);
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    if (event.getTypeInt() == Event.ONCHANGE) {
      event.stopPropagation();
      onChange(event);
    }

    if ((event.getTypeInt() != Event.ONCLICK) && event.getEventTarget().<Element> cast().isOrHasChild(file)) {
      button.onBrowserEvent(event);
    }

    if (event.getTypeInt() == Event.ONKEYPRESS
        && button.getElement().isOrHasChild(event.getEventTarget().<XElement> cast())) {
      int key = event.getKeyCode();
      switch (key) {
        case KeyCodes.KEY_ENTER:
        case 32:
          file.click();
          break;
      }
    }
  }

  public void onTap(TouchData touchData) {
    Event event = touchData.getLastNativeEvent().cast();
    XElement targetElement = event.getEventTarget().cast();
    if(file.isOrHasChild(targetElement) ||
        button.getElement().isOrHasChild(targetElement)) {
      file.click();
    }
  }

  @Override
  public void reset() {
    clear();
  }

  /**
   * Sets whether a field is valid when its value length = 0 (default to true).
   * 
   * @param allowBlank true to allow blanks, false otherwise
   */
  public void setAllowBlank(boolean allowBlank) {
    input.setAllowBlank(allowBlank);
  }

  /**
   * Convenience function for setting disabled/enabled by boolean.
   * 
   * @param enabled the enabled state
   */
  @Override
  public void setEnabled(boolean enabled) {
    input.setEnabled(enabled);
    setReadOnly(enabled);
    super.setEnabled(enabled);
  }

  /**
   * Sets the file upload field messages.
   * 
   * @param messages the messages
   */
  public void setMessages(FileUploadFieldMessages messages) {
    this.messages = messages;
    this.button.setText(messages.browserText());
  }

  @Override
  public void setName(String name) {
    this.name = name;
    file.<InputElement> cast().setName(name);
  }

  /**
   * Sets the field's read only state.
   * 
   * @param readonly the read only state
   */
  public void setReadOnly(boolean readonly) {
    button.setEnabled(readonly);
    input.setReadOnly(readonly);
    // A invisible file input element hovers over displayed button
    file.setVisibility(readonly);
  }

  @Override
  public void setValue(String value) {
    throw new UnsupportedOperationException("You cannot set the value for file upload field");
  }

  @Override
  public boolean validate(boolean preventMark) {
    return input.validate(preventMark);
  }

  protected void createFileInput() {
    if (file != null) {
      file.removeFromParent();
    }

    file = Document.get().createFileInputElement().cast();
    file.addEventsSunk(Event.ONCHANGE | Event.FOCUSEVENTS);
    file.setId(XDOM.getUniqueId());
    file.addClassName(appearance.fileInputClass());
    file.setTabIndex(-1);
    ((InputElement) file.cast()).setName(name);

    getElement().insertFirst(file);
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(input);
    ComponentHelper.doAttach(button);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(input);
    ComponentHelper.doDetach(button);
  }

  /**
   * Returns the file input element. You should not store a reference to this. When resetting this field the file input
   * will change.
   */
  protected InputElement getFileInput() {
    return (InputElement) file.cast();
  }

  @Override
  protected XElement getFocusEl() {
    return button.getElement();
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    resizeFile();
  }

  @Override
  protected void onBlur(Event event) {
    super.onBlur(event);
    Rectangle rec = button.getElement().getBounds();
    if (rec.contains(BaseEventPreview.getLastXY())) {
      event.stopPropagation();
      event.preventDefault();
      return;
    }
    super.onBlur(event);
  }

  protected void onChange(Event event) {
    if (GXT.isIE()) {
      input.setReadOnly(false);
    }
    input.removeToolTip();
    String fileValue = getFileInput().getValue();
    if (!(fileValue == null || "".equals(fileValue))) {
      // browsers put C:\fakepath\ in the value.  We don't need to display that
      fileValue = fileValue.substring(fileValue.lastIndexOf("\\") + 1);
      input.clearInvalid();

      TextMetrics tm = TextMetrics.get();
      tm.bind(input.getInputEl());
      int availableSpace = input.getInputEl().getOffsetWidth() - input.getInputEl().getPadding(Side.LEFT, Side.RIGHT);

      if (tm.getWidth(fileValue) > availableSpace) {
        input.setToolTip(fileValue);
      }
    }
    input.setValue(fileValue);
    if (GXT.isIE()) {
      input.setReadOnly(true);
    }
    button.focus();
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    input.setWidth(width - button.getOffsetWidth() - buttonOffset);
    resizeFile();
  }

  protected void resizeFile() {
    // button wraps to new line in IE8
    if (!GXT.isIE8()) {
      int height = input.getOffsetHeight();
      if (height > 25) {
        getElement().getStyle().setMarginBottom(4, Unit.PX);
        height -= 2;
        button.setHeight(height);
        getElement().getFirstChildElement().getStyle().setHeight(height, Unit.PX);
      }
    }

    file.setWidth(button.getOffsetWidth());
  }

}
