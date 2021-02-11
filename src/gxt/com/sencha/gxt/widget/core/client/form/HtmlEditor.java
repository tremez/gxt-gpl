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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.FontSize;
import com.google.gwt.user.client.ui.RichTextArea.Justification;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.ColorMenu;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * Provides an HTML-based rich text editor with a tool bar for selecting formatting options, including fonts, text
 * justification, lists, hyperlinks and text color. Enables switching between formatted and HTML editing modes. Supports
 * copy and paste from Web pages as well as text editing features provided by the browser (e.g. spell checking, text
 * search).
 * <p/>
 * By default, all formatting options are enabled and available via the tool bar. To disable one or more options, use
 * the appropriate setter <b>before</b> adding the HTML editor to its container.
 */
public class HtmlEditor extends AdapterField<String> {

  /**
   * The appearance of this class.
   */
  public interface HtmlEditorAppearance {

    ImageResource bold();

    String editor();

    ImageResource fontColor();

    ImageResource fontDecrease();

    ImageResource fontHighlight();

    ImageResource fontIncrease();

    String frame();

    Element getContentElement(XElement parent);

    ImageResource italic();

    ImageResource justifyCenter();

    ImageResource justifyLeft();

    ImageResource justifyRight();

    ImageResource link();

    ImageResource ol();

    ImageResource source();

    ImageResource ul();

    ImageResource underline();
  }

  /**
   * The locale-sensitive messages used by this class.
   */
  public interface HtmlEditorMessages {
    String backColorTipText();

    String backColorTipTitle();

    String boldTipText();

    String boldTipTitle();

    String createLinkText();

    String decreaseFontSizeTipText();

    String decreaseFontSizeTipTitle();

    String foreColorTipText();

    String foreColorTipTitle();

    String increaseFontSizeTipText();

    String increaseFontSizeTipTitle();

    String italicTipText();

    String italicTipTitle();

    String justifyCenterTipText();

    String justifyCenterTipTitle();

    String justifyLeftTipText();

    String justifyLeftTipTitle();

    String justifyRightTipText();

    String justifyRightTipTitle();

    String linkTipText();

    String linkTipTitle();

    String olTipText();

    String olTipTitle();

    String sourceEditTipText();

    String sourceEditTipTitle();

    String ulTipText();

    String ulTipTitle();

    String underlineTipText();

    String underlineTipTitle();
  }

  protected class HtmlEditorDefaultMessages implements HtmlEditorMessages {

    @Override
    public String backColorTipText() {
      return DefaultMessages.getMessages().htmlEditor_backColorTipText();
    }

    @Override
    public String backColorTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_backColorTipTitle();
    }

    @Override
    public String boldTipText() {
      return DefaultMessages.getMessages().htmlEditor_boldTipText();
    }

    @Override
    public String boldTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_boldTipTitle();
    }

    @Override
    public String createLinkText() {
      return DefaultMessages.getMessages().htmlEditor_createLinkText();
    }

    @Override
    public String decreaseFontSizeTipText() {
      return DefaultMessages.getMessages().htmlEditor_decreaseFontSizeTipText();
    }

    @Override
    public String decreaseFontSizeTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_decreaseFontSizeTipTitle();
    }

    @Override
    public String foreColorTipText() {
      return DefaultMessages.getMessages().htmlEditor_foreColorTipText();
    }

    @Override
    public String foreColorTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_foreColorTipTitle();
    }

    @Override
    public String increaseFontSizeTipText() {
      return DefaultMessages.getMessages().htmlEditor_increaseFontSizeTipText();
    }

    @Override
    public String increaseFontSizeTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_increaseFontSizeTipTitle();
    }

    @Override
    public String italicTipText() {
      return DefaultMessages.getMessages().htmlEditor_italicTipText();
    }

    @Override
    public String italicTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_italicTipTitle();
    }

    @Override
    public String justifyCenterTipText() {
      return DefaultMessages.getMessages().htmlEditor_justifyCenterTipText();
    }

    @Override
    public String justifyCenterTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_justifyCenterTipTitle();
    }

    @Override
    public String justifyLeftTipText() {
      return DefaultMessages.getMessages().htmlEditor_justifyLeftTipText();
    }

    @Override
    public String justifyLeftTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_justifyLeftTipTitle();
    }

    @Override
    public String justifyRightTipText() {
      return DefaultMessages.getMessages().htmlEditor_justifyRightTipText();
    }

    @Override
    public String justifyRightTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_justifyRightTipTitle();
    }

    @Override
    public String linkTipText() {
      return DefaultMessages.getMessages().htmlEditor_linkTipText();
    }

    @Override
    public String linkTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_linkTipTitle();
    }

    @Override
    public String olTipText() {
      return DefaultMessages.getMessages().htmlEditor_olTipText();
    }

    @Override
    public String olTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_olTipTitle();
    }

    @Override
    public String sourceEditTipText() {
      return DefaultMessages.getMessages().htmlEditor_sourceEditTipText();
    }

    @Override
    public String sourceEditTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_sourceEditTipTitle();
    }

    @Override
    public String ulTipText() {
      return DefaultMessages.getMessages().htmlEditor_ulTipText();
    }

    @Override
    public String ulTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_ulTipTitle();
    }

    @Override
    public String underlineTipText() {
      return DefaultMessages.getMessages().htmlEditor_underlineTipText();
    }

    @Override
    public String underlineTipTitle() {
      return DefaultMessages.getMessages().htmlEditor_underlineTipTitle();
    }

  }

  /**
   * Extension of VerticalLayoutContainer to handle misc issues with
   * scrolling, etc of mobile/touch devices
   */
  private static class HtmlEditorContainer extends VerticalLayoutContainer {

    private HtmlEditor editor;

    public void setHtmlEditor(HtmlEditor editor) {
      this.editor = editor;
    }

    @Override
    protected void doLayout() {
      super.doLayout();

      if (GXT.isSafari() && GXT.isTouch()) {
        /* EXTGWT-4532
           Scrolling is messed up with iOS safari and iframes.  Copying width/height and
           adding extra style attributes allows mobile safari to work
        */
        XElement textAreaElement;
        if (editor.sourceEditMode) {
          textAreaElement = editor.sourceTextArea.getElement();
        } else {
          textAreaElement = editor.getTextArea().getElement().cast();
        }
        XElement parent = textAreaElement.getParentElement().cast();
        parent.getStyle().setOverflow(Overflow.AUTO);
        parent.getStyle().setProperty("webkitOverflowScrolling", "touch");
        int textAreaWidth = textAreaElement.getWidth(true);
        // text area height is total height minus toolbar
        int textAreaHeight = editor.getOffsetHeight() - (editor.isShowToolBar() ? editor.toolBar.getOffsetHeight() : 0);
        parent.setHeight(textAreaHeight);
        parent.setWidth(textAreaWidth);
      }
    }
  }

  protected VerticalLayoutContainer container;
  protected ToolBar toolBar;
  private final HtmlEditorAppearance appearance;
  protected HtmlEditorMessages messages;
  protected RichTextArea textArea;
  protected TextArea sourceTextArea;
  protected List<FontSize> fontSizesConstants = new ArrayList<FontSize>();
  protected FontSize activeFontSize = FontSize.SMALL;

  private boolean enableAlignments = true;
  private boolean enableColors = true;
  private boolean enableFont = true;
  private boolean enableFontSize = true;
  private boolean enableFormat = true;
  private boolean enableLinks = true;
  private boolean enableLists = true;
  private boolean showToolBar = true;
  private boolean enableSourceEditMode = true;
  private TextButton fontIncrease;
  private TextButton fontDecrease;
  private SelectHandler buttonHandler;
  private ToggleButton bold;
  private ToggleButton italic;
  private ToggleButton underline;
  private TextButton justifyLeft;
  private TextButton justifyCenter;
  private TextButton justifyRight;
  private TextButton ol;
  private TextButton ul;
  private TextButton link;
  private TextButton fontColor;
  private TextButton fontHighlight;
  private ToggleButton sourceEdit;
  private boolean sourceEditMode;

  /**
   * Creates an HTML-based rich text editor with support for fonts, text justification, lists, hyperlinks and text
   * color.
   */
  public HtmlEditor() {
    this(GWT.<HtmlEditorAppearance> create(HtmlEditorAppearance.class));
  }

  /**
   * Creates an HTML-based rich text editor with support for fonts, text justification, lists, hyperlinks and text
   * color.
   */
  public HtmlEditor(HtmlEditorAppearance appearance) {
    super(new HtmlEditorContainer());
    this.container = (VerticalLayoutContainer) getWidget();
    ((HtmlEditorContainer)container).setHtmlEditor(this);
    this.appearance = appearance;

    addStyleName(appearance.editor());
    setBorders(true);

    toolBar = new ToolBar();
    toolBar.addStyleName("x-html-editor-toolbar-mark");

    textArea = new RichTextArea();
    textArea.addStyleName(appearance.frame());
    textArea.addFocusHandler(new FocusHandler() {

      @Override
      public void onFocus(FocusEvent event) {

        //EXTGWT-2916 - Firefox is re-enabled whenever it is re-attached to the dom
        //and focused.
        if (GXT.isGecko()) {
          Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
              if (!isEnabled()) {
                //shouldn't be focusing anyway since disabled,
                //so we need to re-set this
                textArea.setEnabled(isEnabled());
              }
            }
          });
        }
      }
    });

    container.add(toolBar, new VerticalLayoutData(1, -1));
    container.add(textArea, new VerticalLayoutData(1, 1));

    fontSizesConstants.add(FontSize.XX_SMALL);
    fontSizesConstants.add(FontSize.X_SMALL);
    fontSizesConstants.add(FontSize.SMALL);
    fontSizesConstants.add(FontSize.MEDIUM);
    fontSizesConstants.add(FontSize.LARGE);
    fontSizesConstants.add(FontSize.X_LARGE);
    fontSizesConstants.add(FontSize.XX_LARGE);
  }

  public HtmlEditorAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   * 
   * @return the local-sensitive messages used by this class.
   */
  public HtmlEditorMessages getMessages() {
    if (messages == null) {
      messages = new HtmlEditorDefaultMessages();
    }
    return messages;
  }

  @Override
  public String getValue() {
    if (sourceEditMode && sourceTextArea != null) {
      pushValue();
    }
    return textArea.getHTML();
  }

  /**
   * Returns the {@link RichTextArea}.
   *
   * @return textArea
   */
  public RichTextArea getTextArea() {
    return textArea;
  }

  /**
   * Returns {@code true} if text justification is enabled.
   * 
   * @return {@code true} if text justification is enabled
   */
  public boolean isEnableAlignments() {
    return enableAlignments;
  }

  /**
   * Returns {@code true} if setting text foreground and background colors is enabled.
   * 
   * @return {@code true} if setting text foreground and background colors is enabled
   */
  public boolean isEnableColors() {
    return enableColors;
  }

  /**
   * Returns {@code true} if setting font family name is enabled.
   * 
   * @return {@code true} if setting font family name is enabled
   */
  public boolean isEnableFont() {
    return enableFont;
  }

  /**
   * Returns {@code true} if setting font size is enabled.
   * 
   * @return {@code true} if setting font size is enabled
   */
  public boolean isEnableFontSize() {
    return enableFontSize;
  }

  /**
   * Returns {@code true} if setting font style is enabled.
   * 
   * @return {@code true} if setting font style is enabled
   */
  public boolean isEnableFormat() {
    return enableFormat;
  }

  /**
   * Returns {@code true} if creating a hyperlink from selected text is enabled.
   * 
   * @return {@code true} if creating a hyperlink from selected text is enabled
   */
  public boolean isEnableLinks() {
    return enableLinks;
  }

  /**
   * Returns {@code true} if creating lists is enabled.
   * 
   * @return {@code true} if creating lists is enabled
   */
  public boolean isEnableLists() {
    return enableLists;
  }

  /**
   * Returns {@code true} if the ability to switch to HTML source mode is enabled.
   * 
   * @return {@code true} if the ability to switch to HTML source mode is enabled
   */
  public boolean isEnableSourceEditMode() {
    return enableSourceEditMode;
  }

  /**
   * Returns {@code true} if the toolbar is displayed.
   * 
   * @return {@code true} if the toolbar is displayed
   */
  public boolean isShowToolBar() {
    return showToolBar;
  }

  /**
   * Returns {@code true} if the editor is in source edit mode.
   * 
   * @return {@code true} if editor is in source edit mode
   */
  public boolean isSourceEditMode() {
    return sourceEditMode;
  }

  /**
   * Copies the value of the HTML source editor to the rich text editor.
   */
  public void pushValue() {
    textArea.setHTML(sourceTextArea.getValue());
  }

  /**
   * Sets whether text justification is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enableAlignments {@code true} to enable text justification
   */
  public void setEnableAlignments(boolean enableAlignments) {
    assertPreRender();
    this.enableAlignments = enableAlignments;
  }

  /**
   * Sets whether setting text foreground and background colors is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enableColors {@code true} to enable setting text foreground and background colors.
   */
  public void setEnableColors(boolean enableColors) {
    assertPreRender();
    this.enableColors = enableColors;
  }

  /**
   * Sets whether setting font family name is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enableFont {@code true} to enable setting font family name
   */
  public void setEnableFont(boolean enableFont) {
    assertPreRender();
    this.enableFont = enableFont;
  }

  /**
   * Sets whether setting font size is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enableFontSize {@code true} to enable setting font size
   */
  public void setEnableFontSize(boolean enableFontSize) {
    assertPreRender();
    this.enableFontSize = enableFontSize;
  }

  /**
   * Sets whether setting font style is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enableFormat {@code true} to enable setting font style
   */
  public void setEnableFormat(boolean enableFormat) {
    assertPreRender();
    this.enableFormat = enableFormat;
  }

  /**
   * Sets whether creating a hyperlink from selected text is enable (defaults to {@code true}, pre-render).
   * 
   * @param enableLinks {@code true} to enable creating a hyperlink from selected text
   */
  public void setEnableLinks(boolean enableLinks) {
    assertPreRender();
    this.enableLinks = enableLinks;
  }

  /**
   * Sets whether creating lists is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enableLists {@code true} to enable creating lists
   */
  public void setEnableLists(boolean enableLists) {
    assertPreRender();
    this.enableLists = enableLists;
  }

  /**
   * Sets whether the source edit mode is enabled (defaults to {@code true}, pre-render).
   * 
   * @param enable {@code true} if source edit mode is enabled
   */
  public void setEnableSourceEditMode(boolean enable) {
    assertPreRender();
    this.enableSourceEditMode = enable;
  }

  /**
   * Sets the local-sensitive messages used by this class.
   * 
   * @param messages the locale sensitive messages used by this class.
   */
  public void setMessages(HtmlEditorMessages messages) {
    this.messages = messages;
  }

  /**
   * Sets whether the toolbar should be shown. This property can only be modified before this component is first
   * attached.
   * 
   * @param showToolBar {@code true} to show the toolbar, {@code false} otherwise.
   */
  public void setShowToolBar(boolean showToolBar) {
    assertPreRender();
    this.showToolBar = showToolBar;
  }

  /**
   * Specifies if the editor should be in source edit mode. Only applies when {@link #setEnableSourceEditMode(boolean)}
   * is set to {@code true}.
   * 
   * @param sourceEditMode {@code true} to put editor in source edit mode
   */
  public void setSourceEditMode(boolean sourceEditMode) {
    this.sourceEditMode = sourceEditMode;

    if (!isRendered()) {
      return;
    }

    if (enableSourceEditMode) {

      if (sourceEditMode) {
        if (sourceTextArea == null) {
          sourceTextArea = new TextArea();
        }

        sourceEdit.setValue(true, false);

        sourceTextArea.setHeight(textArea.getOffsetHeight());

        syncValue();
        container.remove(1);
        container.add(sourceTextArea, new VerticalLayoutData(1, 1));
        container.forceLayout();

        sourceTextArea.focus();
      } else {
        pushValue();
        container.remove(1);
        container.add(textArea, new VerticalLayoutData(1, 1));
        container.forceLayout();

        sourceEdit.setValue(false, false);

        textArea.setFocus(true);
      }
    }

    for (int i = 0; i < toolBar.getWidgetCount(); i++) {
      Widget w = toolBar.getWidget(i);
      if (w != sourceEdit && w instanceof Component) {
        Component c = (Component) w;
        if (c.getData("gxt-more") != null) {
          continue;
        }
        c.setEnabled(!sourceEdit.getValue());
      } else {
        if (w instanceof FocusWidget) {
          ((FocusWidget) w).setEnabled(!sourceEdit.getValue());
        }
      }
    }
  }

  @Override
  public void setValue(String value) {
    textArea.setHTML(value);
    if (sourceEditMode && sourceTextArea != null) {
      syncValue();
    }
  }

  /**
   * Copies the value of the rich text editor to the HTML source editor.
   */
  public void syncValue() {
    sourceTextArea.setValue(textArea.getHTML());
  }

  protected void initToolBar() {
    if (!showToolBar) {
      return;
    }

    buttonHandler = new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        Widget button = (Widget) event.getSource();

        if (button == fontIncrease) {
          int i = fontSizesConstants.indexOf(activeFontSize);
          if (i < (fontSizesConstants.size() - 1)) {
            i++;
            activeFontSize = fontSizesConstants.get(i);
            textArea.getFormatter().setFontSize(activeFontSize);
          } else {
            // brings focus back to the editor
            focus();
          }
        } else if (button == fontDecrease) {
          int i = fontSizesConstants.indexOf(activeFontSize);
          if (i > 0) {
            i--;
            activeFontSize = fontSizesConstants.get(i);
            textArea.getFormatter().setFontSize(activeFontSize);
          } else {
            // brings focus back to the editor
            focus();
          }
        } else if (button == bold) {
          textArea.getFormatter().toggleBold();
        } else if (button == italic) {
          textArea.getFormatter().toggleItalic();
        } else if (button == underline) {
          textArea.getFormatter().toggleUnderline();
        } else if (button == justifyLeft) {
          textArea.getFormatter().setJustification(Justification.LEFT);
        } else if (button == justifyCenter) {
          textArea.getFormatter().setJustification(Justification.CENTER);
        } else if (button == justifyRight) {
          textArea.getFormatter().setJustification(Justification.RIGHT);
        } else if (button == ol) {
          textArea.getFormatter().insertOrderedList();
        } else if (button == ul) {
          textArea.getFormatter().insertUnorderedList();
        } else if (button == link) {
          String link = Window.prompt(getMessages().createLinkText(), "http://");
          if (link != null && link.length() > 0) {
            textArea.getFormatter().createLink(link);
          } else {
            textArea.getFormatter().removeLink();
          }
        }
      }
    };

    HtmlEditorMessages m = getMessages();

    if (enableFont) {
      final SimpleComboBox<String> fonts = new SimpleComboBox<String>(new StringLabelProvider<String>());
      fonts.setEditable(false);
      fonts.setTriggerAction(TriggerAction.ALL);
      fonts.setForceSelection(true);
      fonts.add("Arial");
      fonts.add("Courier New");
      fonts.add("Times New Roman");
      fonts.add("Verdana");
      fonts.setValue(fonts.getStore().get(0));

      fonts.addSelectionHandler(new SelectionHandler<String>() {
        @Override
        public void onSelection(SelectionEvent<String> event) {
          textArea.getFormatter().setFontName(event.getSelectedItem());
        }
      });

      toolBar.add(fonts);
      toolBar.add(new SeparatorToolItem());
    }

    if (enableFontSize) {
      fontIncrease = new TextButton();
      configureButton(fontIncrease, appearance.fontIncrease(), m.increaseFontSizeTipTitle(),
          m.increaseFontSizeTipText());
      fontIncrease.addSelectHandler(buttonHandler);
      toolBar.add(fontIncrease);

      fontDecrease = new TextButton();
      configureButton(fontDecrease, appearance.fontDecrease(), m.decreaseFontSizeTipTitle(),
          m.decreaseFontSizeTipText());
      fontDecrease.addSelectHandler(buttonHandler);
      toolBar.add(fontDecrease);

      toolBar.add(new SeparatorToolItem());
    }

    if (enableFormat) {
      bold = new ToggleButton();
      configureButton(bold, appearance.bold(), m.boldTipTitle(), m.boldTipText());
      bold.addSelectHandler(buttonHandler);
      toolBar.add(bold);

      italic = new ToggleButton();
      configureButton(italic, appearance.italic(), m.italicTipTitle(), m.italicTipText());
      italic.addSelectHandler(buttonHandler);
      toolBar.add(italic);

      underline = new ToggleButton();
      configureButton(underline, appearance.underline(), m.underlineTipTitle(), m.underlineTipText());
      underline.addSelectHandler(buttonHandler);
      toolBar.add(underline);

      toolBar.add(new SeparatorToolItem());
    }

    if (enableAlignments) {
      justifyLeft = new TextButton();
      configureButton(justifyLeft, appearance.justifyLeft(), m.justifyLeftTipTitle(), m.justifyLeftTipText());
      justifyLeft.addSelectHandler(buttonHandler);
      toolBar.add(justifyLeft);

      justifyCenter = new TextButton();
      configureButton(justifyCenter, appearance.justifyCenter(), m.justifyCenterTipTitle(), m.justifyCenterTipText());
      justifyCenter.addSelectHandler(buttonHandler);
      toolBar.add(justifyCenter);

      justifyRight = new TextButton();
      configureButton(justifyRight, appearance.justifyRight(), m.justifyRightTipTitle(), m.justifyRightTipText());
      justifyRight.addSelectHandler(buttonHandler);
      toolBar.add(justifyRight);

      toolBar.add(new SeparatorToolItem());
    }

    if (enableLists) {
      ol = new TextButton();
      configureButton(ol, appearance.ol(), m.olTipTitle(), m.olTipText());
      ol.addSelectHandler(buttonHandler);
      toolBar.add(ol);

      ul = new TextButton();
      configureButton(ul, appearance.ul(), m.ulTipTitle(), m.ulTipText());
      ul.addSelectHandler(buttonHandler);
      toolBar.add(ul);

      toolBar.add(new SeparatorToolItem());
    }

    if (enableLinks) {
      link = new TextButton();
      configureButton(link, appearance.link(), m.linkTipTitle(), m.linkTipText());
      link.addSelectHandler(buttonHandler);
      toolBar.add(link);

      toolBar.add(new SeparatorToolItem());
    }

    if (enableColors) {
      fontColor = new TextButton();
      configureButton(fontColor, appearance.fontColor(), m.foreColorTipTitle(), m.foreColorTipText());
      final ColorMenu fontColorMenu = new ColorMenu();
      fontColorMenu.setFocusOnShow(false);
      fontColorMenu.getPalette().addValueChangeHandler(new ValueChangeHandler<String>() {

        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
          fontColorMenu.hide();
          textArea.getFormatter().setForeColor(event.getValue());
        }
      });

      fontColor.setMenu(fontColorMenu);

      toolBar.add(fontColor);

      fontHighlight = new TextButton();
      configureButton(fontHighlight, appearance.fontHighlight(), m.backColorTipTitle(), m.backColorTipText());

      final ColorMenu fontHighlightMenu = new ColorMenu();
      fontHighlightMenu.setFocusOnShow(false);
      fontHighlightMenu.getPalette().addValueChangeHandler(new ValueChangeHandler<String>() {
        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
          fontHighlightMenu.hide();
          textArea.getFormatter().setBackColor(event.getValue());
        }
      });
      fontHighlight.setMenu(fontHighlightMenu);

      toolBar.add(fontHighlight);
    }

    if (enableSourceEditMode) {
      toolBar.add(new SeparatorToolItem());
      sourceEdit = new ToggleButton();
      configureButton(sourceEdit, appearance.source(), m.sourceEditTipTitle(), m.sourceEditTipText());

      sourceEdit.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          toggleSourceEditMode();
        }
      });
      toolBar.add(sourceEdit);

    }
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    initToolBar();

    if (sourceEditMode) {
      setSourceEditMode(sourceEditMode);
    }
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    if (sourceTextArea != null) {
      sourceTextArea.disable();
    }
    textArea.setEnabled(false);
    mask();
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    if (sourceTextArea != null) {
      sourceTextArea.enable();
    }
    textArea.setEnabled(true);
    unmask();
  }

  protected void toggleSourceEditMode() {
    setSourceEditMode(!isSourceEditMode());
  }

  private void configureButton(CellButtonBase<?> button, ImageResource icon, String tipTitle, String tipText) {
    button.setIcon(icon);
    button.setToolTipConfig(createTipConfig(tipTitle, tipText));
    button.setData("gxt-menutext", tipTitle);
  }

  private ToolTipConfig createTipConfig(String title, String text) {
    return new ToolTipConfig(title, text);
  }

}
