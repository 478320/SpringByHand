/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;


/**
 * 概念展示，String工具类
 */
public abstract class StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String FOLDER_SEPARATOR = "/";

    private static final char FOLDER_SEPARATOR_CHAR = '/';

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    private static final int DEFAULT_TRUNCATION_THRESHOLD = 100;

    private static final String TRUNCATION_SUFFIX = " (truncated)...";


    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------


    @Deprecated
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor
     * of length 0.
     * <p>Note: this method returns {@code true} for a {@code CharSequence}
     * that purely consists of whitespace.
     * <p><pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     * @see #hasLength(String)
     * @see #hasText(CharSequence)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>Note: this method returns {@code true} for a {@code String} that
     * purely consists of whitespace.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has length
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than
     * 0, and it contains at least one non-whitespace character.
     * <p><pre class="code">
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},
     * its length is greater than 0, and it does not contain whitespace only
     * @see #hasText(String)
     * @see #hasLength(CharSequence)
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code String} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its
     * length is greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     * @see #hasLength(String)
     * @see Character#isWhitespace
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code CharSequence} contains any whitespace characters.
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not empty and
     * contains at least 1 whitespace character
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code String} contains any whitespace characters.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not empty and
     * contains at least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * Trim leading and trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIndex = 0;
        int endIndex = str.length() - 1;

        while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
            beginIndex++;
        }

        while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIndex, endIndex + 1);
    }

    /**
     * Trim <em>all</em> whitespace from the given {@code CharSequence}:
     * leading, trailing, and in between characters.
     *
     * @param str the {@code CharSequence} to check
     * @return the trimmed {@code CharSequence}
     * @see #trimAllWhitespace(String)
     * @see java.lang.Character#isWhitespace
     * @since 5.3.22
     */
    public static CharSequence trimAllWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb;
    }

    /**
     * Trim <em>all</em> whitespace from the given {@code String}:
     * leading, trailing, and in between characters.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see #trimAllWhitespace(CharSequence)
     * @see java.lang.Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        return trimAllWhitespace((CharSequence) str).toString();
    }

    /**
     * Trim leading whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIdx = 0;
        while (beginIdx < str.length() && Character.isWhitespace(str.charAt(beginIdx))) {
            beginIdx++;
        }
        return str.substring(beginIdx);
    }

    /**
     * Trim trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int endIdx = str.length() - 1;
        while (endIdx >= 0 && Character.isWhitespace(str.charAt(endIdx))) {
            endIdx--;
        }
        return str.substring(0, endIdx + 1);
    }

    /**
     * Trim all occurrences of the supplied leading character from the given {@code String}.
     *
     * @param str              the {@code String} to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIdx = 0;
        while (beginIdx < str.length() && leadingCharacter == str.charAt(beginIdx)) {
            beginIdx++;
        }
        return str.substring(beginIdx);
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given {@code String}.
     *
     * @param str               the {@code String} to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        int endIdx = str.length() - 1;
        while (endIdx >= 0 && trailingCharacter == str.charAt(endIdx)) {
            endIdx--;
        }
        return str.substring(0, endIdx + 1);
    }

    /**
     * Test if the given {@code String} matches the given single character.
     *
     * @param str             the {@code String} to check
     * @param singleCharacter the character to compare to
     * @since 5.2.9
     */
    public static boolean matchesCharacter(String str, char singleCharacter) {
        return (str != null && str.length() == 1 && str.charAt(0) == singleCharacter);
    }

    /**
     * Test if the given {@code String} starts with the specified prefix,
     * ignoring upper/lower case.
     *
     * @param str    the {@code String} to check
     * @param prefix the prefix to look for
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length() &&
                str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    /**
     * Test if the given {@code String} ends with the specified suffix,
     * ignoring upper/lower case.
     *
     * @param str    the {@code String} to check
     * @param suffix the suffix to look for
     * @see java.lang.String#endsWith
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return (str != null && suffix != null && str.length() >= suffix.length() &&
                str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
    }

    /**
     * Test whether the given string matches the given substring
     * at the given index.
     *
     * @param str       the original string (or StringBuilder)
     * @param index     the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        if (index + substring.length() > str.length()) {
            return false;
        }
        for (int i = 0; i < substring.length(); i++) {
            if (str.charAt(index + i) != substring.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Count the occurrences of the substring {@code sub} in string {@code str}.
     *
     * @param str string to search in
     * @param sub string to search for
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (!hasLength(str) || !hasLength(sub)) {
            return 0;
        }

        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     *
     * @param inString   {@code String} to examine
     * @param oldPattern {@code String} to replace
     * @param newPattern {@code String} to insert
     * @return a {@code String} with the replacements
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0;  // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original {@code String}
     * @param pattern  the pattern to delete all occurrences of
     * @return the resulting {@code String}
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any character in a given {@code String}.
     *
     * @param inString      the original {@code String}
     * @param charsToDelete a set of characters to delete.
     *                      E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting {@code String}
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        int lastCharIndex = 0;
        char[] result = new char[inString.length()];
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                result[lastCharIndex++] = c;
            }
        }
        if (lastCharIndex == inString.length()) {
            return inString;
        }
        return new String(result, 0, lastCharIndex);
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------

    /**
     * Quote the given {@code String} with single quotes.
     *
     * @param str the input {@code String} (e.g. "myString")
     * @return the quoted {@code String} (e.g. "'myString'"),
     * or {@code null} if the input was {@code null}
     */

    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a {@code String} with single quotes
     * if it is a {@code String}; keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted {@code String} (e.g. "'myString'"),
     * or the input object as-is if not a {@code String}
     */

    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Unqualify a string qualified by a '.' dot character. For example,
     * "this.name.is.qualified", returns "qualified".
     *
     * @param qualifiedName the qualified name
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character. For example,
     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
     *
     * @param qualifiedName the qualified name
     * @param separator     the separator
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize a {@code String}, changing the first letter to
     * upper case as per {@link Character#toUpperCase(char)}.
     * No other letters are changed.
     *
     * @param str the {@code String} to capitalize
     * @return the capitalized {@code String}
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * Uncapitalize a {@code String}, changing the first letter to
     * lower case as per {@link Character#toLowerCase(char)}.
     * No other letters are changed.
     *
     * @param str the {@code String} to uncapitalize
     * @return the uncapitalized {@code String}
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (!hasLength(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        } else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars);
    }

    /**
     * Extract the filename from the given Java resource path,
     * e.g. {@code "mypath/myfile.txt" &rarr; "myfile.txt"}.
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename, or {@code null} if none
     */

    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * Extract the filename extension from the given Java resource path,
     * e.g. "mypath/myfile.txt" &rarr; "txt".
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename extension, or {@code null} if none
     */

    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    /**
     * Strip the filename extension from the given Java resource path,
     * e.g. "mypath/myfile.txt" &rarr; "mypath/myfile".
     *
     * @param path the file path
     * @return the path with stripped filename extension
     */
    public static String stripFilenameExtension(String path) {
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
        if (folderIndex > extIndex) {
            return path;
        }

        return path.substring(0, extIndex);
    }

    /**
     * Apply the given relative path to the given Java resource path,
     * assuming standard Java folder separation (i.e. "/" separators).
     *
     * @param path         the path to start from (usually a full file path)
     * @param relativePath the relative path to apply
     *                     (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR_CHAR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }


    private static Locale parseLocaleTokens(String localeString, String[] tokens) {
        String language = (tokens.length > 0 ? tokens[0] : "");
        String country = (tokens.length > 1 ? tokens[1] : "");
        validateLocalePart(language);
        validateLocalePart(country);

        String variant = "";
        if (tokens.length > 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }

        if (variant.isEmpty() && country.startsWith("#")) {
            variant = country;
            country = "";
        }

        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                        "Locale part \"" + localePart + "\" contains invalid characters");
            }
        }
    }

    /**
     * Determine the RFC 3066 compliant language tag,
     * as used for the HTTP "Accept-Language" header.
     *
     * @param locale the Locale to transform to a language tag
     * @return the RFC 3066 compliant language tag as {@code String}
     * @deprecated as of 5.0.4, in favor of {@link Locale#toLanguageTag()}
     */
    @Deprecated
    public static String toLanguageTag(Locale locale) {
        return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    /**
     * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
     *
     * @param timeZoneString the time zone {@code String}, following {@link TimeZone#getTimeZone(String)}
     *                       but throwing {@link IllegalArgumentException} in case of an invalid time zone specification
     * @return a corresponding {@link TimeZone} instance
     * @throws IllegalArgumentException in case of an invalid time zone specification
     */
    public static TimeZone parseTimeZoneString(String timeZoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
            // We don't want that GMT fallback...
            throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
        }
        return timeZone;
    }


    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------


    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }

        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[]{beforeDelimiter, afterDelimiter};
    }


    /**
     * Truncate the supplied {@link CharSequence}.
     * <p>Delegates to {@link #truncate(CharSequence, int)}, supplying {@code 100}
     * as the threshold.
     *
     * @param charSequence the {@code CharSequence} to truncate
     * @return a truncated string, or a string representation of the original
     * {@code CharSequence} if its length does not exceed the threshold
     * @since 5.3.27
     */
    public static String truncate(CharSequence charSequence) {
        return truncate(charSequence, DEFAULT_TRUNCATION_THRESHOLD);
    }

    /**
     * Truncate the supplied {@link CharSequence}.
     * <p>If the length of the {@code CharSequence} is greater than the threshold,
     * this method returns a {@linkplain CharSequence#subSequence(int, int)
     * subsequence} of the {@code CharSequence} (up to the threshold) appended
     * with the suffix {@code " (truncated)..."}. Otherwise, this method returns
     * {@code charSequence.toString()}.
     *
     * @param charSequence the {@code CharSequence} to truncate
     * @param threshold    the maximum length after which to truncate; must be a
     *                     positive number
     * @return a truncated string, or a string representation of the original
     * {@code CharSequence} if its length does not exceed the threshold
     * @since 5.3.27
     */
    public static String truncate(CharSequence charSequence, int threshold) {

        if (charSequence.length() > threshold) {
            return charSequence.subSequence(0, threshold) + TRUNCATION_SUFFIX;
        }
        return charSequence.toString();
    }

}
