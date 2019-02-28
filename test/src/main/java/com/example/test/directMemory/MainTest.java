package com.example.test.directMemory;

import java.nio.ByteBuffer;

public class MainTest {
    public static void main(String[] args) {
        while(true) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(10 * 1024 * 1024*10);
        }
    }
}
