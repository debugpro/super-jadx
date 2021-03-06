package jadx.gui.utils;

import java.util.HashMap;
import java.util.Map;

import jadx.api.*;
import jadx.core.utils.exceptions.JadxRuntimeException;
import jadx.gui.treemodel.*;

public class JNodeCache {

	private final Map<JavaNode, JNode> cache = new HashMap<>();

	public JNode makeFrom(JavaNode javaNode) {
		if (javaNode == null) {
			return null;
		}
		JNode jNode = cache.get(javaNode);
		if (jNode == null) {
			jNode = convert(javaNode);
			cache.put(javaNode, jNode);
		}
		return jNode;
	}

	private JNode convert(JavaNode node) {
		if (node == null) {
			return null;
		}
		if (node instanceof JavaClass) {
			JClass p = (JClass) makeFrom(node.getDeclaringClass());
			return new JClass((JavaClass) node, p);
		}
		if (node instanceof JavaMethod) {
			JavaMethod mth = (JavaMethod) node;
			return new JMethod(mth, (JClass) makeFrom(mth.getDeclaringClass()));
		}
		if (node instanceof JavaField) {
			JavaField fld = (JavaField) node;
			return new JField(fld, (JClass) makeFrom(fld.getDeclaringClass()));
		}
		if (node instanceof JavaVar) {
			JavaVar var = (JavaVar) node;
			return new JVar(var, (JClass) makeFrom(var.getDeclaringClass()));
		}
		throw new JadxRuntimeException("Unknown type for JavaNode: " + node.getClass());
	}
}
