package org.laokou.test.juc.thread;

public class Test2 {

    private static final ThreadLocal<StringBuilder> TL = InheritableThreadLocal.withInitial(StringBuilder::new);
    public static void main(String[] args) {
        StringBuilder stringBuilder = TL.get();
        System.out.println(stringBuilder.toString().equals(""));
        stringBuilder.append("3333");
        StringBuilder stringBuilder1 = TL.get();
        System.out.println(stringBuilder1);
        stringBuilder.setLength(0);
        System.out.println(stringBuilder);
    }
}
