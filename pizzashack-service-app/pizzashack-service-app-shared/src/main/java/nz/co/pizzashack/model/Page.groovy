package nz.co.pizzashack.model

import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonIgnore

@ToString(includeNames = true, includeFields=true)
class Page<T> {
	long totalCount
	int totalPages
	int pageOffset
	int pageSize
	List<T> content =[]
	@JsonIgnore
	def metaContent
}
