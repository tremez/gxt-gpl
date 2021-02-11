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
package com.sencha.gxt.widget.core.client.grid;

import java.util.Collections;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.RefreshHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent.RowMouseDownHandler;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent.ViewReadyHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.grid.Grid.Callback;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.selection.AbstractStoreSelectionModel;

/**
 * Grid selection model.
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>AbstractStoreSelectionModel BeforeSelect</dd>
 * <dd>AbstractStoreSelectionModel SelectionChange</dd>
 * </dl>
 */
public class GridSelectionModel<M> extends AbstractStoreSelectionModel<M> {

  /**
   * Determines whether a given cell is selectable.
   */
  public static class SelectionModelCallback implements Callback {

    private GridSelectionModel<?> sm;

    /**
     * Creates a selection model callback that determines whether a given cell is selectable.
     * 
     * @param sm the selection model
     */
    public SelectionModelCallback(GridSelectionModel<?> sm) {
      this.sm = sm;
    }

    public boolean isSelectable(GridCell cell) {
      return sm.isSelectable(cell.getRow(), cell.getCol());
    }
  }

  private class Handler implements RowMouseDownHandler, RowClickHandler, ViewReadyHandler, RefreshHandler {

    @Override
    public void onRefresh(RefreshEvent event) {
      refresh();
      if (getLastFocused() != null) {
        grid.getView().onHighlightRow(listStore.indexOf(getLastFocused()), true);
      }
    }

    @Override
    public void onRowClick(RowClickEvent event) {
      GridSelectionModel.this.onRowClick(event);
    }

    @Override
    public void onRowMouseDown(RowMouseDownEvent event) {
      GridSelectionModel.this.onRowMouseDown(event);
    }

    @Override
    public void onViewReady(ViewReadyEvent event) {
      refresh();
    }
  }

  /**
   * True if this grid selection model supports keyboard navigation (defaults to true).
   */
  protected boolean enableNavKeys = true;
  /**
   * The grid associated with this selection model.
   */
  protected Grid<M> grid;
  /**
   * The current keyboard navigator.
   */
  protected KeyNav keyNav = new KeyNav() {

    @Override
    public void onDown(NativeEvent e) {
      GridSelectionModel.this.onKeyDown(e);
    }

    @Override
    public void onKeyPress(NativeEvent ce) {
      GridSelectionModel.this.onKeyPress(ce);
    }

    public void onLeft(NativeEvent e) {
      GridSelectionModel.this.onKeyLeft(e);
    }

    public void onRight(NativeEvent e) {
      GridSelectionModel.this.onKeyRight(e);
    }

    @Override
    public void onUp(NativeEvent e) {
      GridSelectionModel.this.onKeyUp(e);
    }

  };

  /**
   * The list store for this selection model.
   */
  protected ListStore<M> listStore;
  /**
   * A group of handler registrations for this selection model.
   */
  protected GroupingHandlerRegistration handlerRegistration;

  private Handler handler = new Handler();
  
  /**
   * Track the selection index, when the shift combined with and so then this is the starting point of the selection.
   */
  private int indexOnSelectNoShift;

  /**
   * True to deselect a selected item on click (defaults to true).
   */
  protected boolean deselectOnSimpleClick = true;

  private boolean focusCellCalled;

  @Override
  public void bind(Store<M> store) {
    super.bind(store);
    if (store instanceof ListStore) {
      listStore = (ListStore<M>) store;
    } else {
      listStore = null;
    }
  }

  /**
   * Binds the given grid to this selection model.
   * 
   * @param grid the grid to bind to this selection model
   */
  public void bindGrid(Grid<M> grid) {
    if (this.grid != null) {
      if (handlerRegistration != null) {
        handlerRegistration.removeHandler();
        handlerRegistration = null;
      }

      keyNav.bind(null);
      bind(null);
    }
    this.grid = grid;
    if (grid != null) {
      if (handlerRegistration == null) {
        handlerRegistration = new GroupingHandlerRegistration();
      }
      handlerRegistration.add(grid.addRowMouseDownHandler(handler));
      handlerRegistration.add(grid.addRowClickHandler(handler));
      handlerRegistration.add(grid.addViewReadyHandler(handler));
      handlerRegistration.add(grid.addRefreshHandler(handler));
      keyNav.bind(grid);
      bind(grid.getStore());
    }
  }

  /**
   * Returns the currently bound grid.
   * 
   * @return the grid
   */
  public Grid<M> getGrid() {
    return grid;
  }

  /**
   * Selects the next row.
   * 
   * @param keepexisting true to keep existing selections
   */
  public void selectNext(boolean keepexisting) {
    if (hasNext()) {
      int idx = listStore.indexOf(lastSelected) + 1;
      select(idx, keepexisting);
      grid.getView().focusRow(idx);
    }
  }

  /**
   * Selects the previous row.
   * 
   * @param keepexisting true to keep existing selections
   */
  public void selectPrevious(boolean keepexisting) {
    if (hasPrevious()) {
      int idx = listStore.indexOf(lastSelected) - 1;
      select(idx, keepexisting);
      grid.getView().focusRow(idx);
    }
  }

  /**
   * Handles a row click event. The row click event is responsible for adding to a selection in multiple selection mode.
   * 
   * @param event the row click event
   */
  protected void onRowClick(RowClickEvent event) {
    if (Element.is(event.getEvent().getEventTarget())
        && !grid.getView().isSelectableTarget(Element.as(event.getEvent().getEventTarget()))) {
      return;
    }
    
    if (isLocked()) {
      return;
    }

    if (fireSelectionChangeOnClick) {
      fireSelectionChange();
      fireSelectionChangeOnClick = false;
    }

    XEvent xe = event.getEvent().<XEvent> cast();

    int rowIndex = event.getRowIndex();
    int colIndex = event.getColumnIndex();
    if (rowIndex == -1) {
      deselectAll();
      return;
    }

    M sel = listStore.get(rowIndex);

    boolean isSelected = isSelected(sel);
    boolean isControl = xe.getCtrlOrMetaKey();
    boolean isShift = xe.getShiftKey();

    // we only handle multi select with control key here
    if (selectionMode == SelectionMode.MULTI) {
      if (isSelected && isControl) {
        grid.getView().focusCell(rowIndex, colIndex, false);
        focusCellCalled = true;
        // reset the starting location of the click
        indexOnSelectNoShift = rowIndex;
        doDeselect(Collections.singletonList(sel), false);
      } else if (isControl) {
        grid.getView().focusCell(rowIndex, colIndex, false);
        focusCellCalled = true;
        // reset the starting location of the click
        indexOnSelectNoShift = rowIndex;
        doSelect(Collections.singletonList(sel), true, false);
      } else if (isSelected && !isControl && !isShift && selected.size() > 1) {
        doSelect(Collections.singletonList(sel), false, false);
      }

      if (!focusCellCalled) {
        grid.getView().focusCell(rowIndex, colIndex, false);
      }
    }
  }

  /**
   * Handles a row mouse down event. The row mouse down event is responsible for initiating a selection.
   * 
   * @param event the row mouse down event
   */
  protected void onRowMouseDown(RowMouseDownEvent event) {
    if (Element.is(event.getEvent().getEventTarget())
        && !grid.getView().isSelectableTarget(Element.as(event.getEvent().getEventTarget()))) {
      return;
    }
    
    if (isLocked()) {
      return;
    }
    
    int rowIndex = event.getRowIndex();
    int colIndex = event.getColumnIndex();
    if (rowIndex == -1) {
      return;
    }

    focusCellCalled = false;
    mouseDown = true;

    XEvent e = event.getEvent().<XEvent> cast();

    // it is important the focusCell be called once, and only once in onRowMouseDown and onRowMouseClick
    // everything but multi select with the control key pressed is handled in mouse down

    if (event.getEvent().getButton() == Event.BUTTON_RIGHT) {
      if (selectionMode != SelectionMode.SINGLE && isSelected(listStore.get(rowIndex))) {
        return;
      }
      grid.getView().focusCell(rowIndex, colIndex, false);
      select(rowIndex, false);
      focusCellCalled = true;
    } else {
      M sel = listStore.get(rowIndex);
      if (sel == null) {
        return;
      }
      
      boolean isSelected = isSelected(sel);
      boolean isMeta = e.getCtrlOrMetaKey();
      boolean isShift = event.getEvent().getShiftKey();

      switch (selectionMode) {
        case SIMPLE:
          grid.getView().focusCell(rowIndex, colIndex, false);
          focusCellCalled = true;
          if (!isSelected) {
            select(sel, true);
          } else if (isSelected && deselectOnSimpleClick) {
            deselect(sel);
          }
          break;
          
        case SINGLE:
          grid.getView().focusCell(rowIndex, colIndex, false);
          focusCellCalled = true;
          if (isSelected && isMeta) {
            deselect(sel);
          } else if (!isSelected) {
            select(sel, false);
          }
          break;
          
        case MULTI:
          if (isMeta) {
            break;
          }
          
          if (isShift && lastSelected != null) {
            int last = listStore.indexOf(lastSelected);
            grid.getView().focusCell(last, colIndex, false);
            
            int start;
            int end;
            // This deals with flipping directions
            if (indexOnSelectNoShift < rowIndex) {
              start = indexOnSelectNoShift;
              end = rowIndex;
            } else {
              start = rowIndex;
              end = indexOnSelectNoShift;
            }
            
            focusCellCalled = true;
            select(start, end, false);
          } else if (!isSelected) {
            // reset the starting location of multi select
            indexOnSelectNoShift = rowIndex;
            
            grid.getView().focusCell(rowIndex, colIndex, false);
            focusCellCalled = true;
            doSelect(Collections.singletonList(sel), false, false);
          } 
          break;
      }
    }

    mouseDown = false;
  }

  /**
   * Returns true if there is an item in the list store following the most recently selected item (i.e. the selected
   * item is not the last item in the list store).
   * 
   * @return true if there is an item following the most recently selected item
   */
  protected boolean hasNext() {
    return lastSelected != null && listStore.indexOf(lastSelected) < (listStore.size() - 1);
  }

  /**
   * Returns true if there is an item in the list store preceding the most recently selected item (i.e. the selected
   * item is not the first item in the list store).
   * 
   * @return true if there is an item preceding the most recently selected item
   */
  protected boolean hasPrevious() {
    return lastSelected != null && listStore.indexOf(lastSelected) > 0;
  }

  /**
   * Returns true if the given cell is selectable.
   * 
   * @param row the cell's row
   * @param cell the cell's column
   * @return true if the cell is selectable
   */
  // TODO: Consider changing name, parameters or implementation (i.e. implies
  // row participates in check, which it doesn't)
  protected boolean isSelectable(int row, int cell) {
    return !grid.getColumnModel().isHidden(cell);
  }

  /**
   * Handles a key down event (e.g. as fired by the key navigator).
   * 
   * @param ne the key down event
   */
  protected void onKeyDown(NativeEvent ne) {
    XEvent e = ne.<XEvent> cast();
    if (Element.is(ne.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ne.getEventTarget()))) {
      return;
    }
    if (listStore.size() == 0) {
      return;
    }
    if (!e.getCtrlOrMetaKey() && selected.size() == 0 && getLastFocused() == null) {
      select(0, false);
    } else {
      int idx = listStore.indexOf(getLastFocused());
      if (idx >= 0 && (idx + 1) < listStore.size()) {
        if (e.getCtrlOrMetaKey() || (e.getShiftKey() && isSelected(listStore.get(idx + 1)))) {
          if (!e.getCtrlOrMetaKey()) {
            deselect(idx);
          }

          M lF = listStore.get(idx + 1);
          if (lF != null) {
            setLastFocused(lF);
            grid.getView().focusCell(idx + 1, 0, false);
          }

        } else {
          if (e.getShiftKey() && lastSelected != getLastFocused()) {
            grid.getView().focusCell(idx + 1, 0, false);
            select(listStore.indexOf(lastSelected), idx + 1, true);
          } else {
            if (idx + 1 < listStore.size()) {
              grid.getView().focusCell(idx + 1, 0, false);
              selectNext(e.getShiftKey());
            }
          }
        }
      } else {
        grid.getView().onNoNext(idx);
      }
    }

    e.preventDefault();
  }

  /**
   * Handles a key left event (e.g. as fired by the key navigator).
   * 
   * @param e the key left event
   */
  protected void onKeyLeft(NativeEvent e) {

  }

  /**
   * Handles a key press event (e.g. as fired by the key navigator).
   * 
   * @param ne the key press event
   */
  protected void onKeyPress(NativeEvent ne) {
    if (Element.is(ne.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ne.getEventTarget()))) {
      return;
    }

    int kc = ne.getKeyCode();

    XEvent e = ne.<XEvent> cast();

    if (lastSelected != null && enableNavKeys) {
      if (kc == KeyCodes.KEY_PAGEUP) {
        e.stopPropagation();
        e.preventDefault();
        select(0, false);
        grid.getView().focusRow(0);
      } else if (kc == KeyCodes.KEY_PAGEDOWN) {
        e.stopPropagation();
        e.preventDefault();
        int idx = listStore.indexOf(listStore.get(listStore.size() - 1));
        select(idx, false);
        grid.getView().focusRow(idx);
      }
    }
    // if space bar is pressed
    if (e.getKeyCode() == 32) {
      if (getLastFocused() != null) {
        if (e.getShiftKey() && lastSelected != null) {
          int last = listStore.indexOf(lastSelected);
          int i = listStore.indexOf(getLastFocused());
          grid.getView().focusCell(i, 0, false);
          select(last, i, e.getCtrlOrMetaKey());
        } else {
          if (isSelected(getLastFocused())) {
            deselect(getLastFocused());
          } else {
            grid.getView().focusCell(listStore.indexOf(getLastFocused()), 0, false);
            select(getLastFocused(), true);
          }
        }
      }
    }
  }

  /**
   * Handles a key right event (e.g. as fired by the key navigator).
   * 
   * @param e the key right event
   */
  protected void onKeyRight(NativeEvent e) {

  }

  /**
   * Handles a key up event (e.g. as fired by the key navigator).
   * 
   * @param e the key up event
   */
  protected void onKeyUp(NativeEvent e) {
    XEvent xe = e.<XEvent> cast();
    if (Element.is(e.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(e.getEventTarget()))) {
      return;
    }
    int idx = listStore.indexOf(getLastFocused());
    if (idx >= 1) {
      if (xe.getCtrlOrMetaKey() || (e.getShiftKey() && isSelected(listStore.get(idx - 1)))) {
        if (!xe.getCtrlOrMetaKey()) {
          deselect(idx);
        }

        M lF = listStore.get(idx - 1);
        if (lF != null) {
          setLastFocused(lF);
          grid.getView().focusCell(idx - 1, 0, false);
        }
      } else {
        if (e.getShiftKey() && lastSelected != getLastFocused()) {
          grid.getView().focusCell(idx - 1, 0, false);
          select(listStore.indexOf(lastSelected), idx - 1, true);
        } else {
          if (idx > 0) {
            grid.getView().focusCell(idx - 1, 0, false);
            selectPrevious(e.getShiftKey());
          }
        }

      }
    } else {
      grid.getView().onNoPrev();
    }
    e.preventDefault();
  }

  @Override
  protected void onLastFocusChanged(M oldFocused, M newFocused) {
    int i;
    if (oldFocused != null) {
      i = listStore.indexOf(oldFocused);
      if (i >= 0) {
        grid.getView().onHighlightRow(i, false);
      }
    }
    if (newFocused != null) {
      i = listStore.indexOf(newFocused);
      if (i >= 0) {
        grid.getView().onHighlightRow(i, true);
      }
    }
  }

  @Override
  protected void onRecordChange(StoreRecordChangeEvent<M> event) {
    super.onRecordChange(event);
    refreshRowSelection(event.getRecord().getModel());
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    int idx = listStore.indexOf(model);
    if (idx != -1) {

      if (select) {
        grid.getView().onRowSelect(idx);
      } else {
        grid.getView().onRowDeselect(idx);
      }
    }
  }

  protected void onUpdate(final M model) {
    super.onUpdate(model);
    refreshRowSelection(model);
  }

  private void refreshRowSelection(final M model) {
    Scheduler.get().scheduleFinally(new ScheduledCommand() {

      @Override
      public void execute() {
        if (isSelected(model)) {
          onSelectChange(model, true);
        }
        if (getLastFocused() == model) {
          setLastFocused(getLastFocused());
        }

      }
    });

  }
}
