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

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer.ScrollDirection;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent.LoadExceptionHandler;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent.HasLiveGridViewUpdateHandlers;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent.LiveGridViewUpdateHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * LiveGridView for displaying large amount of data. Data is loaded on demand as the user scrolls the grid.
 */
public class LiveGridView<M> extends GridView<M> implements HasLiveGridViewUpdateHandlers {

  private static Logger logger = Logger.getLogger(LiveGridView.class.getName());
  /**
   * The secondary store.
   */
  protected ListStore<M> cacheStore;
  protected XElement liveScroller;
  /**
   * The paging offset.
   */
  protected int liveStoreOffset = 0;
  /**
   * The total rows in data set. The total count from the loader when using a paging loader.
   */
  protected int totalCount = 0;
  /**
   * The current index of the view.
   */
  protected int viewIndex;
  private int cacheSize = 200;
  private StoreDataChangeHandler<M> cacheStoreHandler;
  private HandlerRegistration cacheStoreHandlerRegistration;
  private boolean ignoreScroll;
  private boolean isLoading;
  // to prevent flickering
  private boolean isMasked;
  private int loadDelay = 200;
  private HandlerRegistration loaderHandlerRegistration;
  private int loaderOffset;
  private DelayedTask loaderTask;
  private double prefetchFactor = .2;
  private int rowHeight = 20;
  private int viewIndexReload = -1;
  private int barWidth = XDOM.getScrollBarWidth() == 0 ? 16 : XDOM.getScrollBarWidth() + 2;
  private int lastViewIndex = -1;
  private int lastScrollDirection = 0;
  private int rowHeightAdjust;
  private boolean measureRowHeight = true;
  private boolean adjustScrollHeight;
  private ScrollGestureRecognizer scrollGestureRecognizer;

  /**
   * Creates a new live grid view.
   */
  public LiveGridView() {
    super();
  }

  /**
   * Creates a new live grid view instance with the given appearance.
   *
   * @param appearance the appearance to use when rendering the grid view
   */
  public LiveGridView(GridAppearance appearance) {
    super(appearance);
  }

  @Override
  public HandlerRegistration addLiveGridViewUpdateHandler(LiveGridViewUpdateHandler handler) {
    return addHandler(LiveGridViewUpdateEvent.getType(), handler);
  }

  /**
   * Returns the numbers of rows that should be cached.
   *
   * @return the cache size
   */
  public int getCacheSize() {
    int c = -1;
    if (grid.isViewReady()) {
      c = getVisibleRowCount();
    }
    return Math.max(c, cacheSize);
  }

  /**
   * Sets the amount of rows that should be cached (default to 200). The cache size is the number of rows that are
   * retrieved each time a data request is made. The cache size should always be greater than the number of visible rows
   * of the grid. The number of visible rows will vary depending on the grid height and the height of each row. If the
   * set cache size is smaller than the number of visible rows of the grid than it gets set to the number of visible
   * rows of the grid.
   *
   * @param cacheSize the new cache size
   */
  public void setCacheSize(int cacheSize) {
    this.cacheSize = cacheSize;
  }

  /**
   * Returns the amount of time before loading is done.
   * 
   * @return the load delay in milliseconds
   */
  public int getLoadDelay() {
    return loadDelay;
  }

  /**
   * Sets the amount of time before loading is done (defaults to 200).
   *
   * @param loadDelay the new load delay in milliseconds
   */
  public void setLoadDelay(int loadDelay) {
    this.loadDelay = loadDelay;
  }

  /**
   * Returns the prefetchFactor.
   *
   * @return the prefetchFactor
   */
  public double getPrefetchFactor() {
    return prefetchFactor;
  }

  /**
   * Sets the pre-fetch factor (defaults to .2). The pre-fetch factor is used to determine when new data should be
   * fetched as the user scrolls the grid. The factor is used with the cache size.
   *
   * <p />
   * For example, if the cache size is 1000 with a pre-fetch of .20, the grid will request new data when the 800th (1000
   * * .20) row of the grid becomes visible.
   *
   * @param prefetchFactor the pre-fetch factor
   */
  public void setPrefetchFactor(double prefetchFactor) {
    this.prefetchFactor = prefetchFactor;
  }

  /**
   * Returns the height of one row.
   *
   * @return the height of one row
   */
  public int getRowHeight() {
    return rowHeight;
  }

  /**
   * Sets the height of one row (defaults to 20). This method only need to be called when the row height changes after
   * the first time the grid is rendered.
   *
   * @param rowHeight the new row height.
   */
  protected void setRowHeight(int rowHeight) {
    this.rowHeight = rowHeight;

    int rh = getRowHeight();
    int visibleHeight = getLiveScrollerHeight();
    int result = (int) ((visibleHeight < 1) ? 0 : Math.floor((double) visibleHeight / rh));
    int calcHeight = rh * result;

    rowHeightAdjust = rowHeight + (calcHeight - visibleHeight);

    if (liveScroller != null) {
      updateScrollerHeight();
    } else {
      adjustScrollHeight = true;
    }

  }

  /**
   * Returns the total number of rows that are visible given the current grid height.
   *
   * @return the visible row count
   */
  public int getVisibleRowCount() {
    int rh = getRowHeight();
    int visibleHeight = getLiveScrollerHeight();
    int result = (int) ((visibleHeight < 1) ? 0 : Math.floor((double) visibleHeight / rh));

    int calcHeight = rh * result;

    while (calcHeight < visibleHeight) {
      result++;
      calcHeight = rh * result;
    }

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("row height: " + rh + " visibleHeight: " + visibleHeight + " visible rows: " + result
          + " calcHeight: " + calcHeight);
    }

    return result;
  }

  /**
   * Refreshed the view. Reloads the store based on the current settings
   */
  public void refresh() {
    maskView();
    loadLiveStore(liveStoreOffset);
  }

  @Override
  public void refresh(boolean headerToo) {
    super.refresh(headerToo);
    if (headerToo) {
      positionLiveScroller();
    }
    if (!preventScrollToTopOnRefresh) {
      // we scrolled to the top
      updateRows(0, false);
    }
  }

  @Override
  public void scrollToTop() {
    liveScroller.setScrollTop(0);
  }

  @Override
  protected void calculateVBar(boolean force) {
    if (force) {
      layout();
    }
  }

  protected void doLoad() {
    if (grid.isLoadMask()) {
      grid.setLoadMask(false);
      Scheduler.get().scheduleFinally(new ScheduledCommand() {
        @Override
        public void execute() {
          grid.setLoadMask(true);
        }
      });
    }
    if (grid.getLoader() instanceof PagingLoader<?, ?>) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("doLoad() with paging loader, loaderOffset: " + loaderOffset + " cacheSize: " + getCacheSize());
      }
      ((PagingLoader<?, ?>) grid.getLoader()).load(loaderOffset, getCacheSize());
    } else {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("doLoad() with non paging loader");
      }
      grid.getLoader().load();
    }
  }

  @Override
  protected void doSort(int colIndex, SortDir sortDir) {
    cacheStore.clear();
    ColumnConfig<M, ?> column = cm.getColumn(colIndex);

    ValueProvider<? super M, ?> vp = column.getValueProvider();

    SortInfoBean bean = new SortInfoBean(vp, sortDir);

    if (sortDir == null && sortState != null && vp.getPath().equals(sortState.getSortField())) {
      bean.setSortDir(sortState.getSortDir() == SortDir.ASC ? SortDir.DESC : SortDir.ASC);

    } else if (sortDir == null) {
      bean.setSortDir(SortDir.ASC);
    }

    grid.getLoader().clearSortInfo();
    grid.getLoader().addSortInfo(bean);
    maskView();
    loadLiveStore(getLiveStoreCalculatedIndex(viewIndex));
  }

  protected int getLiveScrollerHeight() {
    return XElement.as(liveScroller).getHeight(true);
  }

  protected int getLiveStoreCalculatedIndex(int index) {
    int calcIndex = index - (getCacheSize() / 2) + getVisibleRowCount();
    calcIndex = Math.min(totalCount - getCacheSize(), calcIndex);
    calcIndex = Math.min(index, calcIndex);
    return Math.max(0, calcIndex);
  }

  @Override
  protected int getScrollAdjust() {
    return scrollOffset;
  }

  /**
   * Returns the live scroller element.
   *
   * @return the live scroller element.
   */
  @Override
  public XElement getScroller() {
    return liveScroller;
  }

  @Override
  protected void handleComponentEvent(Event event) {
    super.handleComponentEvent(event);

    int type = event.getTypeInt();
    EventTarget t = event.getEventTarget();

    if (ignoreScroll || !Element.is(t)) {
      return;
    }

    Element target = Element.as(t);

    if (type == Event.ONMOUSEWHEEL && dataTable.isOrHasChild(target)) {
      int v = event.<XEvent>cast().getMouseWheelVelocityFix() * getRowHeight();
      liveScroller.setScrollTop(liveScroller.getScrollTop() + v);
    } else if (type == Event.ONSCROLL && liveScroller.isOrHasChild(target)) {
      event.stopPropagation();
      event.preventDefault();
      updateRows((int) Math.ceil((double) liveScroller.getScrollTop() / getRowHeight()), false);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void initData(ListStore<M> ds, ColumnModel<M> cm) {
    if (cacheStoreHandler == null) {
      cacheStoreHandler = new StoreDataChangeHandler<M>() {

        @Override
        public void onDataChange(StoreDataChangeEvent<M> event) {
          liveStoreOffset = getLoaderOffset();

          if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest("cache store handle onDataChanged liveStoreOffset: " + liveStoreOffset + " totalCount: "
                + getLoaderTotalCount());
          }

          if (totalCount != getLoaderTotalCount()) {
            totalCount = getLoaderTotalCount();
            setRowHeight(rowHeight);
          }
          if (totalCount > 0 && viewIndexReload != -1 && !isCached(viewIndexReload)) {
            loadLiveStore(getLiveStoreCalculatedIndex(viewIndexReload));
          } else {
            viewIndexReload = -1;
            ignoreScroll = true;
            int scrollTop = liveScroller.getScrollTop();
            liveScroller.setScrollTop(scrollTop - 1);
            liveScroller.setScrollTop(scrollTop);
            ignoreScroll = false;
            updateRows(viewIndex, true);
            isLoading = false;
            if (isMasked) {
              isMasked = false;
              grid.unmask();
            }
          }
        }
      };

    }
    if (cacheStoreHandlerRegistration != null) {
      cacheStoreHandlerRegistration.removeHandler();
      cacheStoreHandlerRegistration = null;
      cacheStore = null;
    }
    if (loaderHandlerRegistration != null) {
      loaderHandlerRegistration.removeHandler();
      loaderHandlerRegistration = null;
    }
    if (ds != null) {
      cacheStore = new ListStore<M>(ds.getKeyProvider());
      cacheStoreHandlerRegistration = cacheStore.addStoreDataChangeHandler(cacheStoreHandler);
      Loader<Object, ListLoadResult<M>> l = (Loader<Object, ListLoadResult<M>>) grid.getLoader();
      loaderHandlerRegistration = l.addLoadHandler(new LoadResultListStoreBinding<Object, M, ListLoadResult<M>>(
          cacheStore));
    }
    super.initData(ds, cm);
  }

  @Override
  protected void insertRows(int firstRow, int lastRow, boolean isUpdate) {
    super.insertRows(firstRow, lastRow, isUpdate);

    if (measureRowHeight) {
      measureRowHeight = false;
      int rh = measureRowHeight();
      if (rowHeight != rh) {
        setRowHeight(rh);
      }
    }
    if (adjustScrollHeight) {
      adjustScrollHeight = false;
      updateScrollerHeight();
    }
  }

  protected boolean isCached(int index) {
    if ((cacheStore.size() == 0 && totalCount > 0) || (index < liveStoreOffset)
        || (index > (liveStoreOffset + getCacheSize() - getVisibleRowCount()))) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("isCached() FALSE index: " + index + " cacheSize: " + cacheStore.size() + " totalCount: "
            + totalCount + " liveStoreOffset: " + liveStoreOffset);
      }
      return false;
    }
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("isCached() TRUE index: " + index + " cacheSize: " + cacheStore.size() + " totalCount: "
          + totalCount + " liveStoreOffset: " + liveStoreOffset);
    }
    return true;
  }

  protected boolean isHorizontalScrollBarShowing() {
    return cm.getTotalWidth() > scroller.getOffsetWidth();
  }

  protected boolean loadLiveStore(int offset) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("loadLiveStore() offset: " + offset);
    }
    if (loaderTask == null) {
      loaderTask = new DelayedTask() {
        @Override
        public void onExecute() {
          doLoad();
        }
      };
    }
    loaderOffset = offset;
    loaderTask.delay(loadDelay);
    if (isLoading) {
      return true;
    } else {
      isLoading = true;
      return false;
    }
  }

  protected int measureRowHeight() {
    Element row = getRow(0);
    if (row != null) {
      measureRowHeight = false;
      int rh = row.getOffsetHeight();
      return rh;
    }
    return -1;
  }

  @Override
  protected void notifyShow() {
    super.notifyShow();
    updateRows((int) Math.ceil((double) liveScroller.getScrollTop() / getRowHeight()), true);
  }

  /**
   * Invoked after the view has been rendered, may be overridden to perform any
   * activities that require a rendered view.
   */
  @Override
  protected void onAfterRenderView() {
    super.onAfterRenderView();

    if (scrollGestureRecognizer == null) {
      scrollGestureRecognizer = new ScrollGestureRecognizer(getScroller(), ScrollDirection.BOTH) {
        @Override
        protected void onScrollMoveHorizontal(Element scrollElement, TouchData touch) {
          // use the scroll container scroller for horz
          // use the live view scroller for vert
          super.onScrollMoveHorizontal(LiveGridView.this.scroller, touch);
        }
      };
      grid.addGestureRecognizer(scrollGestureRecognizer);
    }
  }

  @Override
  protected void onDataChanged(StoreDataChangeEvent<M> se) {
    boolean current = preventScrollToTopOnRefresh;
    preventScrollToTopOnRefresh = true;
    super.onDataChanged(se);
    preventScrollToTopOnRefresh = current;
  }

  @Override
  protected void onNoNext(final int index) {
    if (isLoading) {
      return;
    }
    if (viewIndex + 1 < getLoaderTotalCount()) {
      updateRows(viewIndex + 1, false);
      liveScroller.setScrollTop(liveScroller.getScrollTop() + getRowHeight());
      // updateRow call may initiate new async call therefore we need to wait till data is returned before
      // updating the selection and scrolling into view
      if (!isLoading) {
        grid.getSelectionModel().select(index, false);
        ensureVisible(index, -1, false);
      } else {
        new NavKeyCallback<M>(grid, index);
      }
    }
  }

  @Override
  protected void onNoPrev() {
    if (isLoading) {
      return;
    }
    if (viewIndex > 0) {
      updateRows(viewIndex - 1, false);

      // updateRow call may initiate new async call therefore we need to wait till data is returned before
      // updating the selection and scrolling into view
      if (!isLoading) {
        grid.getSelectionModel().select(0, false);
        liveScroller.setScrollTop(liveScroller.getScrollTop() - getRowHeight());
      } else {
        new NavKeyCallback<M>(grid, 0);
      }
    }
  }

  @Override
  protected void renderUI() {
    super.renderUI();
    scroller.getStyle().setOverflowY(Overflow.HIDDEN);

    SafeHtml html = SafeHtmlUtils.fromTrustedString(
        "<div style=\"position: absolute; right: 0px; overflow-y: scroll; overflow-x: hidden; z-index: 1; width: "
            + barWidth + "px;\"><div style=\"width: " + barWidth + "px;\">&nbsp;</div></div>");
    liveScroller = grid.getElement().insertFirst(html);
    positionLiveScroller();

    liveScroller.addEventsSunk(Event.ONSCROLL);
    body.addEventsSunk(Event.ONMOUSEWHEEL);
  }

  @Override
  protected void resize() {
    final int oldCount = getVisibleRowCount();
    super.resize();
    if (dataTable != null) {
      resizeLiveScroller();
      scroller.setWidth(grid.getOffsetWidth() - getScrollAdjust(), true);
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          if (oldCount != getVisibleRowCount()) {
            updateRows(LiveGridView.this.viewIndex, true);
          }
        }
      });
    }
  }

  protected boolean shouldCache(int index) {
    int cz = getCacheSize();
    int i = (int) (cz * prefetchFactor);
    double low = liveStoreOffset + i;
    double high = liveStoreOffset + cz - getVisibleRowCount() - i;
    if ((index < low && liveStoreOffset > 0) || (index > high && liveStoreOffset != totalCount - cz)) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("shouldCache() TRUE index: " + index + " cache size: " + cz + " low: " + low + " high: " + high);
      }
      return true;
    }
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("shouldCache() FALSE index: " + index + " cache size: " + cz + " low: " + low + " high: " + high);
    }
    return false;
  }

  @Override
  protected void updateAllColumnWidths() {
    super.updateAllColumnWidths();
    resizeLiveScroller();
    updateRows(viewIndex, false);
  }

  @Override
  protected void updateColumnHidden(int index, boolean hidden) {
    super.updateColumnHidden(index, hidden);
    resizeLiveScroller();
    updateRows(viewIndex, false);
  }

  @Override
  protected void updateColumnWidth(int col, int width) {
    super.updateColumnWidth(col, width);
    resizeLiveScroller();
    updateRows(viewIndex, false);
  }

  /**
   * Updates the rows based on the new index.
   *
   * @param newIndex the new index
   * @param reload true to reload the data
   */
  protected void updateRows(int newIndex, boolean reload) {
    // the number of rows visible within the grid's viewport
    int rowCount = getVisibleRowCount();

    newIndex = Math.min(newIndex, Math.max(0, totalCount - rowCount));

    int diff = newIndex - viewIndex;

    // the difference from the view index and the new index
    int delta = Math.abs(diff);

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("updateRows() newIndex: " + newIndex + " viewIndex: " + viewIndex + " visible rows: " + rowCount
          + " diff: " + diff);
    }

    // nothing has changed and we are not forcing a reload
    if (delta == 0 && !reload) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("updateRows() nothing changed and not forcing reload");
      }
      return;
    }

    viewIndex = newIndex;
    int liveStoreIndex = Math.max(0, viewIndex - liveStoreOffset);

    // load data if not already cached
    if (!isCached(viewIndex)) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("updateRows() not cached, loading data");
      }
      maskView();
      if (loadLiveStore(getLiveStoreCalculatedIndex(viewIndex))) {
        viewIndexReload = viewIndex;
      }
      return;
    }

    // do pre caching
    if (shouldCache(viewIndex) && !isLoading) {
      loadLiveStore(getLiveStoreCalculatedIndex(viewIndex));
    }

    int rc = getVisibleRowCount();
    if (delta > rc - 1) {
      reload = true;
    }

    if (reload) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("updateRows() newIndex: " + newIndex + " viewIndex: " + viewIndex + " visible rows: " + rowCount
            + " diff: " + diff);
      }
      delta = diff = rc;
      if (ds.size() > 0) {
        boolean p = preventScrollToTopOnRefresh;
        preventScrollToTopOnRefresh = true;
        ds.clear();
        preventScrollToTopOnRefresh = p;
      }
    }

    if (delta == 0) {
      return;
    }

    int count = ds.size();
    if (diff > 0) {
      // rolling forward
      for (int c = 0; c < delta && c < count; c++) {
        ds.remove(0);
      }
      count = ds.size();
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("updateRows() forward adding to store from cache, liveStoreIndex: " + liveStoreIndex + " count: "
            + count);
      }
      ds.addAll(cacheStore.subList(liveStoreIndex + count, liveStoreIndex + count + delta));
    } else {
      // rolling back
      for (int c = 0; c < delta && c < count; c++) {
        ds.remove(count - c - 1);
      }

      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("updateRows() reverse adding to store from cache, liveStoreIndex: " + liveStoreIndex + " count: "
            + count);
      }

      ds.addAll(0, cacheStore.subList(liveStoreIndex, liveStoreIndex + delta));
    }

    if (!measureRowHeight) {
      if (viewIndex > lastViewIndex) {
        scroller.setScrollTop(rowHeightAdjust);
        lastScrollDirection = 1;
      } else if (viewIndex < lastViewIndex) {
        lastScrollDirection = 0;
        scroller.setScrollTop(0);
      } else {
        scroller.setScrollTop(lastScrollDirection == 0 ? 0 : rowHeightAdjust);
      }
    }

    fireEvent(new LiveGridViewUpdateEvent(liveStoreOffset, viewIndex, totalCount, getVisibleRowCount()));
    lastViewIndex = viewIndex;
  }

  private int getLoaderOffset() {
    if (grid.getLoader() instanceof PagingLoader<?, ?>) {
      return ((PagingLoader<?, ?>) grid.getLoader()).getOffset();
    } else {
      return 0;
    }
  }

  private int getLoaderTotalCount() {
    if (grid.getLoader() instanceof PagingLoader<?, ?>) {
      return ((PagingLoader<?, ?>) grid.getLoader()).getTotalCount();
    } else {
      return cacheStore.size();
    }
  }

  private void maskView() {
    if (!isMasked && grid.isLoadMask()) {
      grid.mask(DefaultMessages.getMessages().loadMask_msg());
      isMasked = true;
    }
  }

  private void positionLiveScroller() {
    liveScroller.setTop(headerElem.getOffsetHeight());
  }

  private void resizeLiveScroller() {
    int h = grid.getElement().getHeight(true) - headerElem.getHeight(true);
    if (isHorizontalScrollBarShowing()) {
      h -= barWidth;
    }
    if (footer != null) {
      h -= footer.getOffsetHeight();
    }
    liveScroller.setHeight(h, true);

  }

  private void updateScrollerHeight() {
    int height = totalCount * getRowHeight();
    // 1000000 as browser maxheight hack
    int count = height / 1000000;
    int h = 0;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();

    if (count > 0) {
      h = height / count;

      for (int i = 0; i < count; i++) {
        sb.appendHtmlConstant("<div style=\"width: " + barWidth + "px; height: " + h + "px;\">&#160;</div>");
      }
    }
    int diff = height - count * h;
    if (diff != 0) {
      sb.appendHtmlConstant("<div style=\"width: " + barWidth + "px; height: " + diff + "px;\"></div>");
    }
    liveScroller.setInnerSafeHtml(sb.toSafeHtml());
  }

  private static class NavKeyCallback<M> implements LoadHandler<Object, ListLoadResult<M>>,
      LoadExceptionHandler<Object> {
    private HandlerRegistration registration;
    private Grid<M> grid;
    private int index;

    public NavKeyCallback(Grid<M> grid, int index) {
      this.grid = grid;
      this.index = index;
      @SuppressWarnings("unchecked")
      Loader<Object, ListLoadResult<M>> l = (Loader<Object, ListLoadResult<M>>) grid.getLoader();
      registration = l.addLoadHandler(this);
    }

    @Override
    public void onLoad(LoadEvent<Object, ListLoadResult<M>> event) {
      LiveGridView<M> view = (LiveGridView<M>) grid.getView();
      grid.getSelectionModel().select(index, false);
      view.ensureVisible(index, -1, false);
      registration.removeHandler();
    }

    @Override
    public void onLoadException(LoadExceptionEvent<Object> event) {
      registration.removeHandler();
    }

  }
}
