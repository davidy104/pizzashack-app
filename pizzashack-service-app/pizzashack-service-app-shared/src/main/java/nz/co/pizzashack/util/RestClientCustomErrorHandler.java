package nz.co.pizzashack.util;

public interface RestClientCustomErrorHandler {
	/**
	 * 
	 * @param statusCode
	 * @param responseString
	 * @return boolean if continue or stop
	 */
	void handle(int statusCode, String responseString) throws Exception;
}
