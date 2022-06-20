package com.itva.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itva.model.TitusAttribute;
import com.itva.model.TitusAttributes;
import com.itva.model.TitusDocument;

public class Util {
	private static Logger logger = LoggerFactory.getLogger(Util.class);

	public static TitusDocument convertToTitusDocument(TitusAttributes titusAttributes, List<TitusAttribute> tas) {
		Timestamp ts = new Timestamp(Calendar.getInstance().getTimeInMillis());
		TitusDocument td = TitusDocument.builder().documentName(titusAttributes.getDocumentName())
				.userName(titusAttributes.getUserName()).accessType(titusAttributes.getAccessType()).loggedTime(ts)
				.build();

		Field[] allFields = TitusAttributes.class.getDeclaredFields();

		for (Field f : allFields) {

			try {
				String propertyName = f.getName();
				String methodName = "get" + propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1, propertyName.length());
				try {
					Method method = TitusAttributes.class.getMethod(methodName, null);
					String value = (String) method.invoke(titusAttributes, null);
					logger.debug(f.getName() + " " + value);
					if (propertyName.equals("documentName") || propertyName.equals("userName")
							|| propertyName.equals("accessType")) {
						continue;
					}
					tas.add(TitusAttribute.builder().documentName(titusAttributes.getDocumentName())
							.attributeName(f.getName()).attributeValue(value).loggedTime(ts).build());
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return td;
	}

	public static List<TitusAttributes> convertToTitusAttributes(List<TitusDocument> tds, List<TitusAttribute> taList) {
		List<TitusAttributes> tasList = new ArrayList<>();

		for (TitusDocument td : tds) {
			TitusAttributes tas = TitusAttributes.builder().documentName(td.getDocumentName())
					.userName(td.getUserName()).accessType(td.getAccessType()).loggedTime(td.getLoggedTime()).build();

			List<TitusAttribute> taListByDoc = taList.stream()
					.filter(x -> x.getDocumentName().equals(td.getDocumentName())).collect(Collectors.toList());

			for (TitusAttribute ta : taListByDoc) {
				String propertyName = ta.getAttributeName();
				String methodName = "set" + propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1, propertyName.length());

				try {
					Method method = TitusAttributes.class.getMethod(methodName, String.class);
					method.invoke(tas, ta.getAttributeValue());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			tasList.add(tas);
		}

		return tasList;
	}

}
