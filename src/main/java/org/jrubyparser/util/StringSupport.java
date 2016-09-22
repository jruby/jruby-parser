/***** BEGIN LICENSE BLOCK *****
 * Version: EPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Eclipse Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/epl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jrubyparser.util;

import org.jcodings.Encoding;
import org.jcodings.ascii.AsciiTables;
import org.jcodings.specific.ASCIIEncoding;
import org.jruby.util.ByteList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringSupport {
    public static final int CR_UNKNOWN   = 0;
    // We hardcode these so they can be used in a switch below. The assert verifies they match FlagRegistry's value.
    public static final int CR_7BIT      = 16;
    public static final int CR_VALID     = 32;

    public static final int CR_BROKEN    = CR_7BIT | CR_VALID;

    /**
     * Split string into sub-parts.
     * @param str the string
     * @param sep the separator
     * @see String#split(String)
     *
     * @note We differ from the non-limited {@link String#split(String)} in handling consecutive separator chars at the
     * end of string. While <code>"1;;"split(";")<code/> returns `[ "1" ]` this version returns `[ "1", "" ]` which is
     * consistent when consecutive separators occur elsewhere.
     */
    public static List<String> split(final String str, final char sep) {
        return split(str, sep, 0);
    }

    /**
     * Split string into (limited) sub-parts.
     * @param str the string
     * @param sep the separator
     * @param lim has same effect as with {@link String#split(String, int)}
     */
    public static List<String> split(final String str, final char sep, final int lim) {
        final int len = str.length();
        if ( len == 0 ) return Collections.singletonList(str);

        final ArrayList<String> result = new ArrayList<>(lim <= 0 ? 8 : lim);

        int e; int s = 0; int count = 0;
        while ( (e = str.indexOf(sep, s)) != -1 ) {
            if ( lim == ++count ) { // limited (lim > 0) case
                result.add(str.substring(s));
                return result;
            }
            result.add(str.substring(s, e));
            s = e + 1;
        }
        if ( s < len || ( s == len && lim > 0 ) ) result.add(str.substring(s));

        return result;
    }

    // rb_enc_fast_mbclen
    public static int encFastMBCLen(byte[] bytes, int p, int e, Encoding enc) {
        return enc.length(bytes, p, e);
    }

    // rb_enc_mbclen
    public static int length(Encoding enc, byte[]bytes, int p, int end) {
        int n = enc.length(bytes, p, end);
        if (MBCLEN_CHARFOUND_P(n) && MBCLEN_CHARFOUND_LEN(n) <= end - p) return MBCLEN_CHARFOUND_LEN(n);
        int min = enc.minLength();
        return min <= end - p ? min : end - p;
    }

    // rb_enc_precise_mbclen
    public static int preciseLength(Encoding enc, byte[]bytes, int p, int end) {
        if (p >= end) return -1 - (1);
        int n = enc.length(bytes, p, end);
        if (n > end - p) return MBCLEN_NEEDMORE(n - (end - p));
        return n;
    }

    // MBCLEN_NEEDMORE_P, ONIGENC_MBCLEN_NEEDMORE_P
    public static boolean MBCLEN_NEEDMORE_P(int r) {
        return r < -1;
    }

    // MBCLEN_NEEDMORE, ONIGENC_MBCLEN_NEEDMORE
    public static int MBCLEN_NEEDMORE(int n) {
        return -1 - n;
    }

    // MBCLEN_NEEDMORE_LEN, ONIGENC_MBCLEN_NEEDMORE_LEN
    public static int MBCLEN_NEEDMORE_LEN(int r) {
        return -1 - r;
    }

    // MBCLEN_INVALID_P, ONIGENC_MBCLEN_INVALID_P
    public static boolean MBCLEN_INVALID_P(int r) {
        return r == -1;
    }

    // MBCLEN_CHARFOUND_LEN, ONIGENC_MBCLEN_CHARFOUND_LEN
    public static int MBCLEN_CHARFOUND_LEN(int r) {
        return r;
    }

    // MBCLEN_CHARFOUND_P, ONIGENC_MBCLEN_CHARFOUND_P
    public static boolean MBCLEN_CHARFOUND_P(int r) {
        return 0 < r;
    }

    // CONSTRUCT_MBCLEN_CHARFOUND, ONIGENC_CONSTRUCT_MBCLEN_CHARFOUND
    public static int CONSTRUCT_MBCLEN_CHARFOUND(int n) {
        return n;
    }

    // MRI: search_nonascii
    public static int searchNonAscii(byte[]bytes, int p, int end) {
        while (p < end) {
            if (!Encoding.isAscii(bytes[p])) return p;
            p++;
        }
        return -1;
    }

    public static int searchNonAscii(ByteList bytes) {
        return searchNonAscii(bytes.getUnsafeBytes(), bytes.getBegin(), bytes.getBegin() + bytes.getRealSize());
    }

    public static int codeRangeScan(Encoding enc, byte[]bytes, int p, int len) {
        if (enc == ASCIIEncoding.INSTANCE) {
            return searchNonAscii(bytes, p, p + len) != -1 ? CR_VALID : CR_7BIT;
        }
        if (enc.isAsciiCompatible()) {
            return codeRangeScanAsciiCompatible(enc, bytes, p, len);
        }
        return codeRangeScanNonAsciiCompatible(enc, bytes, p, len);
    }

    private static int codeRangeScanAsciiCompatible(Encoding enc, byte[]bytes, int p, int len) {
        int end = p + len;
        p = searchNonAscii(bytes, p, end);
        if (p == -1) return CR_7BIT;

        while (p < end) {
            int cl = preciseLength(enc, bytes, p, end);
            if (cl <= 0) return CR_BROKEN;
            p += cl;
            if (p < end) {
                p = searchNonAscii(bytes, p, end);
                if (p == -1) return CR_VALID;
            }
        }
        return p > end ? CR_BROKEN : CR_VALID;
    }

    private static int codeRangeScanNonAsciiCompatible(Encoding enc, byte[]bytes, int p, int len) {
        int end = p + len;
        while (p < end) {
            int cl = preciseLength(enc, bytes, p, end);
            if (cl <= 0) return CR_BROKEN;
            p += cl;
        }
        return p > end ? CR_BROKEN : CR_VALID;
    }

    public static int codeRangeScan(Encoding enc, ByteList bytes) {
        return codeRangeScan(enc, bytes.getUnsafeBytes(), bytes.getBegin(), bytes.getRealSize());
    }

    private static final long NONASCII_MASK = 0x8080808080808080L;
    private static int countUtf8LeadBytes(long d) {
        d |= ~(d >>> 1);
        d >>>= 6;
        d &= NONASCII_MASK >>> 7;
        d += (d >>> 8);
        d += (d >>> 16);
        d += (d >>> 32);
        return (int)(d & 0xf);
    }

    // MRI: enc_strlen
    public static int strLength(Encoding enc, byte[]bytes, int p, int e, int cr) {
        int c;
        if (enc.isFixedWidth()) {
            return (e - p + enc.minLength() - 1) / enc.minLength();
        } else if (enc.isAsciiCompatible()) {
            c = 0;
            if (cr == CR_7BIT || cr == CR_VALID) {
                while (p < e) {
                    if (Encoding.isAscii(bytes[p])) {
                        int q = searchNonAscii(bytes, p, e);
                        if (q == -1) return c + (e - p);
                        c += q - p;
                        p = q;
                    }
                    p += encFastMBCLen(bytes, p, e, enc);
                    c++;
                }
            } else {
                while (p < e) {
                    if (Encoding.isAscii(bytes[p])) {
                        int q = searchNonAscii(bytes, p, e);
                        if (q == -1) return c + (e - p);
                        c += q - p;
                        p = q;
                    }
                    p += length(enc, bytes, p, e);
                    c++;
                }
            }
            return c;
        }

        for (c = 0; p < e; c++) {
            p += length(enc, bytes, p, e);
        }
        return c;
    }

    public static long strLengthWithCodeRange(Encoding enc, byte[]bytes, int p, int end) {
        if (enc.isFixedWidth()) {
            return (end - p + enc.minLength() - 1) / enc.minLength();
        } else if (enc.isAsciiCompatible()) {
            return strLengthWithCodeRangeAsciiCompatible(enc, bytes, p, end);
        } else {
            return strLengthWithCodeRangeNonAsciiCompatible(enc, bytes, p, end);
        }
    }

    public static long strLengthWithCodeRangeAsciiCompatible(Encoding enc, byte[]bytes, int p, int end) {
        int cr = 0, c = 0;
        while (p < end) {
            if (Encoding.isAscii(bytes[p])) {
                int q = searchNonAscii(bytes, p, end);
                if (q == -1) return pack(c + (end - p), cr == 0 ? CR_7BIT : cr);
                c += q - p;
                p = q;
            }
            int cl = preciseLength(enc, bytes, p, end);
            if (cl > 0) {
                cr |= CR_VALID;
                p += cl;
            } else {
                cr = CR_BROKEN;
                p++;
            }
            c++;
        }
        return pack(c, cr == 0 ? CR_7BIT : cr);
    }

    public static long strLengthWithCodeRangeNonAsciiCompatible(Encoding enc, byte[]bytes, int p, int end) {
        int cr = 0, c = 0;
        for (c = 0; p < end; c++) {
            int cl = preciseLength(enc, bytes, p, end);
            if (cl > 0) {
                cr |= CR_VALID;
                p += cl;
            } else {
                cr = CR_BROKEN;
                p++;
            }
        }
        return pack(c, cr == 0 ? CR_7BIT : cr);
    }

    public static long strLengthWithCodeRange(ByteList bytes) {
        return strLengthWithCodeRange(bytes.getEncoding(), bytes.getUnsafeBytes(), bytes.getBegin(), bytes.getBegin() + bytes.getRealSize());
    }

    public static long strLengthWithCodeRange(ByteList bytes, Encoding enc) {
        return strLengthWithCodeRange(enc, bytes.getUnsafeBytes(), bytes.getBegin(), bytes.getBegin() + bytes.getRealSize());
    }

    // arg cannot be negative
    public static long pack(int result, int arg) {
        return ((long)arg << 31) | result;
    }

    public static int unpackResult(long len) {
        return (int)len & 0x7fffffff;
    }

    public static int unpackArg(long cr) {
        return (int)(cr >>> 31);
    }

    public static int codePoint(Encoding enc, byte[] bytes, int p, int end) {
        if (p >= end) throw new IllegalArgumentException("empty string");
        int cl = preciseLength(enc, bytes, p, end);
        if (cl <= 0) throw new IllegalArgumentException("invalid byte sequence in " + enc);
        return enc.mbcToCode(bytes, p, end);
    }

    public static int codeLength(Encoding enc, int c) {
        return enc.codeToMbcLength(c);
    }

    public static long getAscii(Encoding enc, byte[]bytes, int p, int end) {
        return getAscii(enc, bytes, p, end, 0);
    }

    public static long getAscii(Encoding enc, byte[]bytes, int p, int end, int len) {
        if (p >= end) return pack(-1, len);

        if (enc.isAsciiCompatible()) {
            int c = bytes[p] & 0xff;
            if (!Encoding.isAscii(c)) return pack(-1, len);
            return pack(c, len == 0 ? 0 : 1);
        } else {
            int cl = preciseLength(enc, bytes, p, end);
            if (cl <= 0) return pack(-1, len);
            int c = enc.mbcToCode(bytes, p, end);
            if (!Encoding.isAscii(c)) return pack(-1, len);
            return pack(c, len == 0 ? 0 : cl);
        }
    }

    public static int preciseCodePoint(Encoding enc, byte[]bytes, int p, int end) {
        int l = preciseLength(enc, bytes, p, end);
        if (l > 0) return enc.mbcToCode(bytes, p, end);
        return -1;
    }

    @SuppressWarnings("deprecation")
    public static int utf8Nth(byte[]bytes, int p, int e, int nth) {
        // FIXME: Missing our UNSAFE impl because it was doing the wrong thing: See GH #1986
        while (p < e) {
            if ((bytes[p] & 0xc0 /*utf8 lead byte*/) != 0x80) {
                if (nth == 0) break;
                nth--;
            }
            p++;
        }
        return p;
    }

    public static int nth(Encoding enc, byte[]bytes, int p, int end, int n) {
        return nth(enc, bytes, p, end, n, enc.isSingleByte());
    }

    /**
     * Get the position of the nth character in the given byte array, using the given encoding and range.
     *
     * @param enc encoding to use
     * @param bytes bytes to scan
     * @param p starting byte offset
     * @param end ending byte offset
     * @param n index of character for which to find byte offset
     * @param singlebyte whether the byte contents are in a single byte encoding
     * @return the offset of the nth character in the string, or -1 if nth is out of the string
     */
    public static int nth(Encoding enc, byte[]bytes, int p, int end, int n, boolean singlebyte) {
        if (singlebyte) {
            p += n;
        } else if (enc.isFixedWidth()) {
            p += n * enc.maxLength();
        } else if (enc.isAsciiCompatible()) {
            p = nthAsciiCompatible(enc, bytes, p, end, n);
        } else {
            p = nthNonAsciiCompatible(enc, bytes, p, end, n);
        }
        if (p < 0) return -1;
        return p > end ? end : p;
    }

    private static int nthAsciiCompatible(Encoding enc, byte[]bytes, int p, int end, int n) {
        while (p < end && n > 0) {
            int end2 = p + n;
            if (end < end2) return end;
            if (Encoding.isAscii(bytes[p])) {
                int p2 = searchNonAscii(bytes, p, end2);
                if (p2 == -1) return end2;
                n -= p2 - p;
                p = p2;
            }
            int cl = length(enc, bytes, p, end);
            p += cl;
            n--;
        }
        return n != 0 ? end : p;
    }

    private static int nthNonAsciiCompatible(Encoding enc, byte[]bytes, int p, int end, int n) {
        while (p < end && n-- != 0) {
            p += length(enc, bytes, p, end);
        }
        return p;
    }

    public static int utf8Offset(byte[]bytes, int p, int end, int n) {
        int pp = utf8Nth(bytes, p, end, n);
        return pp == -1 ? end - p : pp - p;
    }

    public static int offset(Encoding enc, byte[]bytes, int p, int end, int n) {
        int pp = nth(enc, bytes, p, end, n);
        return pp == -1 ? end - p : pp - p;
    }

    public static int offset(Encoding enc, byte[]bytes, int p, int end, int n, boolean singlebyte) {
        int pp = nth(enc, bytes, p, end, n, singlebyte);
        return pp == -1 ? end - p : pp - p;
    }

    public static int toLower(Encoding enc, int c) {
        return Encoding.isAscii(c) ? AsciiTables.ToLowerCaseTable[c] : c;
    }

    public static int toUpper(Encoding enc, int c) {
        return Encoding.isAscii(c) ? AsciiTables.ToUpperCaseTable[c] : c;
    }

    public static int caseCmp(byte[]bytes1, int p1, byte[]bytes2, int p2, int len) {
        int i = -1;
        for (; ++i < len && bytes1[p1 + i] == bytes2[p2 + i];) {}
        if (i < len) return (bytes1[p1 + i] & 0xff) > (bytes2[p2 + i] & 0xff) ? 1 : -1;
        return 0;
    }

    public static int scanHex(byte[]bytes, int p, int len) {
        return scanHex(bytes, p, len, ASCIIEncoding.INSTANCE);
    }

    public static int scanHex(byte[]bytes, int p, int len, Encoding enc) {
        int v = 0;
        int c;
        while (len-- > 0 && enc.isXDigit(c = bytes[p++] & 0xff)) {
            v = (v << 4) + enc.xdigitVal(c);
        }
        return v;
    }

    public static int hexLength(byte[]bytes, int p, int len) {
        return hexLength(bytes, p, len, ASCIIEncoding.INSTANCE);
    }

    public static int hexLength(byte[]bytes, int p, int len, Encoding enc) {
        int hlen = 0;
        while (len-- > 0 && enc.isXDigit(bytes[p++] & 0xff)) hlen++;
        return hlen;
    }

    public static int scanOct(byte[]bytes, int p, int len) {
        return scanOct(bytes, p, len, ASCIIEncoding.INSTANCE);
    }

    public static int scanOct(byte[]bytes, int p, int len, Encoding enc) {
        int v = 0;
        int c;
        while (len-- > 0 && enc.isDigit(c = bytes[p++] & 0xff) && c < '8') {
            v = (v << 3) + Encoding.digitVal(c);
        }
        return v;
    }

    public static int octLength(byte[]bytes, int p, int len) {
        return octLength(bytes, p, len, ASCIIEncoding.INSTANCE);
    }

    public static int octLength(byte[]bytes, int p, int len, Encoding enc) {
        int olen = 0;
        int c;
        while (len-- > 0 && enc.isDigit(c = bytes[p++] & 0xff) && c < '8') olen++;
        return olen;
    }

    public static int bytesToFixBrokenTrailingCharacter(byte[] bytes, int begin, int byteSize, Encoding encoding, int usingLength) {
        // read additional bytes to fix broken char
        if (byteSize > 0) {
            // get head offset of broken character
            int charHead = encoding.leftAdjustCharHead(
                    bytes, // string bytes
                    begin, // start of string
                    begin + usingLength - 1, // last byte
                    begin + usingLength); // end of using

            // external offset
            charHead -= begin;

            // byte at char head
            byte byteHead = (byte)(bytes[begin + charHead] & 0xFF);

            // total bytes we would need to complete character
            int extra = encoding.length(byteHead);

            // what we already have
            extra -= usingLength - charHead;

            return extra;
        }

        return 0;
    }

    public static int memchr(byte[] ptr, int start, final int find, int len) {
        for (int i = start; i < start + len; i++) {
            if ( ptr[i] == find ) return i;
        }
        return -1;
    }

    // MRI: str_null_char
    private static int strNullChar(byte[] sBytes, int s, int len, final int minlen, Encoding enc) {
        int e = s + len;

        for (; s + minlen <= e; s += enc.length(sBytes, s, e)) {
            if (zeroFilled(sBytes, s, minlen)) return s;
        }
        return -1;
    }

    // MRI: zero_filled
    private static boolean zeroFilled(byte[] sBytes, int s, int n) {
        for (; n > 0; --n) {
            if (sBytes[s++] != 0) return false;
        }
        return true;
    }

    private static void TERM_FILL(byte[] ptr, final int beg, final int len, final int termlen) {
        final int p = beg + len; Arrays.fill(ptr, p, p + termlen, (byte) '\0');
    }

    public static boolean isEVStr(byte[] bytes, int p, int end) {
        return p < end ? isEVStr(bytes[p] & 0xff) : false;
    }

    public static boolean isEVStr(int c) {
        return c == '$' || c == '@' || c == '{';
    }

    private static int strRindex(ByteList str, ByteList sub, int s, int pos, Encoding enc) {
        int slen;
        byte[] strBytes = str.unsafeBytes();
        byte[] subBytes = sub.unsafeBytes();
        int sbeg, e, t;

        sbeg = str.begin();
        e = str.begin() + str.realSize();
        t = sub.begin();
        slen = sub.realSize();

        while (s >= sbeg && s + slen <= sbeg + str.realSize()) {
            if (ByteList.memcmp(strBytes, s, subBytes, t, slen) == 0) {
                return pos;
            }
            if (pos == 0) break;
            pos--;
            s = enc.prevCharHead(strBytes, sbeg, s, e);
        }

        return -1;
    }

    /**
     * rb_str_tr / rb_str_tr_bang
     */
    public static final class TR {
        public TR(ByteList bytes) {
            p = bytes.getBegin();
            pend = bytes.getRealSize() + p;
            buf = bytes.getUnsafeBytes();
            now = max = 0;
            gen = false;
        }

        final byte[] buf;
        int p, pend, now, max;
        boolean gen;
    }

    public static enum NeighborChar {NOT_CHAR, FOUND, WRAPPED}
}
