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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.StringComboBoxCell;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;

/**
 * A combo box that displays a drop down list of Strings, optionally allowing
 * the user to type arbitrary values in the combo box text field and adding
 * these to the drop down list. This can be useful for saving user entered
 * search history, recent URLs, etc.
 * <p/>
 * To allow users to enter arbitrary values in the combo box text field, set the
 * <i>force selection</i> property to false using
 * {@link #setForceSelection(boolean)}.
 * <p/>
 * To add user values to the drop down list, set the <i>add user values</i>
 * property to true using {@link #setAddUserValues(boolean)}.
 * <p/>
 * User values that are added to the drop down list can be retrieved with
 * {@link #getUserValues()} and removed with {@link #clearUserValues()}.
 * <p/>
 * <b>NOTE:</b> User values are added to the drop down list when the combo box
 * loses focus.
 */
public class StringComboBox extends SimpleComboBox<String> {

  /**
   * Creates an empty combo box in preparation for values to be added to the
   * selection list using {@link #add}.
   */
  public StringComboBox() {
    this(GWT.<TriggerFieldAppearance>create(TriggerFieldAppearance.class));
  }

  /**
   * Creates an empty combo box with a given appearance - values can be added
   * to the selection list with {@link #add}.
   *
   * @param appearance the appearance to use when drawing the combo box
   */
  public StringComboBox(TriggerFieldAppearance appearance) {
    super(new StringComboBoxCell(new ListStore<String>(new ModelKeyProvider<String>() {
      @Override
      public String getKey(String item) {
        return item;
      }
    }), new LabelProvider<String>() {
      @Override
      public String getLabel(String item) {
        return item;
      }
    }, appearance));
  }

  /**
   * Creates a combo box containing the specified values.
   *
   * @param values values to include in the combo box
   */
  public StringComboBox(List<String> values) {
    this();
    add(values);
  }



  /**
   * Clears the list of values typed by the user.
   */
  public void clearUserValues() {
    getCell().clearUserValues();
  }

  @Override
  public StringComboBoxCell getCell() {
    return (StringComboBoxCell) super.getCell();
  }

  /**
   * Returns a list containing values typed by the user in the combo box text
   * field. These values are saved when the focus leaves the combo box if the
   * <i>force selection</i> property is false and the <i>add user values</i>
   * property is true.
   * 
   * @return a list of values typed by the user
   */
  public List<String> getUserValues() {
    return getCell().getUserValues();
  }

  /**
   * Returns true to indicate that arbitrary values typed by the user will be
   * added to the drop down list.
   * 
   * @return true to indicate values typed by user will be saved in drop down
   *         list
   */
  public boolean isAddUserValues() {
    return getCell().isAddUserValues();
  }

  /**
   * Set to true to add arbitrary values typed by the user to the drop down
   * list; be sure to also set <i>force selection</i> to false.
   * <p/>
   * This value is false by default (i.e. user values are not added to the drop
   * down list).
   * 
   * @param isAddUserValues true to add arbitrary values typed by the user to
   *          the drop down list
   */
  public void setAddUserValues(boolean isAddUserValues) {
    getCell().setAddUserValues(isAddUserValues);
  }

}
