package com.codingtutorials.imagesqlite;

/** One row of the images table: its id, a name, and the JPEG bytes. */
public class SavedImage {
    public final long id;
    public final String name;
    public final byte[] bytes;

    public SavedImage(long id, String name, byte[] bytes) {
        this.id = id;
        this.name = name;
        this.bytes = bytes;
    }
}
