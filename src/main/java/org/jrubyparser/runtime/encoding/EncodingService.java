package org.jrubyparser.runtime.encoding;

import org.jcodings.Encoding;
import org.jcodings.EncodingDB;
import org.jcodings.EncodingDB.Entry;
import org.jcodings.util.CaseInsensitiveBytesHash;
import org.jruby.util.ByteList;
import org.jrubyparser.RubyEncoding;

public final class EncodingService {
    private final CaseInsensitiveBytesHash<Entry> encodings;
    private final CaseInsensitiveBytesHash<Entry> aliases;

    // for fast lookup: encoding entry => org.jruby.RubyEncoding
    public final RubyEncoding[] encodingList;
    // for fast lookup: org.joni.encoding.Encoding => org.jruby.RubyEncoding
    private RubyEncoding[] encodingIndex = new RubyEncoding[4];

    public EncodingService() {
        encodings = EncodingDB.getEncodings();
        aliases = EncodingDB.getAliases();
        encodingList = new RubyEncoding[encodings.size()];
    }

    public Entry findEncodingEntry(ByteList bytes) {
        return encodings.get(bytes.getUnsafeBytes(), bytes.getBegin(), bytes.getBegin() + bytes.getRealSize());
    }

    public Entry findAliasEntry(ByteList bytes) {
        return aliases.get(bytes.getUnsafeBytes(), bytes.getBegin(), bytes.getBegin() + bytes.getRealSize());
    }

    public Entry findEncodingOrAliasEntry(ByteList bytes) {
        Entry e = findEncodingEntry(bytes);
        return e != null ? e : findAliasEntry(bytes);
    }

    public Encoding loadEncoding(ByteList name) {
        Entry entry = findEncodingOrAliasEntry(name);
        if (entry == null) return null;
        Encoding enc = entry.getEncoding(); // load the encoding
        int index = enc.getIndex();
        if (index >= encodingIndex.length) {
            RubyEncoding tmp[] = new RubyEncoding[index + 4];
            System.arraycopy(encodingIndex, 0, tmp, 0, encodingIndex.length);
            encodingIndex = tmp;
        }
        encodingIndex[index] = encodingList[entry.getIndex()];
        return enc;
    }

    public RubyEncoding getEncoding(Encoding enc) {
        int index = enc.getIndex();
        RubyEncoding rubyEncoding;
        if (index < encodingIndex.length && (rubyEncoding = encodingIndex[index]) != null) {
            return rubyEncoding;
        }

        enc = loadEncoding(new ByteList(enc.getName(), false));
        return encodingIndex[enc.getIndex()];
    }
}