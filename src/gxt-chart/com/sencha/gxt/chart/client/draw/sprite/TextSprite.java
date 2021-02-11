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
package com.sencha.gxt.chart.client.draw.sprite;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.core.client.util.Util;

/**
 * A {@link Sprite} that represents text.
 */
public class TextSprite extends Sprite {

  /**
   * Enum for {@link TextSprite} anchor.
   */
  public enum TextAnchor {
    /**
     * Sprite places the text starting from the given coordinates.
     */
    START,
    /**
     * Sprite places the text centered at the given coordinates.
     */
    MIDDLE,
    /**
     * Sprite places the text ending at the given coordinates.
     */
    END
  }

  /**
   * Enum for {@link TextSprite} baseline.
   */
  public enum TextBaseline {
    /**
     * Sprite places the text below the given coordinates.
     */
    TOP,
    /**
     * Sprite places the text centered at the given coordinates.
     */
    MIDDLE,
    /**
     * Sprite places the text on top of the given coordinates.
     */
    BOTTOM
  }

  private String text;
  private boolean textDirty = false;
  private FontStyle fontStyle;
  private boolean fontStyleDirty = false;
  private FontWeight fontWeight;
  private boolean fontWeightDirty = false;
  private TextAnchor textAnchor = TextAnchor.START;
  private boolean textAnchorDirty = true;
  private TextBaseline textBaseline = TextBaseline.TOP;
  private boolean textBaselineDirty = true;
  private int fontSize;
  private boolean fontSizeDirty = false;
  private String font;
  private boolean fontDirty = false;
  private double x = 0;
  private boolean xDirty = false;
  private double y = 0;
  private boolean yDirty = false;

  /**
   * Creates a text sprite.
   */
  public TextSprite() {
    // defaults text to black
    setFill(RGB.BLACK);
    setTextBaseline(TextBaseline.TOP);
    setFontSize(12);
    setTextAnchor(TextAnchor.START);
  }

  /**
   * Creates a text sprite with the given text.
   * 
   * @param text the text represented in the sprite
   */
  public TextSprite(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a copy of the given text sprite.
   * 
   * @param sprite the sprite to be copied
   */
  public TextSprite(TextSprite sprite) {
    update(sprite);
  }

  @Override
  public void clearDirtyFlags() {
    super.clearDirtyFlags();
    textDirty = false;
    fontStyleDirty = false;
    fontWeightDirty = false;
    textAnchorDirty = false;
    textBaselineDirty = false;
    fontSizeDirty = false;
    fontDirty = false;
    xDirty = false;
    yDirty = false;
  }

  @Override
  public TextSprite copy() {
    return new TextSprite(this);
  }

  /**
   * Returns the font of the text.
   * 
   * @return the font of the text
   */
  public String getFont() {
    return font;
  }

  /**
   * Returns the font size of the text.
   * 
   * @return the font size of the text
   */
  public int getFontSize() {
    return fontSize;
  }

  /**
   * Returns the {@link FontStyle} of the text.
   * 
   * @return the font style of the text
   */
  public FontStyle getFontStyle() {
    return fontStyle;
  }

  /**
   * Returns the {@link FontWeight} of the text.
   * 
   * @return the font weight of the text
   */
  public FontWeight getFontWeight() {
    return fontWeight;
  }

  @Override
  public PathSprite getPathSprite() {
    return new PathSprite(this);
  }

  /**
   * Returns the content of the text.
   * 
   * @return the content of the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the text anchor.
   * 
   * @return the text anchor
   */
  public TextAnchor getTextAnchor() {
    return textAnchor;
  }

  /**
   * Returns the text baseline.
   * 
   * @return the text baseline
   */
  public TextBaseline getTextBaseline() {
    return textBaseline;
  }

  /**
   * Returns the x-coordinate of the text.
   * 
   * @return the x-coordinate of the text
   */
  public double getX() {
    return x;
  }

  /**
   * Returns the y-coordinate of the text.
   * 
   * @return the y-coordinate of the text
   */
  public double getY() {
    return y;
  }

  @Override
  public boolean isDirty() {
    return super.isDirty() || textDirty || fontStyleDirty || fontWeightDirty || textAnchorDirty || textBaselineDirty
        || fontSizeDirty || fontDirty || xDirty || yDirty;
  }

  /**
   * Returns true if the font changed since the last render.
   * 
   * @return true if the font changed since the last render
   */
  public boolean isFontDirty() {
    return fontDirty;
  }

  /**
   * Returns true if the font size changed since the last render.
   * 
   * @return true if the font size changed since the last render
   */
  public boolean isFontSizeDirty() {
    return fontSizeDirty;
  }

  /**
   * Returns true if the font style changed since the last render.
   * 
   * @return true if the font style changed since the last render
   */
  public boolean isFontStyleDirty() {
    return fontStyleDirty;
  }

  /**
   * Returns true if the font weight changed since the last render.
   * 
   * @return true if the font weight changed since the last render
   */
  public boolean isFontWeightDirty() {
    return fontWeightDirty;
  }

  /**
   * Returns true if the text anchor changed since the last render.
   * 
   * @return true if the text anchor changed since the last render
   */
  public boolean isTextAnchorDirty() {
    return textAnchorDirty;
  }

  public boolean isTextBaselineDirty() {
    return textBaselineDirty;
  }

  /**
   * Returns true if the text changed since the last render.
   * 
   * @return true if the text changed since the last render
   */
  public boolean isTextDirty() {
    return textDirty;
  }

  /**
   * Returns true if the x changed since the last render.
   * 
   * @return true if the x changed since the last render
   */
  public boolean isXDirty() {
    return xDirty;
  }

  /**
   * Returns true if the y changed since the last render.
   * 
   * @return true if the y changed since the last render
   */
  public boolean isYDirty() {
    return yDirty;
  }

  /**
   * Sets the font of the text.
   * 
   * @param font the font of the text
   */
  public void setFont(String font) {
    if (!Util.equalWithNull(this.font, font)) {
      this.font = font;
      fontDirty = true;
    }
  }

  /**
   * Sets the font size of the text.
   * 
   * @param fontSize the font size of the text
   */
  public void setFontSize(int fontSize) {
    if (this.fontSize != fontSize) {
      this.fontSize = fontSize;
      fontSizeDirty = true;
    }
  }

  /**
   * Sets the {@link FontStyle} of the text.
   * 
   * @param fontStyle the font style of the text
   */
  public void setFontStyle(FontStyle fontStyle) {
    if (this.fontStyle != fontStyle) {
      this.fontStyle = fontStyle;
      fontStyleDirty = true;
    }
  }

  /**
   * Sets the {@link FontWeight} of the text.
   * 
   * @param fontWeight the font weight of the text
   */
  public void setFontWeight(FontWeight fontWeight) {
    if (this.fontWeight != fontWeight) {
      this.fontWeight = fontWeight;
      fontWeightDirty = true;
    }
  }

  /**
   * Sets the content of the text.
   * 
   * @param text the content of the text
   */
  public void setText(String text) {
    if (!Util.equalWithNull(this.text, text)) {
      this.text = text;
      textDirty = true;
    }
  }

  /**
   * Sets the {@link TextAnchor} of the text. Defaults to {@code START}.
   * 
   * @param textAnchor the anchor of the text
   */
  public void setTextAnchor(TextAnchor textAnchor) {
    if (this.textAnchor != textAnchor) {
      this.textAnchor = textAnchor;
      textAnchorDirty = true;
    }
  }

  /**
   * Sets the {@link TextBaseline} of the text. Defaults to {@code TOP}.
   * 
   * @param textBaseline the baseline of the text
   */
  public void setTextBaseline(TextBaseline textBaseline) {
    if (this.textBaseline != textBaseline) {
      this.textBaseline = textBaseline;
      textBaselineDirty = true;
    }
  }

  /**
   * Sets the x-coordinate of the text.
   * 
   * @param x the x-coordinate of the text
   */
  public void setX(double x) {
    if (Double.compare(this.x, x) != 0) {
      this.x = x;
      xDirty = true;
    }
  }

  /**
   * Sets the y-coordinate of the text.
   * 
   * @param y the y-coordinate of the text
   */
  public void setY(double y) {
    if (Double.compare(this.y, y) != 0) {
      this.y = y;
      yDirty = true;
    }
  }

  @Override
  public String toString() {
    return text;
  }

  @Override
  public void update(Sprite sprite) {
    super.update(sprite);
    if (sprite instanceof TextSprite) {
      TextSprite text = (TextSprite) sprite;
      setText(text.text);
      setFontStyle(text.fontStyle);
      setFontWeight(text.fontWeight);
      setTextAnchor(text.textAnchor);
      setTextBaseline(text.textBaseline);
      setFontSize(text.fontSize);
      setFont(text.font);
      setX(text.x);
      setY(text.y);
    }
  }

}
