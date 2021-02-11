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
package com.sencha.gxt.widget.core.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextBox;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.gestures.TouchEventToGestureAdapter;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent;
import com.sencha.gxt.data.shared.loader.LoaderHandler;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * A specialized toolbar that is bound to a {@link PagingLoader} and provides automatic paging controls.
 * 
 * <p />
 * The tool bar is "bound" to the loader using the {@link #bind(PagingLoader)} method.
 * <p />
 * 
 * <b>Enabling & disabling child toolbar items:</b> The paging toolbar setEnable overrides any child toolbar items
 * setEnable. Given the paging toolbar overrides the child toolbar items setEnable, deferring child toolbar enablement
 * is needed after the onload. So when the paging toolbar is enabled the has a child toolbar the items enablement can be
 * overridden by scheduling their enablement. The example below shows an example of scheduling child toolbar item
 * enablement.
 */
public class PagingToolBar extends ToolBar {

  public interface PagingToolBarAppearance {

    ImageResource first();

    ImageResource last();

    ImageResource loading();

    ImageResource next();

    ImageResource prev();

    ImageResource refresh();

  }

  public interface PagingToolBarMessages {

    String afterPageText(int page);

    String beforePageText();

    String displayMessage(int start, int end, int total);

    String emptyMessage();

    String firstText();

    String lastText();

    String nextText();

    String prevText();

    String refreshText();

  }

  protected static class DefaultPagingToolBarMessages implements PagingToolBarMessages {

    @Override
    public String afterPageText(int page) {
      return DefaultMessages.getMessages().pagingToolBar_afterPageText(page);
    }

    @Override
    public String beforePageText() {
      return DefaultMessages.getMessages().pagingToolBar_beforePageText();
    }

    @Override
    public String displayMessage(int start, int end, int total) {
      return DefaultMessages.getMessages().pagingToolBar_displayMsg(start, end, total);
    }

    @Override
    public String emptyMessage() {
      return DefaultMessages.getMessages().pagingToolBar_emptyMsg();
    }

    @Override
    public String firstText() {
      return DefaultMessages.getMessages().pagingToolBar_firstText();
    }

    @Override
    public String lastText() {
      return DefaultMessages.getMessages().pagingToolBar_lastText();
    }

    @Override
    public String nextText() {
      return DefaultMessages.getMessages().pagingToolBar_nextText();
    }

    @Override
    public String prevText() {
      return DefaultMessages.getMessages().pagingToolBar_prevText();
    }

    @Override
    public String refreshText() {
      return DefaultMessages.getMessages().pagingToolBar_refreshText();
    }

  }

  protected int activePage = -1, pages;
  protected LabelToolItem beforePage, afterText, displayText;
  protected PagingLoadConfig config;
  protected TextButton first, prev, next, last, refresh;
  protected PagingLoader<PagingLoadConfig, ?> loader;
  protected TextBox pageText;

  protected boolean showToolTips = true;
  protected int start, pageSize, totalLength;
  private final PagingToolBarAppearance appearance;
  private boolean loading;
  private boolean buttonsEnabled;
  // flag to track if refresh was clicked since setIcon will steal focus. If it was focused, we must refocus after icon change
  private boolean activeRefresh = false;

  private LoaderHandler<PagingLoadConfig, ?> handler = new LoaderHandler<PagingLoadConfig, PagingLoadResult<?>>() {

    @Override
    public void onBeforeLoad(final BeforeLoadEvent<PagingLoadConfig> event) {
      loading = true;
      doEnableButtons(false);
      refresh.setIcon(appearance.loading());
      if (activeRefresh) {
        refresh.focus();
      }
      Scheduler.get().scheduleFinally(new ScheduledCommand() {

        @Override
        public void execute() {
          if (event.isCancelled()) {
            refresh.setIcon(appearance.refresh());
            if (activeRefresh) {
              refresh.focus();
            }

            doEnableButtons(true);
            PagingToolBar.this.onLoad(new LoadEvent<PagingLoadConfig, PagingLoadResult<?>>(config,
                new PagingLoadResultBean<Object>(null, totalLength, start)));
          }
        }
      });
    }

    @Override
    public void onLoad(LoadEvent<PagingLoadConfig, PagingLoadResult<?>> event) {
      refresh.setIcon(appearance.refresh());
      if (activeRefresh) {
        refresh.focus();
        activeRefresh = false;
      }

      doEnableButtons(true);
      PagingToolBar.this.onLoad(event);
    }

    @Override
    public void onLoadException(LoadExceptionEvent<PagingLoadConfig> event) {
      refresh.setIcon(appearance.refresh());
      if (activeRefresh) {
        refresh.focus();
        activeRefresh = false;
      }

      doEnableButtons(true);
      //setting this here since we never get into onLoad
      loading = false;
    }
  };

  private HandlerRegistration handlerRegistration;
  private PagingToolBarMessages messages;
  private boolean reuseConfig = true;

  /**
   * Creates a new paging tool bar.
   * 
   * @param pageSize the page size
   */
  @UiConstructor
  public PagingToolBar(int pageSize) {
    this(GWT.<ToolBarAppearance> create(ToolBarAppearance.class),
        GWT.<PagingToolBarAppearance> create(PagingToolBarAppearance.class), pageSize);
  }

  /**
   * Creates a new tool bar.
   * 
   * @param toolBarAppearance the tool bar appearance
   * @param appearance the paging tool bar appearance
   * @param pageSize the page size
   */
  public PagingToolBar(ToolBarAppearance toolBarAppearance, PagingToolBarAppearance appearance, int pageSize) {
    super(toolBarAppearance);
    this.appearance = appearance;
    this.pageSize = pageSize;

    addStyleName("x-paging-toolbar-mark");

    first = new TextButton();
    first.setIcon(appearance.first());
    first.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        first();
      }
    });

    prev = new TextButton();
    prev.setIcon(appearance.prev());
    prev.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        previous();
      }
    });

    next = new TextButton();
    next.setIcon(appearance.next());
    next.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        next();
      }
    });

    last = new TextButton();
    last.setIcon(appearance.last());
    last.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        last();
      }
    });

    refresh = new TextButton();
    refresh.setIcon(appearance.refresh());
    refresh.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        refresh();
      }
    });

    beforePage = new LabelToolItem();
    beforePage.setLabel(getMessages().beforePageText());

    afterText = new LabelToolItem();

    pageText = new TextBox();
    pageText.setWidth("30px");
    pageText.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          onPageChange();
        }
      }
    });
    new TouchEventToGestureAdapter(pageText, new TapGestureRecognizer() {
      @Override
      protected void onTap(TouchData touchData) {
        super.onTap(touchData);
        pageText.setFocus(true);
      }
    });

    displayText = new LabelToolItem();
    displayText.addStyleName(CommonStyles.get().nowrap());

    addToolTips();

    add(first);
    add(prev);
    add(new SeparatorToolItem());
    add(beforePage);
    add(pageText);
    add(afterText);
    add(new SeparatorToolItem());
    add(next);
    add(last);
    add(new SeparatorToolItem());
    add(refresh);
    add(new FillToolItem());
    add(displayText);
  }

  /**
   * Binds the toolbar to the loader.
   * 
   * @param loader the loader
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void bind(PagingLoader<? extends PagingLoadConfig, ?> loader) {
    if (this.loader != null) {
      handlerRegistration.removeHandler();
    }
    this.loader = (PagingLoader) loader;
    if (loader != null) {
      loader.setLimit(pageSize);
      // the loader and the handler have the same generics, the cast is required
      // because neither one cares about the data in the load result. Unsure of
      // a better way to express this.
      handlerRegistration = loader.addLoaderHandler((LoaderHandler) handler);
    }
  }

  /**
   * Clears the current toolbar text.
   */
  public void clear() {
    pageText.setText("");
    afterText.setLabel("");
    displayText.setLabel("");
  }

  /**
   * Called when a load request is initialed and called after completion of the load request. Subclasses may override as
   * needed.
   * 
   * @param enabled the enabled state
   */
  protected void doEnableButtons(boolean enabled) {
    buttonsEnabled = enabled;
    first.setEnabled(enabled);
    prev.setEnabled(enabled);
    beforePage.setEnabled(enabled);
    pageText.setEnabled(enabled);
    afterText.setEnabled(enabled);
    next.setEnabled(enabled);
    last.setEnabled(enabled);
    displayText.setEnabled(enabled);
  }

  /**
   * Moves to the first page.
   */
  public void first() {
    if (!loading) {
      doLoadRequest(0, pageSize);
    }
  }

  /**
   * Returns the active page.
   * 
   * @return the active page
   */
  public int getActivePage() {
    return activePage;
  }

  /**
   * Returns the toolbar appearance.
   * 
   * @return the appearance
   */
  public PagingToolBarAppearance getPagingToolbarAppearance() {
    return appearance;
  }

  /**
   * Returns the toolbar messages.
   * 
   * @return the messages
   */
  public PagingToolBarMessages getMessages() {
    if (messages == null) {
      messages = new DefaultPagingToolBarMessages();
    }
    return messages;
  }

  /**
   * Returns the current page size.
   * 
   * @return the page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the total number of pages.
   * 
   * @return the total pages
   */
  public int getTotalPages() {
    return pages;
  }

  /**
   * Returns true if the paging toolbar buttons are enabled.
   * 
   * @return the buttons enabled.
   */
  public boolean isButtonsEnabled() {
    return buttonsEnabled;
  }

  /**
   * Returns true if the previous load config is reused.
   * 
   * @return the reuse config state
   */
  public boolean isReuseConfig() {
    return reuseConfig;
  }

  /**
   * Returns true if tooltip are enabled.
   * 
   * @return the show tooltip state
   */
  public boolean isShowToolTips() {
    return showToolTips;
  }

  /**
   * Moves to the last page.
   */
  public void last() {
    if (!loading) {
      if (totalLength > 0) {
        int extra = totalLength % pageSize;
        int lastStart = extra > 0 ? (totalLength - extra) : totalLength - pageSize;
        doLoadRequest(lastStart, pageSize);
      }
    }
  }

  /**
   * Moves to the last page.
   */
  public void next() {
    if (!loading) {
      doLoadRequest(start + pageSize, pageSize);
    }
  }

  /**
   * Moves the the previous page.
   */
  public void previous() {
    if (!loading) {
      doLoadRequest(Math.max(0, start - pageSize), pageSize);
    }
  }

  /**
   * Refreshes the data using the current configuration.
   */
  public void refresh() {
    if (!loading) {
      activeRefresh = true;
      doLoadRequest(start, pageSize);
    }
  }

  /**
   * Sets the active page (1 to page count inclusive).
   * 
   * @param page the page
   */
  public void setActivePage(int page) {
    if (page > pages) {
      last();
      return;
    }
    if (page != activePage && page > 0 && page <= pages) {
      doLoadRequest(--page * pageSize, pageSize);
    } else {
      pageText.setText(String.valueOf((int) activePage));
    }
  }

  /**
   * Sets the toolbar messages.
   * 
   * @param messages the messages
   */
  public void setMessages(PagingToolBarMessages messages) {
    this.messages = messages;
  }

  /**
   * Sets the current page size. This method does not effect the data currently being displayed. The new page size will
   * not be used until the next load request.
   * 
   * @param pageSize the new page size
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * True to reuse the previous load config (defaults to true).
   * 
   * @param reuseConfig true to reuse the load config
   */
  public void setReuseConfig(boolean reuseConfig) {
    this.reuseConfig = reuseConfig;
  }

  /**
   * Sets if the button tool tips should be displayed (defaults to true, pre-render).
   * 
   * @param showToolTips true to show tool tips
   */
  public void setShowToolTips(boolean showToolTips) {
    this.showToolTips = showToolTips;
    if (showToolTips) {
      addToolTips();
    } else {
      removeToolTips();
    }
  }

  protected void doLoadRequest(int offset, int limit) {
    if (reuseConfig && config != null) {
      config.setOffset(offset);
      config.setLimit(pageSize);
      loader.load(config);
    } else {
      loader.setLimit(pageSize);
      loader.load(offset, limit);
    }
  }

  protected void onLoad(LoadEvent<PagingLoadConfig, PagingLoadResult<?>> event) {
    loading = false;
    config = event.getLoadConfig();
    PagingLoadResult<?> result = event.getLoadResult();
    start = result.getOffset();
    totalLength = result.getTotalLength();
    activePage = (int) Math.ceil((double) (start + pageSize) / pageSize);

    pages = totalLength < pageSize ? 1 : (int) Math.ceil((double) totalLength / pageSize);

    if (activePage > pages && totalLength > 0) {
      last();
      return;
    } else if (activePage > pages) {
      start = 0;
      activePage = 1;
    }

    pageText.setText(String.valueOf((int) activePage));

    String after = null, display = null;
    after = getMessages().afterPageText(pages);
    afterText.setLabel(after);

    first.setEnabled(activePage != 1);
    prev.setEnabled(activePage != 1);
    next.setEnabled(activePage != pages);
    last.setEnabled(activePage != pages);

    int temp = activePage == pages ? totalLength : start + pageSize;

    display = getMessages().displayMessage(start + 1, (int) temp, totalLength);

    String msg = display;
    if (totalLength == 0) {
      msg = getMessages().emptyMessage();
    }
    displayText.setLabel(msg);

    forceLayout();
  }

  protected void onPageChange() {
    String value = pageText.getText();
    if (value.equals("") || !Util.isInteger(value)) {
      pageText.setText(String.valueOf((int) activePage));
      return;
    }
    int p = Integer.parseInt(value);
    setActivePage(p);
  }

  /**
   * Helper method to apply the tool tip messages to built-in toolbar buttons. Additional tooltips can be set by
   * overriding {@link #setShowToolTips(boolean)}.
   */
  private void addToolTips() {
    PagingToolBarMessages m = getMessages();
    first.setToolTip(m.firstText());
    prev.setToolTip(m.prevText());
    next.setToolTip(m.nextText());
    last.setToolTip(m.lastText());
    refresh.setToolTip(m.refreshText());
  }

  /**
   * Helper method to remove the tool tip messages from built-in toolbar buttons. Additional tooltips can be set by
   * overriding {@link #setShowToolTips(boolean)}.
   */
  private void removeToolTips() {
    first.removeToolTip();
    prev.removeToolTip();
    next.removeToolTip();
    last.removeToolTip();
    refresh.removeToolTip();
  }
}
