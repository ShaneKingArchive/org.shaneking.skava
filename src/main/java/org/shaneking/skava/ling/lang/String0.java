/*
 * @(#)String0.java		Created at 16/3/26
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.ling.lang;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;

public final class String0
{
  public static final String AMPERSAND           = "&";
  public static final String ANGLE               = "∠";
  public static final String APPROXIMATELY       = "≈";
  public static final String ARROW               = "→";
  public static final String ASTERISK            = "*";
  public static final String BACKSLASH           = "\\";
  public static final String BLACK               = " ";
  public static final String CELSIUS             = "℃";
  public static final String CIRCLE              = "⊙";
  public static final String CIRCUMFERENCE       = "○";
  public static final String CLOSE_BRACE         = "}";
  public static final String CLOSE_BRACKET       = "]";
  public static final String CLOSE_PARENTHESIS   = ")";
  public static final String COLON               = ":";
  public static final String COMMA               = ",";
  public static final String DASH                = "-";
  public static final String DEGREE              = "°";
  public static final String DIVIDE              = "÷";
  public static final String DOT                 = ".";
  public static final String DOUBLE_QUOTATION    = "\"";
  public static final String EQUAL               = "=";
  public static final String EQUAL_APPROXIMATELY = "≌";
  public static final String EQUIVALENT          = "≡";
  public static final String EXCLAMATION         = "!";
  public static final String HENCE               = "∴";
  public static final String INFINITY            = "∞";
  public static final String INTEGRAL            = "∫";
  public static final String INTERSECTION        = "∩";
  public static final String LESS                = "<";
  public static final String LESS_EQUAL          = "≤";
  public static final String MINUS               = "-";
  public static final String MINUTE              = "′";
  public static final String MULTIPLY            = "×";
  public static final String MORE                = ">";
  public static final String MORE_EQUAL          = "≥";
  public static final String NOT_EQUAL           = "≠";
  public static final String NOT_LESS            = "≮";
  public static final String NOT_MORE            = "≯";
  public static final String OPEN_BRACE          = "{";
  public static final String OPEN_BRACKET        = "[";
  public static final String OPEN_PARENTHESIS    = "(";
  public static final String PARALLEL            = "‖";
  public static final String PERCENT             = "%";
  public static final String PERMILL             = "‰";
  public static final String PERPENDICULAR       = "⊥";
  public static final String PI                  = "π";
  public static final String PLUS                = "+";
  public static final String PLUS_MINUS          = "±";
  public static final String POUND               = "#";
  public static final String PROPORTION          = "∷";
  public static final String QUESTION            = "?";
  public static final String SECOND              = "〃";
  public static final String SECTION             = "§";
  public static final String SEMICIRCLE          = "⌒";
  public static final String SEMICOLON           = ";";
  public static final String SIGMA               = "∑";
  public static final String SINCE               = "∵";
  public static final String SINGLE_QUOTATION    = "'";
  public static final String SLASH               = "/";
  public static final String SQUARE              = "√";
  public static final String TRIANGLE            = "△";
  public static final String UNDERLINE           = "_";
  public static final String UNDERLINE_DOUBLE    = "__";
  public static final String UNION               = "∪";
  public static final String VARIES              = "∝";
  public static final String VERTICAL            = "|";

  public static final String MALE   = "♂";
  public static final String FEMALE = "♀";

  public static String replaceUpperCase2UnderlineLowerCase(@Nonnull String hasUpperCaseString)
  {
    //jdk8 can't infer it
    Function<String, String> tmpFunc = (alphabet) -> alphabet.equals(alphabet.toUpperCase()) ? String0.UNDERLINE + alphabet.toLowerCase() : alphabet;
    return Joiner.on("").join(Lists.transform(Lists.newArrayList(hasUpperCaseString.split("")), tmpFunc));
  }
}
