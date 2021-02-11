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
package com.sencha.gxt.widget.core.client.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.selection.AbstractStoreSelectionModel;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

/**
 * <code>Tree</code> selection model.
 *
 * @param <M> the model type
 */
public class TreeSelectionModel<M> extends AbstractStoreSelectionModel<M> {
  private class Handler implements MouseDownHandler, ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      onMouseClick(event);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
      TreeSelectionModel.this.onMouseDown(event);
    }

  }

  protected KeyNav keyNav = new KeyNav() {
    public void onDown(NativeEvent evt) {
      onKeyDown(evt);
    }

    @Override
    public void onLeft(NativeEvent evt) {
      onKeyLeft(evt);
    }

    @Override
    public void onRight(NativeEvent evt) {
      onKeyRight(evt);
    }

    @Override
    public void onUp(NativeEvent e) {
      onKeyUp(e);
    }
  };

  protected Tree<M, ?> tree;

  protected TreeStore<M> treeStore;

  private Handler handler = new Handler();
  private GroupingHandlerRegistration handlerRegistration;

  public TreeSelectionModel() {
  }

  public void bindTree(Tree<M, ?> tree) {
    if (this.tree != null) {
      handlerRegistration.removeHandler();
      keyNav.bind(null);
      bind(null);
      this.treeStore = null;
    }
    this.tree = tree;
    if (tree != null) {
      if (handlerRegistration == null) {
        handlerRegistration = new GroupingHandlerRegistration();
      }
      handlerRegistration.add(tree.addDomHandler(handler, MouseDownEvent.getType()));
      handlerRegistration.add(tree.addDomHandler(handler, ClickEvent.getType()));
      keyNav.bind(tree);
      bind(tree.getStore());
      this.treeStore = (TreeStore<M>) tree.getStore();
    }
  }

  @Override
  public void deselect(int index) {
    assert false : "This method not implemented for trees";
  }

  @Override
  public void deselect(int start, int end) {
    assert false : "This method not implemented for trees";
  }

  /**
   * Returns the currently bound tree.
   *
   * @return the tree
   */
  public Tree<M, ?> getTree() {
    return tree;
  }

  @Override
  public boolean isSelected(M item) {
    return selected.contains(item);
  }

  @Override
  public void select(int start, int end, boolean keepExisting) {
    assert false : "This method not implemented for trees";
  }

  /**
   * Selects the item below the selected item in the tree, intelligently walking the nodes.
   */
  public void selectNext() {
    M next = next();
    if (next != null) {
      doSingleSelect(next, false);
    }
  }

  /**
   * Selects the item above the selected item in the tree, intelligently walking the nodes.
   */
  public void selectPrevious() {
    M prev = prev();
    if (prev != null) {
      doSingleSelect(prev, false);
    }
  }

  protected M next() {
    M sel = lastSelected;
    if (sel == null) {
      return null;
    }
    M first = treeStore.getFirstChild(sel);
    if (first != null && tree.isExpanded(sel)) {
      return first;
    } else {
      M nextSibling = treeStore.getNextSibling(sel);
      if (nextSibling != null) {
        return nextSibling;
      } else {
        M p = treeStore.getParent(sel);
        while (p != null) {
          nextSibling = treeStore.getNextSibling(p);
          if (nextSibling != null) {
            return nextSibling;
          }
          p = treeStore.getParent(p);
        }
      }
    }
    return null;
  }

  protected void onKeyDown(NativeEvent e) {
    e.preventDefault();
    M next = next();
    if (next != null) {
      doSingleSelect(next, false);
      tree.scrollIntoView(next);
    }
  }

  protected void onKeyLeft(NativeEvent ce) {
    ce.preventDefault();
    if (lastSelected != null && !tree.isLeaf(lastSelected) && tree.isExpanded(lastSelected)) {
      tree.setExpanded(lastSelected, false);
    } else if (lastSelected != null && treeStore.getParent(lastSelected) != null) {
      doSingleSelect(treeStore.getParent(lastSelected), false);
    }
  }

  protected void onKeyRight(NativeEvent ce) {
    ce.preventDefault();
    if (lastSelected != null && !tree.isLeaf(lastSelected) && !tree.isExpanded(lastSelected)) {
      tree.setExpanded(lastSelected, true);
    }
  }

  protected void onKeyUp(NativeEvent e) {
    e.preventDefault();
    M prev = prev();
    if (prev != null) {
      doSingleSelect(prev, false);
      tree.scrollIntoView(prev);
    }
  }

  protected void onMouseClick(ClickEvent ce) {
    onMouseClick(ce.getNativeEvent());
  }

  protected void onMouseClick(NativeEvent event) {
    XEvent e = event.cast();

    if (isLocked()) {
      return;
    }

    if (fireSelectionChangeOnClick) {
      fireSelectionChange();
      fireSelectionChangeOnClick = false;
    }

    if (selectionMode == SelectionMode.MULTI) {
      TreeNode<M> node = tree.findNode((Element) e.getEventTarget().cast());
      // on dnd prevent drag the node will be null
      if (node != null) {
        M sel = node.getModel();
        if (e.getCtrlOrMetaKey() && isSelected(sel)) {
          doDeselect(Collections.singletonList(sel), false);
          tree.focus();

          // reset the starting location of the click when meta is used during a multiselect
          lastSelected = sel;
        } else if (e.getCtrlOrMetaKey()) {
          doSelect(Collections.singletonList(sel), true, false);
          tree.focus();

          // reset the starting location of the click when meta is used during a multiselect
          lastSelected = sel;
        } else if (isSelected(sel) && !e.getShiftKey() && !e.getCtrlOrMetaKey() && selected.size() > 0) {
          doSelect(Collections.singletonList(sel), false, false);
          tree.focus();
        }
      }
    }
  }

  protected void onMouseDown(MouseDownEvent mde) {
    onMouseDown(mde.getNativeEvent());
  }

  protected void onMouseDown(NativeEvent event) {
    XEvent e = event.cast();
    XElement target = e.getEventTargetEl();
    TreeNode<M> selNode = tree.findNode(target);

    if (selNode == null || tree == null || isLocked()) {
      return;
    }

    M sel = selNode.getModel();
    if (!tree.getView().isSelectableTarget(sel, target)) {
      return;
    }

    mouseDown = true;
    
    boolean isSelected = isSelected(sel);
    boolean isMeta = e.getCtrlOrMetaKey();
    boolean isShift = e.getShiftKey();

    if (e.isRightClick() && isSelected) {
      return;
    } else {
      switch (selectionMode) {
        case SIMPLE:
          tree.focus();
          if (isSelected(sel)) {
            deselect(sel);
          } else {
            doSelect(Collections.singletonList(sel), true, false);
          }
          break;
          
        case SINGLE:
          tree.focus();
          if (isMeta && isSelected) {
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
            List<M> selectedItems = new ArrayList<M>();

            // from last selected or firstly selected
            TreeNode<M> lastSelTreeNode = tree.findNode(lastSelected);
            XElement lastSelTreeEl = tree.getView().getElement(lastSelTreeNode);

            // to selected or secondly selected
            TreeNode<M> selTreeNode = tree.findNode(sel);
            XElement selTreeNodeEl = tree.getView().getElement(selTreeNode);

            // holding shift down, selecting the same item again, selecting itself
            if (sel == lastSelected) {
              tree.focus();
              doSelect(Collections.singletonList(sel), false, false);

            } else if (lastSelTreeEl != null && selTreeNodeEl != null) {
              // add the last selected, as its not added during the walk
              selectedItems.add(lastSelected);

              // After walking reset back to previously selected
              final M previouslyLastSelected = lastSelected;

              // This deals with flipping directions
              if (lastSelTreeEl.getAbsoluteTop() < selTreeNodeEl.getAbsoluteTop()) {
                // down selection
                M next = next();
                while (next != null) {
                  selectedItems.add(next);
                  lastSelected = next;
                  if (next == sel) break;
                  next = next();
                }

              } else {
                // up selection
                M prev = prev();
                while (prev != null) {
                  selectedItems.add(prev);
                  lastSelected = prev;
                  if (prev == sel) break;
                  prev = prev();
                }
              }

              tree.focus();
              doSelect(selectedItems, false, false);

              // change back to last selected, the walking causes this need
              lastSelected = previouslyLastSelected;
            }

          } else if (!isSelected(sel)) {
            tree.focus();
            doSelect(Collections.singletonList(sel), false, false);

            // reset the starting location of multi select
            lastSelected = sel;
          }
          break;
      }
    }

    mouseDown = false;
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    tree.getView().onSelectChange(model, select);
  }

  protected M prev() {
    M sel = lastSelected;
    if (sel == null) {
      return sel;
    }
    M prev = treeStore.getPreviousSibling(sel);
    if (prev != null) {
      if ((!tree.isExpanded(prev) || treeStore.getChildCount(prev) < 1)) {
        return prev;
      } else {
        M lastChild = treeStore.getLastChild(prev);
        while (lastChild != null && treeStore.getChildCount(lastChild) > 0 && tree.isExpanded(lastChild)) {
          lastChild = treeStore.getLastChild(lastChild);
        }
        return lastChild;
      }
    } else {
      M parent = treeStore.getParent(sel);
      if (parent != null) {
        return parent;
      }
    }
    return null;
  }
}
