package cn.yescallop.puzzle;

import java.util.BitSet;

public class Record {

    public static final byte LEFT = 0;
    public static final byte RIGHT = 1;
    public static final byte UP = 2;
    public static final byte DOWN = 3;

    private final BitSet records;
    private int size;

    public Record() {
        records = new BitSet();
        size = 0;
    }

    public int size() {
        return size;
    }

    public void put(byte direction) {
        if (direction == LEFT) {
            size++;
            return;
        }
        int i = size << 1;
        switch (direction) {
            case RIGHT:
                records.set(i | 1);
                break;
            case UP:
                records.set(i);
                break;
            case DOWN:
                records.set(i, i | 1);
                break;
            default:
                return;
        }
        size++;
    }

    public byte get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        int i = index << 1;
        if (records.get(i)) {
            return records.get(i | 1) ? DOWN : UP;
        } else {
            return records.get(i | 1) ? RIGHT : LEFT;
        }
    }

    public byte[] getBytes() {
        byte[] recordBuf = records.toByteArray();
        byte[] res = new byte[recordBuf.length + 4];
        res[0] = (byte) (size >> 24);
        res[1] = (byte) (size >> 16);
        res[2] = (byte) (size >> 8);
        res[3] = (byte) size;
        System.arraycopy(recordBuf, 0, res, 4, recordBuf.length);
        return res;
    }
}
