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
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.DatePicker;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.menu.DateMenu;

import java.text.ParseException;
import java.util.Date;

public class DateCell extends TriggerFieldCell<Date> implements HasExpandHandlers, HasCollapseHandlers {

  public interface DateCellAppearance extends TriggerFieldAppearance {

  }

  private GroupingHandlerRegistration menuHandler;
  private DateMenu menu;
  private boolean expanded;
  private Context expandContext;
  private XElement expandParent;
  private Date expandValue;
  private ValueUpdater<Date> expandValueUpdater;

  /**
   * Creates a new date cell.
   */
  public DateCell() {
    this(GWT.<DateCellAppearance> create(DateCellAppearance.class));
  }

  /**
   * Creates a new date cell.
   * 
   * @param appearance the date cell appearance
   */
  public DateCell(DateCellAppearance appearance) {
    super(appearance);
    setPropertyEditor(new DateTimePropertyEditor());
  }

  @Override
  public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
    return addHandler(handler, CollapseEvent.getType());
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandHandler handler) {
    return addHandler(handler, ExpandEvent.getType());
  }

  public void collapse(final Context context, final XElement parent) {
    collapse(context, parent, true);
  }

  public void collapse(final Context context, final XElement parent, boolean focusInput) {
    if (!expanded) {
      return;
    }

    expanded = false;
    menu.hide();
    // EXTGWT-4175: this was originally changed to focus if the value was updated or on desktop. don't focus on mobile.
    if (focusInput) {
      getInputElement(expandParent).focus();
    } else {
      /*
       * the editor framework persists values on blur - since we never focus the input, blur is never called.
       *
       * EXTGWT-4374 - there's a focus/blur timing issue on Android devices here.  lastContext is being wiped out before
       * manually triggering blur, resulting in a null pointer.  As a workaround, keep track of the contexts and values
       * on expand (making sure to update the value appropriately) and pass them into the trigger blur method.
       */
      doTriggerBlur(expandContext, expandParent, expandValue, expandValueUpdater);
    }

    expandParent = null;
    expandContext = null;
    expandValue = null;
    expandValueUpdater = null;

    fireEvent(context, new CollapseEvent(context));
  }

  public void expand(final Context context, final XElement parent, Date value, ValueUpdater<Date> valueUpdater) {
    if (expanded) {
      return;
    }

    this.expanded = true;

    // expand may be called without the cell being focused
    // saveContext sets focusedCell so we clear if cell 
    // not currently focused
    boolean focused = focusedCell != null;
    saveContext(context, parent, null, valueUpdater, value);
    if (!focused) {
      focusedCell = null;
    }

    DatePicker picker = getDatePicker();

    Date d;
    try {
      d = getPropertyEditor().parse(getText(parent));
    } catch (ParseException e) {
      d = value == null ? new Date() : value;
    }
    
    picker.setValue(d, false);

    expandContext = context;
    expandParent = parent;
    expandValue = d;
    expandValueUpdater = valueUpdater;

    // handle case when down arrow is opening menu
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        menu.setOnHideFocusElement(getFocusElement(parent));
        menu.show(parent, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true));

        // focus on the date picker once it's been expanded
        menu.getDatePicker().focus();
        
        fireEvent(context, new ExpandEvent(context));
      }
    });
  }

  /**
   * Returns the cell's date picker.
   * 
   * @return the date picker
   */
  public DatePicker getDatePicker() {
    if (menu == null) {
      setMenu(new DateMenu());
    }
    return menu.getDatePicker();
  }

  /**
   * @return the menu instance used to get the datepicker from
   */
  public DateMenu getMenu() {
    return menu;
  }

  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Sets the DateMenu instance to use in this cell when drawing a datepicker
   * @param menu the menu instance to get the datepicker from
   */
  public void setMenu(final DateMenu menu) {
    if (this.menu != null) {
      menuHandler.removeHandler();
      menuHandler = null;
    }
    this.menu = menu;
    if (this.menu != null) {
      menuHandler = new GroupingHandlerRegistration();

      menuHandler.add(menu.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
        @Override
        public void onValueChange(ValueChangeEvent<Date> event) {
          expandValue = event.getValue();
          String valueString = getPropertyEditor().render(expandValue);
          FieldViewData viewData = ensureViewData(expandContext, expandParent);
          if (viewData != null) {
            viewData.setCurrentValue(valueString);
          }
          getInputElement(expandParent).setValue(valueString);
          menu.hide();
        }
      }));
      menuHandler.add(menu.addHideHandler(new HideHandler() {
        @Override
        public void onHide(HideEvent event) {
          /*
           * For now, on touch devices, we're simply not focusing on the input element after the value has changed.
           * The editor framework has a dependency on blur, which it uses to persist changed values.  Since we're not
           * focusing on touch devices, we need to remember to trigger the blur event manually.
           *
           * It's worth noting that we took a look at other datepickers (including Ext JS), and they all had some sort
           * of focus issue either before or after a value had been selected.
           */
          collapse(expandContext, expandParent, !GXT.isTouch());
        }
      }));
    }
  }

  @Override
  protected boolean isFocusedWithTarget(Element parent, Element target) {
    boolean result = parent.isOrHasChild(target)
        || (menu != null && (menu.getElement().isOrHasChild(target) || menu.getDatePicker().getElement().isOrHasChild(
            target)));
    return result;
  }

  @Override
  protected void onNavigationKey(Context context, Element parent, Date value, NativeEvent event,
      ValueUpdater<Date> valueUpdater) {
    if (event.getKeyCode() == KeyCodes.KEY_DOWN && !isExpanded()) {
      event.stopPropagation();
      event.preventDefault();
      onTriggerClick(context, parent.<XElement> cast(), event, value, valueUpdater);
    }
  }

  @Override
  protected void onTriggerClick(Context context, XElement parent, NativeEvent event, Date value,
      ValueUpdater<Date> updater) {
    super.onTriggerClick(context, parent, event, value, updater);
    if (!isReadOnly() && !isDisabled()) {
      // EXTGWT-4176: can't call this on touch devices - timing issue creates null pointers
      if (!GXT.isTouch()) {
        onFocus(context, parent, value, event, updater);
      }
      expand(context, parent, value, updater);
    }
  }

  @Override
  protected void triggerBlur(Context context, XElement parent, Date value, ValueUpdater<Date> valueUpdater) {
    super.triggerBlur(context, parent, value, valueUpdater);

    collapse(context, parent);
  }

  /*
   * EXTGWT-4176 - There's problems here with touch devices.  On touch devices, we can't focus on the input element to
   * trigger the editor, so we call the TriggerBlur method manually (see EXTGWT-4175).  If the super method is called
   * here on touch, it will call clearContext and set the lastContext to null.  When we manually call triggerBlur, if
   * lastContext is null, we will hit a null pointer.
   *
   * Note: this can't be abstracted into the superclass - other TriggerFieldCells (ComboBoxCell for one) depend on it.
   */
  @Override
  protected void handleFocusManagerExecute(Context context, XElement parent, Date value, ValueUpdater<Date> updater) {
    if (!GXT.isTouch()) {
      super.handleFocusManagerExecute(context, parent, value, updater);
    }
  }
}