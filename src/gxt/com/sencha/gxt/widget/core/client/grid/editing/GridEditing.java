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

import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.HasBeforeStartEditHandlers;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.HasCancelEditHandlers;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.HasCompleteEditHandlers;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.HasStartEditHandlers;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;

/**
 * Defines the interface for classes that support grid editing.
 * 
 * @see GridInlineEditing
 * @see GridRowEditing
 * 
 * @param <M> the model type
 */
public interface GridEditing<M> extends HasBeforeStartEditHandlers<M>, HasStartEditHandlers<M>,
    HasCompleteEditHandlers<M>, HasCancelEditHandlers<M> {

  /**
   * Adds an editor for the given column.
   * 
   * @param columnConfig the column config
   * @param converter the converter
   * @param field the field
   */
  <N, O> void addEditor(ColumnConfig<M, N> columnConfig, Converter<N, O> converter, IsField<O> field);

  /**
   * Adds an editor for the given column.
   * 
   * @param columnConfig the column config
   * @param field the field
   */
  <N> void addEditor(ColumnConfig<M, N> columnConfig, IsField<N> field);

  /**
   * Cancels an active edit.
   */
  void cancelEditing();

  /**
   * Completes the active edit.
   */
  void completeEditing();

  /**
   * Returns the converter for the given column.
   * 
   * @param columnConfig the column config
   * @return the converter
   */
  <N, O> Converter<N, O> getConverter(ColumnConfig<M, N> columnConfig);

  /**
   * Returns the target grid.
   * 
   * @return the target grid
   */
  Grid<M> getEditableGrid();

  /**
   * Returns the editor for the given column.
   * 
   * @param columnConfig the column config
   * @return the editor
   */
  <O> IsField<O> getEditor(ColumnConfig<M, ?> columnConfig);

  /**
   * Returns true if editing is active.
   * 
   * @return true if editing
   */
  boolean isEditing();

  /**
   * Removes the editor for the given column.
   * 
   * @param columnConfig the column config
   */
  void removeEditor(ColumnConfig<M, ?> columnConfig);

  /**
   * Sets the target grid to be edited.
   * 
   * @param editableGrid the editable grid
   */
  void setEditableGrid(Grid<M> editableGrid);

  /**
   * Starts editing for the given cell.
   * 
   * @param cell the cell
   */
  void startEditing(GridCell cell);
}
