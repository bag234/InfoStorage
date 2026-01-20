package org.mrbag.InfoStorage.Storge.Cloud;

/**
 * Ошибка, для точной локции именно своих исключений
 */
public class CloudExceptionProcess extends Exception {

	public CloudExceptionProcess(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
