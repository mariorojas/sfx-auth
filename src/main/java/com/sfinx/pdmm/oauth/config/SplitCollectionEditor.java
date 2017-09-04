package com.sfinx.pdmm.oauth.config;

import java.util.Collection;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;

/**
 * Crea una colección en base a una cadena de texto.
 * 
 * Si la cadena está vacía o nula, retorna una coleción vacía. En caso contrario
 * parte una cadena en acorde a la expresión regular deseada y retorna un vector.
 * 
 * @author marojas
 *
 */
public class SplitCollectionEditor extends CustomCollectionEditor{
	
	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> collectionType;
	private String splitRegex;

	@SuppressWarnings("rawtypes")
	public SplitCollectionEditor(Class<? extends Collection> collectionType, String splitRegex) {
		super(collectionType, true);
		this.collectionType = collectionType;
		this.splitRegex = splitRegex;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text==null || text.isEmpty()) {
			super.setValue(super.createCollection(collectionType, 0));
		} else {
			super.setValue(text.split(splitRegex));
		}
	}

}
