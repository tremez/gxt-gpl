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
package com.sencha.gxt.widget.core.client.grid.editing;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.HeaderMouseDownEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TriggerField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.form.error.HasErrorHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Cell based inline editing.
 * 
 * @param <M> the model type
 */
public class GridInlineEditing<M> extends AbstractGridEditing<M> {

  protected class GridEditingKeyNav extends AbstractGridEditingKeyNav {

    @Override
    public void onTab(NativeEvent evt) {
      GridInlineEditing.this.onTab(evt);
    }
  }

  protected GroupingHandlerRegistration fieldRegistration = new GroupingHandlerRegistration();
  protected boolean ignoreScroll;

  private static Logger logger = Logger.getLogger(GridInlineEditing.class.getName());

  private boolean ignoreNextEnter;
  private boolean focusOnComplete;
  private boolean revertInvalid = false;

  protected boolean activeEdit;
  protected boolean rowUpdated;

  public GridInlineEditing(Grid<M> editableGrid) {
    setEditableGrid(editableGrid);
  }

  @Override
  public void cancelEditing() {
    ignoreScroll = false;
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("cancelEditing active is " + (activeCell == null ? "null" : "no null"));
    }
    if (activeCell != null) {
      Element elem = getEditableGrid().getView().getCell(activeCell.getRow(), activeCell.getCol());
      elem.getFirstChildElement().getStyle().setVisibility(Style.Visibility.VISIBLE);

      ColumnConfig<M, ?> c = columnModel.getColumn(activeCell.getCol());
      IsField<?> field = getEditor(c);
      field.clear();

      removeEditor(activeCell, field);

      final GridCell gc = activeCell;
      activeCell = null;

      fireEvent(new CancelEditEvent<M>(gc));

      if (focusOnComplete) {
        focusOnComplete = false;
        focusGrid();
        // EXTGWT-2856 focus of grid not working after canceling an edit in IE.
        // something is stealing focus and the only fix so far is to run focus call in a timer. deferred does not fix.
        // need to find why focus is not staying on first call.
        if (GXT.isIE()) {
          Timer t = new Timer() {
            @Override
            public void run() {
              focusGrid();
            }
          };
          t.schedule(100);
        }
      }

    }
    stopMonitoring();
  }

  @Override
  public void completeEditing() {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("completeEditing active is " + (activeCell == null ? "null" : "no null"));
    }
    if (activeCell != null) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("completeEditing");
      }

      Element elem = getEditableGrid().getView().getCell(activeCell.getRow(), activeCell.getCol());
      elem.getFirstChildElement().getStyle().setVisibility(Style.Visibility.VISIBLE);

      doCompleteEditing();
    }
    stopMonitoring();
  }

  /**
   * Returns {@code true} of the editor reverts the value to the start value on invalid.
   * 
   * @return the revert invalid state
   */
  public boolean isRevertInvalid() {
    return revertInvalid;
  }

  /**
   * True to automatically revert the field value and cancel the edit when the user completes an edit and the field
   * validation fails (defaults to {@code false}).
   * 
   * @param revertInvalid true to revert
   */
  public void setRevertInvalid(boolean revertInvalid) {
    this.revertInvalid = revertInvalid;
  }

  @Override
  public void startEditing(final GridCell cell) {
    if (getEditableGrid() != null && getEditableGrid().isAttached() && cell != null) {
      ColumnConfig<M, ?> c = columnModel.getColumn(cell.getCol());

      M value = getEditableGrid().getStore().get(cell.getRow());

      // editable
      if (value != null && getEditor(c) != null) {
        BeforeStartEditEvent<M> ce = new BeforeStartEditEvent<M>(cell);
        fireEvent(ce);
        if (ce.isCancelled()) {
          return;
        }

        if (getEditableGrid().getSelectionModel() instanceof CellSelectionModel) {
          if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest("startEditing selectCell");
          }

          ((CellSelectionModel<?>) getEditableGrid().getSelectionModel()).selectCell(cell.getRow(), cell.getCol());
        }

        Element elem = getEditableGrid().getView().getCell(cell.getRow(), cell.getCol());
        elem.getFirstChildElement().getStyle().setVisibility(Style.Visibility.HIDDEN);

        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest("startEditing call cancelEditing, ignoreScroll true, ensure visible");
        }

        cancelEditing();
        ignoreScroll = true;
        getEditableGrid().getView().ensureVisible(cell.getRow(), cell.getCol(), true);

        doStartEditing(cell);
      }
    }
  }

  @SuppressWarnings("unchecked")
  protected <N, O> void doCompleteEditing() {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("doCompleteEditing activeCell is " + (activeCell != null ? " is not null" : "is null"));
    }

    if (activeCell != null) {
      final ColumnConfig<M, N> c = columnModel.getColumn(activeCell.getCol());

      IsField<O> field = getEditor(c);

      if (field != null) {

        Converter<N, O> converter = getConverter(c);

        if (!field.isValid(false) && revertInvalid) {
          cancelEditing();
          return;
        }

        O fieldValue = null;

        if (field instanceof ValueBaseField) {
          fieldValue = ((ValueBaseField<O>) field).getCurrentValue();
        } else {
          fieldValue = field.getValue();
        }

        final N convertedValue;
        if (converter != null) {
          convertedValue = converter.convertFieldValue(fieldValue);
        } else {
          convertedValue = (N) fieldValue;
        }

        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest("Converted value: " + convertedValue);
        }

        removeEditor(activeCell, field);

        ListStore<M> store = getEditableGrid().getStore();
        ListStore<M>.Record r = store.getRecord(store.get(activeCell.getRow()));

        rowUpdated = true;

        r.addChange(c.getValueProvider(), convertedValue);
        fireEvent(new CompleteEditEvent<M>(activeCell));

        if (focusOnComplete) {
          focusOnComplete = false;
          focusGrid();
        }
      }

      activeCell = null;
    }
  }

  protected void doFocus(IsWidget field) {
    try {
      Widget widget = field.asWidget();
      if (widget instanceof Component) {
        ((Component) widget).focus();
      } else if (widget instanceof Focusable) {
        ((Focusable) widget).setFocus(true);
      } else {
        widget.getElement().focus();
      }
    } catch (Exception e) {
      // IE throws exception if element not focusable
    }
  }

  @SuppressWarnings("unchecked")
  protected <N, O> void doStartEditing(final GridCell cell) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("doStartEditing");
    }

    if (getEditableGrid() != null && getEditableGrid().isAttached() && cell != null) {
      M value = getEditableGrid().getStore().get(cell.getRow());

      ColumnConfig<M, N> c = columnModel.getColumn(cell.getCol());
      if (c != null && value != null) {

        Converter<N, O> converter = getConverter(c);

        ValueProvider<? super M, N> v = c.getValueProvider();
        N colValue = getEditableGrid().getStore().hasRecord(value)
            ? getEditableGrid().getStore().getRecord(value).getValue(v) : v.getValue(value);
        O convertedValue;
        if (converter != null) {
          convertedValue = converter.convertModelValue(colValue);
        } else {
          convertedValue = (O) colValue;
        }

        final IsField<O> field = getEditor(c);
        if (field != null) {
          if (field instanceof HasErrorHandler) {
            ((HasErrorHandler) field).setErrorSupport(null);
          }

          activeCell = cell;

          if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest("doStartEditing convertedValue: " + convertedValue);
          }

          field.setValue(convertedValue);

          if (field instanceof TriggerField<?>) {
            ((TriggerField<?>) field).setMonitorTab(false);
          }

          if (field instanceof CheckBox) {
            ((CheckBox) field).setBorders(true);
          }

          Widget w = field.asWidget();
          getEditableGrid().getView().getEditorParent().appendChild(w.getElement());
          ComponentHelper.setParent(getEditableGrid(), w);
          ComponentHelper.doAttach(w);

          w.setPixelSize(c.getWidth(), Integer.MIN_VALUE);
          w.getElement().<XElement>cast().makePositionable(true);

          Element row = getEditableGrid().getView().getRow(cell.getRow());

          int left = 0;
          for (int i = 0; i < cell.getCol(); i++) {
            if (!columnModel.isHidden(i)) {
              left += columnModel.getColumnWidth(i);
            }
          }

          w.getElement().<XElement>cast().setLeftTop(left,
              row.getAbsoluteTop() - getEditableGrid().getView().getBody().getAbsoluteTop());

          field.asWidget().setVisible(true);

          startMonitoring();

          Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
              if (GXTLogConfiguration.loggingIsEnabled()) {
                logger.finest("doStartEditing scheduleDeferred doFocus ");
              }

              // browsers select all when tabbing into a input and put cursor at location when clicking into an input
              // with inline editing, the field is not visible at time of click so we select all. we ignore
              // field.isSelectOnFocus as this only applies when clicking into a field
              if (field instanceof ValueBaseField<?>) {
                ValueBaseField<?> vf = (ValueBaseField<?>) field;
                vf.selectAll();
              }

              // EXTGWT-2856 calling doFocus before selectAll is causing blur to fire which ends the edit immediately
              // after it starts
              doFocus(field);

              ignoreScroll = false;

              fieldRegistration.removeHandler();

              fieldRegistration.add(field.addValueChangeHandler(new ValueChangeHandler<O>() {

                @Override
                public void onValueChange(ValueChangeEvent<O> event) {

                  if (GXTLogConfiguration.loggingIsEnabled()) {
                    logger.finest("doStartEditing onValueChanged");
                  }

                  // if enter key cause value change we want to ignore the next
                  // enter key otherwise
                  // new edit will start by onEnter
                  ignoreNextEnter = true;

                  Timer t = new Timer() {

                    @Override
                    public void run() {
                      ignoreNextEnter = false;
                    }
                  };

                  completeEditing();

                  t.schedule(100);
                }
              }));

              fieldRegistration.add(field.addBlurHandler(new BlurHandler() {

                @Override
                public void onBlur(final BlurEvent event) {
                  if (GXTLogConfiguration.loggingIsEnabled()) {
                    logger.finest("doStartEditing onBlur");
                  }

                  ignoreNextEnter = true;

                  Timer t = new Timer() {

                    @Override
                    public void run() {
                      ignoreNextEnter = false;
                    }
                  };

                  if (GXTLogConfiguration.loggingIsEnabled()) {
                    logger.finest("doStartEditing onBlur call cancelEditing");
                  }

                  cancelEditing();

                  t.schedule(100);
                }
              }));

              fireEvent(new StartEditEvent<M>(cell));
            }
          });
        }
      }
    }

  }

  @Override
  protected KeyNav ensureInternalKeyNav() {
    if (keyNav == null) {
      keyNav = new GridEditingKeyNav();
    }
    return keyNav;
  }

  @Override
  protected SafeHtml getErrorHtml() {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    sb.appendHtmlConstant("<ul>");
    ColumnConfig<M, ?> c = columnModel.getColumn(activeCell.getCol());
    IsField<?> f = getEditor(c);

    getErrorMessage(f, sb, c.getHeader());

    sb.appendHtmlConstant("</ul>");
    return sb.toSafeHtml();
  }

  @Override
  protected <N, O> void handleHeaderMouseDown(HeaderMouseDownEvent event) {
    if (activeCell != null) {
      final ColumnConfig<M, N> c = columnModel.getColumn(activeCell.getCol());

      IsField<O> field = getEditor(c);

      // EXTGWT-3366 rather than calling completeEditing directly, have the field
      // finish editing which will cause completeEditing to be called in the correct sequence
      field.finishEditing();
    }
  }

  protected boolean isValid() {
    if (activeCell == null) {
      return true;
    }
    ColumnConfig<M, ?> c = columnModel.getColumn(activeCell.getCol());
    IsWidget w = getEditor(c);
    if (w instanceof ValueBaseField<?>) {
      ValueBaseField<?> f = (ValueBaseField<?>) w;
      if (!f.isCurrentValid(true)) {
        return false;
      }
    } else if (w instanceof Field<?>) {
      Field<?> f = (Field<?>) w;
      if (!f.isValid(true)) {
        return false;
      }
    }

    return true;
  }

  @Override
  protected void onEnter(NativeEvent evt) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("onEnter");
    }
    if (ignoreNextEnter) {
      ignoreNextEnter = false;
      focusGrid();
      return;
    }

    // enter key with no value changed fired
    if (activeCell != null) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("onEnter activeCell not null (enter key no value change), cancel edit");
      }

      ColumnConfig<M, ?> c = columnModel.getColumn(activeCell.getCol());
      IsField<?> f = getEditor(c);
      if (f instanceof TextArea) {
        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest("onEnter editor type TextArea so ignoring");
        }
        return;
      }

      focusOnComplete = true;
      cancelEditing();
      return;
    }

    GridSelectionModel<M> sm = getEditableGrid().getSelectionModel();
    if (sm instanceof CellSelectionModel) {
      CellSelection<M> cell = ((CellSelectionModel<M>) sm).getSelectCell();
      if (cell != null) {
        evt.preventDefault();
        startEditing(new GridCell(cell.getRow(), cell.getCell()));
      }
    }
  }

  @Override
  protected void onEsc(NativeEvent evt) {
    if (activeCell != null) {
      focusOnComplete = true;
      super.onEsc(evt);
    }
  }

  @Override
  protected void onMouseDown(MouseDownEvent event) {
    // do we have an active edit at time of mouse down
    activeEdit = activeCell != null;
    rowUpdated = false;
  }

  @Override
  protected void onMouseUp(MouseUpEvent event) {
    // there was an active edit on mouse down and that edit has ended
    // we do not get a "click" event if the previous edit caused the row to be updated
    if (getClicksToEdit() == ClicksToEdit.ONE && activeEdit && rowUpdated && activeCell == null) {
      Element target = event.getNativeEvent().getEventTarget().cast();
      startEditing(target);
    }
    activeEdit = false;
    rowUpdated = false;
  }

  protected void onScroll(ScrollEvent event) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("onScroll ignoreScroll " + (ignoreScroll ? "true" : "false calling cancelEditing"));
    }
    if (!ignoreScroll) {
      cancelEditing();
    }
  }

  protected void onTab(NativeEvent evt) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("onTab");
    }

    // keep active cell since we manually fire blur (finishEditing) which will
    // call cancel edit
    // clearing active cell
    final GridCell active = activeCell;

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("onTab activeCell is " + (activeCell == null ? "null" : "not null"));
    }

    if (activeCell != null) {
      ColumnConfig<M, ?> c = columnModel.getColumn(activeCell.getCol());

      IsField<?> field = getEditor(c);

      // we handle navigation programatically
      evt.preventDefault();

      // since we are preventingDefault on tab key, the field will not blur on
      // its
      // own, which means the value change event will not fire so we manually
      // blur
      // the field, so we call finishEditing
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("onTab calling field.finishEditing()");
      }
      field.finishEditing();

    }

    if (active != null) {

      GridCell newCell = null;

      if (evt.getShiftKey()) {
        newCell = getEditableGrid().walkCells(active.getRow(), active.getCol() - 1, -1, callback);
      } else {
        newCell = getEditableGrid().walkCells(active.getRow(), active.getCol() + 1, 1, callback);
      }
      if (newCell != null) {
        final GridCell c = newCell;

        Scheduler.get().scheduleFinally(new ScheduledCommand() {

          @Override
          public void execute() {
            if (GXTLogConfiguration.loggingIsEnabled()) {
              logger.finest("onTab scheduleFinally startEditing");
            }
            startEditing(c);
          }
        });
      } else {
        // when tabbing and no next cell to start edit, current edit is not ending
        // the focusCell call is not causing field to blur and finish editing
        if (isEditing()) {
          completeEditing();
        }
        getEditableGrid().getView().focusCell(active.getRow(), active.getCol(), true);
      }
    }
  }

  protected void showTooltip(SafeHtml msg) {
    if (activeCell == null) {
      return;
    }

    ColumnConfig<M, ?> c = columnModel.getColumn(activeCell.getCol());
    IsField<?> f = getEditor(c);

    if (tooltip == null) {
      ToolTipConfig config = new ToolTipConfig();
      config.setAutoHide(false);
      config.setMouseOffsetX(0);
      config.setMouseOffsetY(0);
      config.setAnchor(Side.LEFT);
      tooltip = new ToolTip(f.asWidget(), config);
      tooltip.setMaxWidth(600);
    }

    tooltip.initTarget(f.asWidget());

    ToolTipConfig config = tooltip.getToolTipConfig();
    config.setBody(msg);
    tooltip.update(config);
    tooltip.enable();
    if (!tooltip.isAttached()) {
      tooltip.show();
    }
  }

  private void removeEditor(final GridCell cell, final IsField<?> field) {
    assert field != null;
    removeFieldBlurHandler();

    if (GXT.isIE() && field instanceof ValueBaseField<?>) {
      ValueBaseField<?> f = (ValueBaseField<?>) field;
      f.getCell().getInputElement(f.getElement()).blur();
    }

    Widget w = field.asWidget();
    if (field != null && w.isAttached()) {
      field.asWidget().setVisible(false);
      ComponentHelper.setParent(null, w);
      ComponentHelper.doDetach(w);
    }
  }

  private void removeFieldBlurHandler() {
    fieldRegistration.removeHandler();
  }

}
