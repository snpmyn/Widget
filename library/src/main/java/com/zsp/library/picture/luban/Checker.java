package com.zsp.library.picture.luban;

import android.graphics.BitmapFactory;

import com.zsp.library.picture.luban.provider.InputStreamProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import timber.log.Timber;
import value.WidgetLibraryMagic;

/**
 * @decs: Checker
 * @author: 郑少鹏
 * @date: 2019/8/28 18:20
 */
enum Checker {
    /**
     * 单
     */
    SINGLE;
    private static final String JPG = ".jpg";
    private final byte[] JPEG_SIGNATURE = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

    /**
     * Determine if it is JPG.
     *
     * @param is image file input stream
     */
    boolean isJpg(InputStream is) {
        return isJpg(toByteArray(is));
    }

    /**
     * Returns the degrees in clockwise.
     * <p>
     * Values are 0, 90, 180, or 270.
     *
     * @param inputStream InputStream
     * @return the degrees in clockwise
     */
    int getOrientation(InputStream inputStream) {
        return getOrientation(toByteArray(inputStream));
    }

    private boolean isJpg(byte[] data) {
        if (data == null || data.length < WidgetLibraryMagic.INT_THREE) {
            return false;
        }
        byte[] bSignature = new byte[]{data[0], data[1], data[2]};
        return Arrays.equals(JPEG_SIGNATURE, bSignature);
    }

    private int getOrientation(byte[] jpeg) {
        if (jpeg == null) {
            return 0;
        }
        int offset = 0;
        int length = 0;
        // ISO/IEC 10918-1:1993(E)
        while (offset + WidgetLibraryMagic.INT_THREE < jpeg.length && (jpeg[offset++] & WidgetLibraryMagic.INT_ZERO_X_F_F) == WidgetLibraryMagic.INT_ZERO_X_F_F) {
            int marker = jpeg[offset] & 0xFF;
            // Check if the marker is a padding.
            if (marker == 0xFF) {
                continue;
            }
            offset++;
            // Check if the marker is SOI or TEM.
            if (marker == 0xD8 || marker == 0x01) {
                continue;
            }
            // Check if the marker is EOI or SOS.
            if (marker == 0xD9 || marker == 0xDA) {
                break;
            }
            // Get the length and check if it is reasonable.
            length = pack(jpeg, offset, 2, false);
            if (length < 2 || offset + length > jpeg.length) {
                Timber.d("invalid length");
                return 0;
            }
            // Break if the marker is EXIF in APP1.
            if (marker == 0xE1 && length >= 8
                    && pack(jpeg, offset + 2, 4, false) == 0x45786966
                    && pack(jpeg, offset + 6, 2, false) == 0) {
                offset += 8;
                length -= 8;
                break;
            }
            // Skip other markers.
            offset += length;
            length = 0;
        }
        // JEITA CP-3451 Exif Version 2.2
        if (length > WidgetLibraryMagic.INT_EIGHT) {
            // Identify the byte order.
            int tag = pack(jpeg, offset, 4, false);
            if (tag != WidgetLibraryMagic.INT_ZERO_X_FOUR_NINE_FOUR_NINE_TWO_A_ZERO_ZERO && tag != WidgetLibraryMagic.INT_ZERO_X_FOUR_D_FOUR_D_ZERO_ZERO_TWO_A) {
                Timber.d("invalid byte order");
                return 0;
            }
            boolean littleEndian = (tag == 0x49492A00);
            // Get the offset and check if it is reasonable.
            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
            if (count < WidgetLibraryMagic.INT_TEN || count > length) {
                Timber.d("invalid offset");
                return 0;
            }
            offset += count;
            length -= count;
            // Get the count and go through all the elements.
            count = pack(jpeg, offset - 2, 2, littleEndian);
            while (count-- > 0 && length >= WidgetLibraryMagic.INT_TWELVE) {
                // Get the tag and check if it is orientation.
                tag = pack(jpeg, offset, 2, littleEndian);
                if (tag == 0x0112) {
                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                    switch (orientation) {
                        case 1:
                            return 0;
                        case 3:
                            return 180;
                        case 6:
                            return 90;
                        case 8:
                            return 270;
                        default:
                            break;
                    }
                    Timber.d("unsupported orientation");
                    return 0;
                }
                offset += 12;
                length -= 12;
            }
        }
        Timber.d("orientation not found");
        return 0;
    }

    String extSuffix(InputStreamProvider input) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input.open(), null, options);
            return options.outMimeType.replace("image/", ".");
        } catch (Exception e) {
            return JPG;
        }
    }

    boolean needCompress(int leastCompressSize, String path) {
        if (leastCompressSize > 0) {
            File source = new File(path);
            return source.exists() && source.length() > (leastCompressSize << 10);
        }
        return true;
    }

    private int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }
        int value = 0;
        while (length-- > 0) {
            value = (value << 8) | (bytes[offset] & 0xFF);
            offset += step;
        }
        return value;
    }

    private byte[] toByteArray(InputStream is) {
        if (is == null) {
            return new byte[0];
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[4096];
        try {
            while ((read = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
        } catch (Exception ignored) {
            return new byte[0];
        } finally {
            try {
                buffer.close();
            } catch (IOException ignored) {
            }
        }
        return buffer.toByteArray();
    }
}
