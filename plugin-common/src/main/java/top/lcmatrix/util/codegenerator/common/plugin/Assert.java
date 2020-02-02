package top.lcmatrix.util.codegenerator.common.plugin;

public class Assert {
	public static void assertTrue(String msg, boolean condition) throws AssertFailException {
		if(!condition) {
			throw new AssertFailException(msg);
		}
	}
	
	public static class AssertFailException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AssertFailException() {
			super();
		}

		public AssertFailException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public AssertFailException(String message, Throwable cause) {
			super(message, cause);
		}

		public AssertFailException(String message) {
			super(message);
		}

		public AssertFailException(Throwable cause) {
			super(cause);
		}
		
	}
}
