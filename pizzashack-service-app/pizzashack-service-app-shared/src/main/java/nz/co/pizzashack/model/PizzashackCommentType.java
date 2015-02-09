package nz.co.pizzashack.model;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Enums;
import com.google.common.base.Optional;

public enum PizzashackCommentType {
	LIKE, DISLIKE;

	public static PizzashackCommentType getPizzashackCommentType(final String pizzashackCommentTypeStr) {
		String val = StringUtils.trimToEmpty(pizzashackCommentTypeStr).toUpperCase();
		Optional<PizzashackCommentType> possible = Enums.getIfPresent(PizzashackCommentType.class, val);
		if (!possible.isPresent()) {
			throw new IllegalArgumentException(val + "? There is no such planet!");
		}
		return possible.get();
	}
}
