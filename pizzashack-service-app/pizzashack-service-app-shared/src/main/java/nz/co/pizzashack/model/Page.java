package nz.co.pizzashack.model;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Sets;

public class Page<T> {
	private int totalCount = 0;
	private int totalPages = 0;
	private int pageOffset = 0;
	private int pageSize = 5;
	Set<T> content = Sets.<T>newHashSet();
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPageOffset() {
		return pageOffset;
	}
	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Set<T> getContent() {
		return content;
	}
	public void setContent(Set<T> content) {
		this.content = content;
	}
	
	public void addContent(final T object){
		content.add(object);
	}
	
	public static class Builder<T> {
		private int totalCount = 0;
		private int totalPages = 0;
		private int pageOffset = 0;
		private int pageSize = 5;

		public Builder<T> totalCount(int totalCount) {
			this.totalCount = totalCount;
			return this;
		}

		public Builder<T> totalPages(int totalPages) {
			this.totalPages = totalPages;
			return this;
		}

		public Builder<T> pageOffset(int pageOffset) {
			this.pageOffset = pageOffset;
			return this;
		}

		public Builder<T> pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public Page<T> build() {
			Page<T> page = new Page<T>();
			page.setPageOffset(pageOffset);
			page.setPageSize(pageSize);
			page.setTotalCount(totalCount);
			page.setTotalPages(totalPages);
			return page;
		}
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("totalCount", totalCount)
				.append("totalPages", totalPages)
				.append("pageOffset", pageOffset)
				.append("pageSize", pageSize)
				.toString();
	}
}
