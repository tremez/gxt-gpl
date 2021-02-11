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
package com.sencha.gxt.widget.core.client.treegrid;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnData;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.Tree.Joint;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import com.sencha.gxt.widget.core.client.tree.TreeView.TreeViewRenderMode;

/**
 * A {@code GridView} subclass that adds tree related view features.
 */
public class TreeGridView<M> extends GridView<M> {

  protected TreeGrid<M> tree;
  protected TreeStore<M> treeStore;

  /**
   * Creates a new view instance.
   */
  public TreeGridView() {
    this(GWT.<GridAppearance> create(GridAppearance.class));
  }

  /**
   * Creates a new view instance with the given grid appearance.
   * 
   * @param appearance the grid appearance
   */
  public TreeGridView(GridAppearance appearance) {
    super(appearance);
  }

  /**
   * Collapses the given node.
   * 
   * @param node the node to be collapsed
   */
  public void collapse(TreeNode<M> node) {
    M p = node.getModel();
    M lc = treeStore.getLastChild(p);
    // make sure there is a last child (i.e. at least one child) before trying to thin the liststore
    if (lc != null) {
      int start = ds.indexOf(p);
      if (start != -1) {
        // ensure the node is visible
        int end = tree.findLastOpenChildIndex(lc);
        for (int i = end; i > start; i--) {
          ds.remove(i);
        }
      }
    }
    // regardless, refresh the parent icon
    tree.refresh(p);
  }

  /**
   * Expands the given node.
   * 
   * @param node the node to be expanded
   */
  public void expand(TreeNode<M> node) {
    M p = node.getModel();
    List<M> children = treeStore.getChildren(p);
    if (children.size() > 0) {
      int idx = ds.indexOf(p);

      ds.addAll(idx + 1, children);

      for (M child : children) {
        TreeNode<M> cn = findNode(child);
        if (cn != null && cn.isExpanded()) {
          expand(cn);
        }
      }
    }
    tree.refresh(p);
  }

  /**
   * Gets the rendered element, if any, for the given tree node object. This method will look up the dom element if it
   * has not yet been seen. The getElement() method for the node will return the same value as this method does after it
   * has been cached.
   * 
   * @param node the tree node to find an element for
   * @return the element that the node represents, or null if not yet rendered
   */
  public XElement getElement(TreeNode<M> node) {
    XElement elt = node.getElement().cast();
    if (elt == null) {
      if (tree.isAttached() && tree.getElement().getOffsetParent() != null) {
        elt = Document.get().getElementById(node.getDomId()).cast();
      } else {
        elt = tree.getElement().child("*#" + node.getDomId());
      }
      node.setElement(elt);
    }
    return elt;
  }

  /**
   * Returns the element which wraps the children of the given node. This is the element that is displayed / hidden when
   * a node is expanded / collapsed.
   * 
   * @param node the target node
   * @return the element
   */
  public XElement getElementContainer(TreeNode<M> node) {
    if (node.getElementContainer() == null) {
      node.setElContainer(getElement(node) != null ? tree.getTreeAppearance().getContainerElement(getElement(node))
          : null);
    }
    return node.getElementContainer().cast();
  }

  /**
   * Returns the element in which the nodes icon is rendered.
   * 
   * @param node the target node
   * @return the icon elmeent
   */
  public Element getIconElement(TreeNode<M> node) {
    if (node.getIconElement() == null) {
      Element row = getRowElement(node);
      if (row != null) {
        XElement r = row.cast();
        XElement icon = tree.getTreeAppearance().findIconElement(r);
        node.setIconElement(icon);
      }
    }
    return node.getIconElement();
  }

  /**
   * Returns the element in which the nodes joint (expand / collapse) icon is rendered.
   * 
   * @param node the target node
   * @return the joint element
   */
  public Element getJointElement(TreeNode<M> node) {
    if (node.getJointElement() == null) {
      Element row = getRowElement(node);
      if (row != null) {
        XElement r = row.cast();
        XElement joint = tree.getTreeAppearance().findJointElement(r);
        node.setJointElement(joint);
      }
    }
    return node.getJointElement();
  }

  /**
   * Returns the markup that is used to render a node.
   * 
   * @param m the model
   * @param id the id of the node (store model key provider)
   * @param html the node html
   * @param icon the node icon or null
   * @param checkable true if the node is checked
   * @param joint the joint state
   * @param level the tree depth
   * @return the markup as safe html
   */
  public SafeHtml getTemplate(M m, String id, SafeHtml html, ImageResource icon, boolean checkable, Joint joint,
      int level) {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    tree.getTreeAppearance().renderNode(sb, id, html, tree.getStyle(), icon, checkable, CheckState.UNCHECKED, joint,
        level - 1, TreeViewRenderMode.ALL);
    return sb.toSafeHtml();
  }

  /**
   * Returns the element in which the node's text is rendered.
   * 
   * @param node the target node
   * @return the text element
   */
  public Element getTextElement(TreeNode<M> node) {
    if (node == null) {
      return null;
    }
    if (node.getTextElement() == null) {
      Element row = getRowElement(node);
      if (row != null) {
        XElement t = row.<XElement> cast().selectNode(tree.getTreeAppearance().textSelector());
        node.setTextElement(t);
      }
    }
    return node.getTextElement();
  }

  @Override
  public boolean isSelectableTarget(Element target) {
    boolean b = super.isSelectableTarget(target);
    if (!b) {
      return false;
    }

    TreeNode<M> node = tree.findNode(target);
    if (node != null) {
      Element j = getJointElement(node);
      if (j != null && j.isOrHasChild(target)) {
        return false;
      }
    }
    return true;
  }

  public void onDropChange(Element e, boolean drop) {
    tree.getTreeAppearance().onDropOver(XElement.as(e), drop);
  }

  public void onIconStyleChange(TreeNode<M> node, ImageResource icon) {
    Element iconEl = getIconElement(node);
    if (iconEl != null) {
      Element e;
      if (icon != null) {
        e = (Element) IconHelper.getElement(icon);
      } else {
        e = DOM.createSpan();
      }
      e.setClassName(iconEl.getClassName());
      node.setIconElement((Element) iconEl.getParentElement().insertBefore(e, iconEl));
      iconEl.removeFromParent();
    }
  }

  public void onJointChange(TreeNode<M> node, Joint joint) {
    Element jointEl = getJointElement(node);
    if (jointEl != null) {
      XElement elem = getElement(node);
      node.setJointElement(tree.getTreeAppearance().onJointChange(elem, jointEl.<XElement> cast(), joint,
          tree.getStyle()));
    }
  }

  public void onLoading(TreeNode<M> node) {
    onIconStyleChange(node, tree.getTreeAppearance().loadingIcon());
  }

  @Override
  public void refresh(boolean headerToo) {
    if (grid != null && grid.isViewReady()) {
      for (TreeNode<M> node : tree.nodes.values()) {
        node.clearElements();

      }
    }
    super.refresh(headerToo);
  }

  @Override
  protected void doSort(int colIndex, SortDir sortDir) {
    ColumnConfig<M, ?> column = cm.getColumn(colIndex);
    if (!isRemoteSort()) {
      treeStore.clearSortInfo();

      StoreSortInfo<M> s = createStoreSortInfo(column, sortDir);

      if (sortDir == null && storeSortInfo != null
              && storeSortInfo.getValueProvider().getPath().equals(column.getValueProvider().getPath())) {
        s.setDirection(storeSortInfo.getDirection() == SortDir.ASC ? SortDir.DESC : SortDir.ASC);
      } else if (sortDir == null) {
        s.setDirection(SortDir.ASC);
      }

      if (GWT.isProdMode()) {
        treeStore.addSortInfo(s);
      } else {
        try {
          // addSortInfo will apply its sort when called, which might trigger an
          // exception if the column passed in's data isn't Comparable
          treeStore.addSortInfo(s);
        } catch (ClassCastException ex) {
          GWT.log("Column can't be sorted " + column.getValueProvider().getPath() + " is not Comparable. ", ex);
          throw ex;
        }
      }

    } else {
      // not supported
    }

  }

  protected TreeNode<M> findNode(M m) {
    return tree.findNode(m);
  }

  @Override
  protected List<ColumnData> getColumnData() {
    List<ColumnData> data = super.getColumnData();

    for (int i = 0; i < data.size(); i++) {
      if (cm.indexOf(tree.getTreeColumn()) == i) {
        ColumnData cd = data.get(i);
        cd.setClassNames(cd.getClassNames() + " x-treegrid-column");
      }
    }

    return data;
  }

  protected int getIndenting(TreeNode<M> node) {
    return 18;
  }

  @Override
  protected <N> SafeHtml getRenderedValue(int rowIndex, int colIndex, M m, ListStore<M>.Record record) {
    ColumnConfig<M, N> cc = cm.getColumn(colIndex);
    SafeHtml s = super.getRenderedValue(rowIndex, colIndex, m, record);
    TreeNode<M> node = findNode(m);
    if (node != null && cc == tree.getTreeColumn()) {
      return getTemplate(m, node.getDomId(), s, tree.calculateIconStyle(m), false, tree.calculateJoint(m),
          treeStore.getDepth(m));
    }
    return s;
  }

  protected Element getRowElement(TreeNode<M> node) {
    return (Element) getRow(ds.indexOf(node.getModel()));
  }

  @Override
  public StoreSortInfo<M> getSortState() {
    if (treeStore.getSortInfo().size() > 0) {
      return treeStore.getSortInfo().get(0);
    }
    return null;
  }

  @Override
  protected void init(Grid<M> grid) {
    tree = (TreeGrid<M>) grid;
    super.init(grid);
  }

  @Override
  protected void initData(ListStore<M> ds, ColumnModel<M> cm) {
    super.initData(ds, cm);
    treeStore = tree.getTreeStore();
  }

  @Override
  protected void onRemove(M m, int index, boolean isUpdate) {
    super.onRemove(m, index, isUpdate);
    TreeNode<M> node = findNode(m);
    if (node != null) {
      node.clearElements();
    }
  }
}
