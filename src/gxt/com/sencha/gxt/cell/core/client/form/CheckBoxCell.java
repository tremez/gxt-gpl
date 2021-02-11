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
package com.sencha.gxt.cell.core.client.form;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer.CellTapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * Renders an HTML check box.
 * 
 * <p />
 * To avoid increasing the row height when used in a {@link Grid} you can adjust the grids cell padding with this code:
 * 
 * <pre>
      ColumnConfig&lt;Stock, Boolean> splitCol = new ColumnConfig&lt;Stock, Boolean>(props.split(), 50, "Split");
      splitCol.setCell(new CheckBoxCell());
      
      // reduce the padding on text element as we have widgets in the cells
      SafeStyles textStyles = SafeStylesUtils.fromTrustedString("padding: 1px 3px;");
      splitCol.setColumnTextStyle(textStyles);
 * </pre>
 */
public class CheckBoxCell extends ValueBaseInputCell<Boolean> {


  private class MouseUpHandler implements NativePreviewHandler {

    private HandlerRegistration reg;

    MouseUpHandler() {
      reg = Event.addNativePreviewHandler(this);
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
      int type = event.getTypeInt();

      if (type == Event.ONMOUSEUP) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

          @Override
          public void execute() {
            ignoreNextBlur = false;
            reg.removeHandler();
          }
        });
      }
    }
  }

  public interface CheckBoxAppearance extends ValueBaseFieldAppearance {
    void render(SafeHtmlBuilder sb, Boolean value, CheckBoxCellOptions opts);

    void setBoxLabel(SafeHtml boxLabel, XElement parent);
  }

  public static class CheckBoxCellOptions extends FieldAppearanceOptions {

    private SafeHtml boxLabel = SafeHtmlUtils.EMPTY_SAFE_HTML;

    public SafeHtml getBoxLabel() {
      return boxLabel;
    }

    public void setBoxLabel(SafeHtml boxLabel) {
      this.boxLabel = boxLabel;
    }

  }

  private SafeHtml boxLabel = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private boolean ignoreNextBlur;

  public CheckBoxCell() {
    this(GWT.<CheckBoxAppearance> create(CheckBoxAppearance.class));
  }

  public CheckBoxCell(CheckBoxAppearance appearance) {
    super(appearance);
    addCellGestureAdapter(new CellTapGestureRecognizer<Boolean>() {
      @Override
      protected void onTap(TouchData tap, Context context, Element parent, Boolean value, ValueUpdater<Boolean> valueUpdater) {
        Event event = tap.getLastNativeEvent().cast();
        updateCheckBoxValue(parent, event, value, valueUpdater);
      }
    });
  }

  @Override
  public CheckBoxAppearance getAppearance() {
    return (CheckBoxAppearance) super.getAppearance();
  }

  public SafeHtml getBoxLabel() {
    return boxLabel;
  }

  @Override
  public boolean isEditing(Context context, Element parent, Boolean value) {
    // A checkbox is never in "edit mode". There is no intermediate state
    // between checked and unchecked.
    return false;
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event,
      ValueUpdater<Boolean> valueUpdater) {
    Element target = event.getEventTarget().cast();
    if (!parent.isOrHasChild(target)) {
      return;
    }
    super.onBrowserEvent(context, parent, value, event, valueUpdater);

    boolean enterPressed = "keydown".equals(event.getType()) && event.getKeyCode() == KeyCodes.KEY_ENTER;

    if ("click".equals(event.getType()) || enterPressed) {
      event.stopPropagation();

      if (enterPressed) {
        event.preventDefault();
      }

      updateCheckBoxValue(parent, event, value, valueUpdater);
    }
  }

  private void updateCheckBoxValue(Element parent, NativeEvent event, Boolean value, ValueUpdater<Boolean> valueUpdater) {
    // if disabled, return immediately
    if (isDisabled()) {
      event.preventDefault();
      event.stopPropagation();
      return;
    }

    String eventType = event.getType();

    // on macs and chrome windows, the checkboxes blur on click which causes issues with inline editing as edit
    // is cancelled
    if ((GXT.isChrome() || GXT.isMac()) && "mousedown".equals(eventType)) {
      ignoreNextBlur = true;
    }

    final Element target = event.getEventTarget().cast();
    final InputElement input = getInputElement(parent);
    Boolean checked = input.isChecked();

    boolean label = "LABEL".equals(target.getTagName());
    boolean enterPressed = "keydown".equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER;


    // TODO this should be changed to remove reference to known subclass
    boolean radio = this instanceof RadioCell;

    if (label || enterPressed) {
      event.preventDefault();

      if (checked && radio) {
        return;
      }

      // input will NOT have been updated for label clicks
      checked = !checked;
      input.setChecked(checked);

    } else if (radio && value) {
      // no action required if value is already true and this is a radio
      return;
    }

    if (valueUpdater != null && checked != value) {
      valueUpdater.update(checked);
    }

    if (ignoreNextBlur) {
      ignoreNextBlur = false;
      input.focus();
    }
  }

  @Override
  protected void onMouseDown(XElement parent, NativeEvent event) {
    super.onMouseDown(parent, event);
    if (GXT.isMac()) {
      new MouseUpHandler();
    }
  }

  @Override
  public void render(com.google.gwt.cell.client.Cell.Context context, Boolean value, SafeHtmlBuilder sb) {
    CheckBoxCellOptions opts = new CheckBoxCellOptions();

    opts.setName(name);

    opts.setReadonly(isReadOnly());
    opts.setDisabled(isDisabled());
    opts.setBoxLabel(getBoxLabel());

    getAppearance().render(sb, value == null ? false : value, opts);
  }

  /**
   * The text that appears beside the checkbox (defaults to null).
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the box label text
   */
  public void setBoxLabel(XElement parent, String text) {
    setBoxLabel(parent, SafeHtmlUtils.fromString(text));
  }

  /**
   * The html that appears beside the checkbox (defaults to null).
   * 
   * @param html the box label html
   */
  public void setBoxLabel(XElement parent, SafeHtml html) {
    this.boxLabel = html;
    getAppearance().setBoxLabel(html, parent);
  }

  @Override
  protected void onBlur(com.google.gwt.cell.client.Cell.Context context, XElement parent, Boolean value,
      NativeEvent event, ValueUpdater<Boolean> valueUpdater) {

    if (ignoreNextBlur) {
      return;
    }

    super.onBlur(context, parent, value, event, valueUpdater);
  }

  @Override
  protected void onEnterKeyDown(com.google.gwt.cell.client.Cell.Context context, Element parent, Boolean value,
      NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
    // intentionally not calling super as we handle enter key
  }

}
