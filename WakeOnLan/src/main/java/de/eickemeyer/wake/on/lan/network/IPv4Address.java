package de.eickemeyer.wake.on.lan.network;

public class IPv4Address {

    private final int value;

    public IPv4Address(int value) {
        this.value = value;
    }

    public IPv4Address(String stringValue) {
        String[] parts = stringValue.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException();
        }
        value = (Integer.parseInt(parts[0], 10) << (8 * 3)) & 0xFF000000 |
                (Integer.parseInt(parts[1], 10) << (8 * 2)) & 0x00FF0000 |
                (Integer.parseInt(parts[2], 10) << (8)) & 0x0000FF00 |
                (Integer.parseInt(parts[3], 10)) & 0x000000FF;
    }

    private int getOctet(int i) {
        if (i < 0 || i >= 4) throw new IndexOutOfBoundsException();
        return (value >> (i * 8)) & 0x000000FF;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i >= 0; --i) {
            sb.append(getOctet(i));
            if (i != 0) sb.append(".");
        }

        return sb.toString();

    }

    public IPv4Address increment() {
        return new IPv4Address(value + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IPv4Address ipAddress = (IPv4Address) o;

        return value == ipAddress.value;

    }

    @Override
    public int hashCode() {
        return value;
    }
}