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
package org.jrubyparser;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

public class RubyEncoding {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static String decodeUTF8(byte[] bytes, int start, int length) {
        return getUTF8Coder().decode(bytes, start, length);
    }

    private static class UTF8Coder {
        private final CharsetEncoder encoder = UTF8.newEncoder();
        private final CharsetDecoder decoder = UTF8.newDecoder();
        /** The maximum number of characters we can encode/decode in our cached buffers */
        private static final int CHAR_THRESHOLD = 1024;
        /** The resulting encode/decode buffer sized by the max number of
         * characters (using 4 bytes per char possible for utf-8) */
        private static final int BUF_SIZE = CHAR_THRESHOLD * 4;
        private final ByteBuffer byteBuffer = ByteBuffer.allocate(BUF_SIZE);
        private final CharBuffer charBuffer = CharBuffer.allocate(BUF_SIZE);

        UTF8Coder() {
            decoder.onMalformedInput(CodingErrorAction.REPLACE);
            decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        }

        public final byte[] encode(CharSequence cs) {
            ByteBuffer buffer;
            if (cs.length() > CHAR_THRESHOLD) {
                buffer = UTF8.encode(CharBuffer.wrap(cs));
            } else {
                buffer = byteBuffer;
                CharBuffer cbuffer = charBuffer;
                buffer.clear();
                cbuffer.clear();
                cbuffer.put(cs.toString());
                cbuffer.flip();
                encoder.encode(cbuffer, buffer, true);
                buffer.flip();
            }

            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            return bytes;
        }

        public final String decode(byte[] bytes, int start, int length) {
            CharBuffer cbuffer;
            if (length > CHAR_THRESHOLD) {
                cbuffer = UTF8.decode(ByteBuffer.wrap(bytes, start, length));
            } else {
                cbuffer = charBuffer;
                ByteBuffer buffer = byteBuffer;
                cbuffer.clear();
                buffer.clear();
                buffer.put(bytes, start, length);
                buffer.flip();
                decoder.decode(buffer, cbuffer, true);
                cbuffer.flip();
            }

            return cbuffer.toString();
        }

        public final String decode(byte[] bytes) {
            return decode(bytes, 0, bytes.length);
        }
    }

    /**
     * UTF8Coder wrapped in a SoftReference to avoid possible ClassLoader leak.
     * See JRUBY-6522
     */
    private static final ThreadLocal<SoftReference<UTF8Coder>> UTF8_CODER =
        new ThreadLocal<SoftReference<UTF8Coder>>();

    private static UTF8Coder getUTF8Coder() {
        UTF8Coder coder;
        SoftReference<UTF8Coder> ref = UTF8_CODER.get();
        if (ref == null || (coder = ref.get()) == null) {
            coder = new UTF8Coder();
            ref = new SoftReference<UTF8Coder>(coder);
            UTF8_CODER.set(ref);
        }

        return coder;
    }
}
