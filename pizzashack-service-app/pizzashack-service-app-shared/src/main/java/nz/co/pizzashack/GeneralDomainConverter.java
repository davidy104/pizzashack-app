package nz.co.pizzashack;

public interface GeneralDomainConverter<D, T> {
	T fromDomain(D domain, Object... params) throws Exception;

	D toDomain(T bean, Object... params) throws Exception;
}
