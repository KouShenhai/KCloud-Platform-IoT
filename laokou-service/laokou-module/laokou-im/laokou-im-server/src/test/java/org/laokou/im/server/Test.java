package org.laokou.im.server;

public class Test {

	public static void main(String[] args) {
		new B().set();
		new C().set();
	}

	static class A {

		protected int i = 0;

	}

	static class C extends A {

		public void set() {
			System.out.println(i);
			i = 2;
			System.out.println(i);
		}

	}

	static class B extends A {

		public void set() {
			System.out.println(i);
			i = 1;
			System.out.println(i);
		}

	}

}
